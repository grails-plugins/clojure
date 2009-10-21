package grails.clojure

class ClojureProxyTests extends GroovyTestCase {
    static transactional = false
    
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
    
    void testClojureBinding() {
      	def proxy = new ClojureProxy()

      	assertEquals 15, proxy.fifteen
      	assertEquals "test string", proxy.test_string
      	shouldFail { proxy.blah }
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
}