description( "Creates a new Clojure script" ) {
    usage "grails create-clojure-script [FILE NAME]"
    argument name:'Clojure Script File Name', description:"The name of the clojure script file to create"
    flag name:'force', description:"Whether to overwrite existing files"
}
def scriptName = args[0]
def model = model(scriptName)
def overwrite = flag('force') ? true : false
render  template: template('clojure/template.clj'),
        destination: file("src/main/clj/${model.lowerCaseName}.clj"),
        model: model,
        overwrite: overwrite