package org.greenfield

class SpecificationOption {

	String name

	Date dateCreated
	Date lastUpdated

	static belongsTo = [ specification: Specification ]

	static hasMany = [ products: Product ]
	
	static mapping = {
		sort name: "asc"
	}
	
	static constraints = {
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_OPTION_PK_SEQ']
    }
	
}
