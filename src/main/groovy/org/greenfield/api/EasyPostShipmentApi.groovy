package org.greenfield.api

import com.easypost.EasyPost
import com.easypost.model.Rate
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import org.greenfield.api.ShipmentRate

public class EasyPostShipmentApi implements ShipmentApi {
	
	def applicationService
	def currencyService
	
	EasyPostShipmentApi(applicationService, currencyService){
		this.applicationService = applicationService
		this.currencyService = currencyService
	}
	
	def getApiKey(){
		def apiKey
		if(Environment.current == Environment.DEVELOPMENT) {
			 apiKey = applicationService.getEasyPostTestApiKey()
			 if(!apiKey){
				 throw new Exception("EasyPost development api key is not entered...")
			 }
		}
		
		if(Environment.current == Environment.PRODUCTION) {
			apiKey = applicationService.getEasyPostLiveApiKey()
			if(!apiKey){
				throw new Exception("EasyPost live api key is not entered...")
			}
		}
		
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
			
			return true
			
		}catch (Exception e){
			println e.printStackTrace()
			return false
		}
	}
	
	
	
	def calculateShipping(packageSize, toAddress, fromAddress){
	
		if(toAddress.country != fromAddress.country){
			throw new Exception("International shipping is not supported.")
		}
		
		def apiKey = getApiKey()
	
		EasyPost.apiKey = apiKey;
		
		Address verifiedToAddress = getVerifiedAddress(toAddress)
		Address verifiedFromAddress = getVerifiedAddress(fromAddress)
		
		println "84" + verifiedToAddress
		println "85" + verifiedFromAddress
		
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
			
			println "parcel : " + parcel
			
			/**
			TODO:how to handle customs, see below
			Map<String, Object> customsItemMap = new HashMap<String, Object>();
			customsItemMap.put("description", "T-shirt");
			customsItemMap.put("quantity", 1);
			customsItemMap.put("value", 10);
			customsItemMap.put("weight", 5);
			customsItemMap.put("origin_country", "us");
			customsItemMap.put("hs_tariff_number", "123456");
			CustomsItem customsItem1 = CustomsItem.create(customsItemMap);
			**/

			Map<String, Object> shipmentMap = new HashMap<String, Object>();
			shipmentMap.put("to_address", verifiedToAddress);
			shipmentMap.put("from_address", verifiedFromAddress);
			shipmentMap.put("parcel", parcel);


			//TODO:how to handle customs?
			//shipmentMap.put("customs_info", customsItem1)


			//println "creating shipment using api..."
			Shipment shipment = Shipment.create(shipmentMap);	
				
			if(shipment){
				println "shipment: " + shipment
				def rate = getLowestRate(shipment)
				
				def shipmentRate = new ShipmentRate()
				shipmentRate.rate = rate.rate
				shipmentRate.shipmentId = rate.shipmentId
				shipmentRate.estDeliveryDays = (rate.estDeliveryDays) ? rate.estDeliveryDays : 0
				shipmentRate.carrier = rate.carrier
				shipmentRate.service = rate.service
				shipmentRate.rateId = rate.id
				println "get rate currency : " + rate.toString()
				shipmentRate.currency = rate.currency
				
				
				return shipmentRate
				
			}
			
			throw new Exception("Something went wrong while creating the shipment. Addresses might not be supported...")
				
		}
	}
	
	
	def getCarriersList(packageSize, toAddress, fromAddress){

		def apiKey = getApiKey()
	
		EasyPost.apiKey = apiKey;
		
		Address verifiedToAddress = getVerifiedAddress(toAddress)
		Address verifiedFromAddress = getVerifiedAddress(fromAddress)
		
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
			
			/**
			TODO:how to handle customs, see below
			Map<String, Object> customsItemMap = new HashMap<String, Object>();
			customsItemMap.put("description", "T-shirt");
			customsItemMap.put("quantity", 1);
			customsItemMap.put("value", 10);
			customsItemMap.put("weight", 5);
			customsItemMap.put("origin_country", currencyService.getCountryCode());
			customsItemMap.put("hs_tariff_number", "123456");
			CustomsItem customsItem1 = CustomsItem.create(customsItemMap);
			**/

			Map<String, Object> shipmentMap = new HashMap<String, Object>();
			shipmentMap.put("to_address", verifiedToAddress);
			shipmentMap.put("from_address", verifiedFromAddress);
			shipmentMap.put("parcel", parcel);


			//TODO:how to handle customs?
			//shipmentMap.put("customs_info", customsItem1)


			//println "creating shipment using api..."
			Shipment shipment
			try{
				shipment = Shipment.create(shipmentMap);	
				
				if(shipment){
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
						option.currency = rate.currency
				
						carriers[rate.carrier].add(option)
					}
			
		
					return carriers
					
				}
			}catch(Exception e){
				e.printStackTrace()
			}
			
			
		}else{
			return [:]
		}
		
	}
	
	
	def buyShippingLabel(shipmentId, shipmentRateId){

		def apiKey = getApiKey()
		EasyPost.apiKey = apiKey;
		
		Shipment shipment = Shipment.retrieve(shipmentId)
		Rate rate = Rate.retrieve(shipmentRateId)
		
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
			throw new Exception("Something went wrong while purchasing label")
		}
		
		def postage = [:]
		
		postage.id = shipment.postageLabel?.id
		postage.labelUrl = shipment.postageLabel?.labelUrl
		
		return postage
		
	}
	
	
	
	def getVerifiedAddress(address){
		println "281 getVerifiedAddress"
		Map<String, Object> addressMap = new HashMap<String, Object>();
		if(address.name)addressMap.put("name", address.name)
		if(address.company)addressMap.put("company", address.company)
		addressMap.put("street1", address.street1)
		addressMap.put("street2", address.street2)
		addressMap.put("city", address.city)
		addressMap.put("country", address.country)
		if(addressMap.state)addressMap.put("state", address.state)
		addressMap.put("zip", address.zip)
		if(addressMap.phone)addressMap.put("phone", address.phone)

		Address verifyAddress = Address.create(addressMap)
		Address verifiedAddress = verifyAddress.verify()
		
		return verifiedAddress
	}
	
	
	
	def getLowestRate(shipment){
		def rate
		println "rates : " + shipment.rates
		if(!shipment.rates){
			throw new Exception("Apologies for the inconvenience. No carriers found.")
		}
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