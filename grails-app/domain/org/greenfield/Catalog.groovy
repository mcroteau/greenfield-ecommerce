package org.greenfield

class Catalog {

	String name
	String description

	Date dateCreated
	Date lastUpdated
	
	boolean toplevel
	
	Catalog parentCatalog
	//TODO:remove
	//static hasMany = [ subcatalogs: Catalog, specifications : Specification ]
	static hasMany = [ subcatalogs: Catalog ]

	static mappedBy = [ specifications: "catalog" ]

	static mapping = {
		sort name: "asc"
		description type: "text"
		subcatalogs sort: "name", order: "asc"
	}
	
	static constraints = {
		name(unique:true)
		description(nullable:true,size:0..65535)			
		parentCatalog(nullable:true)
		toplevel(nullable:true)	
		id generator: 'sequence', params:[sequence:'ID_CATALOG_PK_SEQ']
    }
}
