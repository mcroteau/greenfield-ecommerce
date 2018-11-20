package org.greenfield

class Page {
	
	Page(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String title
	String content
	Layout layout
	
	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		content type: "text"
	}
	
    static constraints = {
		uuid(nullable:true)
		title(nullable:false, unique:true)
		content(size:0..65535)
		layout(nullable:false)
		id generator: 'sequence', params:[sequence:'ID_PAGE_PK_SEQ']
    }

}
