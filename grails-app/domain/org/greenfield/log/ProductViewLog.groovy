package org.greenfield.log

import org.greenfield.Product
import org.greenfield.Account


class ProductViewLog {

	Product product
	Account account
	
	Date dateCreated
	Date lastUpdated
	
	
	static constraints = {
		product(nullable:false)
		account(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_PRODUCT_VIEW_LOG_PK_SEQ']
    }
	
}
