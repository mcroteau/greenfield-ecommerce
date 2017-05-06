package greenfield.common

import org.apache.shiro.SecurityUtils

import org.greenfield.AppConstants
import org.greenfield.Account
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.ProductOption 
import org.greenfield.ProductSpecification
import org.greenfield.Specification
import org.greenfield.SpecificationOption 
import org.greenfield.Transaction
import org.greenfield.ShoppingCart
import org.greenfield.common.RoleName
import org.greenfield.Specification
import org.greenfield.Variant
import org.greenfield.State


//http://mrpaulwoods.wordpress.com/2011/01/23/a-pattern-to-simplify-grails-controllers/

class BaseController {
	
	//TODO:remove : edit consider creating general authenticated 
	// private def authenticated(Closure c){
	// 	def subject = SecurityUtils.getSubject();
	
	// 	if(!subject.isAuthenticated()){
	// 		flash.message = "Please sign in to continue"
	// 		forward(controller : 'auth', action: 'login')
	// 		return
	// 	}
		
	// 	c.call subject
	// }
		
	// private def authenticatedAccount(Closure c){
	// 	def subject = SecurityUtils.getSubject();
	
	// 	if(!subject.isAuthenticated()){
	// 		flash.message = "Please sign in to continue"
	// 		forward(controller : 'auth', action: 'login')
	// 		return
	// 	}
		
	// 	def accountInstance = Account.findByUsername(subject.principal)
		
	// 	if(!accountInstance){
	// 		flash.message = "Please login to continue"
	// 		forward(controller: 'auth', action: 'login')
	// 		return
	// 	}else{
	// 		c.call accountInstance
	// 	}
	// }

	private def authenticatedAccount(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		c.call accountInstance
	}


	private def authenticatedCustomer(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_CUSTOMER.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
		c.call accountInstance
	}
	


	private def authenticatedAdmin(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
		c.call accountInstance
	}
	
	

	private def authenticatedAdminProduct(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
    	def productInstance = Product.get(params.id)
		
        if (!productInstance) {
            flash.message = "Product not found..."
            forward(controller: 'product', action: "list")
			return
        }
		
		c.call accountInstance, productInstance		
	}
	

	
	private def authenticatedAdminCatalog(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
    	def catalogInstance = Catalog.get(params.id)
		
        if (!catalogInstance) {
            flash.message = "Catalog not found..."
            forward(controller: 'catalog', action: "list")
			return
        }
		
		c.call accountInstance, catalogInstance		
	}
	

	
	
	private def authenticatedAdminPage(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
    	def pageInstance = Page.get(params.id)
		
        if (!pageInstance) {
            flash.message = "Page not found..."
            forward(controller: 'page', action: "list")
			return
        }
		
		c.call accountInstance, pageInstance
	}
    
	
	
	
	private def authenticatedAdminShoppingCart(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
    	def shoppingCartInstance = ShoppingCart.get(params.id)
		
        if (!shoppingCartInstance) {
            flash.message = "Shopping Cart not found..."
            forward(controller: 'page', action: "list")
			return
        }
		
		c.call accountInstance, shoppingCartInstance
	}
	
	
	
	
	private def authenticatedAdminTransaction(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
    	def transactionInstance = Transaction.get(params.id)
	
		
        if (!transactionInstance) {
            flash.message = "Transaction not found..."
            forward(controller: 'transaction', action: "list")
			return
        }
		
		c.call accountInstance, transactionInstance
	}
	

	
	
	private def authenticatedAdminProductOption(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
		def productOption = ProductOption.get(params.id)
    	
		
    	if (!productOption) {
    	    flash.message = "Product Option not found..."
    	    forward(controller: 'product', action: "list")
			return
    	}
		
		c.call accountInstance, productOption
	}




	private def authenticatedAdminSpecification(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
		def specificationInstance = Specification.get(params.id)

		if (!specificationInstance) {
			flash.message = "Specification not found..."
			forward(controller: 'catalog', action: "list")
			return
		}

		c.call accountInstance, specificationInstance
	}




