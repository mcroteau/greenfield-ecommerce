package org.greenfield

import greenfield.common.ControllerConstants

import org.greenfield.common.RoleName
import org.greenfield.Permission
import org.greenfield.Role

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

	def pageViews
	def catalogViews
	def productViews
	def searches

	//TODO:reconsider transients
 	//static transients = ['pageViews', 'catalogViews', 'productViews', 'searches']

	//TODO:Remove cleanup
	//static hasMany = [ authorities: AccountRole, permissions: Permission, transactions : Transaction ]
	//static hasMany = [ permissions: Permission, transactions : Transaction ]
	static hasMany = [ permissions: Permission, transactions : Transaction ]
	
	Set<Role> getAuthorities() {
		AccountRole.findAllByAccount(this)*.role
	}

	def createAccountPermission(){
		createPermission(ControllerConstants.ACCOUNT_PERMISSION + this.id)
	}

	def createTransactionPermission(transaction){
		createPermission(ControllerConstants.TRANSACTION_PERMISSION + transaction.id)
	}

	def createShoppingCartPermission(shoppingCart){
		createPermission(ControllerConstants.SHOPPING_CART_PERMISSION + shoppingCart.id)
	}


	def createAdminAccountRole(){
		def adminRole = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
		createAccountRole(adminRole)
	}

	def createAccountRoles(includeAdminRole){
		this.hasAdminRole = false
		this.save(flush:true)

		def role = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
		createAccountRole(role)
	
		if(includeAdminRole){
			def adminRole = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
			createAccountRole(adminRole)
			this.hasAdminRole = true
		}

		this.save(flush:true)
	}

	def createAccountRole(role){
		def accountRole = new AccountRole()
		accountRole.account = this
		accountRole.role = role
		accountRole.save(flush:true)	
	}

	def createPermission(permissionString){
		def permission = new Permission()
		//TODO:Remove cleanup
		//Suggested domain field think `user` will conflict with database user table
		//permission.user = this
		permission.account = this
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
