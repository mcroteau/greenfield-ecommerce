package org.greenfield

import org.apache.shiro.crypto.hash.Sha256Hash
import grails.transaction.Transactional

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


@Transactional
class DevelopmentDataService {

	
	def MAX_DAYS = 40
	def PRODUCTS_COUNT        = 15
	def CUSTOMERS_COUNT       = 10
	def ORDERS_COUNT          = 20
	def PAGE_VIEWS_COUNT      = 70
	def PRODUCT_VIEWS_COUNT   = 50
	def CATALOG_VIEWS_COUNT   = 100
	def SEARCH_QUERIES_COUNT  = 40
	def ABANDONED_CARTS_COUNT = 5
	
	
	def queries = ["gaming systems", "slot machines", "rummy sets", "double decks", "porcelain dice", "dice set", "poker chips", "chip sets", "game machines", "price is right"]	
	
	
	def catalogs = ["Card Decks", "Porcelain Dice", "Slot Machines", "Everything Rummy", "Gaming Systems"]
	
	
	
	def generate(){
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
	
		if(Catalog.count() == 0){
			catalogs.each(){ name ->
				def catalog = new Catalog()
				catalog.name = name
				catalog.save(flush:true)
			}
		}

		println "Catalogs : ${Catalog.count()}"	
	}
	
	
	
	def createProducts(){
		
		def max = 50
		int catalogMax = Catalog.count()
		Random rand = new Random()
		
		(1..PRODUCTS_COUNT).each {
			
			int id = rand.nextInt(catalogMax) + 1
			def catalog = Catalog.get(id)
			
			def num = rand.nextInt(max)
			
			def product = new Product()
			product.productNo = num + "no"
			product.name = catalog.name + " " + it
			product.description = "description of product ${it}"
			product.quantity = num * 5
			product.price = num
			product.weight = num * 2
			product.length = 5
			product.height = 5
			product.width = 5
			product.imageUrl = "images/app/no-image.jpg"
			product.detailsImageUrl = "images/app/no-image.jpg"
			product.catalog = catalog
			product.save(flush:true)
			
		}
		
		println "Products : ${Product.count()} "
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
			
			if(customer.username != "admin"){

				int productId = rand.nextInt(productMax) + 1
				def product = Product.get(productId)
			
			
				def taxes = 2.00
				def shipping = 4.00

				def quantity = rand.nextInt(3) + 1
			
				def shoppingCart = new ShoppingCart()
				shoppingCart.account = customer
				shoppingCart.status = ShoppingCartStatus.TRANSACTION.description()
				shoppingCart.save(flush:true)

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
				def orderDate = generateRandomDate()
			
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
			def pageViewLog = new PageViewLog()
			
			pageViewLog.page = page
			pageViewLog.save(flush:true)
			
			pageViewLog.dateCreated = generateRandomDate()
			pageViewLog.save(flush:true)
		}
		println "PageViews : ${PageViewLog.count()}"
	}
	
	
	def generateProductViews(){
		Random rand = new Random()
		int productMax = Product.count()
		
		(1..PRODUCT_VIEWS_COUNT).each(){
			int id = rand.nextInt(productMax) + 1
			def product = Product.get(id)
			def productViewLog = new ProductViewLog()
			
			productViewLog.product = product
			productViewLog.save(flush:true)

			productViewLog.dateCreated = generateRandomDate()
			productViewLog.save(flush:true)
		}
		println "ProductViews : ${ProductViewLog.count()}"
	}
	
	
	def generateCatalogViews(){		
		Random rand = new Random()
		int catalogMax = Catalog.count()
		
		(1..CATALOG_VIEWS_COUNT).each(){
			int id = rand.nextInt(catalogMax) + 1
			def catalog = Catalog.get(id)
			def catalogViewLog = new CatalogViewLog()
			
			catalogViewLog.catalog = catalog
			catalogViewLog.save(flush:true)

			catalogViewLog.dateCreated = generateRandomDate()
			catalogViewLog.save(flush:true)
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
			
			if(customer.username != "admin"){
				
				def product = Product.get(productId)
				
				def taxes = 2.00
				def shipping = 4.00

				def quantity = rand.nextInt(3) + 1
			
				def shoppingCart = new ShoppingCart()
				shoppingCart.account = customer
				shoppingCart.status = ShoppingCartStatus.ACTIVE.description()
				shoppingCart.save(flush:true)

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
		
		println "Abandoned/Active Carts : ${ShoppingCart.countByStatus(ShoppingCartStatus.ACTIVE.description())}"
	}
	
	
	def generateRandomDate(){
		Random rand = new Random()
		def days = rand.nextInt(MAX_DAYS) + 1
		return new Date() - days
	}

}


