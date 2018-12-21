package greenfield.interceptors


import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class PagesActiveInterceptorSpec extends Specification implements InterceptorUnitTest<PagesActiveInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    /**~/(list|create|edit|show)/**/
    void "Test pagesActive interceptor matching"() {
        when: "A request matches the interceptor"
        withRequest(controller: "page", action: "list")

        then: "The interceptor does match"
        interceptor.doesMatch()
    }
}
