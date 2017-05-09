package greenfield.interceptors

import greenfield.common.ControllerConstants

class SettingsActiveInterceptor {

	SettingsActiveInterceptor(){
		match(controller:"configuration", action: ~/(settings|email_settings|stripe_settings|shipping_settings)/)
	}

    boolean before() { 
    	request.settingsActive = ControllerConstants.ACTIVE_CLASS_NAME
    	true 
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
