package greenfield.interceptors


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LayoutActiveInterceptor)
class LayoutActiveInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }
	/**~/(index|tags|how)/**/
    void "Test layoutActive interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"layout", action: "index")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
