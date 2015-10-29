package org.greenfield.common

public enum ShoppingCartStatus {

	ACTIVE('ACTIVE'),
	TRANSACTION('TRANSACTION')
	
	private final String description
	
	ShoppingCartStatus(String description){
		this.description = description
	}
	
	public String description(){
		return this.description
	}
	
}
