package org.greenfield.log

import org.greenfield.Page
import org.greenfield.Account


class PageViewLog {
	
	PageViewLog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Page page
	Account account
	
	Date dateCreated
	Date lastUpdated
	
	static constraints = {
		uuid(nullable:true)
		page(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PAGE_VIEW_LOG_PK_SEQ']
    }
	
}
