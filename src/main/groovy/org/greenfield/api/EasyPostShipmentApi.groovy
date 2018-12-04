package org.greenfield.api

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import org.greenfield.api.ShipmentRate

public class EasyPostShipmentApi implements ShipmentApi {
	
	def applicationService
	
	EasyPostShipmentApi(applicationService){
		println "here..."
		this.applicationService = applicationService
	}
	
	def getApiKey(){
		def apiKey
		if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getEasyPostTestApiKey()
		if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getEasyPostLiveApiKey()
		println "here..."
		println apiKey
		println "here..."
		return apiKey
	}
	
	
	
	
	def validAddress(address){
		try{
		
			def apiKey = getApiKey()
		
			EasyPost.apiKey = apiKey;
		
	    	Map<String, Object> addressMap = new HashMap<String, Object>();
	    	addressMap.put("street1", address.street1);
	    	addressMap.put("street2", address.street2);
			addressMap.put("city", address.city);
	    	addressMap.put("country", address.country);
	    	addressMap.put("state", address.state);
			addressMap.put("zip", address.zip);
			
    		Address validateAddress = Address.create(addressMap);
			Address verifiedAddress = validateAddress.verify();
			
			println verifiedAddress
			
			return true
			
		}catch (Exception e){
			println e
			return false
		}
	}
	
	def calculateShipping(package, toAddress, fromAddress){
	
		def apiKey = getApiKey()
		EasyPost.apiKey = apiKey;
		
		Address verifiedToAddress = getVerifiedAddress(toAddress)
		Address verifiedFromAddress = getVerifiedAddress(fromAddress)
		
		Map<String, Object> parcelMap = new HashMap<String, Object>();
		
		if(package.height > 0 &&
				package.width > 0 &&
				package.length > 0 &&
				package.height < 100 &&
				package.width < 100 &&
				package.length < 100){
			parcelMap.put("height", package.height);
			parcelMap.put("width", package.width);
			parcelMap.put("length", package.length);
		}
		
		if(packageSize.weight > 0){

			parcelMap.put("weight", package.weight);
			Parcel parcel = Parcel.create(parcelMap);
			
			/**
			TODO:how to handle customs, see below, what is tariff number?
			shoppingCart.shoppingCartItems.each(){ item ->
			Map<String, Object> customsItemMap = new HashMap<String, Object>();
			customsItemMap.put("description", item.name + " : " item.description);
			customsItemMap.put("quantity", item.quantity);
			customsItemMap.put("value", item.price);
			customsItemMap.put("weight", item.weight);
			customsItemMap.put("origin_country", currencyService.getCurrency());
			customsItemMap.put("hs_tariff_number", item.tariffNumber);
			CustomsItem customsItem1 = CustomsItem.create(customsItemMap);
			}
			**/

			Map<String, Object> shipmentMap = new HashMap<String, Object>();
			shipmentMap.put("to_address", verifiedToAddress);
			shipmentMap.put("from_address", verifiedFromAddress);
			shipmentMap.put("parcel", parcel);


			//TODO:how to handle customs
			//shipmentMap.put("customs_info", customsItem1)


			//println "creating shipment using api..."
			Shipment shipment
			try{
				shipment = Shipment.create(shipmentMap);	
				
				if(shipment){
					def rate = getLowestRate(shipment)
					
					def shipmentRate = new ShipmentRate()
					shipmentRate.rate = rate.rate
					shipmentRate.shipmentId = rate.shipmentId
					shipmentRate.estDeliveryDays = (rate.estDeliveryDays) ? rate.estDeliveryDays : 0
					shipmentRate.carrier = rate.carrier
					shipmentRate.service = rate.service
					shipmentRate.rateId = rate.id
					
					
					return shipmentRate
					
				}
			}catch(Exception e){
				e.printStackTrace()
			}
		}
	}
	
	
	def getCarriersList(package, toAddress, companyAddress){
	
		def apiKey = getApiKey()
		EasyPost.apiKey = apiKey;
	
		
		Address verifiedToAddress = getVerifiedAddress(toAddress)
		Address verifiedFromAddress = getVerifiedAddress(fromAddress)
	
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
			
		
			return carriers
			
		}else{
			return [:]
		}
	
	}
	
	
	
	
	def getVerifiedAddress(address){
		Map<String, Object> addressMap = new HashMap<String, Object>();
		if(address.name)addressMap.put("name", address.name)
		if(address.company)addressMap.put("company", address.company)
		addressMap.put("street1", address.street1)
		addressMap.put("street2", address.street2)
		addressMap.put("city", address.city)
		addressMap.put("country", address.country)
		if(addressMap.state)addressMap.put("state", address.state)
		addressMap.put("zip", address.zip)
		addressMap.put("phone", address.phone)

		Address verifyAddress = Address.create(addressMap)
		Address verifiedAddress = verifyAddress.verify()
		
		return verifiedAddress
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
	
	
	def getLabel(shipment){
	}

}