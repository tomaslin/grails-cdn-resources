package grails.plugin.cloudresources

class CloudResourceMapper {

    def priority = 15000 // after resources have been zipped, cached + properly beaten
	def url 
    def grailsApplication

    def map(resource, config) {
		if( getCDNUrl() != 'none' ){
            resource.actualUrl = url + resource.processedFile.name
        }
    }
	
	def getCDNUrl( ){
		if( !url ){
		    def config = grailsApplication.config.cloudresource
			if( config.cloudFront.enabled ){
                url = config.cloudFront.url
            } else if( config.s3.enabled ){
                url = config.s3.url
            } else if( config.enabled ){
                url = config.url
            } else {
                url = 'none'
            }
            if( url != 'none' && !url.endsWith('/') ){
                url += '/'
            }
		}
		url 
	}

}
