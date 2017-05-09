package greenfield.interceptors

import greenfield.common.ControllerConstants

class PagesActiveInterceptor {

	PagesActiveInterceptor(){
		match(controller:"page", action: ~/(list|create|edit|show)/)
	}

    boolean before() { 
    	request.pagesActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
