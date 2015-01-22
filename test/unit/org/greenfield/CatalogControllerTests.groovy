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

    void testList() {

        def model = controller.list()

        assert model.catalogInstanceList.size() == 0
        assert model.catalogInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.catalogInstance != null
    }

    void testSave() {
        controller.save()

        assert model.catalogInstance != null
        assert view == '/catalog/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/catalog/show/1'
        assert controller.flash.message != null
        assert Catalog.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/catalog/list'

        populateValidParams(params)
        def catalog = new Catalog(params)

        assert catalog.save() != null

        params.id = catalog.id

        def model = controller.show()

        assert model.catalogInstance == catalog
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/catalog/list'

        populateValidParams(params)
        def catalog = new Catalog(params)

        assert catalog.save() != null

        params.id = catalog.id

        def model = controller.edit()

        assert model.catalogInstance == catalog
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/catalog/list'

        response.reset()

        populateValidParams(params)
        def catalog = new Catalog(params)

        assert catalog.save() != null

        // test invalid parameters in update
        params.id = catalog.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/catalog/edit"
        assert model.catalogInstance != null

        catalog.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/catalog/show/$catalog.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        catalog.clearErrors()

        populateValidParams(params)
        params.id = catalog.id
        params.version = -1
        controller.update()

        assert view == "/catalog/edit"
        assert model.catalogInstance != null
        assert model.catalogInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/catalog/list'

        response.reset()

        populateValidParams(params)
        def catalog = new Catalog(params)

        assert catalog.save() != null
        assert Catalog.count() == 1

        params.id = catalog.id

        controller.delete()

        assert Catalog.count() == 0
        assert Catalog.get(catalog.id) == null
        assert response.redirectedUrl == '/catalog/list'
    }
}
