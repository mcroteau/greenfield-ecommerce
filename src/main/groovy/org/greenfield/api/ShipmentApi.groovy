package org.greenfield.api

interface ShipmentApi {
	
	def validAddress(address)
	
	def calculateShipping(toAddress, fromAddress)
	
	def getLabel(shipment)

}