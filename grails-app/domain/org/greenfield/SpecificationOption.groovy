package org.greenfield

class SpecificationOption {

	String name

	Date dateCreated
	Date lastUpdated
	
	static hasMany = [ products: Product ]
	
	
	static mapping = {
		sort name: "asc"
	}
	
	static constraints = {
		name(nullable:false, unique:true)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_OPTION_PK_SEQ']
    }
}
