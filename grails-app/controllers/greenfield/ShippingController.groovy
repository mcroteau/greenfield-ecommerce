package greenfield

import greenfield.common.BaseController

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.ShoppingCart
import org.greenfield.State

@Mixin(BaseController)
class ShippingController {

	def applicationService
	
	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
	def set(Long id){
		authenticatedAccount { customerAccount ->
			def shoppingCart = ShoppingCart.get(id)
			if(shoppingCart){
				if(params.optionId && 
					params.rate &&
						params.rate.isDouble() &&
						params.carrier && 
						params.service &&
						params.days &&
						params.rateId){
					
					shoppingCart.shipping = params.rate.toDouble()
					shoppingCart.shipmentId = params.optionId
					shoppingCart.shipmentCarrier = params.carrier
					shoppingCart.shipmentService = params.service
					shoppingCart.shipmentDays = params.days
					shoppingCart.shipmentRateId = params.rateId
					
					shoppingCart.save(flush:true)
					
					redirect(controller: 'shoppingCart', action: 'checkout_preview', id: id, params: [ set : true ])
				}else{
					flash.message = "Shipment Carrier, Service, Rate and Days invalide"
					redirect(action : 'select', id : id)
				}
			}
		}
	}
	
	
	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
	def select(Long id){
		authenticatedAccount { customerAccount ->
			
			def shoppingCart = ShoppingCart.get(id)
			
			if(shoppingCart){
				try{
				
					def customer = shoppingCart.account
					
					def apiKey
					
					if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getEasyPostTestApiKey()
					if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getEasyPostLiveApiKey()
		
					EasyPost.apiKey = apiKey;
					
					def packageSize = calculatePackageSize(shoppingCart)
				
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
							packageSize.length > 0){
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
                    	
						Shipment shipment = Shipment.create(shipmentMap);
						
						println "here..."

						def carriers = [:]
						shipment.rates.each { rate ->
							if(!carriers[rate.carrier]){
								carriers[rate.carrier] = []
							}
							
							def option = [:]
							option.id = rate.shipmentId
							option.rate = rate.rate
							option.service = rate.service
							option.days = (rate.estDeliveryDays) ? rate.estDeliveryDays : 0
							option.rateId = rate.id
							
							carriers[rate.carrier].add(option)
						}
						
						[ shoppingCart : shoppingCart, carriers : carriers]
					
					}else{
						flash.message = "Dimensions or Weight of package is invalid"
						redirect(controller : 'shoppingCart', action : 'index' )
						return
					}
					
				}catch (Exception e){
					println e
					e.printStackTrace()
					flash.message = "Something went wrong..."
					forward(controller : 'shoppingCart', action : 'index')
					return
				}
			}
		}
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
	
	
}