package org.greenfield.api.payment

import grails.util.Environment
import com.stripe.Stripe
import com.stripe.model.Charge
import org.greenfield.api.payment.PaymentCharge

class StripePaymentsProcessor implements PaymentProcessor {
	
	def applicationService
	def currencyService
	
	StripePaymentsProcessor(applicationService, currencyService){
		this.applicationService = applicationService
		this.currencyService = currencyService
	}

	def charge(amount, token, account){
		
		println "spp 19 -> " +  amount + " : " + token + " : " + account
		
		try{
			def apiKey
			
			println "spp 22 -> " +  amount + " : " + token + " : " + account
			
			if(Environment.current == Environment.DEVELOPMENT) apiKey = applicationService.getStripeDevelopmentApiKey()
			if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getStripeLiveApiKey()
				
			if(!apiKey){
				throw new Exception("Something on our end isn't configured correctly. Please contact support")
			}
			
			println "spp 29 -> " + apiKey + " : " + amount + " : " + token + " : " + account
			
			Stripe.apiKey = apiKey
			def amountInCents = (amount * 100) as Integer
			
			def chargeParams = [
			    'amount': amountInCents, 
			    'currency': currencyService.getCurrency().toLowerCase(), 
			    'source': token, 
			    'description': "Order Placed. Account -> ${account.id} : ${account.username}"
			]
        	
			def charge = Charge.create(chargeParams)
			

			charge.properties.each { 
				println "$it.key -> $it.value" 
			}
			
			if(charge){
				
				def paymentCharge = new PaymentCharge()
				paymentCharge.gateway = PaymentCharge.STRIPE
				paymentCharge.id = result.getTarget().id
				
				return paymentCharge
				
			}else{
				throw new Exception("Something went wrong on our end. Please contact support")
			}
			
			
		}catch(Exception e){
			e.printStackTrace()
		}
		
	}
	
	
	def refund(transactionId){
	
	}
	
}
