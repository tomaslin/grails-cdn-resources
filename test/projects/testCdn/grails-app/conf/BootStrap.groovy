/**
 * Development-mode only bootstrap
 */

class BootStrap {

    def resourceService

    def init = { servletContext ->

        resourceService.with {
            module 'test', [
                'css/default.css',
                'css/default2.css',
                'js/prototype/prototype.js',
                'js/prototype/effects.js'
            ]
        }
    }

    def destroy = {

    }
}