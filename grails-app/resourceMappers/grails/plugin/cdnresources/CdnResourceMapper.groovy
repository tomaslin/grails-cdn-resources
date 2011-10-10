package grails.plugin.cdnresources

import org.grails.plugin.resource.mapper.MapperPhase

class CdnResourceMapper {

    static priority = 15000 // after resources have been zipped, cached + properly beaten
    static phase = MapperPhase.DISTRIBUTION

    def grailsApplication

    def map(resource, config) {

        def mergedConfig = config + grailsApplication.config.grails.resources.cdn

        if( mergedConfig.enabled ){

			def url

			if( resource.module?.name && mergedConfig.moduleUrls[ resource.module.name ] ){
				url = mergedConfig.moduleUrls[ resource.module.name ]
			}

			if( !url ){
				url = mergedConfig.url
			}

			if( url ){
				if( url.endsWith('/') ){
					url = url[0..-2]
				}
				resource.linkOverride = url + resource.linkUrl
			}
		}

    }

}