package greenfield.controllers

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED

import greenfield.AccountController

class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController> {

    void setup() {
		println "here... setting up..."
		def controller = (AccountController)mockController(AccountController)
	}
	
    void "test GET request"() {
		when:
		def controller = (AccountController)mockController(AccountController)
        controller.index()

        then:
        response.text == 'Success'
    }

    void "test POST request"() {
        when:
		def controller = (AccountController)mockController(AccountController)
        request.method = 'POST'
        controller.customer_profile()

        then:
        response.status == METHOD_NOT_ALLOWED.value()
    }
}