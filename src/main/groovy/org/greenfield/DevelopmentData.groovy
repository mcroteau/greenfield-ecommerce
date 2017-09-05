package org.greenfield

import org.apache.shiro.crypto.hash.Sha256Hash
import org.greenfield.common.ShoppingCartStatus
import org.greenfield.common.OrderStatus
import org.greenfield.common.RoleName
import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCartItemOption
import org.greenfield.Transaction
import org.greenfield.Account
import org.greenfield.Product
import org.greenfield.Catalog
import org.greenfield.Page
import org.greenfield.Role

import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.CatalogViewLog
import org.greenfield.log.SearchLog

public class DevelopmentData {

    def NUMBER_CUSTOMERS      = 0

	def MAX_DAYS              = 30
	def CUSTOMERS_COUNT       = 20
	def ORDERS_COUNT          = 20
	def PAGE_VIEWS_COUNT      = 10
	def PRODUCT_VIEWS_COUNT   = 30
	def CATALOG_VIEWS_COUNT   = 40
	def SEARCH_QUERIES_COUNT  = 30
	def ABANDONED_CARTS_COUNT = 10
	
	
	def queries = ["gaming systems", "slot machines", "rummy sets", "double decks", "porcelain dice", "dice set", "poker chips", "chip sets", "game machines", "price is right", "las vegas", "shot glasses", "poker tables", "table tops"]	
	
	def catalogs = [ 
		[ 
			"name" : "Poker",
			"subcatalogs" : [
				[ 
                    "name" : "Poker Tables",
                    "products" : 10 
                ],
				[
					"name" : "Poker Chips",
					"subcatalogs" : [
						[ 
                            "name" : "Ceramic Poker Chips",
                            "products" : 10
                        ],
						[ 
                            "name" : "Clay Poker Chips",
                            "products" : 20
                        ],
						[ 
                            "name" : "Composite Poker Chips",
                            "products" : 10
                        ]
					]
				],
				[ 
                    "name" : "Poker Chip Cases",
                    "products" : 10 
                ]
			]
		 ],
		 [
		 	"name" : "Card Decks",
			"products" : 10
		 ],
		 [ 
             "name" : "Gaming Systems",
             "products" : 20 
         ],
		 [ 
		 	"name" : "Collectibles",
		 	"subcatalogs" : [
				[ 
                    "name" : "Coffee Mugs",
                    "products" : 10
                ],
				[ 
                    "name" : "Coasters",
                    "products" : 20
                ],
				[ 
                    "name" : "Magnets",
                    "products" : 10 
                ]
			]
		]
	]
    
    
    def specifications = [
        [
            "name"     : "Brand",
            "options"  : [ "Brybelly", "Giantex", "Bicycle" ],
            "catalogs" : [ "Poker Chips", 
                            "Ceramic Poker Chips", "Clay Poker Chips", "Composite Poker Chips" ]
        ],
        [
            "name"     : "Weight",
            "options"  : [ "9.0", "9.5", "10.0", "10.5", "11", "11.5" ],
            "catalogs" : [ "Poker Chips", 
                            "Ceramic Poker Chips", "Clay Poker Chips", "Composite Poker Chips" ]
        ],
        [
            "name"     : "Set Size",
            "options"  : [ "100pc", "200pc", "300pc", "400pc", "500pc" ],
            "catalogs" : [ "Poker Chips", 
                            "Ceramic Poker Chips", "Clay Poker Chips", "Composite Poker Chips" ]
        ],        
        [
            "name"     : "Size",
            "options"  : [ "Small", "Medium", "Large" ],
            "catalogs" : [ "Poker Tables" ]
        ]   
    ]

	
	def productOptions = [
		[
			"name" : "Type",
			"catalog" : "Poker Chip Cases",
			"variants" : [
				[
					"name" : "Aluminum",
					"price" : 0
				],
				[
					"name" : "Oak",
					"price" : 100
				],
				[
					"name" : "Cherrywood",
					"price" : 300
				],
				[
					"name" : "Mahogany",
					"price" : 500
				]
			]
		],
		[
			"name" : "Type",
			"catalog" : "Coasters",
			"variants" : [ 
				[
					"name" : "Cork",
					"price" : 0
				],
				[
					"name" : "Stone",
					"price" : 13
				]
			]
		],
		[
			"name" : "Display",
			"catalog" : "Gaming Systems",
			"variants" : [ 
				[
					"name" : "Basic",
					"price" : 0
				],
				[
					"name" : "Full Color Digital",
					"price" : 100
				]
			]
		],		
		[
			"name" : "Color",
			"catalog" : "Coffee Mugs",
			"variants" : [ 
				[
					"name" : "Ivory",
					"price" : 0
				],
				[
					"name" : "Graphite",
					"price" : 3
				]
			]
		],
		[
			"name" : "Size",
			"catalog" : "Coffee Mugs",
			"variants" : [ 
				[
					"name" : "8oz",
					"price" : 0
				],
				[
					"name" : "12oz",
					"price" : 3
				],
				[
					"name" : "16oz",
					"price" : 7
				]
			]
		]	
	]



