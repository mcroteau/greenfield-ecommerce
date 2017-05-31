package greenfield

import org.springframework.dao.DataIntegrityViolationException
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.SecurityUtils
import grails.converters.*
import java.util.UUID
import groovy.text.SimpleTemplateEngine

import greenfield.common.BaseController
import greenfield.common.ControllerConstants

import org.apache.shiro.SecurityUtils
import org.apache.shiro.subject.Subject

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import org.greenfield.Account
import org.greenfield.Role
import org.greenfield.AccountRole
import org.greenfield.Transaction
import org.greenfield.common.RoleName
import org.greenfield.Permission
import org.greenfield.ShoppingCart

import org.greenfield.log.ProductViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.CatalogViewLog
import org.greenfield.log.SearchLog


import grails.plugin.springsecurity.annotation.Secured

@Mixin(BaseController)
class AccountController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", register: "POST"]

	def emailService
	def applicationService
	def springSecurityService
	

	@Secured(['ROLE_CUSTOMER', 'ROLE_ADMIN'])
	def customer_profile(){
		authenticatedAccount { accountInstance ->
		//authenticatedPermittedCustomer { accountInstance ->
			[accountInstance : accountInstance]
		}
	}	
	
	@Secured(['ROLE_CUSTOMER', 'ROLE_ADMIN'])
	def customer_update(){
		//authenticatedAccount { customerAccount ->
		//TODO:consider removing authenticated permitted customer
		authenticatedAccount { customerAccount ->
		
			def accountInstance = Account.get(params.id)
			
   			if (!accountInstance) {
   			    flash.message = "Account not found"
   			    redirect(action: "customer_profile")
   			    return
   			}
        	
			accountInstance.properties = params
			accountInstance.addressVerified = false

			def easypostEnabled = applicationService.getEasyPostEnabled()
			if(easypostEnabled == "true"){
				if(!addressVerified(accountInstance)){
   					flash.error = "<strong>Address cannot be verified.</strong>  Please update your address with valid information..."
   			    	render(view: "customer_profile", model: [accountInstance: accountInstance])
   			    	return
				}
				accountInstance.addressVerified = true
			}
			
			
   			if (!accountInstance.save(flush: true)) {
   				flash.message = "Something went wrong when updating account, please try again..."
   			    render(view: "customer_profile", model: [accountInstance: accountInstance])
   			    return
   			}
   			
   			flash.message = "Your account was successfully updated"
   			redirect(action: "customer_profile", id:accountInstance.id)
		}
	}	
	
	@Secured(['permitAll'])	
	def customer_forgot(){}
	
	def customer_send_reset_email(){
	
		if(params.email){

			def accountInstance = Account.findByEmail(params.email)
			
			if(accountInstance){
				
				def resetUUID = UUID.randomUUID()
				accountInstance.resetUUID = resetUUID
				accountInstance.save(flush:true)
				
				def url = request.getRequestURL()
				
				def split = url.toString().split("/${applicationService.getContextName()}/")
				def httpSection = split[0]
				def resetUrl = "${httpSection}/${applicationService.getContextName()}/account/customer_confirm_reset?"
				def params = "username=${accountInstance.username}&uuid=${resetUUID}"
				resetUrl+= params
				
				sendResetEmail(accountInstance, resetUrl)
				
			}else{
				flash.message = "Account not found with following email address : ${params.email}"
				redirect(action:'customer_forgot')
			}
		}else{
			flash.message = "Please enter an email to continue the reset password process"
			redirect(action:'customer_forgot')
		}
	}
	
	
	
	def sendResetEmail(Account accountInstance, String resetUrl){
		try { 
		
			def fromAddress = applicationService.getSupportEmailAddress()
			def toAddress = accountInstance.email
			def subject = "${applicationService.getStoreName()} : Reset password"

			
			File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/password_reset.html").getFile();

			def binding = [ "companyName" : applicationService.getStoreName(),
				 			"supportEmail" : applicationService.getSupportEmailAddress(),
							"resetUrl": resetUrl ]
			def engine = new SimpleTemplateEngine()
			def template = engine.createTemplate(templateFile).make(binding)
			def bodyString = template.toString()
			
			
			emailService.send(toAddress, fromAddress, subject, bodyString)
			
		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	def customer_confirm_reset(){
		def accountInstance = Account.findByUsernameAndResetUUID(params.username, params.uuid)
		if(accountInstance){
			request.username = accountInstance.username
		}else{
			flash.message = "Something went wrong, please try again."
			redirect(action: 'customer_forgot')
		}		
	}
	
	
	
	def customer_reset_password(){
		def username = params.username
		def newPassword = params.password
		def confirmPassword = params.confirmPassword
		
		def accountInstance = Account.findByUsername(username)
		if(accountInstance && newPassword && confirmPassword){	
		
			if(confirmPassword == newPassword){
			
				if(newPassword.length() >= 5){
					accountInstance.password = new Sha256Hash(newPassword).toHex()
					if(accountInstance.save(flush:true)){
				
						def authToken = new UsernamePasswordToken(username, newPassword as String)
					
						flash.message = "Successfully reset password..."
						//redirect(controller : "auth", action : "customer_sign_in", params : [username : username, password : newPassword, reset : true])
					
					}else{
						flash.message = "We were unable to reset your password, please try again."
						redirect(action:'confirmReset', params : [username : username, uuid : accountInstance.resetUUID ])
					}
				}else{
					flash.message = "Passwords must be at least 5 characters in length. Please try again"
					redirect(action: 'customer_confirm_reset', params : [uuid : accountInstance.resetUUID, username : username])
				}

			}else{
				flash.message = "Passwords must match. Please try again"
				redirect(action: 'customer_confirm_reset', params : [uuid : accountInstance.resetUUID, username : username])
				
			}
		}else{
			flash.message = "Password cannot be blank, please try again."
			redirect(action: 'customer_confirm_reset', params : [uuid : accountInstance.resetUUID, username : username])
		}
	}
	

		
	def addressVerified(accountInstance){
		try{
		
			def apiKey
			
			if(Environment.current == Environment.DEVELOPMENT)  apiKey = applicationService.getEasyPostTestApiKey()
			if(Environment.current == Environment.PRODUCTION) apiKey = applicationService.getEasyPostLiveApiKey()
		
			EasyPost.apiKey = apiKey;
		
	    	Map<String, Object> addressMap = new HashMap<String, Object>();
	    	addressMap.put("street1", accountInstance.address1);
	    	addressMap.put("street2", accountInstance.address2);
	    	addressMap.put("state", accountInstance.state.name);
			addressMap.put("city", accountInstance.city);
			addressMap.put("zip", accountInstance.zip);
			
    		Address address = Address.create(addressMap);
			Address verifiedAddress = address.verify();
			
			return true
			
		}catch (Exception e){
			println e
			return false
		}
	}	

	
	@Secured(['ROLE_ADMIN'])
	def account_activity(Long id){
		authenticatedAdmin { adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found"
        	    redirect(action: "admin_list")
        	    return
        	}   
			def productViewStats = getProductViewsStatistics(accountInstance)
			def pageViewStats = getPageViewStatistics(accountInstance)
			def catalogViewStats = getCatalogViewsStatistics(accountInstance)
			def searchQueryStats = getSearchQueryStatistics(accountInstance)

			[accountInstance: accountInstance, productViewStats: productViewStats, 
			pageViewStats: pageViewStats, catalogViewStats: catalogViewStats, 
			searchQueryStats: searchQueryStats]
		}
	}


	@Secured(['ROLE_ADMIN'])
	def product_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found"
        	    redirect(action: "admin_list")
        	    return
        	}   
			def productViewLogs = ProductViewLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def productViewStats = getProductViewsStatistics(accountInstance)
			[accountInstance: accountInstance, productViewLogs: productViewLogs, productViewStats: productViewStats]
		}
	}


	@Secured(['ROLE_ADMIN'])
	def catalog_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found"
        	    redirect(action: "admin_list")
        	    return
        	}   
			def catalogViewLogs = CatalogViewLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def catalogViewStats = getCatalogViewsStatistics(accountInstance)
			[accountInstance: accountInstance, catalogViewLogs: catalogViewLogs, catalogViewStats: catalogViewStats]
		}
	}




	@Secured(['ROLE_ADMIN'])
	def page_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found"
        	    redirect(action: "admin_list")
        	    return
        	}   
			def pageViewLogs = PageViewLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def pageViewStats = getPageViewStatistics(accountInstance)
			[accountInstance: accountInstance, pageViewLogs: pageViewLogs, pageViewStats: pageViewStats]
		}
	}




	@Secured(['ROLE_ADMIN'])
	def search_activity(Long id){
		authenticatedAdmin{ adminAccount ->
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found"
        	    redirect(action: "admin_list")
        	    return
        	}   
			def searchLogs = SearchLog.findAllByAccount(accountInstance, [sort:"dateCreated", order:"desc"])
			def searchQueryStats = getSearchQueryStatistics(accountInstance)
			[accountInstance: accountInstance, searchLogs: searchLogs, searchQueryStats: searchQueryStats]
		}
	}



	
	def getProductViewsStatistics(accountInstance){
		def stats = [:]
		def productViewLogs = ProductViewLog.findAllByAccount(accountInstance)
		
		
		productViewLogs?.each(){ productLog ->
			if(stats[productLog.product.id]){
				stats[productLog.product.id].count += 1
			}else{
				stats[productLog.product.id] = [:]
				stats[productLog.product.id].count = 1
				stats[productLog.product.id].product = productLog.product.name
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	
	
	def getPageViewStatistics(accountInstance){
		def stats = [:]
		def pageViewLogs = PageViewLog.findAllByAccount(accountInstance)
		
		pageViewLogs?.each(){ pageLog ->
			if(stats[pageLog.page.id]){
				stats[pageLog.page.id].count += 1
			}else{
				stats[pageLog.page.id] = [:]
				stats[pageLog.page.id].count = 1
				stats[pageLog.page.id].page = pageLog.page.title
			}
		}
		
		return stats.sort(){ -it.value.count }			
	}
	
	
	
	
	def getCatalogViewsStatistics(accountInstance){
		def stats = [:]
		def catalogViewLogs = CatalogViewLog.findAllByAccount(accountInstance)
		
		catalogViewLogs?.each(){ catalogLog ->
			if(stats[catalogLog.catalog.id]){
				stats[catalogLog.catalog.id].count += 1
			}else{
				stats[catalogLog.catalog.id] = [:]
				stats[catalogLog.catalog.id].count = 1
				stats[catalogLog.catalog.id].catalog = catalogLog.catalog.name
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	
	
	def getSearchQueryStatistics(accountInstance){
		def stats = [:]
		def searchLogs = SearchLog.findAllByAccount(accountInstance)
		

		searchLogs?.each(){ searchLog ->
			if(stats[searchLog.query]){
				stats[searchLog.query].count += 1
			}else{
				stats[searchLog.query] = [:]
				stats[searchLog.query].count = 1
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	

		
	@Secured(['permitAll'])
	def order_history(){
		authenticatedAccount { customerAccount ->
			def transactions = Transaction.findAllByAccount(customerAccount)
			[transactions : transactions]
		}	
	}
	
	
	@Secured(['permitAll'])
	def customer_registration(){}
	

	@Secured(['permitAll'])
	def customer_register(){
	
		def accountInstance = new Account(params)
		
		if(params.password && params.passwordRepeat){
			
			if(params.password == params.passwordRepeat){

				if(params.password.length() >= 5){
				
					params.ipAddress = request.getRemoteHost()
					accountInstance.properties = params
		
			   		//def password = new Sha256Hash(params.password).toHex()
					def password = springSecurityService.encodePassword(params.password)
			   		accountInstance.password = password
		
					if(accountInstance.save(flush:true)){
					
						accountInstance.createAccountRoles(false)
						accountInstance.createAccountPermission()

						//TODO:Remove/cleanup
						// def customerRole = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
						// def customerAccountRole = new AccountRole()
						// customerAccountRole.account = accountInstance
						// customerAccountRole.role = customerRole
						// customerAccountRole.save(flush:true)

				
						// //accountInstance.createAccountProfilePermission()
						// customerAccount.addToPermissions(ControllerConstants.ACCOUNT_PERMISSION + customerAccount.id)
						// customerAccount.save(flush:true)


						sendAdminEmail(accountInstance)
						sendThankYouEmail(accountInstance)
			
						flash.message = "You have successfully registered... sign into your new account to continue"
			
						redirect(controller : 'auth', action: 'customer_login', params : [ accountInstance: accountInstance, username : params.username, password : params.password, new_account : true])
			
					}else{
						flash.message = "There was a problem with your registration, please try again or contact the administrator"
						render(view: "customer_registration", model: [accountInstance: accountInstance])
						return
					}
					
				
				}else{
					flash.message = "Password must be at least 5 characters long.  Please try again"
					render(view: "customer_registration", model: [accountInstance: accountInstance])
				}
	
			}else{
				//passwords don't match
				flash.message = "Passwords don't match.  Please try again"
				render(view: "customer_registration", model: [accountInstance: accountInstance])
			}
		}else{
			flash.message = "Passwords cannot be blank"
			render(view: "customer_registration", model: [accountInstance: accountInstance])
		}
	
	}
	
	
	
	def sendAdminEmail(Account accountInstance){
		try { 
			
			def fromAddress = applicationService.getSupportEmailAddress()
			if(fromAddress){

				def customerSubject = "${applicationService.getStoreName()} : New Registration."
			
				File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/registration-notification.html").getFile();
	    	
				def binding = [ "companyName" : applicationService.getStoreName(),
					 			"accountInstance" : accountInstance ]
				def engine = new SimpleTemplateEngine()
				def template = engine.createTemplate(templateFile).make(binding)
				def bodyString = template.toString()
				
				emailService.send(applicationService.getAdminEmailAddress(), fromAddress, customerSubject, bodyString)
				
				
			}

		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	
	
	
	def sendThankYouEmail(Account accountInstance){
		try { 
			def fromAddress = applicationService.getSupportEmailAddress()
			if(fromAddress){
				def customerToAddress = accountInstance.email
				def customerSubject = "${applicationService.getStoreName()} : Thank you for registering"
				
				File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/registration.html").getFile();

				def binding = [ "companyName" : applicationService.getStoreName(),
					 			"supportEmail" : applicationService.getSupportEmailAddress()]
				def engine = new SimpleTemplateEngine()
				def template = engine.createTemplate(templateFile).make(binding)
				def bodyString = template.toString()
				
				emailService.send(customerToAddress, fromAddress, customerSubject, bodyString)				
			}
		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	
	
	
	/** ADMINISTRATION FUNCTIONS **/
	
		
	@Secured(['ROLE_ADMIN'])
	def admin_create(){
		authenticatedAdmin{ account ->
			if(params.admin == "true"){
				request.admin = "true"
			}			
        	[accountInstance: new Account(params)]
		}	
	}	


	@Secured(['ROLE_ADMIN'])
	def admin_show(Long id){
		authenticatedAdmin { adminAccount ->
       		def accountInstance = Account.get(id)
       		if (!accountInstance) {
       		    flash.message = "Account not found"
       		    redirect(action: "admin_list")
       		    return
       		}  		
       		[accountInstance: accountInstance]
		}	
	}
	

	@Secured(['ROLE_ADMIN'])
	def admin_edit(Long id){
		authenticatedAdmin { adminAccount ->
        	def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found"
        	    redirect(action: "admin_list")
        	    return
        	}   	

			def admin = false
			if(accountInstance.hasAdminRole)admin = true
			
        	[accountInstance: accountInstance, admin : admin]
		}
	}
	
	
	@Secured(['ROLE_ADMIN'])
	def admin_save(){
		authenticatedAdmin { adminAccount ->
			def accountInstance = new Account(params)
			
	   		//def password = new Sha256Hash(params.password).toHex()
			def password = springSecurityService.encodePassword(params.password)
			accountInstance.password = password

			def includeAdminRole = false
			if(params.admin == "true" ||
					params.admin == "on"){
				includeAdminRole = true
			}

			accountInstance.createAccountRoles(includeAdminRole)
			accountInstance.createAccountPermission()

			
       		flash.message = "Account successfully saved"
       		redirect(action: "admin_show", id: accountInstance.id)
		}
	}
	
	
	
	@Secured(['ROLE_ADMIN'])
	def admin_update(Long id){
		authenticatedAdmin { adminAccount ->
			
			def accountInstance = Account.get(id)
			
       		if (!accountInstance) {
       		    flash.message = "Account not found"
       		    redirect(action: "admin_list")
       		    return
       		}

			
			accountInstance.properties = params
			def adminRole = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
			

			if(params.admin == "true" ||
					params.admin == "on"){
				accountInstance.createAccountRole(adminRole)
				accountInstance.hasAdminRole = true
			}else{
				def accountRole = AccountRole.findByRoleAndAccount(adminRole, accountInstance)
				if(accountRole){
					accountRole.delete(flush:true)
					accountInstance.hasAdminRole = false
				}
			}

	   		if (!accountInstance.save(flush: true)) {
	   			flash.message = "Something went wrong when updating account, please try again..."
       		    render(view: "admin_edit", model: [accountInstance: accountInstance])
       		    return
       		}
       		
       		flash.message = "Account successfully updated"
       		redirect(action: "admin_show", id: accountInstance.id)
		}
	}
	


	@Secured(['ROLE_ADMIN'])
	def admin_order_history(Long id){
		authenticatedAdmin { adminAccount ->
			def accountInstance = Account.get(id)
			
       		if (!accountInstance) {
       		    flash.message = "Account not found"
       		    redirect(action: "admin_list")
       		    return
       		}

       		def transactions = Transaction.findAllByAccount(accountInstance, [sort: "orderDate", order: "asc"])
       		def transactionTotal = Transaction.countByAccount(accountInstance)

       		[accountInstance : accountInstance, transactionInstanceList: transactions, transactionInstanceTotal: transactionTotal]
		
		}
	}
	


	@Secured(['ROLE_ADMIN'])
	def review_order(Long id){
		authenticatedAdmin { adminAccount ->
			def transactionInstance = Transaction.get(id)
			
       		if (!transactionInstance) {
       		    flash.message = "Transaction not found"
       		    redirect(action: "admin_list")
       		    return
       		}

       		[ transactionInstance : transactionInstance ]		
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def admin_list(){
		authenticatedAdmin { adminAccount ->
        	//params.max = Math.min(max ?: 10, 100)

        	def max = 10
			def offset = params.offset ? params.offset : 0
			def sort = params.sort ? params.sort : "id"
			def order = params.order ? params.order : "asc"

			def role
			
			if(params.admin == "true"){
				request.admin = true
				role = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
			}else{
				role = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
			}

			def accountRoles = AccountRole.findAllByRole(role, [max: max, offset: offset])
			def accountInstanceTotal = AccountRole.countByRole(role)
			def accountInstanceList = accountRoles.collect(){ it.account }

			
			accountInstanceList.each(){ it ->
				def pageViews = 0
				def catalogViews = 0
				def productViews = 0
				def searches = 0

				pageViews = PageViewLog.countByAccount(it)
				catalogViews = CatalogViewLog.countByAccount(it)
				productViews = ProductViewLog.countByAccount(it)
				searches = SearchLog.countByAccount(it)

				it.pageViews = pageViews 
				it.catalogViews = catalogViews
				it.productViews = productViews 
				it.searches = searches
				it.save(flush:true)
			}

			[ accountInstanceList: accountInstanceList, accountInstanceTotal: accountInstanceTotal ]
		}
	}

	@Secured(['ROLE_ADMIN'])
	def admin_edit_password(Long id){
		authenticatedAdmin { adminAccount ->
	        def accountInstance = Account.get(id)
	        if (!accountInstance) {
	            flash.message = "Account not found..."
	            redirect(action: "admin_list")
	            return
	        }
			[ accountInstance : accountInstance ]
		}
	}
	

	@Secured(['ROLE_ADMIN'])
	def admin_update_password(Long id){
		authenticatedAdmin { adminAccount ->
	        def accountInstance = Account.get(id)
	        if (!accountInstance) {
	            flash.message = "Account not found..."
	            redirect(action: "admin_list")
	            return
	        }
			
			
	   		def password = springSecurityService.encodePassword(params.password)
	   		accountInstance.password = password

	   		if (!accountInstance.save(flush: true)) {
	   			flash.message = "Something went wrong when updating account, please try again..."
       		    render(view: "admin_edit", model: [accountInstance: accountInstance])
       		    return
       		}
       		
       		flash.message = "Account successfully updated password"
       		redirect(action: "admin_show", id:accountInstance.id)
			
		}
	}
	
		
	
	@Secured(['ROLE_ADMIN'])
	def admin_delete(Long id){
		authenticatedAdmin { adminAccount ->
	        
			def accountInstance = Account.get(id)
        	if (!accountInstance) {
        	    flash.message = "Account not found..."
        	    redirect(action: "admin_list")
        	    return
        	}
			
			def transactions = Transaction.findByAccount(accountInstance)
			if(!transactions){
			    try {
			    	
			    	def accountRoles = AccountRole.findAllByAccount(accountInstance)
			    	accountRoles.each(){
			    		it.delete(flush:true)
			    	}

			    	def permissions = Permission.findAllByAccount(accountInstance)
			    	permissions.each(){
			    		it.delete(flush:true)
			    	}

			    	def shoppingCarts = ShoppingCart.findAllByAccount(accountInstance)
			    	shoppingCarts.each(){
			    		it.delete(flush:true)
			    	}

			    	def pageViews = PageViewLog.findAllByAccount(accountInstance)
			    	pageViews.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

			    	def productViews = ProductViewLog.findAllByAccount(accountInstance)
					productViews.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

			    	def catalogViews = CatalogViewLog.findAllByAccount(accountInstance)
					catalogViews.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

			    	def searches = SearchLog.findAllByAccount(accountInstance)
					searches.each(){
			    		it.account = null
			    		it.save(flush:true)
			    	}

	        	    accountInstance.delete(flush: true)
	        	    flash.message = "Successfully deleted the account"
	        	    redirect(action: "admin_list")
	        	
	        	} catch (DataIntegrityViolationException e) {
	        	    flash.message = "Something went wrong when trying to delete. Check to see if Orders exist under this account before deleting."
	        	    redirect(action: "admin_show", id: id)
	        	}
			}else{
				flash.message = "The account has existing orders. Delete the old orders before deleting the customer"
	        	redirect(action: "admin_show", id: id)
			}
		}
	}
	
}