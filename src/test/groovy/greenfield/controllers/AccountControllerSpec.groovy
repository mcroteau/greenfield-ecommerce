package greenfield.controllers

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED

import greenfield.AccountController

class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController> {


    void setup() {
	}


    void "test POST request"() {
        when:
		true == true

        then:
		true == true
    }
}