	def springSecurityService

	DevelopmentData(springSecurityService){
		this.springSecurityService = springSecurityService
	}
	
	def init(){
		println "***********************************************"
		println "***       Generating Development Data       ***"
		println "***********************************************"

		createAccounts()
		createCatalogs()
		createProducts()
		createProductOptions()
        createSpecifications()
        createProductSpecifications()
		createAdditionalPhotos()
		createUploads()
		createOrders()
		createActivityLogs()
		createAbandonedCarts()
	}
	
	

	
	def createCatalogs(){
		catalogs.each{ c ->
			def catalog = new Catalog()
			catalog.name = c.name
			catalog.toplevel = true
			catalog.save(flush:true)
			if(c.subcatalogs){
				createSubcatalogs(c, catalog)
			}
		}
		
		println "Catalogs : ${Catalog.count()}"
	}
	
	
	
	def createSubcatalogs(catalogData, parentCatalog){
		catalogData.subcatalogs.each { c ->
			def subcatalog = new Catalog()
			subcatalog.name = c.name
			subcatalog.toplevel = false
			subcatalog.parentCatalog = parentCatalog
			subcatalog.save(flush:true)
			
			parentCatalog.addToSubcatalogs(subcatalog)
			parentCatalog.save(flush:true)
			
			if(c.subcatalogs){
				createSubcatalogs(c, subcatalog)
			}
		}
	}
	
	
	
	def createProducts(){
		catalogs.each { c ->
			def catalog = Catalog.findByName(c.name)
			if(!c.subcatalogs){
				def catalogIdsArray = []
				catalogIdsArray.add(catalog.id)
				createCatalogProducts(catalogIdsArray, c.products)
			}else{
				createSubcatalogProducts(c, catalog)
			}
		}
		println "Products : ${Product.count()}"
	}
	
	
	
	
	def createSubcatalogProducts(catalogData, parentCatalog){
		catalogData.subcatalogs.each { c ->
			def catalog = Catalog.findByName(c.name)
			if(c.subcatalogs){
				createSubcatalogProducts(c, catalog)
			}else{
				def ids = getCatalogIdsArray(catalog)
				def catalogIdsArray = ids.split(',').collect{it as int}
				createCatalogProducts(catalogIdsArray, c.products)
			}
		}
	}
	
	
	def createCatalogProducts(catalogIdsArray, numberProducts){
        if(numberProducts > 0){
            (1..numberProducts).each{ i ->
    			def product = new Product()
    			product.price = i * 10
    			product.quantity = i * 10
    			product.weight = 16
    			catalogIdsArray.each {
    				def cc = Catalog.get(it)
    				product.addToCatalogs(cc)
    			}
    			def lastCatalogId = catalogIdsArray[catalogIdsArray.size() - 1 ]
    			def lastCatalog = Catalog.get(lastCatalogId)
    			product.name = "${lastCatalog.name} ${i}"
			
    			product.save(flush:true)
    		}
        }
	}
	
    def createProductOptions(){
		
		productOptions.each() { po ->

			def catalog = Catalog.findByName(po.catalog)
			if(catalog){
				def products = Product.createCriteria().list(){
					catalogs {
				    	idEq(catalog.id)
				 	}
				}
				products.each(){ p ->
					def productOption = new ProductOption()
					productOption.name = po.name
					productOption.product = p
					productOption.save(flush:true)
					
					po.variants.each() { v -> 
						def variant = new Variant()
						variant.name = v.name
						variant.price = v.price
						variant.productOption = productOption
						variant.save(flush:true)
						
						productOption.addToVariants(variant)
						productOption.save(flush:true)
					}
					
					p.addToProductOptions(productOption)
					p.save(flush:true)
				}
			}
		}
		
		println "Product Options : ${ProductOption.count()}"
		println "Option Variants : ${Variant.count()}"
	}
	
	
	
