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
		roleMaintenance(controller: "role", action: "(create|edit|save|update|delete|list)") {
		    before = {
		        accessControl {
		            role("ROLE_ADMIN")
		        }
		    }
		}
    }
}
