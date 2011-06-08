package grails.plugin.cdnresources

class CdnResourceMapper {

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

		    def config = grailsApplication.config.cdnresources

            if( config.enabled ){
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
