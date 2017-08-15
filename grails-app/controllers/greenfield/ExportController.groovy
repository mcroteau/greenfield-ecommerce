package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account
import org.greenfield.Permission
import org.greenfield.Catalog
import org.greenfield.Product

import groovy.json.*
import groovy.json.JsonOutput
import grails.converters.JSON
import groovy.json.JsonOutput

import java.io.InputStream
import java.io.ByteArrayInputStream

@Mixin(BaseController)
class ExportDataController {
	
 	@Secured(['ROLE_ADMIN'])
	def view(){
	}

	
 	@Secured(['ROLE_ADMIN'])
	def export_data(){
		/**
			data : {
				accounts : []
				permissions : []
				catalogs : []
				products : []
				productOptions : []
				variants : []
				specifications : []
				specificationOptions : []
				productSpecifications : []
				orders : []
				layout : []
				pages : []
			}
		**/
		
		def accounts = Account.list()
		println new JSON(accounts)
		accounts = formatAccounts(accounts)
		def accountsJson = new JSON(accounts)
		
		
		def permissions = Permission.list()
		//def catalogs = Catalog.findAllByToplevel(true)
		def catalogs = Catalog.list()
		def products = Product.list()
		
		println catalogs.size()
		//println new JsonBuilder(catalogs).toPrettyString()

		//println JsonOutput.toJson(accounts)
		println new JSON(accounts)
		/**
		def productOptions = ProductOption.list()
		def variants = Variant.list()
		def specifications = Specification.list()
		def specificationOptions = SpecificationOption.list()
		**/
	
		def json = formatJson(accounts)
		InputStream is = new ByteArrayInputStream(json.getBytes());
		
		//render accounts as JSON

		render(file: is, fileName: "greenfield-data.json")
		
		
	}
	
	
	def formatJson(data){
		def jsonData = new JSON(data)
		def jsonString = jsonData.toString()
		def jsonOutput = new JsonOutput()
		return jsonOutput.prettyPrint(jsonString)
	}
	
	
	def formatAccounts(unformattedAccounts){
		/**
			Fields
			
			id
			email
		    username
		    password
			name	
			address1
			address2
			city
			state
			zip
			phone
			ipAddress
			enabled
			accountExpired
			accountLocked
			passwordExpired
			hasAdminRole
			addressVerified	
			dateCreated
			lastUpdated		
		**/
		
		def accounts = []
		
		unformattedAccounts.each(){ it ->
			
			def account = [:]
			account['id'] = it.id
			account['email'] = it.email
		    account['username'] = it.username
		    account['password'] = it.password
			account['name'] = it.name
			
			account['address1'] = (it?.address1 ? it.address1 : "")
			account['address2'] = (it?.address2 ? it.address2 : "")
			account['city'] = (it?.city ? it.city : "")
			account['state'] = (it?.state ? it.state.id : "")
			account['zip'] = (it?.zip ? it.zip : "")

			account['phone'] = (it?.phone ? it.phone : "")
			
			account['ipAddress'] = (it?.ipAddress ? it.ipAddress : "")

			account['enabled'] = it.enabled
			account['accountExpired'] = it.accountExpired
			account['accountLocked'] = it.accountLocked
			account['passwordExpired'] = it.passwordExpired
			account['hasAdminRole'] = it.hasAdminRole
			account['addressVerified'] = it.addressVerified
			account['dateCreated'] = it.dateCreated
			account['lastUpdated'] = it.lastUpdated
			
			accounts.add(account)
		}
		
		return accounts
	}
	
}