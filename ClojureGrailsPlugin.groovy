import clojure.lang.Compiler

class ClojureGrailsPlugin {
    // the plugin version
    def version = "0.1-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/clj/*",
            "grails-app/controllers/*"
    ]
    
    def watchedResources = "file:./grails-app/clj/*.clj"
    
    def observe = ['*']
    
    // TODO Fill in these fields
    def author = "Jeff Brown"
    def authorEmail = "jeff.brown@springsource.com"
    def title = "Grails Clojure Plugin"
    def description = '''\\
The Clojure plugin adds support for easily accessing Clojure code in a Grails application.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Clojure+Plugin"

    def doWithDynamicMethods = { ctx ->
        def clojureFiles = plugin.watchedResources
        clojureFiles.each {
            it.file.withReader { reader ->
                Compiler.load reader
            }
        }
        application.allClasses.each {
            addDynamicProperty(it)
        }
    }

    private void addDynamicProperty(clazz) {
        def config = org.codehaus.groovy.grails.commons.ConfigurationHolder.config
        def clojurePropertyName = config.grails?.clojure?.dynamicPropertyName
        if(clojurePropertyName) {
            clojurePropertyName = clojurePropertyName[0].toUpperCase() + clojurePropertyName[1..-1]
        } else {
            clojurePropertyName = 'Clj'
        }
        def proxy = new grails.clojure.ClojureProxy()
        clazz.metaClass*."get${clojurePropertyName}" = {
            return proxy
        }
    }

    def onChange = { event ->
        def source = event.source
        if(source instanceof org.springframework.core.io.FileSystemResource &&
            (source.file.name.endsWith('.clj'))) {
                source.file.withReader { reader ->
                    Compiler.load reader
                }
        } else if(source instanceof Class) {
            addDynamicProperty(source)
        }
    }
}
