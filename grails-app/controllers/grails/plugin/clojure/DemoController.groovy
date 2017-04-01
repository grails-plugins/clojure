package grails.plugin.clojure

class DemoController {

    def demoHelper
    def firstHelper
    def secondHelper

    def simple() {
        render view:'plain', model: [text: demoHelper.simple()]
    }
    
    def one() {
        render view:'plain', model: [text: firstHelper['one'].doit()]
    }
    
    def two() {
        render view:'plain', model: [text: secondHelper['two'].doit()]
    }
    
    def fibo() {
        def cnt = params.cnt?.toInteger()
        def numbers
        if(cnt) {
            numbers = demoHelper.fibo(cnt)
        } else {
            numbers = []
        }
        render view: 'fibo', model: [numbers: numbers]
    }
}
