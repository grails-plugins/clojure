package grails.clojure

import clojure.lang.RT

class ClojureProxy {
    
    def ns = 'grails'
    
    def getAt(String ns) {
        return new ClojureProxy(ns: ns)
    }

    def convertArg(Closure fn) {
        fn as clojure.lang.IFn
    }

    def convertArg(Map mp) {
        clojure.lang.PersistentHashMap.create(mp)
    }

    def convertArg(arg) {
        arg
    }

    def convertArgs(args) {
        args.collect {
            convertArg(it)
        } as Object[]
    }
    
    def methodMissing(String name, args) {
        def impl = { Object[] a = new Object[0] ->
            def var = RT.var(delegate.ns, name)
            var.invoke (*convertArgs(a))
        }
        ClojureProxy.metaClass."${name}" = impl
        impl(args)
    }

    def propertyMissing(String name) {
        def impl = { -> RT.var(ns, name)?.get() }
        name.metaClass.capitalize = { delegate[0].toUpperCase() + delegate[1..-1] }
        ClojureProxy.metaClass."get${name.capitalize()}" = impl
        impl()
    }
}
