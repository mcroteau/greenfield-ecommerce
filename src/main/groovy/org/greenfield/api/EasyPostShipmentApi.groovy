package org.greenfield.api

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment


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
	
	def calculateShipping(toAddress, fromAddress){

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
			Shipment shipment
			try{
				shipment = Shipment.create(shipmentMap);	
			}catch(Exception e){
				e.printStackTrace()
			}
		}
	}
	
	def getVerifiedAddress(address){
		Map<String, Object> addressMap = new HashMap<String, Object>();
		if(address.name)addressMap.put("name", toAddress.name)
		if(address.company)addressMap.put("name", toAddress.company)
		toAddressMap.put("street1", addressMap.street1)
		toAddressMap.put("street2", addressMap.street2)
		toAddressMap.put("city", addressMap.city)
		toAddressMap.put("country", addressMap.country)
		if(toAddress.state)addressMap.put("state", toAddress.state)
		toAddressMap.put("zip", addressMap.zip)
		toAddressMap.put("phone", addressMap.phone)

		Address addressMap = Address.create(addressMap)
		Address verifiedAddress = addressMap.verify()
		
		return verifiedAddress
	}
	
	
	def getLabel(shipment){
	}

}