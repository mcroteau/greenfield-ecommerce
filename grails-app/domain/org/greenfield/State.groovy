package org.greenfield

class State {
	
	State(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid
	
	String name
	
	Country country
	static belongsTo = [ Country ]
	
	static mapping = {
	    sort "name"
	}
		
    static constraints = {
		uuid(nullable:true)
		name(unique:true)
		id generator: 'sequence', params:[sequence:'ID_STATE_PK_SEQ']
    }
}
