package org.greenfield

class Country {
	
	String name
	
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [states : State]
	
    static constraints = {
		name(unique:true)
		id generator: 'sequence', params:[sequence:'ID_COUNTRY_PK_SEQ']
    }
}
