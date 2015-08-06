package org.greenfield

class ShiroSecurityFilters {
    def filters = {
		admin(uri: "/admin/**") {
	    	before = {
				accessControl {
				    role("ROLE_ADMIN")
				}
	    	}
		}
    }
}
