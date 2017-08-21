package org.greenfield

class Catalog {
	
	Catalog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String name
	String description

	Date dateCreated
	Date lastUpdated
	
	boolean toplevel
	int position

	Catalog parentCatalog

	static hasMany = [ subcatalogs: Catalog ]

	static mapping = {
		sort position: "asc"
		description type: "text"
		subcatalogs sort: "position", order: "asc"
	}
	
	static constraints = {
		uuid(nullable:true)
		name(unique:true)
		description(nullable:true,size:0..65535)			
		parentCatalog(nullable:true)
		toplevel(nullable:true)	
		position(defaultValue: 0)
		id generator: 'sequence', params:[sequence:'ID_CATALOG_PK_SEQ']
    }
}
