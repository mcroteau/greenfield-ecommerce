package org.greenfield

class CatalogProductSpecification {
	
	SpecificationOption specificationOption
	Catalog catalog
	
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [products : Product]
	
    static constraints = {
		id generator: 'sequence', params:[sequence:'ID_CATALOG_PRODUCT_SPECIFICATION_PK_SEQ']
    }
}
