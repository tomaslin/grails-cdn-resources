package grails.cloudresource

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.jets3t.service.impl.rest.httpclient.RestS3Service
import org.jets3t.service.S3Service
import org.jets3t.service.security.AWSCredentials
import org.jets3t.service.CloudFrontService
import org.jets3t.service.multithread.S3ServiceSimpleMulti
import org.jets3t.service.model.S3Bucket
import org.apache.commons.io.FilenameUtils
import org.jets3t.service.model.S3Object
import org.jets3t.service.model.cloudfront.Distribution
import org.jets3t.service.acl.AccessControlList
import org.jets3t.service.acl.GroupGrantee
import org.jets3t.service.acl.Permission

class AmazonService {

  static transactional = false

  // deploy all method - intended to upload an entire copy of static directory into a bucket
  def deployAll(deployDir) {
    def cdnConfig = ConfigurationHolder.config.cloudresources

    if (cdnConfig.s3.enabled) {
      log.info "starting to deploy to Amazon CDN"

      String awsAccessKey = cdnConfig.s3.accessKey
      String awsSecretKey = cdnConfig.s3.secret
      String bucketName = cdnConfig.s3.bucketName

      AWSCredentials awsCredentials = new AWSCredentials(awsAccessKey, awsSecretKey)
      S3Service s3Service = new RestS3Service(awsCredentials)
      S3Bucket bucket = s3Service.getOrCreateBucket(bucketName)
      S3ServiceSimpleMulti simpleMulti = new S3ServiceSimpleMulti(s3Service);

      // make the bucket publicly accessible
      AccessControlList bucketAcl = s3Service.getBucketAcl(bucket);
      bucketAcl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
      bucket.setAcl(bucketAcl);
      s3Service.putBucketAcl(bucket);

      def filesToUpload = []
      new File(deployDir.absolutePath).eachFile { topDir ->
        if (topDir.name == 'js' || topDir.name == 'css' || topDir.name == 'images') {
          filesToUpload += addToBucket(topDir, deployDir, bucketAcl)
        }
      }

      log.info "uploading ${filesToUpload.size } files"
      simpleMulti.putObjects(bucket, filesToUpload as S3Object[])
      log.info "deployment to Amazon CDN finished"

      if (cdnConfig.cloudFront.enabled) {

        log.info "checking CloudFront distribution"
        CloudFrontService cloudFrontService = new CloudFrontService(awsCredentials);
        Distribution[] bucketDistributions = cloudFrontService.listDistributions(bucketName);

        def distributionExists = false
        def cloudFrontCName = cdnConfig.cloudFront.url as String

        for (int i = 0; i < bucketDistributions.length; i) {
          (bucketDistributions[i].CNAMEs).each { cname ->
            if (cname == cloudFrontCName) {
              log.info "CloudFront distribution found - domain name is "
              log.info "${ bucketDistributions[i].domainName }"
              lof.info "CloudFront deployment done"
              distributionExists = true
            }
          }
        }

        if (!distributionExists) {
          log.info "CloudFront distribution does not exist for CNAME '${ cloudFrontCName }', creating"

          Distribution newDistribution = cloudFrontService.createDistribution(
                  bucketName,
                  "" + System.currentTimeMillis(), // Caller reference - a unique string value
                  [cloudFrontCName] as String[], // CNAME aliases for distribution
                  "", // Comment
                  true,  // Distribution is enabled?
                  null  // Logging status of distribution (null means disabled)
          )

          log.info "CloudFront distribution created - domain name is"
          log.info "${ bucketDistributions[i].domainName }"
          log.info "CloudFront deployment done"

        }
      }

    }
  }

  private static def addToBucket(file, directoryRoot, accessControl) {
    def filesToUpload = []
    def now = new Date()
    if (file.isDirectory()) {
      file.eachFile { childFile ->
        filesToUpload += addToBucket(childFile, directoryRoot, accessControl)
      }
    } else {
      def path = file.absolutePath - (directoryRoot.absolutePath + File.separator)
      path = path.replaceAll('\\\\', '/')
      S3Object fileObject = new S3Object(file)
      fileObject.setKey(path)
      fileObject.setAcl(accessControl)
      // set metadata
      fileObject.setLastModifiedDate(now)
      use([org.codehaus.groovy.runtime.TimeCategory]) {
        fileObject.addMetadata("Expires", now + 10.years)
        fileObject.addMetadata("Cache-control", 'public,max-age=' + now+10.years)
      }

      switch (FilenameUtils.getExtension(file.name).toLowerCase()) {
        case 'jpg':
          fileObject.setContentType("image/jpeg")
          break;
        case 'png':
          fileObject.setContentType("image/png")
          break;
        case 'gif':
          fileObject.setContentType("image/gif")
          break;
        case 'css':
          fileObject.setContentType("text/css")
          break;
        case 'js':
          fileObject.setContentType("text/javascript")
          break;
      }

      if (file.name.contains(".gz.")) {
        fileObject.setContentEncoding("gzip")
      }

      filesToUpload.add(fileObject)
    }
    filesToUpload
  }
}
