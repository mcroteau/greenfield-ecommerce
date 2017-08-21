package org.greenfield

class Specification {
	
	Specification(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String name
	String filterName
    int position
	
	Date dateCreated
	Date lastUpdated

	static hasMany = [ specificationOptions: SpecificationOption, catalogs: Catalog ]
	
	static mapping = {
		sort position: "asc"
        specificationOptions cascade: "all-delete-orphan"
    }

	static constraints = {
		uuid(nullable:true)
		name(nullable:false)
        position(nullable:false)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_PK_SEQ']
    }
	
}
