package org.greenfield.log

import org.greenfield.Account


class SearchLog {

	String query
	Account account
	
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		query(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_SEARCH_LOG_PK_SEQ']
    }
	
}
