package org.greenfield.common

public enum RoleName {

	ROLE_ADMIN('ROLE_ADMIN'),
	ROLE_CUSTOMER('ROLE_CUSTOMER'),
	ROLE_SALESMAN('ROLE_SALESMAN'),
	ROLE_AFFILIATE('ROLE_AFFILIATE'),
	ROLE_CUSTOMER_SERVICE('ROLE_CUSTOMER_SERVICE')
	
	private final String description
	
	RoleName(String description){
		this.description = description
	}
	
	public String description(){
		return this.description
	}
	
}
