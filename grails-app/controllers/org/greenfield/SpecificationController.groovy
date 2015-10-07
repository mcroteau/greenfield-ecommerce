package org.greenfield

import org.greenfield.BaseController

@Mixin(BaseController)
class SpecificationController {

    def numberSpaces = 1


    def product_specifications(Long id){
        authenticatedAdminSpecification { adminAccount, specificationInstance ->
            def max = params.max ? params.max : 10
            def offset = params.offset ? params.offset : 0

            def catalogOptions = getCatalogOptions(specificationInstance)
            def products = []
            def productsTotal = 0
            def catalog = null


            if(params.catalogId){
                catalog = Catalog.get(params.catalogId)

                if(catalog){

                    def subcatalogIds = []
                    def specificationIds = specificationInstance.catalogs.collect{ it.id }
                    setSubcatalogIds(catalog, subcatalogIds)

                    println "subcatalog ids : " + subcatalogIds

                    if(subcatalogIds){
                        def ids = []
                        subcatalogIds.each { it ->
                            if(specificationIds.contains(it)){
                                ids.push(it)
                            }
                        }

                        println "ids : " + ids

                        productsTotal = Product.createCriteria().count{
                            catalogs{
                                'in'('id', ids)
                            }
                        }

                        products = Product.createCriteria().list(max: max, offset: offset){
                            catalogs{
                                'in'('id', ids)
                            }
                        }
                    }

                }
            }
            //println "products : " + products
            [ specificationInstance: specificationInstance, catalogOptions: catalogOptions, products: products, productsTotal: productsTotal, catalogInstance: catalog ]
        }
    }


    def setSubcatalogIds(catalog, subcatalogIds){
        if(catalog.subcatalogs){
            catalog.subcatalogs.each{ it ->
                if(it.subcatalogs){
                    setSubcatalogIds(it, subcatalogIds)
                }else{
                    subcatalogIds.push(it.id)
                }
            }
        }else{
            subcatalogIds.push(catalog.id)
        }
    }


    def set_product_specifications(Long id){
        authenticatedAdminSpecification { adminAccount, specificationInstance ->

            def max = params.max ? params.max : 10
            def offset = params.offset ? params.offset : 0

            if(params.productSpecifications){

                def specifications = params.productSpecifications.split(",")

                specifications.each { specification ->
                    def details = specification.split("-")

                    def productId = details.getAt(0)
                    def optionId = details.getAt(1)

                    def option = [:]
                    def product = Product.get(productId)

                    if(optionId == "NONE" && product){
                        println "\n********** equals NONE **********"
                        def existingProductSpec = ProductSpecification.findBySpecificationAndProduct(specificationInstance, product)


                        if(existingProductSpec){
                            println "********** existingProductSpec **********"
                            println "existing product spec : " + product.name + " : " + existingProductSpec.specificationOption.name
                            println "\n"
                            product.removeFromProductSpecifications(existingProductSpec)
                            existingProductSpec.delete(flush:true)
                        }
                    }else{
                        option = SpecificationOption.get(optionId)
                    }

                    if(option && product) {
                        def existingProductSpecification = ProductSpecification.findBySpecificationAndProduct(specificationInstance, product)

                        if (existingProductSpecification) {
                            product.removeFromProductSpecifications(existingProductSpecification)
                            existingProductSpecification.delete(flush:true)
                        }

                        def productSpecification = new ProductSpecification()
                        productSpecification.specification = specificationInstance
                        productSpecification.specificationOption = option
                        productSpecification.product = product
                        productSpecification.save(flush: true)

                        product.addToProductSpecifications(productSpecification)
                        product.save(flush: true)

                    }
                }

                flash.message = "Successfully set product specifications"

            }else{
                flash.message = "No changes were made"
            }
            println "*********************"
            println "product specifications : " + ProductSpecification.count()
            println "*********************"
            redirect(action: 'product_specifications', id: id, params: [catalogId: params.catalogId, max: max, offset: offset])

        }
    }



