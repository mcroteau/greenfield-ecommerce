package org.greenfield

import org.apache.shiro.crypto.hash.Sha256Hash
import org.greenfield.common.ShoppingCartStatus
import org.greenfield.common.OrderStatus
import org.greenfield.common.RoleName
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCart
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


	def MAX_DAYS              = 90
	def MAX_PRODUCTS          = 30
	def CUSTOMERS_COUNT       = 20
	def ORDERS_COUNT          = 108
	def PAGE_VIEWS_COUNT      = 430
	def PRODUCT_VIEWS_COUNT   = 350
	def CATALOG_VIEWS_COUNT   = 450
	def SEARCH_QUERIES_COUNT  = 70
	def ABANDONED_CARTS_COUNT = 25
	
	
	def queries = ["gaming systems", "slot machines", "rummy sets", "double decks", "porcelain dice", "dice set", "poker chips", "chip sets", "game machines", "price is right", "las vegas", "shot glasses", "poker tables", "table tops"]	
	
	def catalogs = [ 
		[ 
			"name" : "Poker",
			"subcatalogs" : [
				[ 
                    "name" : "Poker Tables",
                    "products" : 20 
                ],
				[
					"name" : "Poker Chips",
					"subcatalogs" : [
						[ 
                            "name" : "Ceramic Poker Chips",
                            "products" : 20
                        ],
						[ 
                            "name" : "Clay Poker Chips",
                            "products" : 20
                        ],
						[ 
                            "name" : "Composite Poker Chips",
                            "products" : 20
                        ]
					]
				],
				[ 
                    "name" : "Poker Chip Cases",
                    "products" : 20 
                ]
			]
		 ],
		 [ 
             "name" : "Card Decks",
             "products" : 20 
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
                    "products" : 20
                ],
				[ 
                    "name" : "Ornaments",
                    "products" : 20
                ],
				[ 
                    "name" : "Coasters",
                    "products" : 20 
                ],
				[ 
                    "name" : "Keychains",
                    "products" : 20 
                ],
				[ 
                    "name" : "Magnets",
                    "products" : 20 
                ]
			]
		]
	]

	
	def init(){
		println "***************************************"
		println "***   Generating Development Data   ***"
		println "***************************************"
		
		createCatalogs()
		createProducts()
		createCustomers()
		createOrders()
		createActivityLogs()
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
				createCatalogProducts(catalogIdsArray)
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
				createCatalogProducts(catalogIdsArray)
			}
		}
	}
	
	
	def createCatalogProducts(catalogIdsArray){
		Random rand = new Random()
		def numberProducts = rand.nextInt(MAX_PRODUCTS) + 1
		
		(1..numberProducts).each{ i ->
			def product = new Product()
			product.price = i * 10
			product.quantity = i * 3
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
	
	
	
	def getCatalogIdsArray(catalog){
		def ids = new StringBuffer()
		ids.append(catalog.id)
		if(catalog.parentCatalog){
			ids.insert(0, getCatalogIdsArray(catalog.parentCatalog) + ",")
		}
		return ids.toString()
	}
	
	
	
	def createCustomers = {
		
		def customerRole = Role.findByName(RoleName.ROLE_CUSTOMER.description())
		def password = new Sha256Hash('customer').toHex()

		(1..CUSTOMERS_COUNT).each {
			def customer = new Account()
			customer.username = "customer${it}"
			customer.passwordHash = password
			customer.name = "John Smith ${it}"
			customer.email = "customer${it}@email.com"
			
			customer.address1 = "${it} Main Street"
			customer.address2 = "Apt. #${it}"
			customer.city = "Anchorage"
			customer.state = State.findByName("Alaska")
			customer.zip = "99501"
			customer.addToRoles(customerRole)
			
			customerRole.addToAccounts(customer)
			customerRole.save(flush:true)
			customer.save(flush:true)
		
			customer.addToPermissions("account:customer_profile:" + customer.id)
			customer.addToPermissions("account:customer_update:" + customer.id)
			customer.addToPermissions("account:customer_order_history:" + customer.id)
			customer.save(flush:true)
		
		}
		
		println "Customers : ${Account.count()}"
	}
	
	

	
	def createOrders(){
		
		Random rand = new Random()
		int customerMax = Account.count()
		int productMax = Product.count()
		
		(1..ORDERS_COUNT).each(){
		
			int customerId = rand.nextInt(customerMax) + 1
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
					shoppingCart.addToShoppingCartItems(shoppingCartItem)
					shoppingCart.save(flush:true)
		

					shoppingCart.taxes = taxes
					shoppingCart.shipping = shipping
					shoppingCart.subtotal = (product.price * quantity)
					shoppingCart.total = shoppingCart.subtotal + taxes + shipping
					shoppingCart.save(flush:true)
		
		
					def transaction = new Transaction()
			
					//println "${customer.username} : ${product.name} x ${quantity} -> ${orderDate.getDateString()}"
			
			
			    	transaction.orderDate = orderDate
		
					transaction.total = shoppingCart.total
					transaction.subtotal = shoppingCart.subtotal
					transaction.taxes = shoppingCart.taxes
					transaction.shipping = shoppingCart.shipping
		
					transaction.status = OrderStatus.OPEN.description()
					transaction.shoppingCart = shoppingCart
					transaction.account = customer
		
					transaction.chargeId = "DEVELOPEMENT"
		
					//Shipping Info
					transaction.shipName = customer.username
					transaction.shipAddress1 = customer.address1
					transaction.shipAddress2 = customer.address2
					transaction.shipCity = customer.city
					transaction.shipState = customer.state 
					transaction.shipZip = customer.zip
		
					transaction.save(flush:true)
				}
			}
		}
		
		println "Orders : ${Transaction.count()} "
	}

	
	
	
	def createActivityLogs(){
		generatePageViews()
		generateProductViews()
		generateCatalogViews()
		generateSearchQueries()
		generateAbandonedCarts()
	}
	
	
	def generatePageViews(){
		Random rand = new Random()
		int pageMax = Page.count()
		
		(1..PAGE_VIEWS_COUNT).each(){
			int id = rand.nextInt(pageMax) + 1
			def page = Page.get(id)
			if(page){
				def pageViewLog = new PageViewLog()
				
				pageViewLog.page = page
				pageViewLog.save(flush:true)
				
				pageViewLog.dateCreated = generateRandomDate()
				pageViewLog.save(flush:true)
			}
		}
		println "PageViews : ${PageViewLog.count()}"
	}
	
	
	def generateProductViews(){
		Random rand = new Random()
		int productMax = Product.count()
		
		(1..PRODUCT_VIEWS_COUNT).each(){
			int id = rand.nextInt(productMax) + 1
			def product = Product.get(id)
			
			if(product){
				def productViewLog = new ProductViewLog()
			
				productViewLog.product = product
				productViewLog.save(flush:true)

				productViewLog.dateCreated = generateRandomDate()
				productViewLog.save(flush:true)
			}
		}
		println "ProductViews : ${ProductViewLog.count()}"
	}
	
	
	def generateCatalogViews(){		
		Random rand = new Random()
		int catalogMax = Catalog.count()
		
		(1..CATALOG_VIEWS_COUNT).each(){
			int id = rand.nextInt(catalogMax) + 1
			def catalog = Catalog.get(id)
			
			if(catalog){	
				def catalogViewLog = new CatalogViewLog()
			
				catalogViewLog.catalog = catalog
				catalogViewLog.save(flush:true)

				catalogViewLog.dateCreated = generateRandomDate()
				catalogViewLog.save(flush:true)
			}
		}
		println "CatalogViews : ${CatalogViewLog.count()}"
	}
	
	
	def generateSearchQueries(){
		Random rand = new Random()
		int queriesMax = queries.size()
		
		(1..SEARCH_QUERIES_COUNT).each(){
		
			int random = rand.nextInt(queriesMax)
			def query = queries[random]
			
			def searchLog = new SearchLog()
			
			searchLog.query = query
			searchLog.save(flush:true)

			searchLog.dateCreated = generateRandomDate()
			searchLog.save(flush:true)
		}
		println "SearchQueries : ${SearchLog.count()}"
	}
	
	
	
	def generateAbandonedCarts(){
		Random rand = new Random()
		int productMax = Product.count()
		int customerMax = Account.count()
		
		(1..ABANDONED_CARTS_COUNT).each(){
		
			int productId = rand.nextInt(productMax) + 1
			int customerId = rand.nextInt(customerMax) + 1
			
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

					shoppingCart.addToShoppingCartItems(shoppingCartItem)
					shoppingCart.save(flush:true)
		
					shoppingCart.taxes = taxes
					shoppingCart.shipping = shipping
					shoppingCart.subtotal = (product.price * quantity)
					shoppingCart.total = shoppingCart.subtotal + taxes + shipping
					shoppingCart.save(flush:true)
				}
			}
		}
		
		println "Abandoned/Active Carts : ${ShoppingCart.countByStatus(ShoppingCartStatus.ACTIVE.description())}"
	}
	
	
	
	def generateRandomDate(){
		Random rand = new Random()
		def days = rand.nextInt(MAX_DAYS) + 1
		return new Date() - days
	}

	
}