package org.greenfield

import org.greenfield.BaseController

@Mixin(BaseController)
class SpecificationController {

	def create(Long id){
		authenticatedAdminCatalog { adminAccount, catalogInstance ->
			[ catalogInstance : catalogInstance ]
		}
	}
	
	
	def save(Long id){
		authenticatedAdminCatalog { adminAccount, catalogInstance ->
			if(params.name){

				def specification = new Specification()
				specification.name = params.name
				specification.applySubcatalogs = false
				specification.save(flush:true)
				
				catalogInstance.addToSpecifications(specification)
				catalogInstance.save(flush:true)
				
				if(params.applySubcatalogs == "on"){
					specification.applySubcatalogs = true
					catalogInstance.subcatalogs.each(){ subcatalog ->
						subcatalog.addToSpecifications(specification)
						subcatalog.save(flush:true)
					}
				}

				flash.message = "Successfully added specification, you can now add options"
				redirect(controller : 'specification', action: 'edit', id : specification.id, params : [ catalogId : catalogInstance.id] )
				
			}else{
				flash.message = "Name cannot be blank"
				request.catalogInstance = catalogInstance
				render(view : 'add_product_option')
			}
		}
	}
	
	
	def edit(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
			def catalogInstance = Catalog.get(params.catalogId)
			[ specificationInstance: specificationInstance, catalogInstance: catalogInstance ]
		}
	}
	
	
	//TODO:remove
	def delete_all(){
		def catalogs = Catalog.list()
		catalogs.each(){ catalog ->
			if(catalog.specifications){
				catalog.specifications.each(){ specification ->
					catalog.removeFromSpecifications(specification)
					specification.delete(flush:true)
				}
			}
		}
		render "${Specification.count()}"
	}

}