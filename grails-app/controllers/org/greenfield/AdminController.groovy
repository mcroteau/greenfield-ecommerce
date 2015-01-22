package org.greenfield

import org.greenfield.BaseController

@Mixin(BaseController)
class AdminController {

    def index() { 
		authenticatedAdmin { adminAccount ->
			[ adminAccount : adminAccount ]
		}
	}
	
}
