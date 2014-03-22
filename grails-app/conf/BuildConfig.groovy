grails.project.dependency.resolution = {

    inherits('global') {
    }

    repositories {        
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

       mavenRepo 'http://build.clojure.org/releases'
    }

    dependencies {
        runtime 'org.clojure:clojure:1.5.1'
    }
}