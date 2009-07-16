package grails.clojure

import clojure.lang.RT

class ClojureProxy {
    def methodMissing(String name, args) {
        def impl = {
            def var = RT.var('grails', name)
            var.invoke (*args)
        }
        this.metaClass."${name}" = impl
        impl(args)
    }
}