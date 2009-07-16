package grails.clojure

import clojure.lang.RT

class ClojureProxy {
    
    def ns = 'grails'
    
    def getAt(String ns) {
        return new ClojureProxy(ns: ns)
    }
    
    def methodMissing(String name, args) {
        def var = RT.var(ns, name)
        var.invoke (*args)
    }
}
