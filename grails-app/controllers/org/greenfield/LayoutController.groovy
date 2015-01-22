package org.greenfield
import org.greenfield.BaseController

@Mixin(BaseController)
class LayoutController {

	def applicationService
	def grailsApplication
		
	def how(){}
	def tags(){}
		
		
    def index() {	
		authenticatedAdmin{ adminAccount ->
			File cssFile = grailsApplication.mainContext.getResource("css/store.css").file
			String css = cssFile.text
			css = css.replace("[[CONTEXT_NAME]]", applicationService.getContextName())
			[layout : Layout.findByName("STORE_LAYOUT").content, css : css]
		}
	}
	
	
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
