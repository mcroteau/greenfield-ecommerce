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
				def accountsCount = Account.count()
				saveAccountData(json['accounts'])
				request.accountsImported = Account.count() - accountsCount
			}
			
			
			if(json['catalogs']){
				def catalogsCount = Catalog.count()
				saveCatalogData(json['catalogs'])			
				request.catalogsImported = Catalog.count() - catalogsCount
			}
			
			
			if(json['products']){
				def productCount = Product.count()
				saveProductData(json['products'])
				request.productsImported = Product.count() - productCount
			}
			
			
			if(json['productOptionData']){
				def productOptionCount = ProductOption.count()
				saveProductOptionData(json['productOptionData'])
				request.productOptionsImported = ProductOption.count() - productOptionCount
			}
			
			
			if(json['specificationData']){
				def specificationCount = Specification.count()
				saveSpecificationData(json['specificationData'])
				request.specificationsImported = Specification.count() - specificationCount
			}
			
			
			if(json['shoppingCartData']){
				def shoppingCartCount = ShoppingCart.count()
				saveShoppingCartData(json['shoppingCartData'])
				request.shoppingCartsImported = ShoppingCart.count() - shoppingCartCount
			}
			
			
			
			if(params.performImport == "true"){
				request.importResults = true
			}else{
				request.checkResults = true		
			}
			
			
			render(view: 'view_import')
			
		}catch(IOException ioe){
			flash.message = "No file was selected. Please select a file to continue"
			redirect(action: 'view_import')
		}catch(Exception e){
			flash.message = "Something went wrong, please try again"
			redirect(action: 'view_import')
			e.printStackTrace()
		}
	}
	
	
	def saveShoppingCartData(shoppingCartData){
		def count = 0
		if(shoppingCartData.shoppingCarts){
		
			shoppingCartData.shoppingCarts.each(){ sc ->
				def account = Account.findByUuid(sc.account)
				
				def shoppingCart = new ShoppingCart()
				shoppingCart.uuid = sc.uuid
				shoppingCart.status = sc.status
	        	shoppingCart.taxes = sc.taxes
	        	shoppingCart.shipping = sc.shipping
	        	shoppingCart.subtotal = sc.subtotal
	        	shoppingCart.total = sc.total
	        	shoppingCart.account = account
	        	shoppingCart.shipmentId = sc.shipmentId
	        	shoppingCart.shipmentDays = sc.shipmentDays
	        	shoppingCart.shipmentCarrier = sc.shipmentCarrier
	        	shoppingCart.shipmentService = sc.shipmentService
	        	shoppingCart.shipmentRateId = sc.shipmentRateId
	        	shoppingCart.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", sc.dateCreated)
	        	shoppingCart.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", sc.lastUpdated)
				
				if(params.performImport == "true"){	
					shoppingCart.save(flush:true)
				}
				count++
			}
		}
		
		if(params.performImport == "true"){	
			if(shoppingCartData.shoppingCartItems){
				shoppingCartData.shoppingCartItems.each(){ sci ->
					
					def existingShoppingCartItem = ShoppingCartItem.findByUuid(sci.uui)
					
					if(!existingShoppingCartItem){
						
						def product = Product.findByUuid(sci.product)
						def shoppingCart = ShoppingCart.findByUuid(sci.shoppingCart)
						
						if(product && shoppingCart){
							def shoppingCartItem = new ShoppingCartItem()
							shoppingCartItem.uuid = sci.uuid
							shoppingCartItem.quantity = sci.quantity
							shoppingCartItem.product = product
							shoppingCartItem.shoppingCart = shoppingCart
							shoppingCartItem.save(flush:true)
							
							shoppingCart.addToShoppingCartItems(shoppingCartItem)
							shoppingCart.save(flush:true)
						}
					}
				}
			
				if(shoppingCartData.shoppingCartItemOptions){
					shoppingCartData.shoppingCartItemOptions.each(){ scio ->
						def variant = Variant.findByUuid(sci.variant)
						def shoppingCartItem = ShoppingCartItem.findByUuid(sci.shoppingCartItem)
						
						if(variant && shoppingCartItem){
							def shoppingCartItemOption = new ShoppingCartItemOption()
							//TODO: add uuid
							shoppingCartItemOption.variant = variant
							shoppingCartItemOption.shoppingCartItem = shoppingCartItem
							
							shoppingCartItemOption.save(flush:true)
							
							shoppingCartItemOption.addToShoppingCartItemOptions(shoppingCartItemOption)
							shoppingCartItemOption.save(flush:true)
						}
					}
				}
			}
				
		}
		
		request.shoppingCartsCount = count
	}
	
	
	
	def saveSpecificationData(specificationData){
		/**
        "specifications": [],
        "specificationOptions": [],
        "productSpecifications": []
		**/
		def count = 0
		if(specificationData.specifications){
			specificationData.specifications.each(){ sp ->
									
				def existingSpecification = Specification.findByUuid(sp.uuid)
				if(!existingSpecification){
					
					if(params.performImport == "true"){	
										
						def specification = new Specification()
						specification.uuid = sp.uuid
	    	    		
						specification.name = sp.name
						specification.filterName = sp.filterName
			    		specification.position = sp.position
	    	    		
						specification.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", sp.dateCreated)
						specification.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", sp.lastUpdated)
						
						specification.save(flush:true)
						
						sp.catalogs.each(){ c ->
							def catalog = Catalog.findByUuid(c)
							if(catalog){
								specification.addToCatalogs(catalog)
								specification.save(flush:true)
							}
						}
					}
				}
				
				count++
			}

			if(params.performImport == "true"){	
				if(Specification.count() > 0){
					if(specificationData.specificationOptions){
						println "specification options..."
						specificationData.specificationOptions.each(){ spo ->
							def specification = Specification.findByUuid(spo.specification)
							if(specification){

								def specificationOption = new SpecificationOption()
								specificationOption.uuid = spo.uuid
								specificationOption.name = spo.name
						    	specificationOption.position = spo.position
                            	
								specificationOption.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", spo.dateCreated)
								specificationOption.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", spo.lastUpdated)
								
								specificationOption.specification = specification
								specificationOption.save(flush:true)
								
								specification.addToSpecificationOptions(specificationOption)
								specification.save(flush:true)
							}
						}
					}
				
					if(specificationData.productSpecifications){
						println "product specifications options..."
						specificationData.productSpecifications.each(){ ps ->
							def specification = Specification.findByUuid(ps.specification)
							def specificationOption = SpecificationOption.findByUuid(ps.specificationOption)
							def product = Product.findByUuid(ps.product)
							
							if(product && specification && specificationOption){
								def productSpecification = new ProductSpecification()
								//TODO: add uuid
								productSpecification.product = product
								productSpecification.specification = specification
								productSpecification.specificationOption = specificationOption
								productSpecification.save(flush:true)
								
								product.addToProductSpecifications(productSpecification)
								product.save(flush:true)
							}
						}
				
					}
				}
			}
		}
		
		request.productSpecificationsCount = count
	}
	
	
	def saveProductOptionData(productOptionData){
		def count = 0
		if(productOptionData.productOptions){
			productOptionData.productOptions.each(){ data ->
				def product = Product.findByUuid(data.product)
				if(product){
					println "product = " + product
					if(params.performImport == "true"){		
						def productOption = new ProductOption()
						productOption.uuid = data.uuid
						productOption.name = data.name
						productOption.product = product
						productOption.save(flush:true)
						
						product.addToProductOptions(productOption)
						product.save(flush:true)					}
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
			
			if(!existingProduct && 
					catalogsExist(data.catalogs)){
				/**
					TODO: requires all catalogs to be created. reconsider for updates
				**/
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
	
	
	def catalogsExist(catalogs){
		def allExist = true
		catalogs.each(){ catalog ->
			def existingCatalog = Catalog.findByUuid(catalog)
			if(!existingCatalog){
				allExist = false
			}
		}
		return allExist
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
					saveSubcatalogs(catalog, data.subcatalogs)
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