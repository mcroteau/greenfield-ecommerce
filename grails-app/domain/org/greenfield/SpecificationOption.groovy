package org.greenfield

class SpecificationOption {
	
	SpecificationOption(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String name
    int position

	Date dateCreated
	Date lastUpdated

	static belongsTo = [ specification: Specification ]

    static mapping = {
    	sort position: "asc"
    }

	static constraints = {
		uuid(nullable:true)
        name(nullable:false)
        position(nullable:false)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_OPTION_PK_SEQ']
    }
	
}
