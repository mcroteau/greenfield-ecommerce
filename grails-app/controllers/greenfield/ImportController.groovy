package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account
import org.greenfield.Permission
import org.greenfield.Catalog
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
		
		if(json['permissions']){
			savePermissionData(json['permissions'])
		}
		
		if(json['catalogs']){
			saveCatalogData(json['catalogs'])
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
	

	
	
	def savePermissionData(permissions){
		permissions.each(){ data ->
			if(data['account'] && data['permission']){
				def account = Account.findByUsername(data['account'])
				
				if(account){
					
					def existingPermission = Permission.findByAccountAndPermission(account, data['permission'])
					
					if(!existingPermission){
						def permission = new Permission()
						permission.account = account
						permission.permission = data['permission']
						permission.save(flush:true)
					}
				}
			}
		}
	}
	
	
	def saveCatalogData(catalogs){
		catalogs.each(){ data ->
			def existingCatalog = Catalog.findByName(data['name'])
			
			if(!existingCatalog){
				def catalog =  populateCatalogData(data)
				catalog.toplevel = true
				catalog.save(flush:true)

				existingCatalog = catalog
			}
			if(data['subcatalogs']){
				saveSubcatalogs(existingCatalog, data['subcatalogs'])
			}
		}
	}
	
	
	def saveSubcatalogs(parentCatalog, subcatalogs){
		subcatalogs.each(){ data ->
			def existingCatalog = Catalog.findByName(data['name'])
			
			if(!existingCatalog){
				def catalog = populateCatalogData(data)
				catalog.toplevel = false
				catalog.save(flush:true)
				
				parentCatalog.addToSubcatalogs(catalog)
				parentCatalog.save(flush:true)
				
				existingCatalog = catalog
			}
			println "data = " + data
			if(data['subcatalogs']){
				saveSubcatalogs(existingCatalog, data['subcatalogs'])
			}
		}
	}
	
	
	def populateCatalogData(data){
		def catalog = new Catalog()
		catalog.name = data['name']
		catalog.description = data['description'] 
		catalog.position = data['position']
		def parentCatalog = Catalog.findByName(data['parentCatalog'])
		if(parentCatalog){
			catalog.parentCatalog = parentCatalog
		}
		return catalog
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