package org.greenfield

import org.greenfield.BaseController

@Mixin(BaseController)
class SpecificationController {


	def list(){
		authenticatedAdmin{ adminAccount ->
			def specifications = Specification.list()
			[ specifications : specifications ]
		}
	}


	def create(Long id){
		authenticatedAdmin { adminAccount ->
			def specificationInstance = new Specification(params)
			def catalogIdsArray = []
			if(specificationInstance?.catalogs){
				catalogIdsArray = specificationInstance?.catalogs.collect { it.id }
			}
			//TODO: move getCatalogIdSelectionList to utility class
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)

			def name = params.name ? params.name : ""
			[ catalogIdSelectionList: catalogIdSelectionList, name : name ]
		}
	}
	
	
	def save(Long id){
		authenticatedAdmin { adminAccount ->
			if(params.name){

				if(!params.catalogIds){
					flash.message = "You must assign at least one catalog to continue saving the specification"
					redirect(action: "create", params : [ name : params.name ] )
					return
				}

				def specification = new Specification()
				specification.name = params.name

				def catalogSelectedIdsArray = params.catalogIds.split(',').collect{it as int}

				if(!catalogSelectedIdsArray){
					flash.error = "You must assign at least one catalog to continue saving the specification"
					redirect(action: "create" )
					return
				}

				specification.catalogs = null
				specification.catalog = null
				catalogSelectedIdsArray.eachWithIndex {  catalogId, index ->

					def catalog = Catalog.get(catalogId)
					if(catalog){
						if(index == 0){
							//add base catalog
							specification.catalog = catalog
						}
						specification.addToCatalogs(catalog)
						specification.save(flush:true)

						catalog.addToSpecifications(specification)
						catalog.save(flush:true)
					}

				}

				flash.message = "Successfully added specification, you can now add options"
				redirect(controller : 'specification', action: 'edit', id : specification.id )
				
			}else{
				flash.message = "Name cannot be blank"
				redirect(action:'create')
			}
		}
	}
	
	
	def edit(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
			def catalogIdsArray = []
			if(specificationInstance?.catalogs){
				catalogIdsArray = specificationInstance?.catalogs.collect { it.id }
			}
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)
			[ specificationInstance: specificationInstance, catalogIdSelectionList: catalogIdSelectionList, catalogIdsArray: catalogIdsArray ]
		}
	}


	def update(){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->

			if(params.name){

				specificationInstance.name = params.name

				def catalogSelectedIdsArray = []
				if(params.catalogIds){
					catalogSelectedIdsArray = params.catalogIds.split(',').collect{it as int}
				}

				if(!catalogSelectedIdsArray){
					flash.message = "You must assign at least one catalog to continue saving the specification"
					redirect(action: "edit", id: specificationInstance.id)
					return
				}

				specificationInstance.catalogs = null
				specificationInstance.catalog = null
				catalogSelectedIdsArray.eachWithIndex {  catalogId, index ->

					def catalog = Catalog.get(catalogId)
					if(catalog){
						if(index == 0){
							//add base catalog
							specificationInstance.catalog = catalog
						}
						specificationInstance.addToCatalogs(catalog)
						specificationInstance.save(flush:true)

						catalog.addToSpecifications(specificationInstance)
						catalog.save(flush:true)
					}

				}

				flash.message = "Successfully updated specification"

			}else{
				flash.message = "Name cannot be blank"
			}

			redirect(action:'edit', id : specificationInstance.id)

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

			redirect(action:'edit', id : specificationInstance.id)
		}
	}


	def edit_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			[ specificationOption : specificationOptionInstance ]
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

			redirect(action:"edit_option", id : specificationOptionInstance.id)
		}
	}



	def delete_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			def specificationInstance = specificationOptionInstance.specification;
			specificationOptionInstance.delete(flush:true)
			redirect(action: "edit", id: specificationInstance.id)
		}
	}



	def getCatalogIdSelectionList(catalogIdsArray){
		def catalogMenuString = "<ul class=\"catalog_list admin-catalog-selection\">"
		def toplevelCatalogs = Catalog.findAllByToplevel(true)
		toplevelCatalogs.each{ catalog ->
			def checked = ""
			if(catalogIdsArray.contains(catalog.id)){
				checked = "checked"
			}
			catalogMenuString += "<li><input type=\"checkbox\" id=\"checkbox_${catalog.id}\" class=\"catalog_checkbox\" data-id=\"${catalog.id}\" data-name=\"${catalog.name}\" ${checked}>&nbsp;${catalog.name}"
			if(catalog.subcatalogs){
				def subcatalogMenuString = getAllSubcatalogLists(catalog, catalogIdsArray)
				catalogMenuString += subcatalogMenuString
			}
			catalogMenuString += "</li>"
		}
		catalogMenuString += "</ul>"
	}

	def getAllSubcatalogLists(catalog, catalogIdsArray){
		def subcatalogsMenu = "<ul class=\"catalog_list admin-subcatalog-selection\">"
		catalog.subcatalogs.sort { it.id }
		catalog.subcatalogs.each{ subcatalog ->
			def checked = ""
			if(catalogIdsArray.contains(subcatalog.id)){
				checked = "checked"
			}
			subcatalogsMenu += "<li><input type=\"checkbox\" id=\"checkbox_${subcatalog.id}\" class=\"catalog_checkbox\" data-id=\"${subcatalog.id}\" data-name=\"${subcatalog.name}\" ${checked}>&nbsp;${subcatalog.name}"
			if(subcatalog.subcatalogs){
				subcatalogsMenu += getAllSubcatalogLists(subcatalog, catalogIdsArray)
			}
			subcatalogsMenu += "</li>"
		}
		subcatalogsMenu += "</ul>"

		return subcatalogsMenu
	}
}