package greenfield

import greenfield.common.BaseController
import org.greenfield.AppConstants

//import org.apache.commons.io.IOUtils
import grails.io.IOUtils

import java.io.FileOutputStream;
import java.io.FileInputStream
import grails.converters.*

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import org.greenfield.Country
import org.greenfield.State
import org.greenfield.Upload
import org.greenfield.Product
import org.greenfield.Page

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class ConfigurationController {

	def emailService
	def applicationService
	
	private final String SETTINGS_FILE = "settings.properties"
	
	private final String STORE_CURRENCY = "store.currency"
	private final String STORE_NAME = "store.name"
	private final String STORE_ADDRESS1 = "store.address1"
	private final String STORE_ADDRESS2 = "store.address2"
	private final String STORE_CITY = "store.city"
	private final String STORE_COUNTRY = "store.country"
	private final String STORE_STATE = "store.state"
	private final String STORE_ZIP = "store.zip"
	private final String STORE_SHIPPING = "store.shipping"
	private final String STORE_TAX_RATE = "store.tax.rate"
	
	private final String META_KEYWORDS = "meta.keywords"
	private final String META_DESCRIPTION = "meta.description"
	private final String GOOGLE_ANALYTICS = "google.analytics"
	private final String SOCIAL_MEDIA_ENABLED = "social.media.enabled"
	
	private final String MAIL_ADMIN_EMAIL_ADDRESS = "mail.smtp.adminEmail"
	private final String MAIL_SUPPORT_EMAIL_ADDRESS = "mail.smtp.supportEmail"
	private final String MAIL_USERNAME = "mail.smtp.username"
	private final String MAIL_PASSWORD = "mail.smtp.password"
	private final String MAIL_HOST = "mail.smtp.host"
	private final String MAIL_PORT = "mail.smtp.port"
	private final String MAIL_STARTTLS = "mail.smtp.starttls.enabled"
	private final String MAIL_AUTH = "mail.smtp.auth"
	
	private final String STRIPE_ENABLED_KEY = "stripe.enabled"
	private final String STRIPE_DEVELOPMENT_API_KEY = "stripe.development.apiKey"
	private final String STRIPE_DEVELOPMENT_PUBLISHABLE_KEY = "stripe.development.publishableKey"
	private final String STRIPE_PRODUCTION_API_KEY = "stripe.production.apiKey"
	private final String STRIPE_PRODUCTION_PUBLISHABLE_KEY = "stripe.production.publishableKey"
	
	private final String EASYPOST_ENABLED = "easypost.enabled"
	private final String EASYPOST_TEST_API_KEY = "easypost.test.apiKey"
	private final String EASYPOST_LIVE_API_KEY = "easypost.live.apiKey"
	
	private final String EASYPOST_ADDRESS_EXCEPTION_STRING = "An error occured. Response code: 400 Response body"
	
	def links() {
		authenticatedAdmin{ adminAccount ->
		}
	}
	
 	@Secured(['ROLE_ADMIN'])
    def index() {
		authenticatedAdmin{ adminAccount ->
		}
	}

	

 	@Secured(['ROLE_ADMIN'])
	def settings(){
		authenticatedAdmin{ adminAccount ->
			
			Properties prop = new Properties();
			try{
			
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				prop.load(inputStream);
				
				def settings = [:]
				settings["storeName"] = prop.getProperty(STORE_NAME);
				settings["keywords"] = prop.getProperty(META_KEYWORDS);
				settings["description"] = prop.getProperty(META_DESCRIPTION);
				settings["shipping"] = prop.getProperty(STORE_SHIPPING);
				settings["taxRate"] = prop.getProperty(STORE_TAX_RATE);
				settings["googleAnalytics"] = prop.getProperty(GOOGLE_ANALYTICS);

				String socialMediaEnabled = prop.getProperty(SOCIAL_MEDIA_ENABLED);
				if(socialMediaEnabled == "true")settings["socialMediaEnabled"] = "checked"
				
				[ settings : settings ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_settings(){

		authenticatedAdmin{ adminAccount ->
			
			String storeName = params.storeName
			String keywords = params.keywords
			String description = params.description
			String taxRate = params.taxRate
			String shipping = params.shipping
			String googleAnalytics = params.googleAnalytics
			String socialMediaEnabled = params.socialMediaEnabled
			
			
			if(socialMediaEnabled == "on")socialMediaEnabled = true
			if(!socialMediaEnabled)socialMediaEnabled = false

			Properties prop = new Properties();
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			try{
			    
				prop.setProperty(STORE_NAME, storeName);
				prop.setProperty(META_KEYWORDS, keywords);
				prop.setProperty(META_DESCRIPTION, description);
				prop.setProperty(STORE_TAX_RATE, taxRate);
				prop.setProperty(STORE_SHIPPING, shipping);
				prop.setProperty(GOOGLE_ANALYTICS, googleAnalytics);
				prop.setProperty(SOCIAL_MEDIA_ENABLED, socialMediaEnabled);
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);
				applicationService.setProperties()
				
				flash.message = "Successfully saved store settings"
				redirect(action : 'settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.message = "Something went wrong... "
				redirect(action : 'settings')
				return
			}
		}
	}
	
	





 	@Secured(['ROLE_ADMIN'])
	def email_settings(){
		authenticatedAdmin{ adminAccount ->
			
			Properties prop = new Properties();
			try{
			
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				
				prop.load(inputStream);
				
				def email_settings = [:]
				email_settings["adminEmail"] = prop.getProperty(MAIL_ADMIN_EMAIL_ADDRESS)
				email_settings["supportEmail"] = prop.getProperty(MAIL_SUPPORT_EMAIL_ADDRESS)
				email_settings["username"] = prop.getProperty(MAIL_USERNAME)
				email_settings["password"] = prop.getProperty(MAIL_PASSWORD)
				email_settings["host"] = prop.getProperty(MAIL_HOST)
				email_settings["port"] = prop.getProperty(MAIL_PORT)
				
				def startTls = prop.getProperty(MAIL_STARTTLS)
				def auth = prop.getProperty(MAIL_AUTH)
				
				if(startTls == "true")email_settings["startTls"] = prop.getProperty(MAIL_STARTTLS)
				if(auth == "true")email_settings["auth"] = prop.getProperty(MAIL_AUTH)
				
				if(email_settings["startTls"])email_settings["startTls"] = "checked"
				if(email_settings["auth"])email_settings["auth"] = "checked"
				
				[ email_settings : email_settings ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			}
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_email_settings(){

		authenticatedAdmin{ adminAccount ->
		
			def homepage = Page.findByTitle("Home")
			homepage.content = params.homepage
			homepage.save(flush:true)

			String adminEmail = params.adminEmail
			String supportEmail = params.supportEmail
			String username = params.username
			String password = params.password
			String host = params.host
			String port = params.port
			String startTls = params.startTls
			String auth = params.auth
			
			if(startTls == "on")startTls = true
			if(auth == "on")auth = true
			
			if(!startTls)startTls = false
			if(!auth)auth = false
			
			
			Properties prop = new Properties();
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			
			try{
				
				prop.setProperty(MAIL_ADMIN_EMAIL_ADDRESS, adminEmail)
				prop.setProperty(MAIL_SUPPORT_EMAIL_ADDRESS, supportEmail)
				prop.setProperty(MAIL_USERNAME, username);
				prop.setProperty(MAIL_PASSWORD, password);
				prop.setProperty(MAIL_HOST, host);
				prop.setProperty(MAIL_PORT, port);
				prop.setProperty(MAIL_STARTTLS, startTls)
				prop.setProperty(MAIL_AUTH, auth)
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);
				
				applicationService.setProperties()
				
				flash.message = "Successfully saved email settings"
				redirect(action : 'email_settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.message = "Something went wrong... "
				redirect(action : 'email_settings')
				return
			}
			
			
		}
	}
	
	
	
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def stripe_settings(){
		authenticatedAdmin{ adminAccount ->
			
			Properties prop = new Properties();
			try{
		
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				prop.load(inputStream);
				
				def stripe_settings = [:]
				
				//def enabled =  prop.getProperty(STRIPE_ENABLED_KEY)
				//if(enabled == "true")stripe_settings["enabled"] = "checked"

				stripe_settings["storeCurrency"] = prop.getProperty(STORE_CURRENCY);
				stripe_settings["devApiKey"] = prop.getProperty(STRIPE_DEVELOPMENT_API_KEY)
				stripe_settings["devPublishableKey"] = prop.getProperty(STRIPE_DEVELOPMENT_PUBLISHABLE_KEY)
				stripe_settings["prodApiKey"] = prop.getProperty(STRIPE_PRODUCTION_API_KEY)
				stripe_settings["prodPublishableKey"] = prop.getProperty(STRIPE_PRODUCTION_PUBLISHABLE_KEY)
				
				
				
				[ stripe_settings : stripe_settings ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_stripe_settings(){

		authenticatedAdmin{ adminAccount ->
		
			//String enabled = params.enabled
			String storeCurrency = params.storeCurrency
			String devApiKey = params.devApiKey
			String devPublishableKey = params.devPublishableKey
			String prodApiKey = params.prodApiKey
			String prodPublishableKey = params.prodPublishableKey
			
			
			//if(enabled == "on")enabled = true
			//if(!enabled)enabled = false
			
			
			Properties prop = new Properties();
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			
			try{
				
				//prop.setProperty(STRIPE_ENABLED_KEY, enabled);
				prop.setProperty(STORE_CURRENCY, storeCurrency);
				prop.setProperty(STRIPE_DEVELOPMENT_API_KEY, devApiKey);
				prop.setProperty(STRIPE_DEVELOPMENT_PUBLISHABLE_KEY, devPublishableKey);
				prop.setProperty(STRIPE_PRODUCTION_API_KEY, prodApiKey);
				prop.setProperty(STRIPE_PRODUCTION_PUBLISHABLE_KEY, prodPublishableKey)
				
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);

				applicationService.setProperties()
				
				flash.message = "Successfully saved Stripe/Payment settings"
				redirect(action : 'stripe_settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.message = "Something went wrong... "
				redirect(action : 'stripe_settings')
				return
			}
		}
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def shipping_settings(){
		authenticatedAdmin{ adminAccount ->
			Properties prop = new Properties();
			try{
		
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				prop.load(inputStream);
				
				def shipping_settings = [:]
				
				shipping_settings["address1"] = prop.getProperty(STORE_ADDRESS1);
				shipping_settings["address2"] = prop.getProperty(STORE_ADDRESS2);
				shipping_settings["city"] = prop.getProperty(STORE_CITY);
				shipping_settings["country"] = prop.getProperty(STORE_COUNTRY);
				shipping_settings["state"] = prop.getProperty(STORE_STATE);
				shipping_settings["zip"] = prop.getProperty(STORE_ZIP);
				shipping_settings["shipping"] = prop.getProperty(STORE_SHIPPING);
				
				String easypostEnabled = prop.getProperty(EASYPOST_ENABLED);
				if(easypostEnabled == "true")shipping_settings["easypostEnabled"] = "checked"
				
				shipping_settings["testApiKey"] = prop.getProperty(EASYPOST_TEST_API_KEY);
				shipping_settings["liveApiKey"] = prop.getProperty(EASYPOST_LIVE_API_KEY);
				
				[ shipping_settings : shipping_settings, countries: Country.list() ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_shipping_settings(){
		
		authenticatedAdmin{ adminAccount ->
			
			String easypostEnabled = params.easypostEnabled		
			String testApiKey = params.testApiKey
			String liveApiKey = params.liveApiKey

			String shipping = params.shipping
			
			String address1 = params.address1
			String address2 = params.address2
			String city = params.city
			String countryId = params.country
			String stateId = params.state
			String zip = params.zip
			
			
			if(easypostEnabled == "on")easypostEnabled = true
			
			if(!easypostEnabled)easypostEnabled = false
			
			if(!countryId || !countryId.isInteger()){
				flash.error = "Something went wrong, please try again..."
				redirect(action : 'shipping_settings')
				return
			}
			
			def country = Country.get(countryId)
			if(!country){
				flash.error = "Something went wrong"
				redirect(action : 'shipping_settings')
				return
			}
			
			def state
			
			if(stateId && stateId.isInteger()){
				state = State.get(stateId)
				if(!state){
					flash.error = "Something went wrong with the state"
					redirect(action : 'shipping_settings')
					return
				}
			}
			
			if(easypostEnabled == "true"){
				if(testApiKey == ""){
					flash.error = "EasyPost Test API Key cannot be blank"
					redirect(action : 'shipping_settings')
					return
				}
				if(liveApiKey == ""){
					flash.error = "EasyPost Live API Key cannot be blank"
					redirect(action : 'shipping_settings')
					return
				}
				
				def apiKey
				
				if(Environment.current == Environment.DEVELOPMENT)  apiKey = testApiKey
				if(Environment.current == Environment.PRODUCTION) apiKey = liveApiKey
				
				EasyPost.apiKey = apiKey;
				
		    	Map<String, Object> addressMap = new HashMap<String, Object>();
		    	addressMap.put("company", "testing")//TODO:remove
		    	addressMap.put("company_name", "testing")//TODO:remove
		    	addressMap.put("street1", address1);
		    	addressMap.put("street2", address2);
		    	addressMap.put("country", country.name);
				if(state)addressMap.put("state", state.name);
				addressMap.put("city", city);
				addressMap.put("zip", zip);
    	
				try{
		    		Address address = Address.create(addressMap);
					Address verifiedAddress = address.verify();
				}catch (Exception e){
					if(e.message.indexOf("401") >= 0){
						flash.error = "The API entered is unauthorized by EasyPost."
						redirect(action : 'shipping_settings')
						return
					}
		
					flash.error = "Please make sure all EasyPost settings are correct and address is valid"
					redirect(action : 'shipping_settings')
					return
				}
			}
			
			
			Properties prop = new Properties();
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			
			try{
				
				prop.setProperty(EASYPOST_ENABLED, easypostEnabled);
				prop.setProperty(EASYPOST_TEST_API_KEY, testApiKey);
				prop.setProperty(EASYPOST_LIVE_API_KEY, liveApiKey);
				
				prop.setProperty(STORE_SHIPPING, shipping);
				
				prop.setProperty(STORE_ADDRESS1, address1);
				prop.setProperty(STORE_ADDRESS2, address2);
				prop.setProperty(STORE_CITY, city);
				prop.setProperty(STORE_COUNTRY, countryId);
				if(state)prop.setProperty(STORE_STATE, stateId);
				prop.setProperty(STORE_ZIP, zip);
				
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);
    			applicationService.setProperties()
				
				flash.message = "Successfully saved Shipping settings"
				redirect(action : 'shipping_settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.error = "Something went wrong... "
				redirect(action : 'shipping_settings')
				return
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def edit_homepage(){
		authenticatedAdmin{ adminAccount ->
			def homepage = Page.findByTitle("Home")
			[homepage: homepage]
		}
	}
	
 	@Secured(['ROLE_ADMIN'])
	def save_homepage(){
		authenticatedAdmin{ adminAccount ->
			def homepage = Page.findByTitle("Home")
			homepage.content = params.homepage
			if(homepage.save(flush:true)){
				flash.message = "Successfully saved home page"
			}else{
				flash.message = "Something went wrong while trying to save Home Page. Please try again"
			}
			render(view:'edit_homepage', model:[ homepage: homepage ])
		}
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def remove_upload(Long id){
		authenticatedAdmin{ adminAccount ->
			def upload = Upload.get(id)
			if(upload){
				upload.delete(flush:true)
				flash.message = "Successfully removed upload"
				redirect(action:'uploads')
			}else{
				flash.message = "Upload not found... "
				redirect(action:'uploads')
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def upload(){
		authenticatedAdmin{ adminAccount ->
    		
			def file = request.getFile('file')
			if(file){
				def fullFileName = file.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				def extension = nameSplit[1]
			
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				def newFileName = "${fileName}.${extension}"
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('uploads')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = "${absolutePath}${newFileName}"
				
				InputStream is = file.getInputStream()
				OutputStream os = new FileOutputStream(filePath)
			
				try {
				    IOUtils.copy(is, os);
				} finally {
				    IOUtils.closeQuietly(os);
				    IOUtils.closeQuietly(is);
				}
				
				def upload = new Upload()
				upload.url = "uploads/${fileName}.${extension}"
				
				upload.save(flush:true)
				
				flash.message = "Successfully uploaded file"
				redirect(action:'uploads')
			}else{
				flash.message = "Please specify a file to upload"
				redirect(action:'uploads')
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def uploads(Integer max){
		authenticatedAdmin{ adminAccount ->
			params.max = Math.min(max ?: 10, 100)
			[uploadInstanceList : Upload.list(params), uploadInstanceTotal: Upload.count()]
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def import_products_view(){
		authenticatedAdmin{ adminAccount ->
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def import_products(){
		authenticatedAdmin{ adminAccount ->
			def file = request.getFile('file')
			def is = file.getInputStream()
			
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
        	
 	   		def count = 0
			def skipped = 0
			def errored = 0
			String line;
			
			try {
	    	
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					def fields = line.split(",")
					
					try{
					
						def name = fields[0]
						def quantity = Integer.parseInt(fields[1])
						def price = new BigDecimal(fields[2])
						def weight = new BigDecimal(fields[3])
						def description = fields[4]
        	
						def existingProduct = Product.findByName(name)
						
						if(!existingProduct){
							
							def product = new Product()
							product.name = name
							product.quantity = quantity
							product.price = price
							product.weight = weight
							product.description = description
							product.save(flush:true)
							count++							
							
						}else{
							skipped++
						}
						
					}catch(Exception ae){
						println ae
						errored++
					}
				}
				
			} catch (IOException e) {
				flash.error = "Something went wrong while trying to import.  Please confirm correct formatting"
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(count == 0 && skipped > 0){
				flash.message = "Products already exist"
			}
 	   		if(count > 0){
				flash.message = "Successfully imported <strong>${count}</strong> products"
			}
			if(errored > 0){
				flash.error = "Errored on <strong>${errored}</strong> products.  Please review file and results to resolve"
			}
	    	
			
			render(view: 'import_products_view')
		}
	}
	
}
