package greenfield

import groovy.text.SimpleTemplateEngine

import org.springframework.dao.DataIntegrityViolationException
import greenfield.common.BaseController
import org.greenfield.common.OrderStatus
import org.greenfield.Transaction
import grails.converters.*

import com.easypost.EasyPost
import com.easypost.model.Rate
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import org.greenfield.api.mail.EasyPostShipmentApi

import com.stripe.Stripe
import com.stripe.model.Charge
import com.stripe.model.Refund

import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.api.payment.StripePaymentsProcessor
import org.greenfield.api.payment.BraintreePaymentsProcessor

@Mixin(BaseController)
class TransactionController {

    static allowedMethods = [ send_confirmation: "POST", update_status: "POST", refund: 'POST', delete: "POST" ]


	def emailService
	def currencyService
	def applicationService
	

	@Secured(['ROLE_ADMIN', 'ROLE_CUSTOMER'])
	def details(Long id){
		authenticatedPermittedOrderDetails { customerAccount, transactionInstance ->
	    	[transactionInstance: transactionInstance]
		}
	}
	
	@Secured(['ROLE_ADMIN'])
    def list(Integer max) {
		authenticatedAdmin { adminAccount ->
        	params.max = Math.min(max ?: 10, 100)
        	params.sort = "id"
        	params.order = "desc"
        	[transactionInstanceList: Transaction.list(params), transactionInstanceTotal: Transaction.count()]
		}
    }
	
