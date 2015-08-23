package org.greenfield

class Specification {

	String name

	Date dateCreated
	Date lastUpdated
	
	static hasMany = [ attributes: Attribute ]
	
	
	static mapping = {
		sort name: "asc"
	}
	
	static constraints = {
		name(nullable:false, unique:true)
		id generator: 'sequence', params:[sequence:'ID_FILTER_OPTION_PK_SEQ']
    }
}
