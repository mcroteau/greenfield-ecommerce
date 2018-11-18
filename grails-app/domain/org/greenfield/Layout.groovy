package org.greenfield

class Layout {
	
	Layout(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String name
	String content
	String css
	String javascript
	boolean defaultLayout
	
	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		content type: "text"
	}
	
	static constraints = {
		uuid(nullable:true)
		name(nullable:false, unique:true)
		content(blank:false, nullable:false, size:0..65535)
		css(blank:true, nullable:true, size:0..65535)
		javascript(blank:true, nullable:true, size:0..65535)
		defaultLayout(nullable:false, default:false)
		id generator: 'sequence', params:[sequence:'ID_LAYOUT_PK_SEQ']
    }
	
	public String toString(){
		return this.content
	}
}
