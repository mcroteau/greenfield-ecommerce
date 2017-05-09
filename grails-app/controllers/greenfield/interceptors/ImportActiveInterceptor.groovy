package greenfield.interceptors

import greenfield.common.ControllerConstants

class ImportActiveInterceptor {

	ImportActiveInterceptor(){
		match(controller:"configuration", action: ~/(index|uploads|import_products_view)/)
	}

    boolean before() { 
    	request.importActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