    def createSpecifications(){
        specifications.eachWithIndex() { specificationObj, specificationPosition ->
            def specification = new Specification()
            specification.name = specificationObj.name
            specification.filterName = specificationObj.name.replaceAll(" ", "_").toLowerCase()
            specification.position = specificationPosition
            specification.save(flush:true)
        
            specificationObj.catalogs.each { catalogName ->
                def catalog = Catalog.findByName(catalogName)
                specification.addToCatalogs(catalog)
                specification.save(flush:true)
            }
            
            specificationObj.options.eachWithIndex(){ optionName, optionPosition ->
                def option = new SpecificationOption()
                option.name = optionName
                option.specification = specification
                option.position = optionPosition
                option.save(flush:true)
                
                specification.addToSpecificationOptions(option)
                specification.save(flush:true)                
            }
        }
		println "Specifications : ${Specification.count()}"
		println "SpecificationOptions : ${SpecificationOption.count()}"
    }
	

    
    
    def createProductSpecifications(){
		Random rand = new Random()
        specifications.each{ specificationObj ->
            def specification = Specification.findByName(specificationObj.name)
            def specificationCatalogs = specificationObj.catalogs

            def ids = []
            specificationCatalogs.each { catalogName ->
                def catalog = Catalog.findByName(catalogName)
                if(catalog){
                    ids.add(catalog.id)
                }
            }
            
            def products = Product.createCriteria().list{
                catalogs{
                    'in'('id', ids)
                }
            }
            
            products.unique { it.id }
            
            if(products){
                products.each { product ->
    			    int index = rand.nextInt(specificationObj.options.size())
                    
                    def optionName = specificationObj.options.get(index)
                    def option = SpecificationOption.findByName(optionName)
                    
                    def existing = ProductSpecification.findAllByProductAndSpecificationAndSpecificationOption(product, specification, option)
                    if(!existing){
                        def productSpecification = new ProductSpecification()
                        productSpecification.specificationOption = option
                        productSpecification.product = product
                        productSpecification.specification = specification
                        productSpecification.save(flush:true)
                    }
                }
            }
        }  
            
		println "ProductSpecifications : ${ProductSpecification.count()}"
    }
    
	
	def getCatalogIdsArray(catalog){
		def ids = new StringBuffer()
		ids.append(catalog.id)
		if(catalog.parentCatalog){
			ids.insert(0, getCatalogIdsArray(catalog.parentCatalog) + ",")
		}
		return ids.toString()
	}
	
	
	def createAdditionalPhotos(){
		(["Poker Tables", "Ceramic Poker Chips", "Clay Poker Chips", 
			"Composite Poker Chips", "Coffee Mugs", "Coasters", "Magnets"]).each(){ catalogName ->
				
			def catalog = Catalog.findByName(catalogName)
			
			if(catalog){
				
				def products = Product.createCriteria().list(){
					catalogs {
				    	idEq(catalog.id)
				 	}
				}
				products.eachWithIndex(){ product, k ->
					if(k % 5 == 0){
						def additionalPhoto = new AdditionalPhoto()
						additionalPhoto.product = product
						additionalPhoto.name = catalogName + " Photo " + k
						additionalPhoto.imageUrl = "images/additional-photo" + k + ".png"
						additionalPhoto.detailsImageUrl = "images/details-additional-photo" + k + ".png"
					
						additionalPhoto.save(flush:true)
						product.addToAdditionalPhotos(additionalPhoto)
						product.save(flush:true)
					}
				}
			}
		}
		
		println "AdditionalPhotos : ${AdditionalPhoto.count()}"
	}
    
	
	def createUploads(){
		(0..3).each() { n ->
			def upload = new Upload()
			upload.url = "uploads/upload" + n + ".png"
			upload.save(flush:true)
		}
		
		println "Uploads : ${Upload.count()}"
	}
	
	
	
	
	def createAccounts(){
		
		def customerRole = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
		//def password = new Sha256Hash('customer').toHex()
		def password = springSecurityService.encodePassword("password")

		(1..CUSTOMERS_COUNT).each {
			def customer = new Account()
			customer.username = "customer${it}"
			customer.password = password
			customer.name = "John Smith ${it}"
			customer.email = "customer${it}@email.com"
			
			customer.address1 = "${it} Main Street"
			customer.address2 = "Apt. #${it}"
			customer.city = "Anchorage"
			customer.state = State.findByName("Alaska")
			customer.zip = "99501"
			customer.save(flush:true)

			customer.createAccountRoles(false)
			customer.createAccountPermission()
		
		}
		
		NUMBER_CUSTOMERS = Account.count()
		println "Accounts : ${NUMBER_CUSTOMERS}"
	}
	
	

	
	def createOrders(){
		
		Random rand = new Random()
		int productMax = Product.count()
		
		(1..ORDERS_COUNT).each(){
		
			int customerId = rand.nextInt(NUMBER_CUSTOMERS) + 1
			if(customerId == 1)customerId = customerId + 1
			
			def customer = Account.get(customerId)
			
			if(customer && 
				customer?.username != "admin"){
			
				def orderDate = generateRandomDate()

				int productId = rand.nextInt(productMax) + 1
				def product = Product.get(productId)
				
				if(product){
					def taxes = 2.00
					def shipping = 4.00

					def quantity = rand.nextInt(3) + 1
			
					def shoppingCart = new ShoppingCart()
					shoppingCart.account = customer
					shoppingCart.status = ShoppingCartStatus.TRANSACTION.description()
					shoppingCart.save(flush:true)
				
					shoppingCart.dateCreated = orderDate
				
				
					def shoppingCartItem = new ShoppingCartItem()
					shoppingCartItem.quantity = quantity
					shoppingCartItem.product = product
					shoppingCartItem.save(flush:true)
					
					def optionsTotal = 0
					if(product.productOptions){
						def productOption = product.productOptions[0]
						
						if(productOption.variants){
							def variant = productOption?.variants[1]
							
							def shoppingCartItemOption = new ShoppingCartItemOption()
							shoppingCartItemOption.variant = variant
							shoppingCartItemOption.shoppingCartItem = shoppingCartItem
							shoppingCartItemOption.save(flush:true)
							
							shoppingCartItem.addToShoppingCartItemOptions(shoppingCartItemOption)
							shoppingCartItem.save(flush:true)
							
							optionsTotal = variant.price * quantity
						}
					}
		
					shoppingCart.addToShoppingCartItems(shoppingCartItem)
					shoppingCart.save(flush:true)
					
					shoppingCart.taxes = taxes
					shoppingCart.shipping = shipping
					shoppingCart.subtotal = (product.price * quantity) + optionsTotal
					shoppingCart.total = shoppingCart.subtotal + taxes + shipping
					shoppingCart.save(flush:true)
					customer.createShoppingCartPermission(shoppingCart)
		

					def transaction = new Transaction()
			    	transaction.orderDate = orderDate
		
					transaction.total = shoppingCart.total
					transaction.subtotal = shoppingCart.subtotal
					transaction.taxes = shoppingCart.taxes
					transaction.shipping = shoppingCart.shipping
		
					transaction.status = OrderStatus.OPEN.description()
					transaction.shoppingCart = shoppingCart
					transaction.account = customer
		
					transaction.chargeId = "DEVELOPEMENT"
		
					transaction.shipName = customer.username
					transaction.shipAddress1 = customer.address1
					transaction.shipAddress2 = customer.address2
					transaction.shipCity = customer.city
					transaction.shipState = customer.state 
					transaction.shipZip = customer.zip
		
					transaction.save(flush:true)

					customer.createTransactionPermission(transaction)
				}
			}
		}
		
		println "Orders : ${Transaction.count()} "
	}

	
	def createActivityLogs(){
		generateCatalogViews()
		generateProductViews()
		generatePageViews()
		generateSearchQueries()
	}
	
	
	def generatePageViews(){
		Random rand = new Random()
		int pageMax = Page.count()
		
		(1..PAGE_VIEWS_COUNT).each(){ n ->
			int id = rand.nextInt(pageMax) + 1
			def page = Page.get(id)

			if(page){

				def pageViewLog = new PageViewLog()
				pageViewLog.page = page

				def account
				if(n % 2 == 0){
					int accountId = rand.nextInt(NUMBER_CUSTOMERS) + 1
					account = Account.get(accountId)
					pageViewLog.account = account
				}

				pageViewLog.dateCreated = generateRandomDate()
				pageViewLog.save(flush:true)
				
				if(account){
					account.pageViews = PageViewLog.countByAccount(account)
					account.save(flush:true)
				}
			}
		}
		println "PageViews : ${PageViewLog.count()}"
	}
	
	
	def generateProductViews(){
		Random rand = new Random()
		int productMax = Product.count()
		
		(1..PRODUCT_VIEWS_COUNT).each(){ n ->
			int id = rand.nextInt(productMax) + 1
			def product = Product.get(id)
			
			if(product){
				def productViewLog = new ProductViewLog()
				productViewLog.product = product

				def account
				if(n % 2 == 0){
					int accountId = rand.nextInt(NUMBER_CUSTOMERS) + 1
					account = Account.get(accountId)
					productViewLog.account = account
				}
				productViewLog.dateCreated = generateRandomDate()
				productViewLog.save(flush:true)
				
				if(account){
					account.productViews = ProductViewLog.countByAccount(account)
					account.save(flush:true)
				}
			}
		}
		println "ProductViews : ${ProductViewLog.count()}"
	}
	
	
	def generateCatalogViews(){		
		Random rand = new Random()
		int catalogMax = Catalog.count()
		
		(1..CATALOG_VIEWS_COUNT).each(){ n ->
			int id = rand.nextInt(catalogMax) + 1
			def catalog = Catalog.get(id)
			
			if(catalog){	
				def catalogViewLog = new CatalogViewLog()
				catalogViewLog.catalog = catalog

				def account
				if(n % 2 == 0){
					int accountId = rand.nextInt(NUMBER_CUSTOMERS) + 1
					account = Account.get(accountId)
					catalogViewLog.account = account
				}
				
				catalogViewLog.dateCreated = generateRandomDate()
				catalogViewLog.save(flush:true)
				
				if(account){
					account.catalogViews = CatalogViewLog.countByAccount(account)
					account.save(flush:true)
				}
			}
		}
		println "CatalogViews : ${CatalogViewLog.count()}"
	}
	
	
	def generateSearchQueries(){
		Random rand = new Random()
		int queriesMax = queries.size()
		
		(1..SEARCH_QUERIES_COUNT).each(){ n ->
		
			int random = rand.nextInt(queriesMax)
			def query = queries[random]
			
			def searchLog = new SearchLog()
			searchLog.query = query

			def account
			if(n % 2 == 0){
				int accountId = rand.nextInt(NUMBER_CUSTOMERS) + 1
				account = Account.get(accountId)
				searchLog.account = account
			}

			searchLog.dateCreated = generateRandomDate()
			searchLog.save(flush:true)
			
			if(account){
				account.searches = SearchLog.countByAccount(account)
				account.save(flush:true)
			}		
		}
		println "SearchLogs : ${SearchLog.count()}"
	}
	
	
	
