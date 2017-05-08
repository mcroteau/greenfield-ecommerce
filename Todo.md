#Todos

* refactor permissions logic (customer profile, shopping cart, transaction/order)
* fix dashboard when only one day of data
* fix the need to enter address data on easypost and in app
* fix broken search with empty search box
* implement multi login suggested stack overflow
* add delete functionality to ProductOption
* consider create stack trace log domain to capture all errors
* add item no added to product
* fix date picker admin dashboard
* change directory for image upload/import
* use same method to upload image in variants for additional photos and main image
* consider use permissions for shopping cart
* rethink permissions for profile, use just 1 permission for all per domain?
* restyle catalogs associated to product example product specifications (books >> sci-fi)
* consider removing the AuthController have all actions go through AccountController
* customize the denied.gsp to link back to admin or to logout
* toughen up the design less round on buttons, inputs and admin layout
* remove unnecessary auth controller
* create default page (content/)
* uncomment out layout-wrapper			
* copy over filters conf/
* convert to Permission on Bootstrap and Development Data
* set context path in application.yml or application.groovy
	server:
    	contextPath: /helloworld

* secure all of the following for admin access only

        admin(controller:'admin'
		
		accounts(controller:'account'
			"admin_list" : 
			"admin_create" : 
			"admin_edit" : 
			"admin_show"

		products(controller:'product', 
			"list" : 
			"create" : 
			"edit" : 
			"show" : 
			"additional_photos" : 
			"add_product_option" : 
			"product_options" : 
			"admin_search" 

		catalogs(controller:'catalog',
			"list" : 
			"create" : 
			"edit" : 
			"show"

		transactions(controller:'transaction'
		 	"list" : 
		 	"show"
				
		pages(controller:'page'
			"list" :  
			"create" : 
			"edit" : 
			"show" 
				
		configuration(controller:'configuration'
			"index" : 
			"settings" : 
			"email_settings" : 
			"stripe_settings" : 
			"shipping_settings" : 
			"uploads" : 
			"import_products_view"
			"save" :
			"update"
			
		layout(controller:'layout'  
			"index" : 
			"tags" : 
			"how" : 
			"save" :
			"update"

* consider going through urls change from camel to to lower case to follow your rule (resource request all lower, methods outside web request camel case. same with variables)	

* create a better looking unauthorized view (login as customer and access catalog/list)








#Complete
*FIX def refresh(){
	
		if(!grailsApplication){
			grailsApplication = new Page().domainClass.grailsApplication

			no such property domainClass
