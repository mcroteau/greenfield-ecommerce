import org.apache.shiro.crypto.hash.Sha512Hash
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.crypto.hash.Sha1Hash

import org.greenfield.Account
import org.greenfield.Role
import org.greenfield.common.RoleName
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.common.ShoppingCartStatus
import org.greenfield.common.OrderStatus
import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.Layout
import org.greenfield.Page
import org.greenfield.State
import org.greenfield.Country
import org.greenfield.Transaction
import grails.util.Environment


import java.util.Random
import groovy.io.FileType
			

class BootStrap {

	def adminRole
	def customerRole
	def salesmanRole
	def affiliateRole
	def serviceRole
	
	def grailsApplication
	def productLookup
	
    def init = { servletContext ->
		createCountries()
		createStates()
		createLayout()
		createPages()
		createRoles()
		createAdmin()
		println 'Accounts : ' + Account.count()

		//setupTestEnvironment()
	}
	
	
	
	
	def setupTestEnvironment(){
		createCatalogs()
		createProducts()
		createCustomers()
		createOrders()
	}

	

	def createRoles = {
		if(Role.count() == 0){
			adminRole = new Role(name : RoleName.ROLE_ADMIN.description()).save(flush:true)
			customerRole = new Role(name : RoleName.ROLE_CUSTOMER.description()).save(flush:true)
		}else{
			adminRole = Role.findByName(RoleName.ROLE_ADMIN.description())
			customerRole = Role.findByName(RoleName.ROLE_CUSTOMER.description())
		}
		
		println 'Roles : ' + Role.count()
	
	}
	
	
	
	def createAdmin = {

		if(Account.count() == 0){
			def password = new Sha256Hash('admin').toHex()
			
			def admin = new Account(username : 'admin', passwordHash : password, firstName : 'Admin', lastName: 'Admin', email : 'admin@email.com')
			admin.hasAdminRole = true
			
			admin.addToRoles(customerRole)
			admin.addToRoles(adminRole)
			
			customerRole.addToAccounts(admin)
			adminRole.addToAccounts(admin)
			
			customerRole.save(flush:true)
			adminRole.save(flush:true)
			
			admin.save(flush:true)
        	
			admin.addToPermissions("account:customer_profile:" + admin.id)
			admin.addToPermissions("account:customer_update:" + admin.id)
			admin.addToPermissions("account:customer_order_history:" + admin.id)
			admin.save(flush:true)
		}
		
	}
	

	
	
	def createCustomers = {
		def password = new Sha256Hash('customer').toHex()
		
		def customer = new Account(username : 'customer', passwordHash : password, name : 'customer', lastName: 'customer', email : 'customer1@email.com')
			
		customer.address1 = "1000 Main Street"
		customer.address2 = "Apt. #10"
		customer.city = "Reno"
		customer.state = State.findByName("Nevada")
		customer.zip = "89523"
		
		customer.addToRoles(customerRole)
		
		customerRole.addToAccounts(customer)
		customerRole.save(flush:true)

		
		customer.save(flush:true)
		
		customer.addToPermissions("account:customer_profile:" + customer.id)
		customer.addToPermissions("account:customer_update:" + customer.id)
		customer.addToPermissions("account:customer_order_history:" + customer.id)
		customer.save(flush:true)
		
		
		
		
		def customer2 = new Account(username : 'customer2', passwordHash : password, firstName : 'customer2', lastName: 'customer2', email : 'customer2@email.com')
		
		customer2.addToRoles(customerRole)
		
		customerRole.addToAccounts(customer2)
		customerRole.save(flush:true)
		
		customer2.save(flush:true)
		customer2.addToPermissions("account:customer_profile:" + customer2.id)
		customer2.addToPermissions("account:customer_update:" + customer2.id)
		customer2.addToPermissions("account:customer_order_history:" + customer2.id)
		customer2.save(flush:true)
		
		
		
		def customer3 = new Account(username : 'customer3', passwordHash : password, firstName : 'customer3', lastName: 'customer3', email : 'customer3@email.com')
		
		customer3.addToRoles(customerRole)
		
		customerRole.addToAccounts(customer3)
		customerRole.save(flush:true)
		
		customer3.save(flush:true)
		customer3.addToPermissions("account:customer_profile:" + customer3.id)
		customer3.addToPermissions("account:customer_update:" + customer3.id)
		customer3.addToPermissions("account:customer_order_history:" + customer3.id)
		customer3.save(flush:true)
		
		println "Accounts : ${Account.count()}"
	}
	
	
	
	
	