	def list(){
		authenticatedAdmin{ adminAccount ->
			def specifications = Specification.list()
			[ specifications : specifications ]
		}
	}


	def create(){
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

				def searchName = specification.name.replaceAll(" ", "_").toLowerCase()
				specification.searchName = searchName
                specification.save(flush:true)

				def catalogSelectedIdsArray = params.catalogIds.split(',').collect{it as int}

				if(!catalogSelectedIdsArray){
					flash.error = "You must assign at least one catalog to continue saving the specification"
					redirect(action: "create" )
					return
				}

				specification.catalogs = null

				catalogSelectedIdsArray.eachWithIndex {  catalogId, index ->

					def catalog = Catalog.get(catalogId)
					if(catalog){
						specification.addToCatalogs(catalog)
						specification.save(flush:true)
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


	def update(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->

			if(params.name){

				specificationInstance.name = params.name

				def searchName = specificationInstance.name.replaceAll(" ", "_").toLowerCase()
                specificationInstance.searchName = searchName

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

				catalogSelectedIdsArray.eachWithIndex {  catalogId, index ->
					def catalog = Catalog.get(catalogId)
					if(catalog){
						specificationInstance.addToCatalogs(catalog)
						specificationInstance.save(flush:true)
					}
				}

				flash.message = "Successfully updated specification"

			}else{
				flash.message = "Name cannot be blank"
			}

			redirect(action:'edit', id: specificationInstance.id)

		}
	}


    def delete(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
            try{
                def productSpecifications = ProductSpecification.findAllBySpecification(specificationInstance)
                if(productSpecifications){
                    productSpecifications.each{ productSpecification ->
                        def product = productSpecification.product
                        product.removeFromProductSpecifications(productSpecification)
                        productSpecification.delete(flush:true)
                    }
                }

                specificationInstance.delete(flush:true)
                flash.message = "Successfully deleted specification"
                redirect(action: 'list')
            }catch(Exception e){
                e.printStackTrace()
                flash.message = "Something went wrong while deleting specification"
                redirect(action:'edit', id: specificationInstance.id)
            }
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


    def getCatalogOptions(specificationInstance){
        def catalogOptions = ""
        def catalogs = specificationInstance.catalogs.sort{ it.name }
        catalogs.each{ catalog ->
            catalogOptions += "<option value=\"${catalog.id}\">${catalog.name}</option>"
        }
        return catalogOptions
    }


    ////TODO: consider deleting if use different method
    ////TODO: move to utlities class
    //def getCatalogOptionsOriginal(){
    //    def catalogOptions = ""
    //    def toplevelCatalogs = Catalog.findAllByToplevel(true)
    //    toplevelCatalogs.each{ catalog ->
    //        catalogOptions += "<option value=\"${catalog.id}\">${catalog.name}</option>"
    //        if(catalog.subcatalogs){
    //            def optionsString = getAllSubcatalogOptions(catalog)
    //            catalogOptions += optionsString
    //        }
    // //   }
    //    return catalogOptions
    //}
//
//
    ////TODO: consider deleting if use different method
    ////TODO: move to utlities class
    //def getAllSubcatalogOptions(catalog){
    //    def subcatalogs = ""
    //    catalog.subcatalogs.each{ subcatalog ->
    //        def spaceString = ""
    //        for(def m = 0; m < numberSpaces; m++){
    //            spaceString += "|&nbsp;&nbsp;&nbsp;&nbsp;"
    //        }
    //        subcatalogs += "<option value=\"${subcatalog.id}\">${spaceString}${subcatalog.name}</option>"
    //        if(subcatalog.subcatalogs){
    //            numberSpaces++
    //            def subOptionsString = getAllSubcatalogOptions(subcatalog)
    //            subcatalogs += subOptionsString
    //        }
    //    }
//
    //    if(numberSpaces > 1){
    //        --numberSpaces
    //    }
    //    return subcatalogs
    //}

}