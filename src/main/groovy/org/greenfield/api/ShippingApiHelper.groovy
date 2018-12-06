package org.greenfield.api

import org.greenfield.Country
import org.greenfield.State

import org.greenfield.api.ShipmentAddress


public class ShippingApiHelper {
	
	def applicationService
	ShippingApiHelper(applicationService){
		this.applicationService = applicationService
	}
	
	
	
	/**helper function using ShipmentAddress**/
	def getCustomerAddress(customer){
		def address = new ShipmentAddress()
		address.name = customer.name
		address.street1 = customer.address1
		address.street2 = customer.address2
		address.city = customer.city
		address.country = customer.country.name
		if(customer.state)address.state = customer.state.name
		if(customer.phone)address.phone = customer.phone
		if(customer.zip)address.zip = customer.zip
		return address
	}
	
	
	
	def getStoreAddress(){
		def address = new ShipmentAddress()
		address.company = applicationService.getStoreName()
		address.street1 = applicationService.getStoreAddress1()
		address.street2 = applicationService.getStoreAddress2()
		address.city = applicationService.getStoreCity()
	
		def country = Country.get(applicationService.getStoreCountry())
		def state = State.get(applicationService.getStoreState())
		
		address.country = country.name
		if(state)address.state = state.name
		if(applicationService.getStoreZip())address.zip = applicationService.getStoreZip()
		
		return address
	}
	
	
	/** shipmentPackage is a map
		shipmentPackage.length = length
		shipmentPackage.width = width
		shipmentPackage.height = height
		shipmentPackage.weight = weight
	**/
	def getPackage(shoppingCart){
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
		
		def shipmentPackage = [:]
		shipmentPackage.length = length
		shipmentPackage.width = width
		shipmentPackage.height = height
		shipmentPackage.weight = weight
		
		return shipmentPackage
	}
	
}