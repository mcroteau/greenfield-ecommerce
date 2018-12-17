package org.greenfield.api.payment

interface PaymentProcessor {
	
	/** 
		@return
	**/
	def charge(amount, token)
	
	/**
		@return 
	**/
	def refund(transactionId)

}