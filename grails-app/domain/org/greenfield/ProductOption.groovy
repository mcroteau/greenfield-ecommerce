package org.greenfield

class ProductOption {
	
	ProductOption(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String name
	int position

	static belongsTo = [ product: Product ]
	
	static hasMany = [ variants: Variant ]
	
	static mapping = {
	    sort position: "asc"
        variants cascade: "all-delete-orphan"
	}
	
    static constraints = {
		uuid(nullable:true)
        name(nullable: false, blank: false)
		position(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_OPTION_PK_SEQ']
    }
}
