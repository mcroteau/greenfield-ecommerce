package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(CatalogController)
@Mock(Catalog)
class CatalogControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/catalog/list" == response.redirectedUrl
    }

}
