package org.greenfield

class Product {

	String productNo
	
	String name
	String description
	
	int quantity
	BigDecimal price
	
	String imageUrl
	String detailsImageUrl
	
	Date dateCreated
	Date lastUpdated
	
	boolean disabled
	
	double length
	double width
	double height
	double weight
	
	
	static hasMany = [ catalogs: Catalog, additionalPhotos: AdditionalPhoto, productOptions: ProductOption, productSpecifications: ProductSpecification ]
	
	
	static mapping = {
		description type: "text"
        additionalPhotos cascade: 'all-delete-orphan'
        productOptions cascade: 'all-delete-orphan'
	}
	
    static constraints = {
		length(nullable:true)
		width(nullable:true)
		height(nullable:true)
		weight(nullable:false)
		productNo(nullable:true)
		name(nullable:false, unique:true)
		description(nullable:true,size:0..65535)
		quantity(nullable:false)
		price(nullable:false)
		imageUrl(nullable:true)
		disabled(nullable:true)
		detailsImageUrl(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_PK_SEQ']
    }
}
