package org.greenfield.api

interface ShipmentApi {
	
	def validAddress(address)
	
	def calculateShipping(shipmentPackage, toAddress, fromAddress)
	
	def getCarriersList(shipmentPackage, toAddress, fromAddress)
	
	def getLabel(shipment)

}