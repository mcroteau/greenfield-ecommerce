package org.greenfield

class Permission {
	
	Permission(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

   	Account account
   	String permission

   	static constraints = {
		uuid(nullable:true)
   	}
}

