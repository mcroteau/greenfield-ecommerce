package greenfield

import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account


class NewsletterController {

    static allowedMethods = [signup: ["GET", "POST"], opt_opt: "POST"]


    @Secured(['permitAll'])
    def index(){}


    @Secured(['permitAll'])
    def signup(){
    	def account = new Account()
    	account.username = params.email
    	account.email = params.email
    	account.password = "change"

    	def existingAccount = Account.findByEmail(params.email)
    	if(existingAccount && existingAccount.emailOptIn){
    		flash.message = "You have already signed up for news and updates"
    		render(view : "signup")
    		return
    	}


    	if(existingAccount && !existingAccount.emailOptIn){
    		flash.message = "We found an account already for email entered. Please opt in for news and updates"
    		redirect(action: "found", id: existingAccount.id)
    		return
    	}

    	if(!account.save(flush:true)){
    		flash.message = "Please enter a valid email address"
    	}else{
    		flash.message = "Successfully signed up for news & updates"
    	}


    	account.errors.allErrors.each{ println it }

    	redirect(action: "index")
    }



    @Secured(['permitAll'])
    def found(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    		redirect(action: "signup")
    		return
    	}
		[account : account]
    }


    @Secured(['ROLE_ADMIN'])
    def opt_in(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = true
    	account.save(flush:true)

    	flash.message = "Successfully opted in account: " + account.email
		redirect(action: "index")
    }


    @Secured(['ROLE_ADMIN'])
    def admin_opt_in(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = true
    	account.save(flush:true)

    	flash.message = "Successfully opted in account: " + account.email
		redirect(controller:"account", action: "edit", id: account.id)
    }



    @Secured(["permitAll", "ROLE_ADMIN"])
    def opt_out(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = false
    	account.save(flush:true)

    	flash.message = "Successfully opted out account: " + account.email

    	if(params.redirect == "true"){
    		redirect(action: "index")
    		return
    	}

		redirect(action: "list")
    }


    @Secured(["permitAll"])
    def confirm(){
    	def account = Account.findByEmail(params.email)
    	if(!account){
    		flash.message = "Unable to find account"
    		redirect(action: "index")
    		return
    	}

    	[account: account]
    }



    @Secured(['ROLE_ADMIN'])
    def list(){
    	def max = 10
		def offset = params?.offset ? params.offset : 0
		def sort = params?.sort ? params.sort : "id"
		def order = params?.order ? params.order : "asc"

		def accountsList = Account.findAllByEmailOptIn(true, [max: max, offset: offset, sort: sort, order: order ])

		[accountsList: accountsList, accountsTotal: Account.countByEmailOptIn(true)]
    }


}