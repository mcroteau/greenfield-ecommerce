package org.greenfield.api

interface ShipmentApi {
	
	def validAddress(address)
	
	def calculateShipment(toAddress, fromAddress)
	
	def getLabel(shipment)

}