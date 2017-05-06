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
			File cssFile = grailsApplication.mainContext.getResource("css/store.css").file
			String css = cssFile.text
			css = css.replace("[[CONTEXT_NAME]]", applicationService.getContextName())
			[layout : Layout.findByName("STORE_LAYOUT").content, css : css]
		}
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
}
