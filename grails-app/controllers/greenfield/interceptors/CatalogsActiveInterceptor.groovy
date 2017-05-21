package greenfield.interceptors

import greenfield.common.ControllerConstants

class CatalogsActiveInterceptor {

	CatalogsActiveInterceptor(){
		match(controller:"catalog", action: ~/(list|create|edit|show)/)
	}

    boolean before() { 
    	request.catalogsActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
