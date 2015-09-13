package org.greenfield

class CatalogProductSpecification {

	Catalog catalog
	SpecificationOption specificationOption
	
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [ products : Product ]
	
    static constraints = {
		id generator: 'sequence', params:[sequence:'ID_CATALOG_PRODUCT_SPECIFICATION_PK_SEQ']
    }
}
