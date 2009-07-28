class DemoControllerFunctionalTests extends functionaltestplugin.FunctionalTestCase {
    void testCallingClojureFunction() {
        get '/demo/simple'
        assertStatus 200
        assertContentContains 'A Simple Clojure Function'
    }
    
    void testPassingParamsToClojureFunction() {
        get '/fibonacci/5'
        assertStatus 200
        assertContentContains '<li>0</li>'
        assertContentContains '<li>1</li>'
        assertContentContains '<li>2</li>'
        assertContentContains '<li>3</li>'
    }
    
    void testCallingFunctionInNonGrailsNamespace() {
        get '/demo/two'
        assertStatus 200
        assertContentContains 'two::doit'
        get '/demo/one'
        assertStatus 200
        assertContentContains 'one::doit'
    }
}
