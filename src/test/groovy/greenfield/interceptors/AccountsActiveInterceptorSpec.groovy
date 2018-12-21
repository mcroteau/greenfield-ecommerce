package greenfield.interceptors


import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class AccountsActiveInterceptorSpec extends Specification implements InterceptorUnitTest<AccountsActiveInterceptor> {

    /**~/(admin_list|admin_create|admin_edit|admin_show)/**/

    void "Test accountsActive interceptor matching"() {
        when: "A request matches the interceptor"
        withRequest(controller: "account", action: "admin_list")

        then: "The interceptor does match"
        interceptor.doesMatch()
    }
}
