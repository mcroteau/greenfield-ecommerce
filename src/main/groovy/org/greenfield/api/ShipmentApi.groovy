package org.greenfield.api

interface ShipmentApi {
	
	def validAddress(address)
	
	def calculateShipping(package, toAddress, fromAddress)
	
	def getLabel(shipment)

}