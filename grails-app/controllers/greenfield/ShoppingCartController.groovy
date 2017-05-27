package greenfield

import org.springframework.dao.DataIntegrityViolationException

import org.greenfield.common.ShoppingCartStatus
import org.greenfield.common.OrderStatus
import org.greenfield.common.RoleName
import org.apache.shiro.SecurityUtils
import greenfield.common.BaseController
import com.stripe.Stripe
import com.stripe.model.Charge
import grails.util.Environment
import groovy.text.SimpleTemplateEngine
import grails.converters.*

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import org.greenfield.Account
import org.greenfield.Product
import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCartItemOption
import org.greenfield.Transaction
import org.greenfield.Permission
import org.greenfield.Variant
import org.greenfield.State

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class ShoppingCartController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def applicationService
	def emailService

	//Grails 2 explicitly set
    //def grailsApplication


	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
    def index() {	
		authenticatedAccount { customerAccount ->	
			def shoppingCartInstance = ShoppingCart.findByAccountAndStatus(customerAccount, ShoppingCartStatus.ACTIVE.description())
			calculateShoppingCartSubtotal(shoppingCartInstance)
			[shoppingCartInstance : shoppingCartInstance]			
		}
    }


	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
	def view(){
		redirect(action : 'index')
	}
	
	
	def checkout(){

		authenticatedAccount { customerAccount -> 

			def transaction = new Transaction()
			def shoppingCart = ShoppingCart.get(Long.parseLong(params.id))
			
			if(shoppingCart){
				
				if(!shoppingCart.account?.address1 ||
					!shoppingCart.account?.city ||
					!shoppingCart.account?.zip){
					flash.message = "Please update your address to continue the checkout process"
					redirect(controller : 'account', action : 'customer_profile')
					return
				}
				
				
				if(shoppingCart.status == ShoppingCartStatus.TRANSACTION.description()){
					flash.message = "Your order has already been placed... "
					redirect(action:'index')
					return
				}
				
				try {

    				def total = shoppingCart.total
					def token = params.stripeToken
					
    				transaction.orderDate = new Date()
					
					transaction.total = total
					transaction.subtotal = shoppingCart.subtotal
					transaction.taxes = shoppingCart.taxes
					transaction.shipping = shoppingCart.shipping
					
					transaction.status = OrderStatus.OPEN.description()
					transaction.shoppingCart = shoppingCart
					transaction.account = customerAccount
					
					transaction.shipName = shoppingCart.account?.name
					transaction.shipAddress1 = shoppingCart.account?.address1
					transaction.shipAddress2 = shoppingCart.account?.address2
					transaction.shipCity = shoppingCart.account?.city
					transaction.shipState = shoppingCart.account?.state
					transaction.shipZip = shoppingCart.account?.zip


					if (!transaction.validate()) {
					    flash.message = "Something went wrong while processing order, please try again"
						redirect(action: 'index', id : shippingCart.id)
						return
					}
					
					if(!transaction.save(flush:true)){
						flash.message = "Something went wrong while checking out, please try again or contact Administrator"
						redirect(controller: 'store', action: 'index')
					}
				
					customerAccount.createTransactionPermission(transaction)

					shoppingCart.status = ShoppingCartStatus.TRANSACTION.description()
					shoppingCart.save(flush:true)
					

    				//TODO: Stripe charge logic
    				def apiKey
					
					if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getStripeDevelopmentApiKey()
					if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getStripeLiveApiKey()
						
					if(!apiKey){
						flash.message = "Please configure Stripe before continuing"
						redirect(controller:'store', action:'index')
						return
					}
					
					Stripe.apiKey = apiKey
					def amountInCents = (total * 100) as Integer
					
    				def chargeParams = [
    				    'amount': amountInCents, 
    				    'currency': 'usd', 
    				    'source': token, 
    				    'description': "Order Placed. Account -> ${customerAccount.id} : ${customerAccount.username}"
    				]

					def charge = Charge.create(chargeParams)
    		    	transaction.chargeId = charge.id
					transaction.save(flush:true)

					adjustInventory(shoppingCart)

					sendNewOrderEmail(customerAccount, transaction)
					
					[ transaction : transaction ]
				
				}catch(Exception e){
					e.printStackTrace()
					flash.message = "Something went wrong, make sure everything is completed and credit card information is correct"
					redirect(action:'checkout_preview', params : [id : shoppingCart.id ])
				}
				
			}else{
				flash.message = "Shopping Cart is empty..."
				redirect(controller:'store', action:'index')
			}
		}
	}


	def adjustInventory(shoppingCart){
		shoppingCart.shoppingCartItems.each(){ shoppingCartItem ->
			def product = shoppingCartItem.product
			def quantityAdjustment = shoppingCartItem.quantity
			product.quantity = product.quantity - quantityAdjustment
			//TODO:catch before adding to cart if item quantity is atleast number requested
			product.save(flush:true)
		}
	}
	
	
	def calculateShoppingCartSubtotal(shoppingCart){
		if(shoppingCart &&
			shoppingCart?.shoppingCartItems.size() > 0){
			shoppingCart.subtotal = calculateSubTotal(shoppingCart)
			shoppingCart.save(flush:true)
		}
	}
	
	
	def calculateSubTotal(shoppingCart){
		def subtotal = 0
		shoppingCart.shoppingCartItems.each{ shoppingCartItem ->
			def optionsTotal = 0
			if(shoppingCartItem.shoppingCartItemOptions?.size() > 0){
				shoppingCartItem.shoppingCartItemOptions.each(){ option ->
					optionsTotal += option.variant.price
				}
			}				
			def productTotal = shoppingCartItem.product.price + optionsTotal
			subtotal += (productTotal * shoppingCartItem.quantity)
		}
		return subtotal
	}
	
	
	def calculateTotal(shoppingCart){
	
		if(shoppingCart &&
			shoppingCart?.shoppingCartItems.size() > 0){
			
			def subtotal = calculateSubTotal(shoppingCart)
			
			calculateShipping(shoppingCart)
			
			def taxRate = applicationService.getTaxRate()
			def taxPercent = taxRate/100

			def taxes = 0
			taxes = subtotal * taxPercent
			taxes = applicationService.formatPrice(taxes)
		
			def total = 0 
			total = subtotal + taxes + shoppingCart.shipping
		
			shoppingCart.subtotal = subtotal
			shoppingCart.taxes = taxes
			shoppingCart.total = total
		
			shoppingCart.save(flush:true)
			
		}
	}
	
	
	def calculateShipping(shoppingCart){

		def shipping
		def customer = shoppingCart.account
		def easypostEnabled = applicationService.getEasyPostEnabled()

		if(easypostEnabled == "true" && 
				params.set != "true"){
			
			try{
			
			
				def apiKey
				
				if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getEasyPostTestApiKey()
				if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getEasyPostLiveApiKey()
		
				EasyPost.apiKey = apiKey;
				
				def packageSize = calculatePackageSize(shoppingCart)
				
				//println "here... calculated packageSize " + packageSize
				
				Map<String, Object> toAddressMap = new HashMap<String, Object>();
				toAddressMap.put("name", customer.name)
				toAddressMap.put("street1", customer.address1)
				toAddressMap.put("street2", customer.address2)
				toAddressMap.put("city", customer.city)
				toAddressMap.put("state", customer.state.name)
				toAddressMap.put("zip", customer.zip)
				toAddressMap.put("phone", customer.phone)
    	
				Address toAddress = Address.create(toAddressMap)
				Address verifiedToAddress = toAddress.verify()
			
				def state = State.get(applicationService.getStoreState())
		
				Map<String, Object> fromAddressMap = new HashMap<String, Object>()
				fromAddressMap.put('company', applicationService.getStoreName())
				fromAddressMap.put('street1', applicationService.getStoreAddress1())
				fromAddressMap.put('street2', applicationService.getStoreAddress2())
				fromAddressMap.put('city', applicationService.getStoreCity());
				fromAddressMap.put('state', state.name);
				fromAddressMap.put('zip', applicationService.getStoreZip());

				Address fromAddress = Address.create(fromAddressMap)
				Address verifiedFromAddress = fromAddress.verify()
				
				Map<String, Object> parcelMap = new HashMap<String, Object>();
				
				if(packageSize.height > 0 &&
						packageSize.width > 0 &&
						packageSize.length > 0 &&
						packageSize.height < 100 &&
						packageSize.width < 100 &&
						packageSize.length < 100){
					parcelMap.put("height", packageSize.height);
					parcelMap.put("width", packageSize.width);
					parcelMap.put("length", packageSize.length);
				}
				
				if(packageSize.weight > 0){
					parcelMap.put("weight", packageSize.weight);
					Parcel parcel = Parcel.create(parcelMap);
				
					Map<String, Object> shipmentMap = new HashMap<String, Object>();
					shipmentMap.put("to_address", verifiedToAddress);
					shipmentMap.put("from_address", verifiedFromAddress);
					shipmentMap.put("parcel", parcel);

					//println "creating shipment using api..."
					Shipment shipment = Shipment.create(shipmentMap);
					
					//println shipment
					if(shipment && shipment.rates.size() > 0){
						def rate = getLowestRate(shipment)
						shoppingCart.shipping = rate.rate
						shoppingCart.shipmentId = rate.shipmentId
						shoppingCart.shipmentDays = (rate.estDeliveryDays) ? rate.estDeliveryDays : 0
						shoppingCart.shipmentCarrier = rate.carrier
						shoppingCart.shipmentService = rate.service
						shoppingCart.shipmentRateId = rate.id
					}else{
						shoppingCart.shipping = applicationService.getShipping()
					}
					
				}else{
					shoppingCart.shipping = applicationService.getShipping()
				}
				
			}catch (Exception e){
				println e
				shoppingCart.shipping = applicationService.getShipping()
				flash.message = "Something went wrong while trying to calcuate shipping cost.  Please make sure all information is correct"
				forward(action : 'index')
				return
			}
			
		}else{
			if(params.set != "true"){
				shoppingCart.shipping = applicationService.getShipping()
			}
		}
		
		
		if(shoppingCart.shipping == applicationService.getShipping()){
			shoppingCart.shipmentId = "BASE"
			shoppingCart.shipmentDays = ""
			shoppingCart.shipmentCarrier = ""
			shoppingCart.shipmentService = ""
		}
		
		shoppingCart.save(flush:true)
	}
	
	
	def getLowestRate(shipment){
		def rate
		shipment.rates.each { r ->
			if(!rate){
				rate = r
			}
			if(r.rate < rate?.rate){
				rate = r
			}
		}
		return rate
	}
	
	
	def calculatePackageSize(shoppingCart){
		def length = 0
		def width = 0
		def height = 0
		def weight = 0
		
		shoppingCart.shoppingCartItems.each{ item ->
			if(item.product.length > length){
				length = item.product.length
			}
			if(item.product.width > width){
				width = item.product.width
			}
			
			for(int m = 0; m < item.quantity; m++){
				height += item.product.height
				weight += item.product.weight
			}
		}
		
		def packageSize = [:]
		packageSize.length = length
		packageSize.width = width
		packageSize.height = height
		packageSize.weight = weight
		
		return packageSize
	}
	



	def remove_item(Long id){
		authenticatedAccount { customerAccount ->
			def shoppingCart = ShoppingCart.get(id)
			def shoppingCartItem = ShoppingCartItem.get(Long.parseLong(params.itemId))
			
			if(shoppingCart && shoppingCartItem){
				flash.message = "Successfully removed item from cart"
				shoppingCartItem.delete(flush:true)
				
				redirect(action:'index', id:id)
			}else{
				flash = "Item not found in cart"
				redirect(action:'index', id:id)
			}				
		}	
	}
	


	def checkout_preview(){
		authenticatedPermittedShoppingCart { accountInstance, shoppingCart -> 

			println "here..."
			if(!addressComplete(accountInstance)){
				println "redirect..."
				flash.message = "Please complete address to complete checkout"
				redirect(controller:'account', action:'customer_profile')
				return
			}

			println "didnt make it this far"

			def easypostEnabled = applicationService.getEasyPostEnabled()
			if(easypostEnabled == "true"){
				if(!accountInstance.addressVerified){
					flash.message = "Please update your address to continue with checkout process"
					redirect(controller:'account', action:'customer_profile')
				}
			}
			
			if(shoppingCart && shoppingCart.status == ShoppingCartStatus.ACTIVE.description()){
				calculateTotal(shoppingCart)
				[shoppingCart : shoppingCart, accountInstance: accountInstance]
			}else{
				flash.message = "Shopping Cart is empty..."
				forward(controller:'store', action:'index')
				return
			}
		}
	}


	def addressComplete(accountInstance){
		if(!accountInstance.name){
			return false
		}
		if(!accountInstance.address1){
			return false
		}
		if(!accountInstance.city){
			return false
		}	
		if(!accountInstance.zip){
			return false
		}	
		return true	
	}


	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
	def add(){
		authenticatedAccount { customerAccount ->
			
			def productInstance = Product.findById(params.id)
			
			if(!productInstance){
				flash.message = "Product could not be found"
				redirect(controller : 'product', action : 'details', id : params.id)
				return
			}
			
			if(!params.quantity || 
				!params.quantity.isInteger()){
				flash.message = "Quantity must be a valid number"
				redirect(controller : 'product', action : 'details', id : params.id )
				return
			}
			
			if(productInstance.quantity <= Integer.parseInt(params.quantity)){
				flash.message = "We do not have enough of this product to cover your request.<br/>We currently have <strong>${productInstance.quantity}</strong> in stock."
				redirect(controller : 'product', action : 'details', id : params.id )
				return
			}

			if(!principal?.username){
				flash.message = "Please sign in to continue ..."
				redirect(controller:"auth", action:"customer_login")
			}
			
			def account = Account.findByUsername(principal?.username)

			if(!account){
				flash.message = "Unable to find your account ..."
				redirect(controller:"logout", action:"index")
				return
			}

			def shoppingCart = ShoppingCart.findByAccountAndStatus(account, ShoppingCartStatus.ACTIVE.description())
			
			if(!shoppingCart){
				shoppingCart = new ShoppingCart()
				shoppingCart.account = account
				shoppingCart.status = ShoppingCartStatus.ACTIVE.description()
				shoppingCart.save(flush:true)

				customerAccount.createShoppingCartPermission(shoppingCart)
			}
			
			def existingCartItem = ShoppingCartItem.findByShoppingCartAndProduct(shoppingCart, productInstance)
			
			if(existingCartItem && productInstance.productOptions?.size() == 0){

				def totalQuantity = existingCartItem.quantity + Integer.parseInt(params.quantity)
				
				if(totalQuantity > productInstance.quantity){
					flash.message = "We do not have enough of this product to cover your request. <br/>We currently have <strong>${productInstance.quantity}</strong> total in stock"
					redirect(controller : 'product', action : 'details', id : params.id )
					return
				}

				existingCartItem.quantity = totalQuantity
				existingCartItem.save(flush:true)
			
			}else{
			
				def shoppingCartItem = new ShoppingCartItem()
			
				if(!params.quantity.isDouble()){
					flash.message = "Please enter a valid quantity"
					redirect(controller : 'product', action : 'details', id : params.id)
					return
				}
			
				shoppingCartItem.quantity = Integer.parseInt(params.quantity)
				shoppingCartItem.product = productInstance				
				shoppingCartItem.save(flush:true)
			
				shoppingCart.addToShoppingCartItems(shoppingCartItem)
				shoppingCart.save(flush:true)
				
			
				if(productInstance.productOptions?.size() > 0){
					productInstance.productOptions.each(){ option ->
						if(option.variants?.size() > 0){

							def id = params["product_option_${option.id}"]
						
							def variant = Variant.get(id)
							if(variant){
								def shoppingCartItemOption = new ShoppingCartItemOption()
							
								shoppingCartItemOption.variant = variant
								shoppingCartItemOption.shoppingCartItem = shoppingCartItem
								shoppingCartItemOption.save(flush:true)
							
						
								shoppingCartItem.addToShoppingCartItemOptions(shoppingCartItemOption)
								shoppingCartItem.save(flush:true)
							}
						}
					}
				}
			
			}
				
			flash.message = "Successfully added item to cart"
			redirect(controller: 'shoppingCart', action : 'index')
			return
		}	
	}	
		
		
	
	

    def list(Integer max) {
		authenticatedAdmin{ adminAccount ->
        	params.max = Math.min(max ?: 10, 100)
        	[ shoppingCartInstanceList: ShoppingCart.list(params), shoppingCartInstanceTotal: ShoppingCart.count()]
    	}
	}

	
	

    def show(Long id) {
		authenticatedAdminShoppingCart{ adminAccount, shoppingCartInstance ->        			[shoppingCartInstance: shoppingCartInstance]
		}
    }
	
	
	
	def sendNewOrderEmail(customerAccount, transaction){
		try { 
		
			def fromAddress = applicationService.getSupportEmailAddress()
			def customerToAddress = customerAccount.email
			def customerSubject = "${applicationService.getStoreName()} : Order Confirmation"
			
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
						optionsString += "${option.variant.name}"
						optionsString += "(\$${applicationService.formatPrice(option.variant.price)})<br/>"
					}	
				}
				optionsString += "</div>"
			
				def productTotal = it.product.price + optionsTotal

				def extendedPrice = productTotal * it.quantity
				
				orderDetails += "<tr>"
				orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">${it.product.id}</td>"
				orderDetails += "<td style=\"padding:3px; border-bottom:dotted 1px #ddd\">${it.product.name}${optionsString}</td>"
				orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">\$${applicationService.formatPrice(productTotal)}</td>"
				orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">${it.quantity}</td>"
				orderDetails += "<td style=\"text-align:center; padding:3px; border-bottom:dotted 1px #ddd\">\$${applicationService.formatPrice(extendedPrice)}</td>"
				orderDetails += "</tr>"
			}
			
			bodyString = bodyString.replace("[[ORDER_DETAILS]]", orderDetails)
			
			def adminEmail = applicationService.getAdminEmailAddress()
			def allEmails = customerToAddress
			if(adminEmail){
			 	allEmails += ",${adminEmail}"
			}
			
			emailService.send(allEmails, fromAddress, customerSubject, bodyString)
	
			
		}catch(Exception e){
			e.printStackTrace()
		}		
	}
	
	
}
