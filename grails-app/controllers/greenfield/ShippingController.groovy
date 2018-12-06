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
import org.greenfield.Country

import org.greenfield.api.EasyPostShipmentApi
import org.greenfield.api.ShippingApiHelper

@Mixin(BaseController)
class ShippingController {

	def applicationService
	def currencyService
	
	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
	def set(Long id){
		def shoppingCart = ShoppingCart.get(id)
		if(shoppingCart){
			if(params.optionId && 
				params.rate &&
					params.rate.isDouble() &&
					params.carrier && 
					params.service &&
					params.days &&
					params.rateId &&
					params.currency){
				
				shoppingCart.shipping = params.rate.toDouble()
				shoppingCart.shipmentId = params.optionId
				shoppingCart.shipmentCarrier = params.carrier
				shoppingCart.shipmentService = params.service
				shoppingCart.shipmentDays = params.days
				shoppingCart.shipmentRateId = params.rateId
				shoppingCart.shipmentCurrency = params.currency
				
				println "saving shopping cart after setting new shipping selection..."
				
				println shoppingCart.shipping + shoppingCart.shipmentCurrency
				
				shoppingCart.save(flush:true)

				def anonymous = params.anonymous ? params.anonymouse : ""

				def accountInstance = session['accountInstance']
				
				if(anonymous){
					redirect(controller: 'shoppingCart', action: 'anonymous_preview', params: [ shippingSet : true, accountInstance: accountInstance ])
				}else{
					redirect(controller: 'shoppingCart', action: 'checkout_preview', id: id, params: [ shippingSet : true  ])
				}
				
			}else{
				flash.message = "Shipment Carrier, Service, Rate and Days invalide"
				redirect(action : 'select', id : id)
			}
		}
	}
	
	
	@Secured(['ROLE_CUSTOMER','ROLE_ADMIN'])
	def select(Long id){
		def shoppingCart = ShoppingCart.get(id)
		
		if(shoppingCart){
			try{
			
				def customer = shoppingCart.account
				
				def shipmentApi = new EasyPostShipmentApi(applicationService, currencyService)
				def shippingApiHelper = new ShippingApiHelper(applicationService)
				def shipmentPackage = shippingApiHelper.getPackage(shoppingCart)
				def storeAddress = shippingApiHelper.getStoreAddress()
				def toAddress = shippingApiHelper.getCustomerAddress(customer)
				def carriers = shipmentApi.getCarriersList(shipmentPackage, toAddress, storeAddress)
				def anonymous = params.anonymous ? params.anonymouse : ""
				
				[ shoppingCart : shoppingCart, carriers : carriers, anonymous: anonymous]
				
				
			}catch (Exception e){
				println e
				e.printStackTrace()
				flash.message = "Something went wrong..."
				forward(controller : 'shoppingCart', action : 'index')
				return
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