package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(AccountController)
@Mock(Account)
class AccountControllerTests {

	//TODO:add controller action tests
	
    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }


    void testCustomer_registration() {
        controller.customer_registration()
        assert flash.message == null
    }


}
