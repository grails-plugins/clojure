package grails.clojure

import clojure.lang.RT

class ClojureProxy {
    
    def ns = 'grails'
    
    def getAt(String ns) {
        return new ClojureProxy(ns: ns)
    }
    
    def methodMissing(String name, args) {
        def impl = { Object[] a = new Object[0] ->
            def var = RT.var(delegate.ns, name)
            var.invoke (*a)
        }
        ClojureProxy.metaClass."${name}" = impl
        impl(args)
    }
}
