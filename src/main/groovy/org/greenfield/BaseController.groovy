package org.greenfield

import org.greenfield.common.RoleName
import org.apache.shiro.SecurityUtils
import org.greenfield.Account

import org.greenfield.AppConstants

//http://mrpaulwoods.wordpress.com/2011/01/23/a-pattern-to-simplify-grails-controllers/

class BaseController {
	
	private def authenticated(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		c.call subject
	}
	
	
	private def authenticatedAccount(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account
		}
	}
	
	
	
	
	private def authenticatedPermittedOrderDetails(Closure c){
		def subject = SecurityUtils.getSubject();
		
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'customer_login')
			return
		}
		
	
		if(!subject.hasRole(RoleName.ROLE_CUSTOMER.description())){
			flash.message = "Unauthorized"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def accountInstance = Account.findByUsername(subject.principal)
		def transactionInstance = Transaction.get(params.id)
		
		
		if(!transactionInstance){
			flash.message = "Unable to find Order, please try again"
			forward(controller:'store', action:'index')
			return
		}
		
		
		
		if(!accountInstance){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'customer_login')
			return
		}else{
			if(!subject.isPermitted("transaction:order_details:${transactionInstance.id}")){
				flash.message = "You do not have permission to access this order..."
				forward(controller:'store', action:'index')
				return
			}
			
			c.call accountInstance, transactionInstance
		}
	}
	
	
	private def authenticatedPermittedCustomer(Closure c){
		def subject = SecurityUtils.getSubject();
		
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'customer_login')
			return
		}
		
	
		if(!subject.hasRole(RoleName.ROLE_CUSTOMER.description())){
			flash.message = "Unauthorized"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def accountInstance = Account.findByUsername(subject.principal)
		
		if(!accountInstance){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'customer_login')
			return
		}else{
			if(!subject.isPermitted("account:customer_profile:${accountInstance.id}")){
				flash.message = "You do not have permission to access this account..."
				forward(controller:'store', action:'index')
				return
			}
			
			c.call accountInstance
		}
		
	}
	
	
	private def authenticatedCustomer(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'customer_login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_CUSTOMER.description())){
			flash.message = "Unauthorized"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'customer_login')
			return
		}else{
			c.call account
		}
	}
	
	
	private def authenticatedAdmin(Closure c){
		def subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account
		}
	}
	
	
	private def authenticatedAdminProduct(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
    	def product = Product.get(params.id)
		
        if (!product) {
            flash.message = "Product not found..."
            forward(controller: 'product', action: "list")
			return
        }
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, product
		}
	}
	
	
	private def authenticatedAdminCatalog(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
    	def catalog = Catalog.get(params.id)
		
        if (!catalog) {
            flash.message = "Catalog not found..."
            forward(controller: 'catalog', action: "list")
			return
        }
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, catalog
		}
	}
	
	
	
	private def authenticatedAdminPage(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
    	def page = Page.get(params.id)
		
        if (!page) {
            flash.message = "Page not found..."
            forward(controller: 'page', action: "list")
			return
        }
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, page
		}
	}
    
	
	
	
	private def authenticatedAdminShoppingCart(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
    	def shoppingCart = ShoppingCart.get(params.id)
		
        if (!shoppingCart) {
            flash.message = "Shopping Cart not found..."
            forward(controller: 'page', action: "list")
			return
        }
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, shoppingCart
		}
	}
	
	
	
	
	private def authenticatedAdminTransaction(Closure c){
		def subject = SecurityUtils.getSubject();
	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
    	def transaction = Transaction.get(params.id)
	
		
        if (!transaction) {
            flash.message = "Transaction not found..."
            forward(controller: 'transaction', action: "list")
			return
        }
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, transaction
		}
	}
	
	
	
	private def authenticatedAdminProductOption(Closure c){
		def subject = SecurityUtils.getSubject();
    	
		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}
		
		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}
		
		def account = Account.findByUsername(subject.principal)
		def productOption = ProductOption.get(params.id)
    	
		
    	if (!productOption) {
    	    flash.message = "Product Option not found..."
    	    forward(controller: 'product', action: "list")
			return
    	}
		
		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, productOption
		}
	}


	private def authenticatedAdminSpecification(Closure c){
		def subject = SecurityUtils.getSubject();

		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}

		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}

		def account = Account.findByUsername(subject.principal)
		def specification = Specification.get(params.id)


		if (!specification) {
			flash.message = "Specification not found..."
			forward(controller: 'catalog', action: "list")
			return
		}

		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, specification
		}
	}


	private def authenticatedAdminSpecificationOption(Closure c){
		def subject = SecurityUtils.getSubject();

		if(!subject.isAuthenticated()){
			flash.message = "Please sign in to continue"
			forward(controller : 'auth', action: 'login')
			return
		}

		if(!subject.hasRole(RoleName.ROLE_ADMIN.description())){
			flash.message = "Unauthorized to view admin section"
			forward(controller : 'store', action: 'index')
			return
		}

		def account = Account.findByUsername(subject.principal)
		def specificationOption = SpecificationOption.get(params.id)


		if (!specificationOption) {
			flash.message = "Option not found..."
			forward(controller: 'catalog', action: "list")
			return
		}

		if(!account){
			flash.message = "Please login to continue"
			forward(controller: 'auth', action: 'login')
			return
		}else{
			c.call account, specificationOption
		}
	}

}