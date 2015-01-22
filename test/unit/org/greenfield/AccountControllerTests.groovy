package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(AccountController)
@Mock(Account)
class AccountControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/account/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.accountInstanceList.size() == 0
        assert model.accountInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.accountInstance != null
    }

    void testSave() {
        controller.save()

        assert model.accountInstance != null
        assert view == '/account/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/account/show/1'
        assert controller.flash.message != null
        assert Account.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/account/list'

        populateValidParams(params)
        def account = new Account(params)

        assert account.save() != null

        params.id = account.id

        def model = controller.show()

        assert model.accountInstance == account
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/account/list'

        populateValidParams(params)
        def account = new Account(params)

        assert account.save() != null

        params.id = account.id

        def model = controller.edit()

        assert model.accountInstance == account
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/account/list'

        response.reset()

        populateValidParams(params)
        def account = new Account(params)

        assert account.save() != null

        // test invalid parameters in update
        params.id = account.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/account/edit"
        assert model.accountInstance != null

        account.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/account/show/$account.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        account.clearErrors()

        populateValidParams(params)
        params.id = account.id
        params.version = -1
        controller.update()

        assert view == "/account/edit"
        assert model.accountInstance != null
        assert model.accountInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/account/list'

        response.reset()

        populateValidParams(params)
        def account = new Account(params)

        assert account.save() != null
        assert Account.count() == 1

        params.id = account.id

        controller.delete()

        assert Account.count() == 0
        assert Account.get(account.id) == null
        assert response.redirectedUrl == '/account/list'
    }
}
