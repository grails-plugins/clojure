/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        def impl  = { Object[] a = new Object[0] ->
                def var = RT.var(delegate.ns, name)
                def res = var.invoke (*convertArgs(a))
                def depthCounter = 0
                while (var.isMacro() 
                    && res instanceof clojure.lang.ISeq
                    && res.size() > 0
                       && depthCounter < ClojureProxy.getMaxMacroDepth())
                {
                  var = clojure.lang.Var.find(res.first())
                  res = var.invoke(*res.more())
                  depthCounter++
                }

                if (depthCounter >= ClojureProxy.getMaxMacroDepth()) {
                    throw new Exception("Macro recursion exceeded maxMacroDepth of ${ClojureProxy.getMaxMacroDepth()}")
                }
                res
            }
        ClojureProxy.metaClass."${name}" = impl
        impl(args)
    }

    def propertyMissing(String name) {
        def impl = { -> RT.var(ns, name)?.get() }
        def getterName = "get${name[0].toUpperCase()}"
        if(name.size() > 1) {
            getterName += name[1..-1]
        }
        ClojureProxy.metaClass."${getterName}" = impl
        impl()
    }
}
