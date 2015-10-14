package org.greenfield

class Specification {

	String name
	String filterName
    int position
	
	Date dateCreated
	Date lastUpdated

    SortedSet specificationOptions

	static hasMany = [ specificationOptions: SpecificationOption, catalogs: Catalog ]
	
	static mapping = {
		sort position: "asc"
        specificationOptions cascade: "all-delete-orphan"
    }

	static constraints = {
		name(nullable:false)
        position(nullable:false)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_PK_SEQ']
    }
	
}