	private def authenticatedAdminSpecificationOption(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
		def specificationOption = SpecificationOption.get(params.id)

		if (!specificationOption) {
			flash.message = "Option not found..."
			forward(controller: 'catalog', action: "list")
			return
		}

		c.call accountInstance, specificationOption
	}





	private def authenticatedPermittedCustomer(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}

		def permission = accountInstance.permissions.find { 
			it.permission == "account:customer_profile:${accountInstance.id}" 
		}

		if (!permission){
			flash.message = "You do not have permission to access this account..."
			forward(controller:'store', action:'index')
			return
		}
		
		c.call accountInstance
	}
	


	
	private def authenticatedPermittedOrderDetails(Closure c){
		if(!principal?.username){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}

		def accountInstance = Account.findByUsername(principal?.username)

		if(!accountInstance){
			flash.message = "You must log in to continue..."
			forward(controller:'auth', action:'customer_login')
			return
		}
		
		def accountRole = accountInstance.getAuthorities().find { 
			it.authority == RoleName.ROLE_ADMIN.description() 
		}

		if(!accountRole){
			flash.message = "You do not have permission to access..."
			forward(controller:'store', action:'index')
			return	
		}
		
		def transactionInstance = Transaction.get(params.id)
		
		if(!transactionInstance){
			flash.message = "Unable to find Order, please try again"
			forward(controller:'store', action:'index')
			return
		}
		
		def permission = accountInstance.permissions.find { 
			it.permission == "transaction:order_details:${transactionInstance.id}" 
		}

		if (!permission){
			flash.message = "You do not have permission to access this account..."
			forward(controller:'store', action:'index')
			return
		}
		
		c.call accountInstance, transactionInstance
		
	}
	

	




	// private def getCheckAuthenticatedAccount(){
	// 	if(!principal?.username){
	// 		flash.message = "You must log in to continue..."
	// 		forward(controller:'auth', action:'customer_login')
	// 		return
	// 	}

	// 	def accountInstance = Account.findByUsername(principal?.username)

	// 	if(!accountInstance){
	// 		flash.message = "You must log in to continue..."
	// 		forward(controller:'auth', action:'customer_login')
	// 		return
	// 	}

	// 	return account
	// }



	// private def getCheckAuthenticatedAdmin(){
	// 	if(!principal?.username){
	// 		flash.message = "You must log in to continue..."
	// 		forward(controller:'auth', action:'admin_login')
	// 		return
	// 	}

	// 	def accountInstance = Account.findByUsername(principal?.username)

	// 	if(!accountInstance){
	// 		flash.message = "You must log in to continue..."
	// 		forward(controller:'auth', action:'admin_login')
	// 		return
	// 	}
		
	// 	def accountRole = accountInstance.getAuthorities().find { 
	// 		it.authority == RoleName.ROLE_ADMIN.description() 
	// 	}

	// 	if(!accountRole){
	// 		flash.message = "You do not have permission to access..."
	// 		forward(controller:'store', action:'index')
	// 		return	
	// 	}

	// 	return account
	// }


	// private def getCheckAuthenticatedCustomer(){
	// 	if(!principal?.username){
	// 		flash.message = "You must log in to continue..."
	// 		forward(controller:'auth', action:'customer_login')
	// 		return
	// 	}

	// 	def accountInstance = Account.findByUsername(principal?.username)

	// 	if(!accountInstance){
	// 		flash.message = "You must log in to continue..."
	// 		forward(controller:'auth', action:'customer_login')
	// 		return
	// 	}
		
	// 	def accountRole = accountInstance.getAuthorities().find { 
	// 		it.authority == RoleName.ROLE_CUSTOMER.description() 
	// 	}

	// 	if(!accountRole){
	// 		flash.message = "You do not have permission to access..."
	// 		forward(controller:'store', action:'index')
	// 		return	
	// 	}

	// 	return account
	// }


}