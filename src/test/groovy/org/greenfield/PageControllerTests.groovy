package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(PageController)
@Mock(Page)
class PageControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testCreate() {
        def model = controller.create()
        assert model.pageInstance != null
    }

  
}
