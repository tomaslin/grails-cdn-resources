Cdn-resources -- Content Delivery Network support for grails resources plugin
=============================================================================

<a href="http://flattr.com/thing/304127/CND-Resources-plugin-for-grails" target="_blank"><img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" /></a>

## DESCRIPTION

This plugin allows you to use a content delivery network ( like Amazon CloudFront ) to deliver resources served by the grails resources plugin. It is loaded after the zipped and cached resources plugins so that any modifications made by these plugins will also apply.

## INSTALLATION

In your application directory, call

	$ grails install-plugin cdn-resources
	
You would need to add the following parameters to your config.groovy file

	$ grails.resources.cdn.enabled = true
	$ grails.resources.cdn.url = "http://static.mydomain.com/"

## SPECIFYING URLS PER MODULES

You can also define a separate CDN location per module ( for files being hosted by Google, for example ).

The syntax for this is

	$ grails.resources.cdn.moduleUrls = [ 'google' : 'http://www.google.com/apis', 'core' : 'http://subdomain.mysite.com' ]

If you set up your Content Delivery Network correctly, all your resource files will be served from the CDN from now on. 
	
## SETTING UP AMAZON CLOUDFRONT CDN SUPPORT

Before you can use this plugin, you need to set up a content delivery network to dispatch your resources. 

The following section describes how to do so on Amazon CloudFront. You would need an account for this, which you can sign up for at http://aws.amazon.com/cloudfront/

1 Login to your Amazon AWS Console - https://console.aws.amazon.com/cloudfront/home?

2 Click on Create Distribution

3 Select custom Origin and enter the URL of your site. 

![Set custom origin](https://github.com/tomaslin/grails-cdn-resources/raw/master/docs/origin.png "specifying an origin")

4 If you wish to map the URL to a CNAME ( ie, point to cdn.mydomain.com instead of 5kfd933kkdd.cloudfront.net ), you can specify this in the next screen.

5 Review the details and click OK.

![Review Details](https://github.com/tomaslin/grails-cdn-resources/raw/master/docs/details.png "Review Details")

6 You should now see your Cloudfront distribution. 

![Distribution](https://github.com/tomaslin/grails-cdn-resources/raw/master/docs/dist.png "Distribution details")

## A NOTE ON URLS

You will see an url of the form jlsadf423kl24hlf.cloudfront.net. This will be the value that you enter in the cdnresources.url value in Config.groovy.

	$ grails.resources.cdn.url = "http://jlsadf423kl24hlf.cloudfront.net"
