package org.greenfield.log

import org.greenfield.Catalog
import org.greenfield.Account


class CatalogViewLog {

	Catalog catalog
	Account account
	
	Date dateCreated
	Date lastUpdated
	
	
    static constraints = {
		catalog(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_CATALOG_VIEW_LOG_PK_SEQ']
    }
	
}
