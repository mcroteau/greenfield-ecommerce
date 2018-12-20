package greenfield.interceptors


import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class SettingsActiveInterceptorSpec extends Specification implements InterceptorUnitTest<SettingsActiveInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    /**~/(settings|email_settings|stripe_settings|shipping_settings)/**/
    void "Test settingsActive interceptor matching"() {
        when: "A request matches the interceptor"
        withRequest(controller: "configuration", action: "settings")

        then: "The interceptor does match"
        interceptor.doesMatch()
    }
}