	def createCatalogs(){		
		def c1 = new Catalog()
		c1.name = "Poker Chips"
		c1.save(flush:true);
		
		def c2 = new Catalog()
		c2.name = "Porcelain Dice"
		c2.save(flush:true);
		
		def c3 = new Catalog()
		c3.name = "Double Decks"
		c3.save(flush:true);
		
		def c4 = new Catalog()
		c4.name = "Rummy"
		c4.save(flush:true);
		
		println "Catalogs : ${Catalog.count()}"
	}
	
	
	def createProducts(){
		
		int max = 50
		int catalog_max = 4
		Random rand = new Random()
		
		def catalogs = Catalog.findAll()
		
		(1..10).each {
			
			def catalog_id = rand.nextInt(catalog_max)
			
			def productName = ""
			switch(catalog_id){
				case 0 :
					productName = "Poker Chips ${it}"
					break;
				case 1 : 
					productName = "Porcelain Dice ${it}"
					break;
				case 2 : 
					productName = "Card Decks ${it}"
					break;
				case 3 : 
					productName = "Rummy Set ${it}"
					break;
				default : 
					productName = "Casino Set ${it}"
					break;
			}
			
			def num = rand.nextInt(max)
			
			def catalog = catalogs.get(catalog_id)
			
			def product = new Product()
			
			product.productNo = num + "no"
			product.name = productName
			product.description = "description of product ${it}"
			product.quantity = num * 20
			product.price = num
			product.weight = 10
			product.length = 5
			product.height = 5
			product.width = 5
			product.imageUrl = "images/app/no-image.jpg"
			product.detailsImageUrl = "images/app/no-image.jpg"
			product.catalog = catalog
			product.save(flush:true)
			
			productLookup = num
		}
		
		println "Products : ${Product.count()} "
		
	}
	
	
	def createOrders(){

		def customer = Account.findByUsername("customer")
		
		(1..3).each(){
			createMockTransaction(customer)
		}
		
		println "Orders : ${Transaction.count()} "
	}

	
	
