package org.greenfield

class ProductAttribute {

	Attribute attribute
	boolean allowFiltering
	
	static belongsTo = [ product: Product ]


	Date dateCreated
	Date lastUpdated
	
	
	static constraints = {
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_ATTRIBUTE_PK_SEQ']
    }
}