	def createAbandonedCarts(){
		Random rand = new Random()
		int productMax = Product.count()
		
		(1..ABANDONED_CARTS_COUNT).each(){
		
			int productId = rand.nextInt(productMax) + 1
			
			int customerId = rand.nextInt(NUMBER_CUSTOMERS) + 1
			if(customerId == 1)customerId = customerId + 1
			
			def customer = Account.get(customerId)
			
			if(customer && 
				customer?.username != "admin"){
				
				def product = Product.get(productId)
				
				if(product){
					def taxes = 2.00
					def shipping = 4.00

					def quantity = rand.nextInt(3) + 1
			
					def shoppingCart = new ShoppingCart()
					shoppingCart.account = customer
					shoppingCart.status = ShoppingCartStatus.ACTIVE.description()
					shoppingCart.save(flush:true)

					shoppingCart.dateCreated = generateRandomDate()
				
					def shoppingCartItem = new ShoppingCartItem()
					shoppingCartItem.quantity = quantity
					shoppingCartItem.product = product
					shoppingCartItem.save(flush:true)
					
					def optionsTotal = 0
					if(product.productOptions){
						def productOption = product.productOptions[0]
						
						if(productOption.variants){
							def variant = productOption?.variants[1]
							
							def shoppingCartItemOption = new ShoppingCartItemOption()
							shoppingCartItemOption.variant = variant
							shoppingCartItemOption.shoppingCartItem = shoppingCartItem
							shoppingCartItemOption.save(flush:true)
							
							shoppingCartItem.addToShoppingCartItemOptions(shoppingCartItemOption)
							shoppingCartItem.save(flush:true)
							
							optionsTotal = variant.price * quantity
						}
					}
					
					shoppingCart.addToShoppingCartItems(shoppingCartItem)
					shoppingCart.save(flush:true)
		
					shoppingCart.taxes = taxes
					shoppingCart.shipping = shipping
					shoppingCart.subtotal = (product.price * quantity) + optionsTotal
					shoppingCart.total = shoppingCart.subtotal + taxes + shipping
					shoppingCart.save(flush:true)
				}
			}
		}
		
		println "ShoppingCarts : ${ShoppingCartItem.count()}"
		println "ShoppingCartItems : ${ShoppingCartItem.count()}"
		println "ShoppingCartItemOptions : ${ShoppingCartItemOption.count()}"
		println "Abandoned/Active Carts : ${ShoppingCart.countByStatus(ShoppingCartStatus.ACTIVE.description())}"
	}
	
	
	
	def generateRandomDate(){
		Random rand = new Random()
		def days = rand.nextInt(MAX_DAYS) + 1
		return new Date() - days
	}

	
}