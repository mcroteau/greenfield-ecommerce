package org.greenfield.common

public enum OrderStatus {

	OPEN('OPEN'),
	COMPLETED('COMPLETED'),
	RECEIVED('RECEIVED'),
	SHIPPED('SHIPPED'),
	CANCELED('CANCELED'),
	REFUNDED('REFUNDED'),
	FAILED('FAILED')
	
	private final String description
	
	OrderStatus(String description){
		this.description = description
	}
	
	public String description(){
		return this.description
	}
	
}
