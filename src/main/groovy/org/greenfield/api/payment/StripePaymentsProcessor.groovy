package org.greenfield.api.payment


class StripePaymentsProcessor implements PaymentProcessor {
	
	def applicationService
	def currencyService
	
	StripePaymentsProcessor(applicationService, currencyService){
		this.applicationService = applicationService
		this.currencyService = currencyService
	}

	def charge(amount, token){
	}
	
	
	def refund(transactionId){
	
	}
	
}
