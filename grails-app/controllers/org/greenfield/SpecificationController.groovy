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
					specification.save(flush:true)
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


	def update(){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->

			if(params.name){

				specificationInstance.name = params.name

				if(params.applySubcatalogs == "on"){
					specificationInstance.applySubcatalogs = true
				}else{
					specificationInstance.applySubcatalogs = false
				}

				specificationInstance.save(flush:true)

				flash.message = "Successfully updated specification"

			}else{
				flash.message = "Name cannot be blank"
			}

			redirect(action:'edit', id : specificationInstance.id, params:[catalogId : params.catalogId] )

		}
	}


	def add_option(){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
			if(params.name){
				def option = new SpecificationOption()
				option.name = params.name
				option.specification = specificationInstance
				option.save(flush:true)

				specificationInstance.addToSpecificationOptions(option)
				specificationInstance.save(flush:true)

				flash.message = "Successfully created option"

			}else{
				flash.optionMessage = "Name cannot be blank"
			}

			redirect(action:'edit', id : specificationInstance.id, params:[catalogId : params.catalogId] )
		}
	}


	def edit_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			def catalogInstance = Catalog.get(params.catalogId)
			[ specificationOption : specificationOptionInstance, catalogInstance: catalogInstance ]
		}
	}



	def update_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			if(params.name){

				specificationOptionInstance.name = params.name
				specificationOptionInstance.save(flush:true)

				flash.message = "Successfully updated option"

			}else{
				flash.message = "Name cannot be blank"
			}

			redirect(action:"edit_option", id : specificationOptionInstance.id, params : [catalogId : params.catalogId])
		}
	}



	def delete_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			def specificationInstance = specificationOptionInstance.specification;
			specificationOptionInstance.delete(flush:true)
			redirect(action: "edit", id: specificationInstance.id, params: [catalogId: params.catalogId])
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