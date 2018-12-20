package greenfield.interceptors


import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class ImportActiveInterceptorSpec extends Specification implements InterceptorUnitTest<ImportActiveInterceptor> {

    def setup() {
    }

    def cleanup() {

    }
    /**~/(index|uploads|import_products_view)/**/
    void "Test importActive interceptor matching"() {
        when: "A request matches the interceptor"
        withRequest(controller: "configuration", action: "index")

        then: "The interceptor does match"
        interceptor.doesMatch()
    }
}
