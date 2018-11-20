package greenfield

import org.springframework.dao.DataIntegrityViolationException
import greenfield.common.BaseController

import org.greenfield.Account
import org.greenfield.Page
import org.greenfield.log.PageViewLog

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class PageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	/** STORE METHODS **/
	
	def view(Long id){   
    	def pageInstance = Page.get(id)
    	if (!pageInstance) {
			flash.message = "Page not found"
    	    redirect(controller : 'store', action: "index")
    	    return
    	} 

    	[pageInstance: pageInstance]
	}
	
	
 	@Secured(['permitAll'])
	def store_view(String title){
		def t = java.net.URLDecoder.decode(params.title, "UTF-8");
		def pageInstance = Page.findByTitle(t)
		if(!pageInstance){
			flash.message = "Unable to find page"
    	    redirect(controller : 'store', action: "index")
		}
		
		def pageViewLog = new PageViewLog()
		pageViewLog.page = pageInstance

		def accountInstance
		if(principal?.username){
			accountInstance = Account.findByUsername(principal?.username)
			pageViewLog.account = accountInstance
		}
		pageViewLog.save(flush:true)
		
		if(accountInstance){
			accountInstance.pageViews = PageViewLog.countByAccount(accountInstance)
			accountInstance.save(flush:true)
		}
		
		[pageInstance : pageInstance]
	}
	
	
	//TODO:catch if deleted in database
 	@Secured(['permitAll'])
	def home(){
		def pageInstance = Page.findByTitle("Home")
		[pageInstance: pageInstance]
	}
	
	
	
	

	/** ADMINISTRATION METHODS **/
	
 	@Secured(['ROLE_ADMIN'])
    def create() {
		def layouts = Layout.list()
        [pageInstance: new Page(params), layouts: layouts]
    }
	
	
 	@Secured(['ROLE_ADMIN'])
	def index() {
		authenticatedAdmin{ adminAccount ->
        	redirect(action: "list", params: params)
		}
    }

    
	
 	@Secured(['ROLE_ADMIN'])
	def list(Integer max) {
		authenticatedAdmin{ adminAccount ->
        	params.max = Math.min(max ?: 10, 100)
        	[pageInstanceList: Page.list(params), pageInstanceTotal: Page.count()]
    	}
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def show(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        	[pageInstance: pageInstance]
		}
    }
	
	

 	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
			def layouts = Layout.list()
        	[pageInstance: pageInstance, layouts: layouts]
		}
    }



 	@Secured(['ROLE_ADMIN'])
    def save() {	
		authenticatedAdmin { adminAccount ->
			
        	def pageInstance = new Page(params)
        	if (!pageInstance.save(flush: true)) {
				flash.message = "Something went wrong. Please try again"
        	    render(view: "create", model: [pageInstance: pageInstance])
        	    return
        	}
        	
        	flash.message = "Successfully saved page"
        	redirect(action: "show", id: pageInstance.id)
    	}
	}
	
	
	

 	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        
        	pageInstance.properties = params
        	
        	if (!pageInstance.save(flush: true)) {
				flash.message = "Something went wrong. Please try again"
        	    render(view: "edit", model: [pageInstance: pageInstance])
        	    return
        	}
        	
        	flash.message = "Page sucessfully updated"
        	redirect(action: "show", id: pageInstance.id)
		}
    }
	
	
	
 	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        	try {

				//Delete all ProductViewLogs
				PageViewLog.executeUpdate("delete PageViewLog p where p.page = :page", [page : pageInstance])
				
        	    pageInstance.delete(flush: true)
        	    flash.message = "Successfully deleted the page."
        	    redirect(action: "list")
        	}
        	catch (DataIntegrityViolationException e) {
        	    flash.message = "Something went wrong delete the page.  Please try again"
        	    redirect(action: "show", id: id)
        	}
		}
    }
		
}
