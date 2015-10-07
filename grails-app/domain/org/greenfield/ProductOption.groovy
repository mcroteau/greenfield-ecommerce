package org.greenfield

class ProductOption {

	String name

	static belongsTo = [ product: Product ]
	
	static hasMany = [ variants: Variant ]
	
	static mapping = {
	    sort "name"
        variants cascade: "all-delete-orphan"
	}
	
    static constraints = {
        name(nullable: false, blank: false)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_OPTION_PK_SEQ']
    }
}
