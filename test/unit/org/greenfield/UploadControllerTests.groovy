package org.greenfield



import org.junit.*
import grails.test.mixin.*

@TestFor(UploadController)
@Mock(Upload)
class UploadControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/upload/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.uploadInstanceList.size() == 0
        assert model.uploadInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.uploadInstance != null
    }

    void testSave() {
        controller.save()

        assert model.uploadInstance != null
        assert view == '/upload/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/upload/show/1'
        assert controller.flash.message != null
        assert Upload.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/upload/list'

        populateValidParams(params)
        def upload = new Upload(params)

        assert upload.save() != null

        params.id = upload.id

        def model = controller.show()

        assert model.uploadInstance == upload
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/upload/list'

        populateValidParams(params)
        def upload = new Upload(params)

        assert upload.save() != null

        params.id = upload.id

        def model = controller.edit()

        assert model.uploadInstance == upload
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/upload/list'

        response.reset()

        populateValidParams(params)
        def upload = new Upload(params)

        assert upload.save() != null

        // test invalid parameters in update
        params.id = upload.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/upload/edit"
        assert model.uploadInstance != null

        upload.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/upload/show/$upload.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        upload.clearErrors()

        populateValidParams(params)
        params.id = upload.id
        params.version = -1
        controller.update()

        assert view == "/upload/edit"
        assert model.uploadInstance != null
        assert model.uploadInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/upload/list'

        response.reset()

        populateValidParams(params)
        def upload = new Upload(params)

        assert upload.save() != null
        assert Upload.count() == 1

        params.id = upload.id

        controller.delete()

        assert Upload.count() == 0
        assert Upload.get(upload.id) == null
        assert response.redirectedUrl == '/upload/list'
    }
}
