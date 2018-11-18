package greenfield

import greenfield.common.BaseController
import org.greenfield.Layout

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class LayoutController {

	def applicationService
	//Grails 2 explicitly set
    //def grailsApplication
	

 	@Secured(['ROLE_ADMIN'])	
	def how(){}


 	@Secured(['ROLE_ADMIN'])
	def tags(){}
		
	
 	@Secured(['ROLE_ADMIN'])	
    def index() {	
		authenticatedAdmin{ adminAccount ->
			def offset = params.offset ? params.offset : 0
			def layouts = Layout.findAll(max:10, offset:offset, sort:"name", order: "asc")
			
			
			/**
			File cssFile = grailsApplication.mainContext.getResource("css/store.css").file
			String css = cssFile.text
			css = css.replace("[[CONTEXT_NAME]]", applicationService.getContextName())
			[layout : Layout.findByName("STORE_LAYOUT").content, css : css]
			**/
			
			[layouts: layouts]
			
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def create(){}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def save(){
		def layoutInstance = new Layout(params)
		println "content" + layoutInstance.content
		println "css" + layoutInstance.css
		println "javascript" + layoutInstance.javascript
		
		
		if(!layoutInstance.name){
			flash.message = "Please make sure layout name is not empty and is unique"
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
		}
		
		def existingLayout = Layout.findByName(layoutInstance.name)
		if(existingLayout){
			flash.message = "Please make sure layout name is unique"
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
		}
		
		
		if(layoutInstance.content && !layoutInstance.content.contains("[[CONTENT]]")){
			flash.message = "Please make sure layout contains [[CONTENT]] tag"
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
		}
		

		def existingDefaultLayouts = Layout.findAllByDefaultLayout(true)
		
		if(layoutInstance.defaultLayout){
			if(existingDefaultLayouts){
				existingDefaultLayouts.each { layout ->
					layout.defaultLayout = false
					layout.save(flush:true)
				}
			}
		}else{
			if(!existingDefaultLayouts){
				layoutInstance.defaultLayout = true
			}
		}
		
		
		if(!layoutInstance.save(flush: true)) {
			flash.message = "Please make sure layout is not empty and contains [[CONTENT]] tag"
	        render(view: "create", model: [layoutInstance: layoutInstance])
	        return
	    }
		
		
		
		redirect(action:"show", id: layoutInstance.id)
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
 	def update(){
		authenticatedAdmin{ adminAccount ->
			
			if(!params.layout.contains("[[CONTENT]]")){
				flash.message = "Layout must contain <strong>[[CONTENT]]</strong> tag."
				redirect(action : 'index')
				return
			}
			
			File cssfile = grailsApplication.mainContext.getResource("css/store.css").file;
			FileWriter fw = new FileWriter(cssfile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			def css = params.css
			css = css.replace("[[CONTEXT_NAME]]",  applicationService.getContextName())
			bw.write(params.css);
			bw.close();
			
			
			def layout = Layout.findByName("STORE_LAYOUT")
			
			def layoutNew = params.layout
			
			if(layoutNew){
				layout.content = layoutNew
				layout.save(flush:true)
			}
			
			applicationService.refresh()
			
			flash.message = "Successfully updated layout"
			redirect( action:'index')
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def edit_wrapper(){
		File wrapperFile = grailsApplication.mainContext.getResource("templates/storefront/layout-wrapper.html").file
		String html = wrapperFile.text
		[html: html]
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def update_wrapper(){
	}
	
	
 	@Secured(['ROLE_ADMIN'])	
	def show_wrapper(){
	}
	
}
