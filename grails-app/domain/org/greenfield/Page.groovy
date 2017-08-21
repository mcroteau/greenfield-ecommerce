package org.greenfield

class Page {
	
	Page(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String title
	String content
	
	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		content type: "text"
	}
	
    static constraints = {
		uuid(nullable:true)
		title(nullable:false, unique:true)
		content(size:0..65535)
		id generator: 'sequence', params:[sequence:'ID_PAGE_PK_SEQ']
    }

}
