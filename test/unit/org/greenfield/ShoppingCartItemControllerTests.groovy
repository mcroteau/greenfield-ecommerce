package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(ShoppingCartItemController)
@Mock(ShoppingCartItem)
class ShoppingCartItemControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/shoppingCartItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.shoppingCartItemInstanceList.size() == 0
        assert model.shoppingCartItemInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.shoppingCartItemInstance != null
    }

    void testSave() {
        controller.save()

        assert model.shoppingCartItemInstance != null
        assert view == '/shoppingCartItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/shoppingCartItem/show/1'
        assert controller.flash.message != null
        assert ShoppingCartItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCartItem/list'

        populateValidParams(params)
        def shoppingCartItem = new ShoppingCartItem(params)

        assert shoppingCartItem.save() != null

        params.id = shoppingCartItem.id

        def model = controller.show()

        assert model.shoppingCartItemInstance == shoppingCartItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCartItem/list'

        populateValidParams(params)
        def shoppingCartItem = new ShoppingCartItem(params)

        assert shoppingCartItem.save() != null

        params.id = shoppingCartItem.id

        def model = controller.edit()

        assert model.shoppingCartItemInstance == shoppingCartItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCartItem/list'

        response.reset()

        populateValidParams(params)
        def shoppingCartItem = new ShoppingCartItem(params)

        assert shoppingCartItem.save() != null

        // test invalid parameters in update
        params.id = shoppingCartItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/shoppingCartItem/edit"
        assert model.shoppingCartItemInstance != null

        shoppingCartItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/shoppingCartItem/show/$shoppingCartItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        shoppingCartItem.clearErrors()

        populateValidParams(params)
        params.id = shoppingCartItem.id
        params.version = -1
        controller.update()

        assert view == "/shoppingCartItem/edit"
        assert model.shoppingCartItemInstance != null
        assert model.shoppingCartItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCartItem/list'

        response.reset()

        populateValidParams(params)
        def shoppingCartItem = new ShoppingCartItem(params)

        assert shoppingCartItem.save() != null
        assert ShoppingCartItem.count() == 1

        params.id = shoppingCartItem.id

        controller.delete()

        assert ShoppingCartItem.count() == 0
        assert ShoppingCartItem.get(shoppingCartItem.id) == null
        assert response.redirectedUrl == '/shoppingCartItem/list'
    }
}
