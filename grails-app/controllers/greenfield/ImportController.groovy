package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account
import org.greenfield.Permission
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.ProductOption
import org.greenfield.Variant

import org.greenfield.Specification
import org.greenfield.SpecificationOption
import org.greenfield.ProductSpecification

import org.greenfield.AdditionalPhoto

import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCartItemOption

import org.greenfield.Transaction
import org.greenfield.Page
import org.greenfield.Upload
import org.greenfield.Layout

import org.greenfield.log.CatalogViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.SearchLog
import org.greenfield.State


import java.io.File
import groovy.json.JsonSlurper

//TODO:remove, only using for parse method
import grails.converters.JSON

@Mixin(BaseController)
class ImportController {
	
	def catalogCount = 0
	
	@Secured(['ROLE_ADMIN'])
	def parse(){
		def p = [:]
		
		def dateString = "2017-08-16T07:46:25Z"
		def date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateString)
		
		p['date'] = date
		
		def accounts = Account.list()
		
		
		render accounts as JSON
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def view_import(){
		//TODO:add numbers of data to be exported
	}

	
 	@Secured(['ROLE_ADMIN'])
	def import_data(){
			
		
		try{
			def jsonMultipartFile = request.getFile('json-data')
		
		
			def jsonFile = convert(jsonMultipartFile)
			
			def jsonSlurper = new JsonSlurper()
			def json = jsonSlurper.parseText(jsonFile.text)
			
			if(json['accounts']){
				saveAccountData(json['accounts'])
			}
			
			if(json['catalogs']){
				saveCatalogData(json['catalogs'])
			}
			
			if(json['productOptionData']){
				saveProductOptionData(json['productOptionData'])
			}
			
			if(json['products']){
				saveProductData(json['products'])
			}
			
			render(view: 'view_import')
			
		}catch(IOException ioe){
			flash.message = "No file was selected. Please select a file to continue"
			redirect(action: 'view_import')
		}catch(Exception e){
			flash.message = "Something went wrong, please try again"
			redirect(action: 'view_import')
		}
	}
	
	
	def saveProductOptionData(productOptionData){
		def count = 0
		if(productOptionData.productOptions){
			println "here..."
			productOptionData.productOptions.each(){ data ->
				def product = Product.findByUuid(data.product)
				if(product){
					println "product = " + product
					if(params.performImport == "true"){		
						def productOption = new ProductOption()
						productOption.uuid = data.uuid
						productOption.product = product
						productOption.save(flush:true)
						
						product.addToProductOptions(productOption)
						product.save(flush:true)
					}
					count++
				}
			}
			
			if(productOptionData.optionVariants){
				productOptionData.optionVariants.each(){ datac ->
					def productOption = ProductOption.findByUuid(datac.productOption)
					
					if(productOption){	
						def variant = new Variant()
						variant.uuid = datac.uuid
						variant.name = datac.name
						variant.price = datac.price
						variant.imageUrl = datac.imageUrl
						variant.position = datac.position
						variant.productOption = productOption

						if(params.performImport == "true"){	
							variant.save(flush:true)
							productOption.addToVariants(variant)
							productOption.save(flush:true)
						}
					}
				}
			}
		}
		
		request.productOptionsCount = count
	}
	
	
	
	def saveProductData(products){
		def count = 0
		
		products.each(){ data ->
			def existingProduct = Product.findByUuid(data.uuid)
			
			if(!existingProduct && data.catalogs){
				def product = new Product()
				product.uuid = data.uuid
				product.name = data.name
				product.description = data.description
				product.quantity = data.quantity
				product.price = data.price
				product.imageUrl = data.imageUrl
				product.detailsImageUrl = data.detailsImageUrl
				product.disabled = data.disabled
				product.length = data.length
				product.width = data.width
				product.height = data.height
				product.weight = data.weight
				product.productNo = data.productNo
				
				product.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", data.dateCreated)
		    	product.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", data.lastUpdated)
						
				if(params.performImport == "true"){		
					product.save(flush:true)
			    	
					data.catalogs.each(){ c ->
						def catalog = Catalog.findByUuid(c)
						if(catalog){
							product.addToCatalogs(catalog)
							product.save(flush:true)
						}
					}
				}
				count++			
			}else{
				flash.message = "Make sure catalogs have been imported before products otherwise products will not be imported."
			}
			request.productsCount = count			
		}
	}
	
	
	def saveAccountData(accounts){
		def count = 0
		accounts.each(){ data ->
			if(data.username != 'admin'){
			
				def existingAccount = Account.findByUsername(data.username)
	
				if(!existingAccount){
					println data.username
					def account = new Account()
					
					account.uuid = data.uuid
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
					
					if(params.performImport == "true"){
						account.save(flush:true)
					
						if(account.hasAdminRole){
							account.createAccountRoles(true)
						}else{
							account.createAccountRoles(false)
						}
						account.createAccountPermission()
						
						request.importResults = "show"
					}else{
						request.checkResults = "show"
					}
					
					count++
					request.accountsCount = count
				}
			}
		}
	}
	
	
	def saveCatalogData(catalogs){
		catalogCount = 0
		catalogs.each(){ data ->
			def existingCatalog = Catalog.findByUuid(data.uuid)
			
			if(!existingCatalog){
				def catalog =  populateCatalogData(data)
				
				if(params.performImport == "true"){
					catalog.toplevel = true
					catalog.save(flush:true)
				}

				if(data.subcatalogs){
					saveSubcatalogs(catalog, data.subcatalogs)
				}
				catalogCount++
			}
		}
		request.catalogsCount = catalogCount
	}
	
	
	def saveSubcatalogs(parentCatalog, subcatalogs){
		subcatalogs.each(){ data ->
			def existingCatalog = Catalog.findByUuid(data.uuid)
			
			if(!existingCatalog){
				def catalog = populateCatalogData(data)
				
				if(params.performImport == "true"){
					catalog.toplevel = false
					catalog.save(flush:true)
					
					parentCatalog.addToSubcatalogs(catalog)
					parentCatalog.save(flush:true)
					
				}
				if(data.subcatalogs){
					saveSubcatalogs(existingCatalog, data.subcatalogs)
				}
				catalogCount++
			}
		}
	}
	
	
	def populateCatalogData(data){
		def catalog = new Catalog()
		catalog.uuid = data.uuid
		catalog.name = data.name
		catalog.description = data.description
		catalog.position = data.position
		def parentCatalog = Catalog.findByUuid(data.parentCatalog)
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