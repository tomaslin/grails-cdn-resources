Cdn-resources -- Content Delivery Network support for resources plugin
======================================================================

## DESCRIPTION

This plugin allows you to use a content delivery network ( like Amazon CloudFront ) to deliver resources served by the grails resources plugin. It is loaded after the zipped and cached resources plugins so that any modifications made by these plugins will also apply.

## Installation

In your application directory, call

	$ grails install-plugin cdn-resources
	
You would need to add the following parameters to your config.groovy file

	$ cdnresources.enabled = true
	$ cdnresources.url = "http://static.mydomain.com/"
	
If you set up your Content Delivery Network correctly, all your resource files will be served from the CDN from now on. details.
	
## Setting up your CDN.

Before you can use this plugin, you need to set up a content delivery network to dispatch your resources. 

The following section describes how to do so on Amazon CloudFront. You would need an account for this, which you can sign up for at http://aws.amazon.com/cloudfront/

1. Login to your Amazon AWS Console - https://console.aws.amazon.com/cloudfront/home?

2. Click on Create Distribution

3. Select custom Origin and enter the URL of your site. 

![Set custom origin](raw/master/docs/origin.png "specifying an origin")

4. If you wish to map the URL to a CNAME ( ie, point to cdn.mydomain.com instead of 5kfd933kkdd.cloudfront.net ), you can specify this in the next screen.

5. Review the details and click OK.

![Review Details](raw/master/docs/details.png "Review Details")

6. You should now see your Cloudfront distribution. 

![Distribution](raw/master/docs/dist.png "Distribution details")

You will see an url of the form jlsadf423kl24hlf.cloudfront.net . This will be the value that you enter in the cdnresources.url value in Config.groovy.

	$ cdnresources.url = "http://jlsadf423kl24hlf.cloudfront.net"





