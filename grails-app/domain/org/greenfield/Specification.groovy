package org.greenfield

class Specification {

	String name

	Date dateCreated
	Date lastUpdated


	static belongsTo = [ catalog: Catalog ]
	
	static hasMany = [ specificationOptions: SpecificationOption ]
	
	
	static mapping = {
		sort name: "asc"
	}
	
	static constraints = {
		name(nullable:false, unique:true)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_PK_SEQ']
    }
}
