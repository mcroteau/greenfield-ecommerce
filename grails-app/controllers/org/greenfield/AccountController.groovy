package org.greenfield

import org.springframework.dao.DataIntegrityViolationException
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.SecurityUtils
import grails.converters.*
import java.util.UUID
import org.greenfield.common.RoleName
import org.greenfield.Account
import groovy.text.SimpleTemplateEngine
import org.greenfield.BaseController


import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

@Mixin(BaseController)
class AccountController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", register: "POST"]

	def emailService
	def applicationService
	

	/** CUSTOMER FUNCTIONS **/
	
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
					accountInstance.passwordHash = new Sha256Hash(newPassword).toHex()
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
	
	
	
	def customer_profile(){
		authenticatedPermittedCustomer { accountInstance ->
			[accountInstance : accountInstance]
		}
	}	
	
	
		
	def customer_update(){
		authenticatedPermittedCustomer { customerAccount ->
		
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
		
		
	def order_history(){
		authenticatedCustomer { customerAccount ->
			def transactions = Transaction.findAllByAccount(customerAccount)
			[transactions : transactions]
		}	
	}
	
	
	def customer_registration(){}
	
	def customer_register(){
	
		def accountInstance = new Account(params)
		
		if(params.passwordHash && params.passwordRepeat){
			
			if(params.passwordHash == params.passwordRepeat){

				if(params.passwordHash.length() >= 5){
				
					params.ipAddress = request.getRemoteHost()
					accountInstance.properties = params
		
			   		def	password = new Sha256Hash(params.passwordHash).toHex()
			   		accountInstance.passwordHash = password
		
					def role = Role.findByName(RoleName.ROLE_CUSTOMER.description())
					role.addToAccounts(accountInstance)
					role.save(flush:true)
		
					if(accountInstance.save(flush:true)){
					
						accountInstance.addToRoles(role)
						accountInstance.addToPermissions("account:customer_profile,customer_update,customer_order_history:" + accountInstance.id)
						accountInstance.save(flush:true)
					
						sendNewRegistrationEmails(accountInstance)
			
						flash.message = "You have successfully registered... "
			
						redirect(controller : 'auth', action: 'customer_sign_in', params : [ accountInstance: accountInstance, username : params.username, password : params.passwordHash, new_account : true])
			
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
	
	
	
	
	def sendNewRegistrationEmails(Account accountInstance){
		try { 
			def fromAddress = applicationService.getSupportEmailAddress()
			def customerToAddress = accountInstance.email
			def customerSubject = "${applicationService.getStoreName()} : Thank you for registering"
			
			File templateFile = grailsAttributes.getApplicationContext().getResource(  "/templates/email/registration.html").getFile();

			def binding = [ "companyName" : applicationService.getStoreName(),
				 			"supportEmail" : applicationService.getSupportEmailAddress()]
			def engine = new SimpleTemplateEngine()
			def template = engine.createTemplate(templateFile).make(binding)
			def bodyString = template.toString()
			
			def adminEmail = applicationService.getAdminEmailAddress()
			def allEmails = customerToAddress
			if(adminEmail){
			 	allEmails += ",${adminEmail}"
			}
			
			
			emailService.send(allEmails, fromAddress, customerSubject, bodyString)
			
			
		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	
	
	
	/** ADMINISTRATION FUNCTIONS **/
	
		
	def admin_create(){
		authenticatedAdmin{ account ->
			if(params.admin == "true"){
				request.admin = "true"
			}			
        	[accountInstance: new Account(params)]
		}	
	}	


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
	
	
	def admin_save(){
		authenticatedAdmin { adminAccount ->

			def accountInstance = new Account(params)
			
	   		def	password = new Sha256Hash(params.passwordHash).toHex()
	   		accountInstance.passwordHash = password
		
			def role = Role.findByName(RoleName.ROLE_CUSTOMER.description())
			role.addToAccounts(accountInstance)
			role.save(flush:true)
		
			accountInstance.addToRoles(role)
		
			if(params.admin == "true" ||
					params.admin == "on"){
				def adminRole = Role.findByName(RoleName.ROLE_ADMIN.description())
				adminRole.addToAccounts(accountInstance)
				adminRole.save(flush:true)
				accountInstance.addToRoles(adminRole)
				accountInstance.hasAdminRole = true
			}else{
				accountInstance.hasAdminRole = false
			}
			
	   		
       		if (!accountInstance.save(flush: true)) {
	   			flash.message = "Something went wrong when saving the account, please try again..."
				render(view: "admin_create", model: [accountInstance: accountInstance])
       		    return
       		}
       		
		
			accountInstance.addToPermissions("account:customer_profile,customer_update,customer_order_history:" + accountInstance.id)
			accountInstance.save(flush:true)
			
       		flash.message = "Account successfully saved"
       		redirect(action: "admin_show", id:accountInstance.id)
		}
	}
	
	
	
	def admin_update(Long id){
		authenticatedAdmin { adminAccount ->
			
			def accountInstance = Account.get(id)
			
       		if (!accountInstance) {
       		    flash.message = "Account not found"
       		    redirect(action: "admin_list")
       		    return
       		}

			
			def hadAdmin = accountInstance.hasAdminRole
			
			accountInstance.properties = params
			def adminRole = Role.findByName(RoleName.ROLE_ADMIN.description())
			
			
			if(params.admin == "true" ||
					params.admin == "on"){
				adminRole.addToAccounts(accountInstance)
				adminRole.save(flush:true)
				accountInstance.addToRoles(adminRole)
				accountInstance.hasAdminRole = true
			}else{
				accountInstance.hasAdminRole = false
				if(hadAdmin){
					adminRole.removeFromAccounts(accountInstance)
					adminRole.save(flush:true)
					
					accountInstance.removeFromRoles(adminRole)
				}
			}
			
			
	   		if (!accountInstance.save(flush: true)) {
	   			flash.message = "Something went wrong when updating account, please try again..."
       		    render(view: "admin_edit", model: [accountInstance: accountInstance])
       		    return
       		}
       		
       		flash.message = "Account successfully updated"
       		redirect(action: "admin_show", id:accountInstance.id)
		}
	}
	
	
	
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
	
	
	
	def admin_list(Integer max){
		authenticatedAdmin { adminAccount ->
        	params.max = Math.min(max ?: 10, 100)
			def role
			
			if(params.admin == "true"){
				request.admin = true
				role = Role.findByName(RoleName.ROLE_ADMIN.description())
			}else{
				role = Role.findByName(RoleName.ROLE_CUSTOMER.description())
			}
			
			[ accountInstanceList: role.accounts, accountInstanceTotal: role.accounts.size() ]
		}
	}


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
	
	def admin_update_password(Long id){
		authenticatedAdmin { adminAccount ->
	        def accountInstance = Account.get(id)
	        if (!accountInstance) {
	            flash.message = "Account not found..."
	            redirect(action: "admin_list")
	            return
	        }
			
			
	   		def	password = new Sha256Hash(params.passwordHash).toHex()
	   		accountInstance.passwordHash = password

	   		if (!accountInstance.save(flush: true)) {
	   			flash.message = "Something went wrong when updating account, please try again..."
       		    render(view: "admin_edit", model: [accountInstance: accountInstance])
       		    return
       		}
       		
       		flash.message = "Account successfully updated password"
       		redirect(action: "admin_show", id:accountInstance.id)
			
		}
	}
	
	
}