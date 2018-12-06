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

import org.greenfield.common.ShoppingCartStatus

@Mixin(BaseController)
class ShippingController {

	def applicationService
	def currencyService
	
	@Secured(['permitAll'])
	def set(){
		
		def id
		def shoppingCart
		if(params.id){
			shoppingCart = ShoppingCart.get(params.id)
		}else if(session['shoppingCart']){
			def uuid = session['shoppingCart']
			shoppingCart = ShoppingCart.findByUuidAndStatus(uuid, ShoppingCartStatus.ACTIVE.description())
		}else{
			flash.message = "Something went wrong... shopping cart not found."
			redirect(controller: "store", action:"index")
		}
		
		
		
		if(shoppingCart){
			if(params.optionId && 
				params.rate &&
					params.rate.isDouble() &&
					params.carrier && 
					params.service &&
					params.days &&
					params.rateId &&
					params.currency){
				
				id = shoppingCart.id
						
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
				
				if(anonymous == "true"){
					redirect(controller: 'shoppingCart', action: 'anonymous_preview', params: [ shippingSet : true, accountInstance: accountInstance ])
					return
				}else{
					redirect(controller: 'shoppingCart', action: 'checkout_preview', id: id, params: [ shippingSet : true  ])
					return
				}
				
			}else{
				flash.message = "Shipment Carrier, Service, Rate and Days invalide"
				redirect(action : 'select', id : id)
			}
		}
	}
	
	
	@Secured(['permitAll'])
	def select(){
		def anonymous = params?.anonymous ? params?.anonymous : ""
		
		def shoppingCart
		def uuid
		if(params.id){
			shoppingCart = ShoppingCart.get(params.id)
		}else if(session['shoppingCart']){
			uuid = session['shoppingCart']
			shoppingCart = ShoppingCart.findByUuidAndStatus(uuid, ShoppingCartStatus.ACTIVE.description())
		}else{
			flash.message = "Something went wrong... shopping cart not found."
			redirect(controller: "store", action:"index")
		}
		
		def customer
		if(shoppingCart?.account){
			customer = shoppingCart.account
		}else if(session["accountInstance"]){
			customer = session["accountInstance"]
		}else{
			flash.message = "Something went wrong... account information missing, session may have ended"
			redirect(controller: "store", action:"index")
		}
		
		
		if(shoppingCart && customer){
			try{
				
				def shipmentApi = new EasyPostShipmentApi(applicationService, currencyService)
				def shippingApiHelper = new ShippingApiHelper(applicationService)
				def shipmentPackage = shippingApiHelper.getPackage(shoppingCart)
				def storeAddress = shippingApiHelper.getStoreAddress()
				def toAddress = shippingApiHelper.getCustomerAddress(customer)
				def carriers = shipmentApi.getCarriersList(shipmentPackage, toAddress, storeAddress)
				
				[ shoppingCart : shoppingCart, accountInstance: customer, carriers : carriers, anonymous: anonymous]
				
				
			}catch (Exception e){
				println e
				e.printStackTrace()
				flash.message = "Something went wrong..." + e
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