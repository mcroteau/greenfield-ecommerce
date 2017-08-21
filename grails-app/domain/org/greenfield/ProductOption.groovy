package org.greenfield

class ProductOption {
	
	ProductOption(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String name

	static belongsTo = [ product: Product ]
	
	static hasMany = [ variants: Variant ]
	
	static mapping = {
	    sort "name"
        variants cascade: "all-delete-orphan"
	}
	
    static constraints = {
		uuid(nullable:true)
        name(nullable: false, blank: false)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_OPTION_PK_SEQ']
    }
}
