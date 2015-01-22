package org.greenfield

import org.springframework.dao.DataIntegrityViolationException
import org.greenfield.BaseController

@Mixin(BaseController)
class CatalogController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }
    
	
	
	def products(Long id){
		def catalogInstance = Catalog.findById(id)
		def products = Product.findAllByCatalogAndDisabledAndQuantityGreaterThan(catalogInstance, false, 0)
		[products : products, catalogInstance: catalogInstance]
	}
	
	
	
	
	def catalog_products(String name){
		if(params.name){
			def catalog = Catalog.findByName(params.name)
			def products = Product.findAllByCatalogAndDisabled(catalog, false)
			request.products = products
			render view:'products'
		}
	}	
	
	

	
	
    def list(Integer max) {
		authenticatedAdmin { adminAccount -> 
			params.max = Math.min(max ?: 10, 100)
        	[catalogInstanceList: Catalog.list(params), catalogInstanceTotal: Catalog.count()]
    	}
	}




    def create() {
		authenticatedAdmin { adminAccount -> 
	    	[catalogInstance: new Catalog(params)]
    	}
	}



    def save() {
		authenticatedAdmin { adminAccount ->
    	    def catalogInstance = new Catalog(params)
    	    if (!catalogInstance.save(flush: true)) {
				flash.error = "Something went wrong while saving product. Please try again."
    	        render(view: "create", model: [catalogInstance: catalogInstance])
    	        return
    	    }
    	
    	    flash.message = "Successfully saved product"
    	    redirect(action: "show", id: catalogInstance.id)
    	}
    }	
		
		
		
    def show(Long id) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->	
    	    [catalogInstance: catalogInstance ]			
		}
    }




    def edit(Long id) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->	
    	    [catalogInstance: catalogInstance ]			
		}
    }
	
	
	

    def update(Long id, Long version) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->	
    	
        	catalogInstance.properties = params
        	
        	if (!catalogInstance.save(flush: true)) {
				flash.error = "Something went wrong while trying to update. Please try again"
        	    render(view: "edit", model: [catalogInstance: catalogInstance])
        	    return
        	}
        	flash.message = "Successfully updated product"
        	redirect(action: "show", id: catalogInstance.id)
		}
	}
	





    def delete(Long id) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->
    	    try {
    	        catalogInstance.delete(flush: true)
    	        flash.message = "Successfully deleted the product"
    	        redirect(action: "list")
    	    }catch (DataIntegrityViolationException e) {
    	        flash.error = "Something went wrong when deleting product. Please make sure no products belong to this catalog"
    	        redirect(action: "show", id: id)
    	    }
    	}
	}
}
