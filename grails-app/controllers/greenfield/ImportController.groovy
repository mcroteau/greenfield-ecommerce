package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account

import java.io.File
import groovy.json.JsonSlurper

@Mixin(BaseController)
class ImportController {
	
 	@Secured(['ROLE_ADMIN'])
	def view_import(){
		//TODO:add numbers of data to be exported
	}

	
 	@Secured(['ROLE_ADMIN'])
	def import_data(){
			
		def jsonMultipartFile = request.getFile('json-data')
		def jsonFile = convert(jsonMultipartFile)
		
		println jsonFile.getClass()
		//println jsonFile.text
		
		def jsonSlurper = new JsonSlurper()
		def json = jsonSlurper.parseText(jsonFile.text)
		
		json.each(){ data ->
			if(data.username != 'admin'){
				
				def existingAccount = Account.get(data.id)
				
				if(!existingAccount){
					println data.id
					def account = new Account()
					account.id = data.id
					account.username = data.username
					account.password = data.password
					account.name = data.name
					account.email = data.email
					account.save(flush:true)

					account.createAccountRoles(false)
					account.createAccountPermission()
				}
	
			}
		}
		
		render(view : 'view_import')
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