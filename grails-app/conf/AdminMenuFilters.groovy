class AdminMenuFilters {
	
	def ACTIVE_CLASS_NAME = "active"
	
    def filters = {
	
        admin(controller:'admin', action:'index') {
            before = {
				request.dashboardActive = ACTIVE_CLASS_NAME
			}
		}
		
		layout(controller:'layout', action:'*'){
			before = {
				def layoutActive = false
				switch(actionName){
					case null :
						layoutActive = true
						break;
					case "index" :
						layoutActive = true
						break;
					case "tags" :
						layoutActive = true
						break;
					case "how" :
						layoutActive = true
						break;
				}
				if(layoutActive){
					request.layoutActive = ACTIVE_CLASS_NAME
				}
			}
		}
		
		configuration(controller:'configuration', action:'*'){
			before = {
				switch(actionName){
					case null :
						request.importActive = ACTIVE_CLASS_NAME
						break;
					case "index" :
						request.importActive = ACTIVE_CLASS_NAME
						break;
					case "settings" :
						request.settingsActive = ACTIVE_CLASS_NAME
						break;
					case "email_settings" :
						request.settingsActive = ACTIVE_CLASS_NAME
						break;
					case "stripe_settings" :
						request.settingsActive = ACTIVE_CLASS_NAME
						break;
					case "shipping_settings" :
						request.settingsActive = ACTIVE_CLASS_NAME
						break;
					case "uploads" :
						request.importActive = ACTIVE_CLASS_NAME
						break;
					case "import_products_view" :
						request.importActive = ACTIVE_CLASS_NAME
						break;
				}
			}
		}
		
		accounts(controller:'account', action:'*'){
			before = {
				def accountsActive = false
				switch(actionName){
					case "admin_list" :
						accountsActive = true
						break;
					case "admin_create" :
						accountsActive = true
						break;
					case "admin_edit" :
						accountsActive = true
						break;
					case "admin_show" :
						accountsActive = true
						break;
				}

				if(accountsActive){
					request.accountsActive = ACTIVE_CLASS_NAME
				}
			}
		}
		
		pages(controller:'page', action:'*'){
			before = {
				def pagesActive = false
				switch(actionName){
					case "list" :
						pagesActive = true
						break;
					case "create" :
						pagesActive = true
						break;
					case "edit" :
						pagesActive = true
						break;
					case "show" :
						pagesActive = true
						break;
				}

				if(pagesActive){
					request.pagesActive = ACTIVE_CLASS_NAME
				}
			}
		}
		
		transactions(controller:'transaction', action:'*'){
			before = {
				def ordersActive = false
				switch(actionName){
					case "list" :
						ordersActive = true
						break;
					case "show" :
						ordersActive = true
						break;
				}

				if(ordersActive){
					request.ordersActive = ACTIVE_CLASS_NAME
				}
			}
		}
		
		catalogs(controller:'catalog', action:'*'){
			before = {
				def catalogsActive = false
				switch(actionName){
					case "list" :
						catalogsActive = true
						break;
					case "create" :
						catalogsActive = true
						break;
					case "edit" :
						catalogsActive = true
						break;
					case "show" :
						catalogsActive = true
						break;
				}

				if(catalogsActive){
					request.catalogsActive = ACTIVE_CLASS_NAME
				}
			}
		}	
				
		products(controller:'product', action:'*'){
			before = {
				def productsActive = false
				switch(actionName){
					case "list" :
						productsActive = true
						break;
					case "create" :
						productsActive = true
						break;
					case "edit" :
						productsActive = true
						break;
					case "show" :
						productsActive = true
						break;
					case "additional_photos" :
						productsActive = true
						break;
					case "add_product_option" :
						productsActive = true
						break;
					case "product_options" :
						productsActive = true
						break;
					case "admin_search" :
						productsActive = true
						break;
				}
				if(productsActive){
					request.productsActive = ACTIVE_CLASS_NAME
				}
			}
		}
	}
}