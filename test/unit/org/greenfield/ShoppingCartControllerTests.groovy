package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(ShoppingCartController)
@Mock(ShoppingCart)
class ShoppingCartControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/shoppingCart/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.shoppingCartInstanceList.size() == 0
        assert model.shoppingCartInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.shoppingCartInstance != null
    }

    void testSave() {
        controller.save()

        assert model.shoppingCartInstance != null
        assert view == '/shoppingCart/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/shoppingCart/show/1'
        assert controller.flash.message != null
        assert ShoppingCart.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCart/list'

        populateValidParams(params)
        def shoppingCart = new ShoppingCart(params)

        assert shoppingCart.save() != null

        params.id = shoppingCart.id

        def model = controller.show()

        assert model.shoppingCartInstance == shoppingCart
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCart/list'

        populateValidParams(params)
        def shoppingCart = new ShoppingCart(params)

        assert shoppingCart.save() != null

        params.id = shoppingCart.id

        def model = controller.edit()

        assert model.shoppingCartInstance == shoppingCart
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCart/list'

        response.reset()

        populateValidParams(params)
        def shoppingCart = new ShoppingCart(params)

        assert shoppingCart.save() != null

        // test invalid parameters in update
        params.id = shoppingCart.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/shoppingCart/edit"
        assert model.shoppingCartInstance != null

        shoppingCart.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/shoppingCart/show/$shoppingCart.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        shoppingCart.clearErrors()

        populateValidParams(params)
        params.id = shoppingCart.id
        params.version = -1
        controller.update()

        assert view == "/shoppingCart/edit"
        assert model.shoppingCartInstance != null
        assert model.shoppingCartInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/shoppingCart/list'

        response.reset()

        populateValidParams(params)
        def shoppingCart = new ShoppingCart(params)

        assert shoppingCart.save() != null
        assert ShoppingCart.count() == 1

        params.id = shoppingCart.id

        controller.delete()

        assert ShoppingCart.count() == 0
        assert ShoppingCart.get(shoppingCart.id) == null
        assert response.redirectedUrl == '/shoppingCart/list'
    }
}
