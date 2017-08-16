package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account
import org.greenfield.State

import java.io.File
import groovy.json.JsonSlurper

//TODO:remove, only using for parse method
import grails.converters.JSON

@Mixin(BaseController)
class ImportController {
	
	@Secured(['ROLE_ADMIN'])
	def parse(){
		def p = [:]
		def dateString = "2017-08-16T07:46:25Z"
		//dateString = "2017-08-16T01:01:01Z"
		//def date = Date.parse("yyyy-MM-dd'T'HH:mm:ssz", dateString)
		def date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateString)
		
		
		
		p['date'] = date
		
		render p as JSON
	}
	
 	@Secured(['ROLE_ADMIN'])
	def view_import(){
		//TODO:add numbers of data to be exported
	}

	
 	@Secured(['ROLE_ADMIN'])
	def import_data(){
			
		def jsonMultipartFile = request.getFile('json-data')
		def jsonFile = convert(jsonMultipartFile)
		
		def jsonSlurper = new JsonSlurper()
		def json = jsonSlurper.parseText(jsonFile.text)
		
		if(json['accounts']){
			saveAccountData(json['accounts'])
		}
		
		render(view : 'view_import')
	}

	def saveAccountData(accounts){
		accounts.each(){ data ->
			if(data.username != 'admin'){
			
				def existingAccount = Account.findByUsername(data.username)
	
				if(!existingAccount){
					println data.username
					def account = new Account()
					//account.id = data.id
					account.username = data.username
					account.password = data.password
					account.name = data.name
					account.email = data.email
					
		            account.address1 = data.address1
		            account.address2 = data.address2
		            account.city = data.city
		            account.state = State.get(data.state) ? State.get(data.state) : null
		            account.zip = data.zip
		            account.phone = data.phone
		            account.ipAddress = data.ipAddress
		            account.enabled = data.enabled
		            account.accountExpired = data.accountExpired
		            account.accountLocked = data.accountLocked
		            account.passwordExpired = data.passwordExpired
		            account.hasAdminRole = data.hasAdminRole
		            account.addressVerified = data.addressVerified
		            account.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", data.dateCreated)
		            account.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", data.lastUpdated)
					
					account.save(flush:true)
					
					account.createAccountRoles(false)
					account.createAccountPermission()
				}

			}
		}
	}
	

	def convert(jsonFile){    
	    File convertedFile = new File(jsonFile.getOriginalFilename());
	    convertedFile.createNewFile(); 
		
	    FileOutputStream fos = new FileOutputStream(convertedFile); 
	    fos.write(jsonFile.getBytes());
	    fos.close(); 
		
	    return convertedFile;
	}
	
}