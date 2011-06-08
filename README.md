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
	
## Setting up a CDN.

Before you can use this plugin, you need to set up a content delivery network to dispatch your resources. 

The following section describes how to do so on Amazon CloudFront. You would need an account for this, which you can sign up for at http://aws.amazon.com/cloudfront/



