package org.greenfield.api

interface ShipmentApi {
	
	def validAddress(address)
	
	def calculateShipping(packageSize, toAddress, fromAddress)
	
	def getLabel(shipment)

}