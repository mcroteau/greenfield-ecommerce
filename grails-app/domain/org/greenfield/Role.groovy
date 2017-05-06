package org.greenfield

class Role {

	String authority

	Role(String authority) {
		this()
		this.authority = authority
	}

	static constraints = {
		authority blank: false, unique: true
		id generator: 'sequence', params:[sequence:'ID_ROLE_PK_SEQ']
	}

}
