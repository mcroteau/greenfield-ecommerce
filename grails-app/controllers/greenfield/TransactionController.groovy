package greenfield

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


import com.stripe.Stripe
import com.stripe.model.Charge
import com.stripe.model.Refund

import grails.plugin.springsecurity.annotation.Secured

@Mixin(BaseController)
class TransactionController {

    static allowedMethods = [ update_status: "POST", refund: 'POST', delete: "POST" ]


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
				
				def apiKey
				
				if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getEasyPostTestApiKey()
				if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getEasyPostLiveApiKey()
		
				EasyPost.apiKey = apiKey;
				
				
				Shipment shipment = Shipment.retrieve(transactionInstance.shoppingCart.shipmentId)
				Rate rate = Rate.retrieve(transactionInstance.shoppingCart.shipmentRateId)
				
				/** TODO : allows to specify other label formats
					formats: ZPL, PDF, EPL2, PNG
					defaults to PNG
			    	Map<String, Object> labelMap = new HashMap<String, Object>();
					labelMap.put("file_format", "zpl")
					shipment = shipment.label(labelMap)
				**/
				
				//println "shipment : + " + shipment.toString()

				shipment = shipment.buy(rate);
				
				
				if(!shipment){
					flash.message = "Problems retrieving shipping label.  Please try again"
					redirect(action: 'show', id : id)
					return
				}
				
				if(!shipment.postageLabel?.id || 
					!shipment.postageLabel?.labelUrl){
					flash.message = "Something went wrong while processing request to purchase Shipping Label"
					redirect(action: 'show', id : id)
					return
				}
	
				transactionInstance.postageId = shipment.postageLabel.id
				transactionInstance.postageUrl = shipment.postageLabel.labelUrl
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

				def apiKey
			
				if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getStripeDevelopmentApiKey()
				if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getStripeLiveApiKey()

			
				Map<String, Object> params = new HashMap<String, Object>();
				Charge charge = Charge.retrieve(transactionInstance.chargeId);
			
				if(!charge){
					flash.message = "Stripe was unable to refund the charge. Please try again or manually refund via Stripe."
					redirect(action: 'show', id : id)
					return
				}

				Charge refundedCharge = charge.refund(params);
				
				if(!refundedCharge){
					flash.message = "Stripe was unable to refund the charge. Please try again or manually refund via Stripe."
					redirect(action: 'show', id : id)
					return
				}
			
				transactionInstance.status = OrderStatus.REFUNDED.description()
				transactionInstance.save(flush:true)
			
				flash.message = "Successfully refunded Order #${id}"
				redirect(action : 'show', id : id)
			
			}catch (Exception e){
				//println e
				if(e.message.indexOf("has already been refunded") >= 0){
					if(transactionInstance.status != OrderStatus.REFUNDED.description()){
						transactionInstance.status = OrderStatus.REFUNDED.description()
						transactionInstance.save(flush:true)
					}
					flash.message = "Order <strong>#${id}</strong> has already been refunded"
				}else{
					flash.message = "Unable to process refund."
				}
				redirect(action : 'show', id : id)
				return
			}
		}	
	}
	
	
}
