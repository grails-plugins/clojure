package grails.plugin.clojure

import grails.test.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

@Integration
class ClojureProxyTests extends Specification {

    @Shared
    def proxy = new ClojureProxy(ns: 'demo')

    void 'test a simple clojure function'() {
        expect:
        'A Simple Clojure Function' == proxy.simple()
    }

    void 'test clojure function a single argument'() {
        when:
        def results = proxy.fibo(5)

        then:
        results?.size() == 5
    }

    void 'test clojure functional with multiple arguments'() {
        expect:
        10 == proxy.add_numbers(3, 2, 5)
        20 == proxy.add_numbers(16, 4)
        35 == proxy.add_numbers(25, 3, 2, 5)
    }

    void 'test clojure function with closure argument'() {
        given:
        def testClosure = {x, y -> x + y}

        expect:
        10 == proxy.execute_closure(testClosure, 7, 3)
    }

    void 'test convert arguments'() {
        given:
        def args = [1, 2, 3]

        expect:
        args == proxy.convertArgs(args)
    }


    void 'test clojure function with Map argument'() {
        given:
        def testMap = [third: 'groovy', second: 'grails', first: 'clojure']

        expect:
        'clojure' == proxy.read_map(testMap, 'first')

        and: 'test cacheing w/ conversion'
        'clojure' == proxy.read_map(testMap, 'first')

    }

    void 'test clojure binding'() {
        expect:
        15 == proxy.fifteen
        'test string' == proxy.test_string
        proxy.blah instanceof clojure.lang.Var.Unbound
    }

    void 'test clojure binding with single character name'() {
        expect:
        42 == proxy.x
    }

    void 'test namespace'() {
        expect:
        // go back and forth several times to make sure
        // the wrong function doesn't get cached and reused
        // in the wrong place
        "one::doit" == proxy['one'].doit()
        "two::doit" == proxy['two'].doit()
        "one::doit" == proxy['one'].doit()
        "two::doit" == proxy['two'].doit()
        "one::doit" == proxy['one'].doit()
        "two::doit" == proxy['two'].doit()
    }

    void 'test macro'() {
        when:
        proxy.silly_adder(42)

        then:
        UnsupportedOperationException ex = thrown()
        "Directly Invoking Macros Is Not Supported. (namespace: demo, macro: silly_adder)" == ex.message
    }
}
