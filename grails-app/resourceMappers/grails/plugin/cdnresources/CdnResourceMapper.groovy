package grails.plugin.cdnresources

import org.grails.plugin.resource.mapper.MapperPhase

class CdnResourceMapper {

    static priority = 15000 // after resources have been zipped, cached + properly beaten
	static def url
    static phase = MapperPhase.DISTRIBUTION
    def grailsApplication

    def map(resource, config) {
        if( getCDNUrl() != 'none' ){
            resource.linkOverride = url + resource.linkUrl
        }
    }
	
	private def getCDNUrl()
    {
    	if( !url ){
            def config = grailsApplication.config.cdnResources
            if( config.enabled ){
                url = config.url
            } else {
                url = 'none'
            }
            if( url != 'none' && url.endsWith('/') ){
                url = url[0..-1]
            }
		}
        url
    }

}
