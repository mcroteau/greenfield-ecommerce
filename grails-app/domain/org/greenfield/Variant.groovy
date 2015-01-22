package org.greenfield

class Variant {

	String name
	double price
	String imageUrl

	int position

	static belongsTo = [ productOption: ProductOption ]
	
	static mapping = {
	    sort position : "asc"
	}
	
    static constraints = {
        name(nullable: false, blank: false)
		price(nullable: false)
		imageUrl(nullable:true)
		position(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_VARIANT_PK_SEQ']
    }
}
