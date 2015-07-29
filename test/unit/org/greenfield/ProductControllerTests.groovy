package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(ProductController)
@Mock(Product)
class ProductControllerTests {

	//TODO : add tests
	
    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert flash.message == null
    }
	

    void testSearch() {
        controller.search()
        assert flash.message == "Search query must be at least 4 characters"
    }
    
}
