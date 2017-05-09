package greenfield.interceptors

import greenfield.common.ControllerConstants

class LayoutActiveInterceptor {

	LayoutActiveInterceptor(){
		match(controller:"layout", action: ~/(index|tags|how)/)
	}

    boolean before() { 
    	request.layoutActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() { }
}
