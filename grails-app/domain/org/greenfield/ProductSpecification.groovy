package org.greenfield

class ProductSpecification {
	
	ProductSpecification(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	SpecificationOption specificationOption
	
	static belongsTo = [ product: Product, specification: Specification ]

	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		uuid(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_SPECIFICATION_PK_SEQ']
    }
	
}
