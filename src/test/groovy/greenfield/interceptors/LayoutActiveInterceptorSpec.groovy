package greenfield.interceptors


import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class LayoutActiveInterceptorSpec extends Specification implements InterceptorUnitTest<LayoutActiveInterceptor> {

    def setup() {
    }

    def cleanup() {

    }
    /**~/(index|tags|how)/**/
    void "Test layoutActive interceptor matching"() {
        when: "A request matches the interceptor"
        withRequest(controller: "layout", action: "index")

        then: "The interceptor does match"
        interceptor.doesMatch()
    }
}
