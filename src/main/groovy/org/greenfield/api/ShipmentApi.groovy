package org.greenfield.api

interface ShipmentApi {
	
	/** validates address**/
	def validAddress(address)
	
	/**
		@return ShipmentRate
	**/
	def calculateShipping(shipmentPackage, toAddress, fromAddress)
	
	/**	TODO: create objects for instead of maps
		@return list of options [:]
	**/
	def getCarriersList(shipmentPackage, toAddress, fromAddress)
	
	/**
		@return postage [:]
	**/
	def buyShippingLabel(shipmentId, shipmentRateId)
	

}