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

    void testIndex() {
        controller.index()
        assert "/page/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.pageInstanceList.size() == 0
        assert model.pageInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.pageInstance != null
    }

    void testSave() {
        controller.save()

        assert model.pageInstance != null
        assert view == '/page/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/page/show/1'
        assert controller.flash.message != null
        assert Page.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/page/list'

        populateValidParams(params)
        def page = new Page(params)

        assert page.save() != null

        params.id = page.id

        def model = controller.show()

        assert model.pageInstance == page
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/page/list'

        populateValidParams(params)
        def page = new Page(params)

        assert page.save() != null

        params.id = page.id

        def model = controller.edit()

        assert model.pageInstance == page
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/page/list'

        response.reset()

        populateValidParams(params)
        def page = new Page(params)

        assert page.save() != null

        // test invalid parameters in update
        params.id = page.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/page/edit"
        assert model.pageInstance != null

        page.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/page/show/$page.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        page.clearErrors()

        populateValidParams(params)
        params.id = page.id
        params.version = -1
        controller.update()

        assert view == "/page/edit"
        assert model.pageInstance != null
        assert model.pageInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/page/list'

        response.reset()

        populateValidParams(params)
        def page = new Page(params)

        assert page.save() != null
        assert Page.count() == 1

        params.id = page.id

        controller.delete()

        assert Page.count() == 0
        assert Page.get(page.id) == null
        assert response.redirectedUrl == '/page/list'
    }
}
