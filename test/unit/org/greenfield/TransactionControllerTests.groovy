package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(TransactionController)
@Mock(Transaction)
class TransactionControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/transaction/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.transactionInstanceList.size() == 0
        assert model.transactionInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.transactionInstance != null
    }

    void testSave() {
        controller.save()

        assert model.transactionInstance != null
        assert view == '/transaction/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/transaction/show/1'
        assert controller.flash.message != null
        assert Transaction.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/transaction/list'

        populateValidParams(params)
        def transaction = new Transaction(params)

        assert transaction.save() != null

        params.id = transaction.id

        def model = controller.show()

        assert model.transactionInstance == transaction
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/transaction/list'

        populateValidParams(params)
        def transaction = new Transaction(params)

        assert transaction.save() != null

        params.id = transaction.id

        def model = controller.edit()

        assert model.transactionInstance == transaction
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/transaction/list'

        response.reset()

        populateValidParams(params)
        def transaction = new Transaction(params)

        assert transaction.save() != null

        // test invalid parameters in update
        params.id = transaction.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/transaction/edit"
        assert model.transactionInstance != null

        transaction.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/transaction/show/$transaction.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        transaction.clearErrors()

        populateValidParams(params)
        params.id = transaction.id
        params.version = -1
        controller.update()

        assert view == "/transaction/edit"
        assert model.transactionInstance != null
        assert model.transactionInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/transaction/list'

        response.reset()

        populateValidParams(params)
        def transaction = new Transaction(params)

        assert transaction.save() != null
        assert Transaction.count() == 1

        params.id = transaction.id

        controller.delete()

        assert Transaction.count() == 0
        assert Transaction.get(transaction.id) == null
        assert response.redirectedUrl == '/transaction/list'
    }
}
