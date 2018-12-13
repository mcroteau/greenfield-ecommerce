package greenfield.interceptors


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ImportActiveInterceptor)
class ImportActiveInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }
	/**~/(index|uploads|import_products_view)/**/
    void "Test importActive interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"configuration", action: "index")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
