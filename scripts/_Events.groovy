eventCompileStart = {
    def cljDestDir = "${grailsSettings.projectWarExplodedDir}/WEB-INF/clj"
    ant.mkdir dir: cljDestDir
    ant.copy (todir: cljDestDir) {
        fileset(dir:"${basedir}/src/clj", includes:"*.clj")
    }
}