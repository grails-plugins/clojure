package grails.plugin.clojure

class DemoController {
    
    def simple() {
        render view:'plain', model: [text: clj.simple()]
    }
    
    def one() {
        render view:'plain', model: [text: clj['one'].doit()]
    }
    
    def two() {
        render view:'plain', model: [text: clj['two'].doit()]
    }
    
    def fibo() {
        def cnt = params.cnt?.toInteger()
        def numbers
        if(cnt) {
            numbers = clj.fibo(cnt)
        } else {
            numbers = []
        }
        render view: 'fibo', model: [numbers: numbers]
    }
}
