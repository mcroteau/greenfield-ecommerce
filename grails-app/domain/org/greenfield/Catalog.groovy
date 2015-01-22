package org.greenfield

class Catalog {

	String name
	String description

	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		description type: "text"
	}
	
	static constraints = {
		name(unique:true)
		description(nullable:true,size:0..65535)		
		id generator: 'sequence', params:[sequence:'ID_CATALOG_PK_SEQ']
    }
}
