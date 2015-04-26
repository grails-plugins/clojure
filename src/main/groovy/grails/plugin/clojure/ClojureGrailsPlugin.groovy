package grails.plugin.clojure

import grails.plugins.Plugin
import clojure.lang.Compiler
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

class ClojureGrailsPlugin extends Plugin {

    def scm = [url: 'https://github.com/grails-plugins/grails-clojure']

    // the plugin version
    def version = "1.0.SNAPSHOT"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "src/main/clj/*",
            "grails-app/controllers/*",
            "**/.gitignore",
            "grails-app/views/demo/*"
    ]

    def watchedResources = "file:./src/main/clj/*.clj"

    def observe = ['*']

    // TODO Fill in these fields
    def author = "Jeff Brown"
    def authorEmail = "jbrown@gopivotal.com"

    def developers = [ [ name: "Jef Brown", email: "jbrown@gopivotal.com" ],
                       [ name: "Puneet Behl", email: "puneet.behl007@gmail.com"]
    ]

    def title = "Grails Clojure Plugin"
    def description = '''\\
The Clojure plugin adds support for easily accessing Clojure code in a Grails application.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Clojure+Plugin"

    void doWithDynamicMethods() {
        def clojureFiles
        if(grailsApplication.warDeployed) {
            clojureFiles = applicationContext?.getResources("**/WEB-INF/clj/*.clj")?.toList()
        } else {
            clojureFiles = plugin.watchedResources
        }
        Class.forName('clojure.lang.RT')
        clojureFiles.each {
            it.file.withReader { reader ->
                clojure.lang.Compiler.load reader
            }
        }
        addDynamicProperty(grailsApplication.allClasses)
    }

    private void addDynamicProperty(classes) {
        def config = grailsApplication.config
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

        def proxy = new ClojureProxy()
        classes*.metaClass*."get${clojurePropertyName}" = {
            return proxy
        }
    }

    void onChange(Map<String, Object> event) {
        def source = event.source
        if(source instanceof org.springframework.core.io.FileSystemResource &&
            (source.file.name.endsWith('.clj'))) {
                source.file.withReader { reader ->
                    clojure.lang.Compiler.load reader
                }
        } else if(source instanceof Class) {
            addDynamicProperty([source])
        }
    }
}
