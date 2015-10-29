package org.greenfield

class State {
	
	String name
	
	Country country
	static belongsTo = [ Country ]
	
	static mapping = {
	    sort "name"
	}
		
    static constraints = {
		name(unique:true)
		id generator: 'sequence', params:[sequence:'ID_STATE_PK_SEQ']
    }
}