	def createMockTransaction(customer){

		def taxes = 2.00
		def shipping = 4.00

		Random rand = new Random()
		def id = rand.nextInt(9) + 1
		def quantity = rand.nextInt(2) + 1
		def days = rand.nextInt(9) + 1

		def shoppingCart = new ShoppingCart()
		shoppingCart.account = customer
		shoppingCart.status = ShoppingCartStatus.TRANSACTION.description()
		shoppingCart.save(flush:true)

		def product = Product.get(id)		
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
    	transaction.orderDate = new Date() + days
		
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
	
	
	
	
	def createLayout(){
	
		if(Layout.count() == 0){		
			
			File layoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout.html").file
			String layoutContent = layoutFile.text
			
			def layout = new Layout()
			layout.content = layoutContent
			layout.name = "STORE_LAYOUT"
			layout.save(flush:true)
			
		}
		
		println "Layouts : ${Layout.count()}"
	}



	
	def createPages(){
		createHomepage()
		createAboutUs()
		createContactUs()
		createPrivacyPolicy()
		println "Pages : ${Page.count()}"
	}
	
	def createHomepage(){
		def homepage = Page.findByTitle("Home")
		if(!homepage){
			def home = new Page()
			home.title = "Home"
			home.content = "Put your home page content here..."
			home.save(flush:true)
		}
	}
	
	
	def createAboutUs(){
		def aboutUs = Page.findByTitle("About Us")
		if(!aboutUs){
			def page = new Page()
			page.title = "About Us"
			page.content = "<p>Located downtown, we are a small boutique gift shop specializing in casino products.  Let us help you choose the perfect set of poker chips or porcelain dice.</p><p>Our knowledgeable and friendly staff is ready to assist you. We will gift wrap, pack and ship any purchase.</p>"
			page.save(flush:true)
		}
	}
	
	def createContactUs(){
		def contactUs = Page.findByTitle("Contact Us")
		if(!contactUs){
			def page = new Page()
			page.title = "Contact Us"
			page.content = "<address><strong>Suited Spades Gift Shop</strong><br>1000 Main Street, Suite 543<br>Henderson, NV 89002<br><abbr title=\"Phone\">P:</abbr> (800) 543-8765</address>"
			page.save(flush:true)
		}
	}
	
	def createPrivacyPolicy(){
		def privacyPage = Page.findByTitle("Privacy Policy")
		if(!privacyPage){
			def page = new Page()
			page.title = "Privacy Policy"
			page.content = "<p>Your privacy is important to us. Any information, both personal and financial, given to Suites Spade's Gift Shop will not be sold or shared with any third parties.</p>"
			page.save(flush:true)
		}
	}
    
	
	
	def createCountries(){
		if(Country.count() == 0){
			def usa = new Country()
			usa.name = "United States"
			usa.save(flush:true)
			println "Countries : ${Country.count()}"
		}		
	}
	
	
	
	def createStates(){
	
		if(State.count() == 0){
			def usa = Country.findByName('United States')
			
			def ca = new State()
			ca.name = 'California'
			ca.country = usa
			ca.save(flush:true)
			
			def tx = new State()
			tx.name = "Texas"
			ca.country = usa
			tx.save(flush:true)
			
			def ny = new State()
			ny.name = 'New York'
			ny.country = usa
			ny.save(flush:true)
			
			def al = new State()
			al.name = 'Alabama'
			al.country = usa
			al.save(flush:true)
			
			def ar = new State()
			ar.name = 'Arkansas'
			ar.country = usa
			ar.save(flush:true)
			
			def az = new State()
			az.name = 'Arizona'
			az.country = usa
			az.save(flush:true)
			
			def co = new State()
			co.name = 'Colorado'
			co.country = usa
			co.save(flush:true)
			
			def cn = new State()
			cn.name = 'Connecticut'
			cn.country = usa
			cn.save(flush:true)
        	
			
			def dc = new State()
			dc.name = 'Dist. Of Columbia'
			dc.country = usa
			dc.save(flush:true)
			
			def de = new State()
			de.name = 'Deleware'
			de.country = usa
			de.save(flush:true)
			
			def fl = new State()
			fl.name = 'Florida'
			fl.country = usa
			fl.save(flush:true)
			
			def ga = new State()
			ga.name = 'Georgia'
			ga.country = usa
			ga.save(flush:true)
			
			def hi = new State()
			hi.name = 'Hawaii'
			hi.country = usa
			hi.save(flush:true)
			
			def id = new State()
			id.name = 'Idaho'
			id.country = usa
			id.save(flush:true)
			
			def il = new State()
			il.name = 'Illinois'
			il.country = usa
			il.save(flush:true)
			
			def ks = new State()
			ks.name = 'Kansas'
			ks.country = usa
			ks.save(flush:true)
			
			def ind = new State()
			ind.name = 'Indiana'
			ind.country = usa
			ind.save(flush:true)
        	
			
			def ky = new State()
			ky.name = 'Kentucky'
			ky.country = usa
			ky.save(flush:true)
			
			def la = new State()
			la.name = 'Louisiana'
			la.country = usa
			la.save(flush:true)
			
			def ma = new State()
			ma.name = 'Massachusetts'
			ma.country = usa
			ma.save(flush:true)
        	
			
			def md = new State()
			md.name = 'Maryland'
			md.country = usa
			md.save(flush:true)
			
			def me = new State()
			me.name = 'Maine'
			me.country = usa
			me.save(flush:true)
			
			def mi = new State()
			mi.name = 'Michigan'
			mi.country = usa
			mi.save(flush:true)
			
			def mn = new State()
			mn.name = 'Minnesota'
			mn.country = usa
			mn.save(flush:true)
			
			def ms = new State()
			ms.name = 'Mississippi'
			ms.country = usa
			ms.save(flush:true)
			
			def mt = new State()
			mt.name = 'Montana'
			mt.country = usa
			mt.save(flush:true)
			
			def nc = new State()
			nc.name = 'North Carolina'
			nc.country = usa
			nc.save(flush:true)
        	
			def ne = new State()
			ne.name = 'Nebraska'
			ne.country = usa
			ne.save(flush:true)
        	
			def nh = new State()
			nh.name = 'New Hampshire'
			nh.country = usa
			nh.save(flush:true)
        	
			def nj = new State()
			nj.name = 'New Jersey'
			nj.country = usa
			nj.save(flush:true)
        	
        	
			def nm = new State()
			nm.name = 'New Mexico'
			nm.country = usa
			nm.save(flush:true)
        	
			def nv = new State()
			nv.name = 'Nevada'
			nv.country = usa
			nv.save(flush:true)
        	
			def ak = new State()
			ak.name = 'Alaska'
			ak.country = usa
			ak.save(flush:true)
        	
			def oh = new State()
			oh.name = 'Ohio'
			oh.country = usa
			oh.save(flush:true)
        	
			def or = new State()
			or.name = 'Oregon'
			or.country = usa
			or.save(flush:true)
        	
			def pa = new State()
			pa.name = 'Pennsylvania'
			pa.country = usa
			pa.save(flush:true)
        	
			def pr = new State()
			pr.name = 'Puerto Rico'
			pr.country = usa
			pr.save(flush:true)
        	
			def ri = new State()
			ri.name = 'Rhode Island'
			ri.country = usa
			ri.save(flush:true)
        	
			def sc = new State()
			sc.name = 'South Carolina'
			sc.country = usa
			sc.save(flush:true)
        	
			def tn = new State()
			tn.name = 'Tennessee'
			tn.country = usa
			tn.save(flush:true)
        	
			def sd = new State()
			sd.name = 'South Dakota'
			sd.country = usa
			sd.save(flush:true)
        	
			def ut = new State()
			ut.name = 'Utah'
			ut.country = usa
			ut.save(flush:true)
        	
        	
			def va = new State()
			va.name = 'Virginia'
			va.country = usa
			va.save(flush:true)
        	
			def vi = new State()
			vi.name = 'Virgin Islands'
			vi.country = usa
			vi.save(flush:true)
        	
			def wa = new State()
			wa.name = 'Washington'
			wa.country = usa
			wa.save(flush:true)
        	
			def wi = new State()
			wi.name = 'Wisconsin'
			wi.country = usa
			wi.save(flush:true)
        	
			def wv = new State()
			wv.name = 'West Virginia'
			wv.country = usa
			wv.save(flush:true)
        	
			def wy = new State()
			wy.name = 'Wyoming'
			wy.country = usa
			wy.save(flush:true)
        	
        	
			def ia = new State()
			ia.name = 'Iowa'
			ia.country = usa
			ia.save(flush:true)
        	
			def mo = new State()
			mo.name = 'Missouri'
			mo.country = usa
			mo.save(flush:true)
        	
			def ok = new State()
			ok.name = 'Oklahoma'
			ok.country = usa
			ok.save(flush:true)
        	
        	
			def vt = new State()
			vt.name = 'Vermont'
			vt.country = usa
			vt.save(flush:true)
			
		}
		
		println "States : ${State.count()}"
		
	}
	
	
	
	
	
	def destroy = {}
	
	
}
