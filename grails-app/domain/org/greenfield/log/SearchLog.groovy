package org.greenfield.log

import org.greenfield.Account


class SearchLog {
	
	SearchLog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String query
	Account account
	
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		uuid(nullable:true)
		query(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_SEARCH_LOG_PK_SEQ']
    }
	
}
