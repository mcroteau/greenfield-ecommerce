package greenfield

import grails.converters.*

import grails.plugin.springsecurity.annotation.Secured

class HealthController {
	
	@Secured(['permitAll'])
	def index(){
		def health = [:]
		health.status = "ACTIVE"
		render health as JSON
	}

}