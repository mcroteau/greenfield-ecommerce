package org.greenfield

import org.springframework.dao.DataIntegrityViolationException
import org.greenfield.BaseController

import org.greenfield.log.PageViewLog
import org.apache.shiro.SecurityUtils

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
	
	
	def store_view(String title){
		def t = java.net.URLDecoder.decode(params.title, "UTF-8");
		def pageInstance = Page.findByTitle(t)
		if(!pageInstance){
			flash.message = "Unable to find page"
    	    redirect(controller : 'store', action: "index")
		}
		
		def pageViewLog = new PageViewLog()
		pageViewLog.page = pageInstance

		def subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			def account = Account.findByUsername(subject.principal)
			if(account){
				pageViewLog.account = account
			}
		}
		
		pageViewLog.save(flush:true)
		
		[pageInstance : pageInstance]
	}
	
	

	/** ADMINISTRATION METHODS **/

    def create() {
        [pageInstance: new Page(params)]
    }
	
	
	def index() {
		authenticatedAdmin{ adminAccount ->
        	redirect(action: "list", params: params)
		}
    }

    
	
	def list(Integer max) {
		authenticatedAdmin{ adminAccount ->
        	params.max = Math.min(max ?: 10, 100)
        	[pageInstanceList: Page.list(params), pageInstanceTotal: Page.count()]
    	}
	}
	
	
	
	
    def show(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        	[pageInstance: pageInstance]
		}
    }
	
	

    def edit(Long id) {
		authenticatedAdminPage { adminAccount, pageInstance ->
        	[pageInstance: pageInstance]
		}
    }



    def save() {	
		authenticatedAdmin { adminAccount ->
        	def pageInstance = new Page(params)
        	if (!pageInstance.save(flush: true)) {
				flash.message = "Something went wrong. Please try again"
        	    render(view: "create", model: [pageInstance: pageInstance])
        	    return
        	}
        	
        	flash.message = message(code: 'default.created.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.id])
        	redirect(action: "show", id: pageInstance.id)
    	}
	}
	
	
	

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
