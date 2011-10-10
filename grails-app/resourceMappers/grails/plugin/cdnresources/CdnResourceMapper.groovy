package grails.plugin.cdnresources

import org.grails.plugin.resource.mapper.MapperPhase

class CdnResourceMapper {

    static priority = 15000 // after resources have been zipped, cached + properly beaten
    static phase = MapperPhase.DISTRIBUTION

    def map(resource, config) {
		if( config.enabled ){

			def url

			if( resource.module?.name && config.baseUrls[ resource.module.name ] ){
				url = config.baseUrls[ resource.module.name ]
			}

			if( !url ){
				url = config.url
			}

			if( url ){
				if( url.endsWith('/') ){
					url = url[0..-1]
				}
				resource.linkOverride = url + resource.linkUrl
			}
		}
    }

}