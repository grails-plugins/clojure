grails.project.dependency.resolution = {

    inherits('global') {
    }

    repositories {        
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }
    
    dependencies {
        runtime 'org.clojure:clojure:1.1.0'
    }
}