/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import clojure.lang.Compiler

class ClojureGrailsPlugin {
    // the plugin version
    def version = "0.5-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "src/clj/*",
            "grails-app/controllers/*",
            "**/.gitignore",
            "grails-app/views/demo/*"
    ]
    
    def watchedResources = "file:./src/clj/*.clj"
    
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
        def clojureFiles
        if(application.warDeployed) {
            clojureFiles = parentCtx?.getResources("**/WEB-INF/clj/*.clj")?.toList()
        } else {
            clojureFiles = plugin.watchedResources
        }
        clojureFiles.each {
            it.file.withReader { reader ->
                Compiler.load reader
            }
        }
        addDynamicProperty(application.allClasses)
    }

    private void addDynamicProperty(classes) {
        def config = org.codehaus.groovy.grails.commons.ConfigurationHolder.config
        def clojurePropertyName = config.grails?.clojure?.dynamicPropertyName
        if(clojurePropertyName) {
            def propName = clojurePropertyName[0].toUpperCase()
            if(clojurePropertyName.size() > 1) {
                propName += clojurePropertyName[1..-1]
            }
            clojurePropertyName = propName
        } else {
            clojurePropertyName = 'Clj'
        }

        def recursiveMacroMaxDepth = config.grails?.clojure?.recursiveMacroMaxDepth
        if(recursiveMacroMaxDepth && recursiveMacroMaxDepth.isNumber()) {
            recursiveMacroMaxDepth = recursiveMacroMaxDepth as Integer
        } else {
            recursiveMacroMaxDepth = 25
        }
        grails.clojure.ClojureProxy.metaClass.static."getMaxMacroDepth" = {
            recursiveMacroMaxDepth
        }
        def proxy = new grails.clojure.ClojureProxy()
        classes*.metaClass*."get${clojurePropertyName}" = {
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
            addDynamicProperty([source])
        }
    }
}
