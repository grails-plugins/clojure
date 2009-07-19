eventCompileStart = {
    def cljDestDir = "${grailsSettings.projectWarExplodedDir}/WEB-INF/grails-app/clj"
    ant.mkdir dir: cljDestDir
    ant.copy (todir: cljDestDir) {
        fileset(dir:"${basedir}/grails-app/clj", includes:"*.clj")
    }
}