	@Secured(['ROLE_ADMIN'])
    def show(Long id) {
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
			[transactionInstance: transactionInstance]
		}
    }


	@Secured(['ROLE_ADMIN'])
	def update_status(Long id){
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
			if(!params.status){
				flash.message = "Please set status before continuing..."
				redirect(action : 'show', id : transactionInstance.id )
				return
			}
			
			transactionInstance.status = params.status
			transactionInstance.save(flush:true)
			flash.message = "Successfully updated Order Status"
			redirect(action : 'show', id : transactionInstance.id )
		}
	}


	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
        	try {
				def shoppingCart = transactionInstance.shoppingCart
				
        	    transactionInstance.delete(flush: true)
				shoppingCart.delete(flush: true)
				
        	    flash.message = "Successfully deleted Order"
        	    redirect(action: "list")
        	}catch (DataIntegrityViolationException e) {
        	    flash.message = "Something went wrong when trying to delete the order.  Please try again."
        	    redirect(action: "show", id: id)
        	}
		
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def confirm_purchase_shipping_label(Long id){
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
			[ transactionInstance : transactionInstance ]
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def purchase_shipping_label(Long id){
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
		
			if(!transactionInstance.shoppingCart.shipmentId){
				flash.message = "Shipment Id needs to be specified"
				redirect( action : 'show', id : id )
				return
			}
			
			if(!transactionInstance.shoppingCart.shipmentRateId){
				flash.message = "Shipment Rate Id needs to be specified"
				redirect( action : 'show', id : id )
				return
			}
			
			
			try{
			
				def shippingApi = new EasyPostShipmentApi(applicationService, currencyService)
				
				
				def shipmentId = transactionInstance.shoppingCart.shipmentId
				def shipmentRateId = transactionInstance.shoppingCart.shipmentRateId
				def postage = shippingApi.buyShippingLabel(shipmentId, shipmentRateId)
				
				if(!postage){
					flash.message = "Something went wrong while processing request to purchase Shipping Label"
					redirect(action: 'show', id : id)
					return
				}
				
				transactionInstance.postageId = postage.id
				transactionInstance.postageUrl = postage.labelUrl
				transactionInstance.save(flush:true)
				
				[ transactionInstance : transactionInstance ]
    
	    	}catch (Exception e) {
				println e
				//TODO: uncomment 
				//e.printStackTrace()
        	    flash.message = "Something went wrong when trying to create Shipping Label. Please try again."
        	    redirect(action: "show", id: id)
				return
        	}
			
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def print_shipping_label(Long id){
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
			[transactionInstance : transactionInstance]
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def confirm_refund(Long id){
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
			[ transactionInstance : transactionInstance ]
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def refund(Long id){
		authenticatedAdminTransaction { adminAccount, transactionInstance ->
		
			try{

				def paymentsProcessor = new StripePaymentsProcessor(applicationService, currencyService)
				println "tr 621 : " + applicationService.getBraintreeEnabled()
				if(applicationService.getBraintreeEnabled() == "true"){
					paymentsProcessor = new BraintreePaymentsProcessor(applicationService, currencyService)
				}
				
				def refundedCharge = paymentsProcessor.refund(transactionInstance.chargeId)
				println "tr 191 -> " + refundedCharge.id
						
				transactionInstance.refundChargeId = refundedCharge.id
				transactionInstance.status = OrderStatus.REFUNDED.description()
				transactionInstance.save(flush:true)
			
				flash.message = "Successfully refunded Order #${id}"
				redirect(action : 'show', id : id)
			
			}catch (Exception e){
				//println e
				e.printStackTrace()
				if(e.message.indexOf("has already been refunded") >= 0){
					if(transactionInstance.status != OrderStatus.REFUNDED.description()){
						transactionInstance.status = OrderStatus.REFUNDED.description()
						transactionInstance.save(flush:true)
					}
					flash.message = "Order #${id} has already been refunded"
				}else{
					flash.message = "Unable to process refund on " + transactionInstance.gateway + ". Please make sure the transaction is settled"
				}
				redirect(action : 'show', id : id)
				return
			}
		}	
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def send_confirmation(Long id){
		def transactionInstance = Transaction.get(id)
		
		if(!transactionInstance){
			flash.message = "Unable to find transaction"
			redirect(action:"")
			return
		}

		try { 
			sendNewOrderEmail(transactionInstance)
		}catch(Exception e){
			e.printStackTrace()
			flash.message = "Something went wrong: " + e + ". Please check your store email settings."
			redirect(action: "show", id: id)
			return
		}	

		flash.message = "Successfully sent email confirmation"
		redirect(action : 'show', id : id)
		return
	}
	
	def sendNewOrderEmail(transaction){
		def fromAddress = applicationService.getSupportEmailAddress()
		def customerToAddress = transaction?.account?.email
		def customerSubject = "${applicationService.getStoreName()} : Your order is placed!"
		
		File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/order_confirmation.html").getFile();
    	
		
		def binding = [ "companyName"  : applicationService.getStoreName(),
			 			"supportEmail" : applicationService.getSupportEmailAddress(),
						"subtotal"     : applicationService.formatPrice(transaction.subtotal),
						"taxes"        : applicationService.formatPrice(transaction.taxes),
						"shipping"     : applicationService.formatPrice(transaction.shipping),
						"total"        : applicationService.formatPrice(transaction.total),
						"transaction"  : transaction,
						"orderNumber"  : transaction.id ]
						
		def engine = new SimpleTemplateEngine()
		def template = engine.createTemplate(templateFile).make(binding)
		def bodyString = template.toString()	
    	
    	
					
		def orderDetails = ""
		transaction.shoppingCart.shoppingCartItems.each {
			def optionsTotal = 0
			def optionsString = "<div style=\"font-size:11px; color:#777\">"
			
			if(it.shoppingCartItemOptions?.size() > 0){
				optionsString += "<strong>options : </strong>"
				it.shoppingCartItemOptions.each(){ option ->
					optionsTotal += option.variant.price
					optionsString += "${option?.variant?.name}"
					optionsString += "(${currencyService.format(applicationService.formatPrice(option.variant.price))})<br/>"
				}	
			}
			optionsString += "</div>"
		
			def productTotal = it.product.price + optionsTotal
    	
			def extendedPrice = productTotal * it.quantity
			
			orderDetails += "<tr>"
			orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">${it.product.id}</td>"
			orderDetails += "<td style=\"padding:3px; border-bottom:dotted 1px #ddd\">${it.product.name}${optionsString}</td>"
			orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">${currencyService.format(applicationService.formatPrice(productTotal))}</td>"
			orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">${it.quantity}</td>"
			orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">${currencyService.format(applicationService.formatPrice(extendedPrice))}</td>"
			orderDetails += "</tr>"
		}
		
		bodyString = bodyString.replace("[[ORDER_DETAILS]]", orderDetails)
		
		def adminEmail = applicationService.getAdminEmailAddress()
		def allEmails = customerToAddress
		if(adminEmail){
		 	allEmails += ",${adminEmail}"
		}
		
		emailService.send(allEmails, fromAddress, customerSubject, bodyString)
		
	}
	
}
