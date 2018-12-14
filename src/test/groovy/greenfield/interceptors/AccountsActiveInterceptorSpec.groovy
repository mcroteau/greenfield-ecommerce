package greenfield.interceptors


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AccountsActiveInterceptor)
class AccountsActiveInterceptorSpec extends Specification {

	/**~/(admin_list|admin_create|admin_edit|admin_show)/**/
	
    void "Test accountsActive interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"account", action:"admin_list")

        then:"The interceptor does match"
		    interceptor.doesMatch()
    }
}
