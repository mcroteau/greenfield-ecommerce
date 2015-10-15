package org.greenfield

class SpecificationOption {

	String name
    int position

	Date dateCreated
	Date lastUpdated

	static belongsTo = [ specification: Specification ]

	//TODO:remove or refactor
	//static hasMany = [ products: Product ]

    static mapping = {
    	sort position: "asc"
    }
        
    //TODO:remove    
    //def beforeInsert(){
    //    def existingOption = SpecificationOption.findByNameAndSpecification(name, specification)
    //    if(existingOption){
    //        return false
    //    } 
    //}

	static constraints = {
        name(nullable:false)
        position(nullable:false)
		id generator: 'sequence', params:[sequence:'ID_SPECIFICATION_OPTION_PK_SEQ']
    }
	
}
