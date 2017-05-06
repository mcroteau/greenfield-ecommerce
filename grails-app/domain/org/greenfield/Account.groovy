package org.greenfield

import org.greenfield.common.RoleName
import org.greenfield.Permission

class Account {
	
    String username
	String email
    String password
	
	String name	
	String ipAddress
	String resetUUID
	
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

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
	
	Date dateCreated
	Date lastUpdated

	//static hasMany = [ authorities: AccountRole, permissions: Permission, transactions : Transaction ]
	static hasMany = [ permissions: Permission, transactions : Transaction ]
	
	Set<Role> getAuthorities() {
		AccountRole.findAllByAccount(this)*.role
	}

	def createAccountPermissions(){
		createPermission("account:customer_profile:${this.id}")
		createPermission("account:customer_update:${this.id}")
		createPermission("account:customer_order_history:${this.id}")
	}

	def createPermission(permissionString){
		def permission = new Permission()
		permission.user = this
		permission.permission = permissionString
		permission.save(flush:true)

		this.addToPermissions(permission)
		this.save(flush:true)
	}

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
		password(minSize: 5, nullable:false, blank:false, column: '`password`')
		id generator: 'sequence', params:[sequence:'ID_ACCOUNT_PK_SEQ']
    }
	
}
