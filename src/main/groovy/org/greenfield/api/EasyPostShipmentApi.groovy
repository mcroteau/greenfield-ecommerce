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
	
	def calculateShipment(toAddressInformation, fromAddressInformation){
	}
	
	def getLabel(shipment){
	}

}