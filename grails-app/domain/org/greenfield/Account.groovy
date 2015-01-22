package org.greenfield

import org.greenfield.common.RoleName

class Account {
	
	Date dateCreated
	Date lastUpdated
	
    String username
	String email
    String passwordHash
    
	String name
	
	String ipAddress
	
	String resetUUID
	
	boolean hasAdminRole
	
	//TODO: Replace with Address
	//TODO: Scheme for international 
	String address1
	String address2
	
	String city
	State state
	String zip
	
	String phone
	
	boolean addressVerified
	
	static hasMany = [ roles: Role, permissions: String, transactions : Transaction ]
	
	static constraints = {
		name(blank:true, nullable:true)
		address1(nullable:true)
		address2(nullable:true)
		city(nullable:true)
		state(nullable:true)
		zip(nullable:true)
		phone(nullable:true)
		hasAdminRole(nullabe:true)
		addressVerified(nullable:true, default:true)
		ipAddress(blank:true, nullable:true)
		resetUUID(nullable:true)
        username(size:5..15, nullable: false, blank: false, unique: true)
        email(email:true, nullable: false, blank: false, unique: true)
		passwordHash(minSize: 5, nullable:false, blank:false)
		id generator: 'sequence', params:[sequence:'ID_ACCOUNT_PK_SEQ']
    }
	
}
