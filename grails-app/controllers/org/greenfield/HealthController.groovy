package org.greenfield

import grails.converters.*


class HealthController {
	
	def index(){
		def health = [:]
		health.status = "ACTIVE"
		render health as JSON
	}

}