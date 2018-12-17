package org.greenfield.api.payment

interface PaymentProcessor {
	
	/** 
		@return
	**/
	def charge(amount, token, account)
	
	/**
		@return 
	**/
	def refund(transactionId)

}