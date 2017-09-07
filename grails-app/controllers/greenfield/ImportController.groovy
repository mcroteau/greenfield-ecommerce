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
import org.greenfield.common.ShoppingCartStatus

import org.greenfield.Transaction
import org.greenfield.Page
import org.greenfield.Upload
import org.greenfield.Layout

import org.greenfield.log.CatalogViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.SearchLog

import org.greenfield.State

import java.text.SimpleDateFormat
import groovy.time.TimeDuration
import groovy.time.TimeCategory

import java.io.File
import groovy.json.JsonSlurper

import grails.converters.JSON


@Mixin(BaseController)
class ImportController {

	def logsImported = 0
	def catalogCount = 0
	def pagesUpdated = 0


	
	@Secured('ROLE_ADMIN')
	def validate(){
		println "Accounts : ${Account.count()}"
		println "Catalogs : ${Catalog.count()}"
		println "Products : ${Product.count()}"
		println "Product Options : ${ProductOption.count()}"
		println "Option Variants : ${Variant.count()}"
		println "Specifications : ${Specification.count()}"
		println "SpecificationOptions : ${SpecificationOption.count()}"
		println "ProductSpecifications : ${ProductSpecification.count()}"
		println "AdditionalPhotos : ${AdditionalPhoto.count()}"
		println "Uploads : ${Upload.count()}"
		println "Orders : ${Transaction.count()}"
		println "CatalogViews : ${CatalogViewLog.count()}"
		println "ProductViews : ${ProductViewLog.count()}"
		println "PageViews : ${PageViewLog.count()}"
		println "SearchLogs : ${SearchLog.count()}"
		println "ShoppingCarts : ${ShoppingCart.count()}"
		println "ShoppingCartItems : ${ShoppingCartItem.count()}"
		println "ShoppingCartItemOptions : ${ShoppingCartItemOption.count()}"
		println "Abandoned/Active Carts : ${ShoppingCart.countByStatus(ShoppingCartStatus.ACTIVE.description())}"
		
		def accounts = ShoppingCart.list()
		
		render accounts as JSON
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def view_import(){
		//TODO:add numbers of data to be exported
	}

	
 	@Secured(['ROLE_ADMIN'])
	def import_data(){
			
		try{
			def sdf = new SimpleDateFormat("dd MMM - hh:mm:ssa")
		
			def startDate = new Date()
			def startDateTime = sdf.format(startDate)
		
			println "**********************************************************"
			println "             Data Import ${startDateTime}                 "
			println "**********************************************************"
			
			def jsonMultipartFile = request.getFile('json-data')
			def jsonFile = convert(jsonMultipartFile)
			
			def jsonSlurper = new JsonSlurper()
			def json = jsonSlurper.parseText(jsonFile.text)
			
			if(json['accounts']){
				println "importing accounts..."
				def accountsCount = Account.count()
				saveAccountData(json['accounts']['data'])
				request.accountsImported = Account.count() - accountsCount
			}
			
			
			if(json['catalogs']){
				println "importing catalogs..."
				def catalogsCount = Catalog.count()
				saveCatalogData(json['catalogs']['data'])			
				request.catalogsImported = Catalog.count() - catalogsCount
			}
			
			
			if(json['products']){
				println "importing products..."
				def productCount = Product.count()
				saveProductData(json['products']['data'])
				request.productsImported = Product.count() - productCount
			}
			
			
			if(json['productOptions']){
				println "importing product options..."
				def productOptionCount = ProductOption.count()
				saveProductOptionData(json['productOptions']['data'])
				request.productOptionsImported = ProductOption.count() - productOptionCount
			}
			
			
			if(json['specifications']){
				println "importing product specifications..."
				def specificationCount = Specification.count()
				saveSpecificationData(json['specifications']['data'], json['productSpecifications']['data'])
				request.specificationsImported = Specification.count() - specificationCount
			}
			
			
			if(json['additionalPhotos']){
				println "importing additional photos..."
				def additionalPhotosCount = AdditionalPhoto.count()
				saveAdditionalPhotos(json['additionalPhotos']['data'])
				request.additionalPhotosImported = AdditionalPhoto.count() - additionalPhotosCount
			
			}
			
			
			if(json['shoppingCarts']){
				println "importing shopping carts..."
				def shoppingCartCount = ShoppingCart.count()
				saveShoppingCartData(json['shoppingCarts']['data'])
				request.shoppingCartsImported = ShoppingCart.count() - shoppingCartCount
			}
			
			
			if(json['orders']){
				println "importing orders..."
				def ordersCount = Transaction.count()
				saveTransactionData(json['orders']['data'])
				request.ordersImported = Transaction.count() - ordersCount
			}
			
			
			if(json['pages']){
				println "importing pages..."
				def pagesCount = Page.count()
				savePageData(json['pages']['data'])
				request.pagesImported = (Page.count() - pagesCount) + pagesUpdated
			}
		
		
			if(json['uploads']){
				println "importing uploads..."
				def uploadsCount = Upload.count()
				saveUploadsData(json['uploads']['data'])
				request.uploadsImported = Upload.count() - uploadsCount
			}
			
			
			if(json['layout']){
				println "importing layout..."
				saveLayoutData(json['layout'])
				request.layoutImported = 1
			}
			
			
			if(json['logs']){
				println "importing logs..."
				def catalogViewLogsCount = CatalogViewLog.count()
				def productViewLogsCount = ProductViewLog.count()
				def pageViewLogsCount = PageViewLog.count()
				def searchQueriesCount = SearchLog.count()
				
				saveLogData(json['logs'])
				
				def queriesTotal = SearchLog.count() - searchQueriesCount
				def pageViewsTotal = PageViewLog.count() - pageViewLogsCount
				def productViewsTotal = ProductViewLog.count() - productViewLogsCount
				def catalogViewsTotal = CatalogViewLog.count() - catalogViewLogsCount
				
				request.logsImported = queriesTotal + pageViewsTotal + productViewsTotal + catalogViewsTotal
			}
		
			def endDate = new Date()
			def endDateTime = sdf.format(endDate)
		
			TimeDuration duration = TimeCategory.minus(endDate, startDate)
			
			println "**********************************************************"
			println "           Import Complete ${endDateTime}                 "
		    println "           Total Time : ${duration}                       "
			println "**********************************************************"
			
			
			if(params.performImport == "true"){
				flash.message = "Successfully imported data"
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
	


	
	
	def saveAccountData(accounts){
		def count = 0
		accounts.each(){ data ->
			if(data.username != 'admin'){
			
				def existingAccount = Account.findByUsername(data.username)
	
				if(!existingAccount){
					
					def account = new Account()
					
					account.uuid = data.uuid
					account.username = data.username
					account.password = data.password
					account.name = data.name
					account.email = data.email
					
		            account.address1 = data.address1
		            account.address2 = data.address2
		            account.city = data.city
					
					def state = null
					if(data.state){
						state = State.get(data.state)
					}
					account.state = state
					
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
						
						if(!account.validate()){
						    account.errors.allErrors.each {
						        println it
						    }
						}
						
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
					
				}else{
					//account already exists
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
			}else{
				//TODO:catalog already exists
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
	
	
	
	
	
	def saveProductData(products){
		def count = 0
		
		products.each(){ data ->
			def existingProduct = Product.findByUuid(data.uuid)
			
			if(!existingProduct){
				/**
					TODO: requires all catalogs to be created. reconsider for updates
				**/
				if(catalogsExist(data.catalogs)){
				
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
						
						product.save(flush:true)//TODO:uncomment
			    		
						data.catalogs.each(){ c ->
							def catalog = Catalog.findByUuid(c)
							if(catalog){
								product.addToCatalogs(catalog)
								product.save(flush:true)//TODO:uncomment
							}
						}
					}	
				}else{
					flash.message = "Make sure catalogs have been imported before products otherwise products will not be imported."
				}	

				count++	
			}else{
				flash.message = "Not all data will import as some data already exists with the same identifier"
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
	
	
	
	def saveProductOptionData(productOptions){
		def count = 0
		productOptions.each(){ po ->
			
			def existingProductOption = ProductOption.findByUuid(po.uuid)
			if(!existingProductOption){
			
				def product = Product.findByUuid(po.product)
				
				if(product){
					if(params.performImport == "true"){		
						def productOption = new ProductOption()
						productOption.uuid = po.uuid
						productOption.name = po.name
						productOption.product = product
						productOption.save(flush:true)
						
						product.addToProductOptions(productOption)
						product.save(flush:true)	
						
						if(po.variants){
							po.variants.each(){ v ->
								def variant = new Variant()
								variant.uuid = v.uuid
								variant.name = v.name
								variant.price = v.price
								variant.imageUrl = v.imageUrl
								variant.position = v.position
								variant.productOption = productOption
                    	
								variant.save(flush:true)
								productOption.addToVariants(variant)
								productOption.save(flush:true)
							}
						}				
					}
				}else{
					//TODO: product doesn't exist
				}
				
				count++
			}else{
				//TODO:existing product option
			}
		}
		
		request.productOptionsCount = count
	}
	
	
	
	
	def saveSpecificationData(specifications, productSpecifications){
		def count = 0
		if(specifications){
			specifications.each(){ sp ->
									
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
						
						if(sp.specificationOptions){
							sp.specificationOptions.each(){ spo ->
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
						
						sp.catalogs.each(){ c ->
							def catalog = Catalog.findByUuid(c)
							if(catalog){
								specification.addToCatalogs(catalog)
								specification.save(flush:true)
							}
						}
					}
				
					count++
				}else{
					//TODO:existing specification
				}
				
			}

			if(params.performImport == "true"){	
				
				if(productSpecifications){
					
					productSpecifications.each(){ ps ->
						
						def specification = Specification.findByUuid(ps.specification)
						def specificationOption = SpecificationOption.findByUuid(ps.specificationOption)
						def product = Product.findByUuid(ps.product)
						
						if(product && 
								specification && 
									specificationOption){
										
							def productSpecification = new ProductSpecification()
							
							productSpecification.uuid = ps.uuid
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
		request.productSpecificationsCount = count
	}
	

	
	def saveAdditionalPhotos(additionalPhotos){
		def count = 0
		additionalPhotos.each(){ ap ->
			
			def existingAdditionalPhoto = AdditionalPhoto.findByUuid(ap.uuid)
			
			if(!existingAdditionalPhoto){ 
			
				def product = Product.findByUuid(ap.product)
				if(product){	
					
					if(params.performImport == "true"){
					
						def additionalPhoto = new AdditionalPhoto()
						additionalPhoto.uuid = ap.uuid
						additionalPhoto.name = ap.name
						additionalPhoto.imageUrl = ap.imageUrl
						additionalPhoto.detailsImageUrl = ap.detailsImageUrl
						
						additionalPhoto.product = product
						
	        			additionalPhoto.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", ap.dateCreated)
	        			additionalPhoto.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", ap.lastUpdated)
						
						additionalPhoto.save(flush:true)
					}
					
				}
				
				
				count++
			}else{
				flash.message = "Not all data will be imported. Please make sure you have catalogs and products created"
			}
		}	
		request.additionalPhotosCount = count
	}
	

	
	
	def saveShoppingCartData(shoppingCarts){
		def count = 0
		if(shoppingCarts){
		
			shoppingCarts.each(){ sc ->
				
				def existingShoppingCart = ShoppingCart.findByUuid(sc.uuid)
				
				if(!existingShoppingCart){
					
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
					
					
					if(sc.shoppingCartItems && 
							params.performImport == "true"){	
							
						if(!shoppingCart.validate()){
						    shoppingCart.errors.allErrors.each {
						        println it
						    }
						}
						shoppingCart.save(flush:true)
										
						/** TODO: anonymous checkout
						if(account){
							account.createShoppingCartPermission(shoppingCart)
						}				
						**/
						
						sc.shoppingCartItems.each(){ sci ->
							def product = Product.findByUuid(sci.product)
							
							if(product){
								
								def shoppingCartItem = new ShoppingCartItem()
								shoppingCartItem.uuid = sci.uuid
								shoppingCartItem.quantity = sci.quantity
								shoppingCartItem.product = product
								shoppingCartItem.shoppingCart = shoppingCart
								shoppingCartItem.save(flush:true)
								
								shoppingCart.addToShoppingCartItems(shoppingCartItem)
								shoppingCart.save(flush:true)
								
								if(sci.shoppingCartItemOptions){
									sci.shoppingCartItemOptions.each(){ scio ->
										
										def variant = Variant.findByUuid(scio.variant)
										if(variant){
											def shoppingCartItemOption = new ShoppingCartItemOption()
											shoppingCartItemOption.uuid = scio.uuid
											shoppingCartItemOption.variant = variant
											shoppingCartItemOption.shoppingCartItem = shoppingCartItem
											shoppingCartItemOption.save(flush:true)
											
											shoppingCartItem.addToShoppingCartItemOptions(shoppingCartItemOption)
											shoppingCartItem.save(flush:true)
										}
										
									}
								}
							}
						}
					}

					count++

				}else{
					flash.message = "Some shopping carts already exist with the same identifier"
				}
			}
		}
		
		request.shoppingCartsCount = count
	}
	
	
	
	def saveTransactionData(transactions){
		def count = 0
		
		if(transactions){
			transactions.each(){ t ->
				
				def existingTransaction = Transaction.findByUuid(t.uuid)
				
				if(!existingTransaction){
					def transaction = new Transaction()
					
					def account = Account.findByUuid(t.account)
					def shoppingCart = ShoppingCart.findByUuid(t.shoppingCart)
					
					if(account && shoppingCart){
						
						transaction.uuid = t.uuid
						transaction.account = account
						transaction.shoppingCart = shoppingCart
						
						transaction.total = t.total
						transaction.subtotal = t.subtotal
						transaction.shipping = t.shipping
						transaction.taxes = t.taxes
						
						transaction.status = t.status
						transaction.orderDate = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", t.orderDate)
						
						transaction.chargeId = t.chargeId
						transaction.postageId = t.postageId
						transaction.postageUrl = t.postageUrl
	                	
                    	transaction.shipName = t.shipName
						transaction.shipAddress1 = t.shipAddress1
						transaction.shipAddress2 = t.shipAddress2
						transaction.shipCity = t.shipCity
						
						def shipState = null
						if(t.shipState){
							shipState = State.get(t.shipState)
						}
						transaction.shipState = shipState
						transaction.shipZip = t.shipZip
	                	
						transaction.billName = t.billName
						transaction.billAddress1 = t.billAddress1
						transaction.billAddress2 = t.billAddress2
						transaction.billCity = t.billCity
						
						def billState = null
						if(t.billState){
							billState = State.get(t.billState)
						}
						transaction.billState = billState
						transaction.billZip = t.billZip
	                	
						transaction.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", t.dateCreated)
						transaction.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", t.lastUpdated)
						
						transaction.save(flush:true)
						
						account.addToTransactions(transaction)
						account.save(flush:true)

						account.orders = Transaction.countByAccount(account)
						account.save(flush:true)
						
						account.createTransactionPermission(transaction)
						
					}
					
					count++
				}
			}		
		}
		request.ordersCount = count
	}

	
	def savePageData(pages){
		def count = 0
		pagesUpdated = 0
		
		pages.each(){ p ->
			
			if(params.performImport == "true"){
				
				def existingPage = Page.findByTitle(p.title)
				if(existingPage){
					existingPage.uuid = p.uuid
					existingPage.title = p.title
					existingPage.content = p.content
					existingPage.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", p.dateCreated)
					existingPage.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", p.lastUpdated)
					
					existingPage.save(flush:true)
					
					pagesUpdated++
					
				}else{
					
					def existingByUuid = Page.findByUuid(p.uuid)
					
					if(!existingByUuid){
						
						def page = new Page()
						page.uuid = p.uuid
						page.title = p.title
						page.content = p.content
						page.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", p.dateCreated)
						page.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", p.lastUpdated)
					
						page.save(flush:true)
						
					}else{
						existingByUuid.title = p.title
						existingByUuid.title = p.title
						existingByUuid.content = p.content
						existingByUuid.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", p.dateCreated)
						existingByUuid.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", p.lastUpdated)
						existingByUuid.save(flush:true)
						
						pagesUpdated++
					}
				}
			}
			count++
		}
		request.pagesCount = count
	}
	
	
	
	def saveUploadsData(uploads){
		def count = 0
		uploads.each(){ u ->	
			def existingUpload = Upload.findByUuid(u.uuid)
			if(!existingUpload){

				if(params.performImport == "true"){
					
					def upload = new Upload()
					
					upload.uuid = u.uuid
					upload.url = u.url
					upload.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", u.dateCreated)
					upload.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", u.lastUpdated)
			    	
					upload.save(flush:true)
				}
				
				count++
			}
		}
		request.uploadsCount = count
	}
	
	
	
	
	def saveLayoutData(layoutData){
		if(params.performImport == "true"){
			def layout = Layout.findByName("STORE_LAYOUT")
			layout.uuid = layoutData.uuid
			layout.content = layoutData.content
			layout.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", layoutData.dateCreated)
			layout.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", layoutData.lastUpdated)
			layout.save(flush:true)
		}
		request.layoutCount = 1
	}
	
	
	
	
	def saveLogData(logData){
		def count = 0

		if(logData.catalogViewLogs){
			logData.catalogViewLogs['data'].each(){ cvl ->
				
				def existingCatalogViewLog = CatalogViewLog.findByUuid(cvl.uuid)
				if(!existingCatalogViewLog){
					def catalog = Catalog.findByUuid(cvl.catalog)
					
					if(catalog){
						if(params.performImport == "true"){
							def account = Account.findByUuid(cvl?.account)
							def catalogViewLog = new CatalogViewLog()
							
							catalogViewLog.uuid = cvl.uuid
							catalogViewLog.catalog = catalog
							catalogViewLog.account = account
							
							catalogViewLog.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", cvl.dateCreated)
							catalogViewLog.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", cvl.lastUpdated)
							
							catalogViewLog.save(flush:true)

							if(account){
								account.catalogViews = CatalogViewLog.countByAccount(account)
								account.save(flush:true)
							}
							
						}
					}

					count++
				}
			}
		}
		println "importing catalog views..."


		if(logData.productViewLogs){
			logData.productViewLogs['data'].each(){ pvl ->
				
				def existingProductViewLog = ProductViewLog.findByUuid(pvl.uuid)
				if(!existingProductViewLog){
					
					def product = Product.findByUuid(pvl.product)
					
					if(product){
						if(params.performImport == "true"){
							
							def account = Account.findByUuid(pvl?.account)
							def productViewLog = new ProductViewLog()
							
							productViewLog.uuid = pvl.uuid
							productViewLog.product = product
							productViewLog.account = account

							productViewLog.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", pvl.dateCreated)
							productViewLog.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", pvl.lastUpdated)
							
							productViewLog.save(flush:true)

							if(account){
								account.productViews = ProductViewLog.countByAccount(account)
								account.save(flush:true)
							}
							
						}
					}
					count++
				}
			}
		}	
		println "importing product views..."
		


		if(logData.pageViewLogs){
			logData.pageViewLogs['data'].each(){ pvl ->
				
				def existingPageViewLog = PageViewLog.findByUuid(pvl.uuid)
				if(!existingPageViewLog){
					
					def page = Page.findByUuid(pvl.page)
					
					if(page){
						if(params.performImport == "true"){
							
							def account = Account.findByUuid(pvl?.account)
							def pageViewLog = new PageViewLog()
							
							pageViewLog.uuid = pvl.uuid
							pageViewLog.page = page
							pageViewLog.account = account
							
							pageViewLog.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", pvl.dateCreated)
							pageViewLog.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", pvl.lastUpdated)
							
							pageViewLog.save(flush:true)
							
							if(account){
								account.pageViews = PageViewLog.countByAccount(account)
								account.save(flush:true)
							}
							
						}
					}
					count++
				}
			}
		}	
		println "importing page views..."


		if(logData.searchLogs){
			logData.searchLogs['data'].each(){ sl ->
				def existingSearchLog = SearchLog.findByUuid(sl.uuid)
				if(!existingSearchLog){
					
					if(params.performImport == "true"){
						
						def account = Account.findByUuid(sl?.account)
						def searchLog = new SearchLog()
						
						searchLog.uuid = sl.uuid
						searchLog.query = sl.query
						searchLog.account = account
						
						searchLog.dateCreated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", sl.dateCreated)
						searchLog.lastUpdated = Date.parse("yyyy-MM-dd'T'HH:mm:ssX", sl.lastUpdated)
						
						searchLog.save(flush:true)

						if(account){
							account.searches = SearchLog.countByAccount(account)
							account.save(flush:true)
						}
						
					}
					count++
				}
			}
		}		
		println "importing searches..."


		//TODO: LoginLogs
		
		request.logsCount = count
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