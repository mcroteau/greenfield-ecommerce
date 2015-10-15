package org.greenfield

class SpecificationOption {
    //TODO:remove comparable functionality
//implements Comparable {

	String name
    int position

	Date dateCreated
	Date lastUpdated

	static belongsTo = [ specification: Specification ]

	//TODO:remove or refactor
	static hasMany = [ products: Product ]

    //used for generic findAll call
    static mapping = {
    	sort position: "asc"
    }

    //used for specification hasMany relationship
	//int compareTo(obj) {
    //    position.compareTo(obj.position)
	//}

	static constraints = {
        name(nullable:false)
        position(nullable:false)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_OPTION_PK_SEQ']
    }
	
}
