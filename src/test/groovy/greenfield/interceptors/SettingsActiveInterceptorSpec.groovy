package greenfield.interceptors


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SettingsActiveInterceptor)
class SettingsActiveInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

	/**~/(settings|email_settings|stripe_settings|shipping_settings)/**/
    void "Test settingsActive interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"configuration", action:"settings")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
