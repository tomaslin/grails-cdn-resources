package grails.plugin.cdnresources

import grails.plugin.spock.UnitSpec

class CdnMapperSpec extends UnitSpec{

    def mapper

    def setup(){

        mapper = new CdnResourceMapper()

    }

    def "test that mappers are configured correctly"(){
        setup:
            def resource = [ linkUrl : 'images.jpg' ]
            def config = [ enabled: true, url: 'http://www.google.com/' ]
        when:
            mapper.map( resource, config )
        then:
            resource.linkOverride == 'http://www.google.com/images.jpg'
    }

    def "when mappers are disabled, links are not processed"(){
        setup:
            def resource = [ linkUrl : 'images.jpg' ]
            def config = [ enabled: false, url: 'http://www.google.com/' ]
        when:
            mapper.map( resource, config )
        then:
            resource.linkOverride == null
    }

    def "a mapper can set a unique url based on module name"(){
        setup:
            def resource = [ linkUrl : 'images.jpg', module: [ name: 'uno'] ]
            def config = [ enabled: true, url:'http://www.google.com/', baseUrls : [ uno: 'http://uno.com/' ] ]
        when:
            mapper.map( resource, config )
        then:
            resource.linkOverride == 'http://uno.com/images.jpg'
    }

    def "a mappers with modules default to base url"(){
        setup:
            def resource = [ linkUrl : 'images.jpg', module: [ name: 'uno'] ]
            def config = [ enabled: true, url:'http://www.google.com/', baseUrls : [ dos: 'http://dos.com/' ] ]
        when:
            mapper.map( resource, config )
        then:
            resource.linkOverride == 'http://www.google.com/images.jpg'
    }

}
