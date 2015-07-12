package grails.plugin.clojure

import geb.spock.GebSpec
import grails.plugin.clojure.pages.FiboPage
import grails.test.mixin.integration.Integration

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Integration
class DemoControllerSpec extends GebSpec {

    void "canary test"() {
       expect:
       true == true
    }

    void "test calling clojure function"() {
        when:
        go '/demo/simple'

        then:
        $("title").text() == 'A Simple Clojure Function'
    }

    void "test for fibonacci action"() {
        when:
        to FiboPage

        then:
        liRows.size() == 0
    }

    void "test passing params to clojure function"() {
        when:
        go "/demo/fibo?cnt=4"

        then:
        at(FiboPage)
        liRows.size() == 4
    }

    void "test fibonacci result"() {
        when:
        go "/demo/fibo?cnt=4"

        then:
        at(FiboPage)
        liRows*.text() == ['0', '1', '1', '2']
    }

    void "test calling function in non grails namespace"() {
        when:  'called action one'
        go '/demo/one'

        then: 'title should be one::doit'
        $("title").text() == 'one::doit'

        when: 'called action two'
        go '/demo/two'

        then: 'title should be two::doit'
        $("title").text() == 'two::doit'
    }
}
