package org.greenfield

class Product {
	
	Product(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String name
	String description
	
	int quantity
	BigDecimal price
	BigDecimal salesPrice
	
	String imageUrl
	String detailsImageUrl
	
	Date dateCreated
	Date lastUpdated
	
	boolean disabled
	
	double length
	double width
	double height
	double weight

	String productNo
	
	Layout layout
	
	static hasMany = [ catalogs: Catalog, additionalPhotos: AdditionalPhoto, productOptions: ProductOption, productSpecifications: ProductSpecification ]
	
	
	static mapping = {
		description type: "text"
        additionalPhotos cascade: 'all-delete-orphan'
        productOptions cascade: 'all-delete-orphan'
        productSpecifications cascade: "all-delete-orphan"
	}
	
    static constraints = {
		uuid(nullable:true)
		name(nullable:false, unique:true)
		description(nullable:true,size:0..65535)
		quantity(nullable:false)
		price(nullable:false)
		salesPrice(nullable:true)
		imageUrl(nullable:true)
		disabled(nullable:true)
		detailsImageUrl(nullable:true)
		length(nullable:true)
		width(nullable:true)
		height(nullable:true)
		weight(nullable:false)
		productNo(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_PK_SEQ']
    }
}
