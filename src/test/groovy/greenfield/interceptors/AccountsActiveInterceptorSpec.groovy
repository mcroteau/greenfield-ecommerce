package greenfield.interceptors


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AccountsActiveInterceptor)
class AccountsActiveInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test accountsActive interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"accountsActive")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
