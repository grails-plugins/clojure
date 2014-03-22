package grails.clojure

class ClojureProxyTests extends GroovyTestCase {
    static transactional = false

    void setUp() {
      grails.clojure.ClojureProxy.metaClass.static."getMaxMacroDepth" = {
        25
      }
    }
    
    void testASimpleClojureFunction() {
        assertEquals 'A Simple Clojure Function', new ClojureProxy().simple()
    }
    
    void testClojureFunctionWithASingleArgument() {
        def results = new ClojureProxy().fibo(5)
        assertNotNull 'results were null', results
        assertEquals 'wrong number of elements in result', 5, results.size()
    }
    
    void testClojureFunctionWithMultipleArguments() {
        def proxy = new ClojureProxy()
        
        assertEquals 10, proxy.add_numbers(3, 2, 5)
        assertEquals 10, proxy.add_numbers(6, 4)
        assertEquals 10, proxy.add_numbers(1, 2, 3, 4)
    }

    void testClojureFunctionWithClosureArgument() {
        def proxy = new ClojureProxy()

        def testClosure = {x, y -> x + y}
        def args = [7, 3]
        assertEquals testClosure(*args), proxy.execute_closure(testClosure, *args)

        shouldFail { proxy.execute_closure({ it }, *args) }
    }

    void testConvertArgs() {
        def proxy = new ClojureProxy()

        def args = [1, 2, 3]
        assertEquals args, proxy.convertArgs(args)
    }

    void testClojureFunctionWithMapArgument() {
        def proxy = new ClojureProxy()

        def testMap = [third: 'groovy', second: 'grails', first: 'clojure']
        assertEquals 'clojure', proxy.read_map(testMap, 'first')
        // test cacheing w/ conversion
        assertEquals 'clojure', proxy.read_map(testMap, 'first')
    }

    void testClojureBinding() {
          def proxy = new ClojureProxy()

          assertEquals 15, proxy.fifteen
          assertEquals "test string", proxy.test_string
          assertTrue proxy.blah instanceof clojure.lang.Var.Unbound
    }

    void testClojureBindingWithSingleChararacterName() {
          def proxy = new ClojureProxy()

          assertEquals 42, proxy.x
    }

    void testNamespaces() {
        def proxy = new ClojureProxy()
        
        // go back and forth several times to make sure
        // the wrong function doesn't get cached and reused
        // in the wrong place
        assertEquals "one::doit", proxy['one'].doit()
        assertEquals "two::doit", proxy['two'].doit()
        assertEquals "one::doit", proxy['one'].doit()
        assertEquals "two::doit", proxy['two'].doit()
        assertEquals "one::doit", proxy['one'].doit()
        assertEquals "two::doit", proxy['two'].doit()
    }

    void testMacro() {
        def proxy = new ClojureProxy()

        def msg = shouldFail(UnsupportedOperationException) {
            proxy.silly_adder(42)
        }
        
        assertEquals "Directly Invoking Macros Is Not Supported. (namespace: grails, macro: silly_adder)", msg
    }

    void testSimpleMacro() {
        def proxy = new ClojureProxy()

        def msg = shouldFail(UnsupportedOperationException) {
            proxy.simple_macro()
        }
        assertEquals "Directly Invoking Macros Is Not Supported. (namespace: grails, macro: simple_macro)", msg
    }

    void testEmptyMacro() {
        def proxy = new ClojureProxy()

        def msg = shouldFail(UnsupportedOperationException) {
            proxy.empty_macro().size()
        }
        assertEquals "Directly Invoking Macros Is Not Supported. (namespace: grails, macro: empty_macro)", msg
    }

    void testListMacro() {
        def proxy = new ClojureProxy()

        def msg = shouldFail(UnsupportedOperationException) {
            proxy.list_macro().size()
        }
        assertEquals "Directly Invoking Macros Is Not Supported. (namespace: grails, macro: list_macro)", msg
    }

    void testListMacroMacro() {
        def proxy = new ClojureProxy()
        def msg = shouldFail(UnsupportedOperationException) {
            proxy.even_sillier_adder(4)
        }
        assertEquals "Directly Invoking Macros Is Not Supported. (namespace: grails, macro: even_sillier_adder)", msg
    }

    void testListMacroMacroMacro() {
        def proxy = new ClojureProxy()

        def msg = shouldFail(UnsupportedOperationException) {
            proxy.ridiculously_silly_adder(4)
        }
        assertEquals "Directly Invoking Macros Is Not Supported. (namespace: grails, macro: ridiculously_silly_adder)", msg
    }
}
