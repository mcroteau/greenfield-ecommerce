package org.greenfield

class Country {
	
	Country(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String name
	
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [states : State]
	
    static constraints = {
		uuid(nullable:true)
		name(unique:true)
		id generator: 'sequence', params:[sequence:'ID_COUNTRY_PK_SEQ']
    }
}
