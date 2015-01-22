package org.greenfield

class Layout {

	String name
	String content
	
	Date dateCreated
	Date lastUpdated
	
	static mapping = {
		content type: "text"
	}
	
	static constraints = {
		name(unique:true)
		content(size:0..65535)
		id generator: 'sequence', params:[sequence:'ID_LAYOUT_PK_SEQ']
    }
	
	public String toString(){
		return this.content
	}
}
