package greenfield

import greenfield.common.BaseController

import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.Specification
import org.greenfield.SpecificationOption
import org.greenfield.ProductSpecification

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class SpecificationController {

    @Secured(['ROLE_ADMIN'])
    def product_specifications(Long id){
        authenticatedAdminSpecification { adminAccount, specificationInstance ->
            def max = params.max ? params.max : 10
            def offset = params.offset ? params.offset : 0

            def catalogHtmlOptions = getCatalogHtmlOptions(specificationInstance)
            println "catalogOptions"

            def catalogInstances = specificationInstance.catalogs.sort { it.id }
            
            def products = []
            def productsTotal = 0
            def catalogInstance = [:]

            if(params.catalogId){
                catalogInstance = Catalog.get(params.catalogId)

                products = Product.createCriteria().list(max: max, offset: params.offset){
                    catalogs {
                        idEq(catalogInstance.id)
                    }
                }

                productsTotal = Product.createCriteria().count(){
                    catalogs {
                        idEq(catalogInstance.id)
                    }
                }

            }

            println "products : ${productsTotal}"
            [ products: products, productsTotal: productsTotal, 
            catalogHtmlOptions: catalogHtmlOptions, specificationInstance: specificationInstance, 
            catalogInstances: catalogInstances ]
        }
    }

    

    @Secured(['ROLE_ADMIN'])
    def product_specifications_old(Long id){
        authenticatedAdminSpecification { adminAccount, specificationInstance ->
            
            def specificationOptions = SpecificationOption.findAllBySpecification(specificationInstance)
            if(!specificationOptions){
                flash.message = "You must define options for this specification before assigning products"
                redirect(action: 'edit', id: specificationInstance.id)
                return
            }
            
            def max = params.max ? params.max : 10
            def offset = params.offset ? params.offset : 0

            def catalogOptions = getCatalogHtmlOptions(specificationInstance)
            def products = []
            def productsTotal = 0
            def catalog = null
            def lowestPriceProduct = null
            def highestPriceProduct = null


            if(params.catalogId){
                catalog = Catalog.get(params.catalogId)

                if(catalog){

                    def specificationIds = specificationInstance.catalogs.collect{ it.id }
                    def subcatalogIds = setSubcatalogIds(catalog, [])

                    if(subcatalogIds){
                        def ids = []
                        subcatalogIds.each { it ->
                            if(specificationIds.contains(it)){
                                ids.push(it)
                            }
                        }

                        println "catalog ids : ${ids}"
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
                        
                        def rangeProducts = Product.createCriteria().list(){
                            catalogs{
                                'in'('id', ids)
                            }
                            order('price', 'asc')
                        }
                        
                        def lastProduct = rangeProducts.size() - 1 
                        lowestPriceProduct = rangeProducts.get(0)
                        highestPriceProduct = rangeProducts.get(lastProduct)
                    }

                }
            }
            
            [ specificationInstance: specificationInstance, catalogOptions: catalogOptions,  
            products: products, productsTotal: productsTotal, catalogInstance: catalog, 
            lowestPriceProduct: lowestPriceProduct, highestPriceProduct: highestPriceProduct ]
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


    @Secured(['ROLE_ADMIN'])
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
                        def existingProductSpec = ProductSpecification.findBySpecificationAndProduct(specificationInstance, product)


                        if(existingProductSpec){
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

            redirect(action: 'product_specifications', id: id, params: [catalogId: params.catalogId, max: max, offset: offset])

        }
    }


    @Secured(['ROLE_ADMIN'])
	def list(){
		authenticatedAdmin{ adminAccount ->
			def specifications = Specification.list()        
            [ specifications : specifications ]
		}
	}


    @Secured(['ROLE_ADMIN'])
	def create(){
		authenticatedAdmin { adminAccount ->
			def specificationInstance = new Specification(params)
			def catalogIdsArray = []
			if(specificationInstance?.catalogs){
				catalogIdsArray = specificationInstance?.catalogs.collect { it.id }
			}
			def catalogIdSelectionList = getCatalogIdSelectionList(catalogIdsArray)

			def name = params.name ? params.name : ""
			[ catalogIdSelectionList: catalogIdSelectionList, name : name ]
		}
	}
	
	
    @Secured(['ROLE_ADMIN'])
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

				def filterName = specification.name.replaceAll(" ", "_").toLowerCase()
				specification.filterName = filterName
                specification.position = 0
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
	
	
    @Secured(['ROLE_ADMIN'])
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


    @Secured(['ROLE_ADMIN'])
	def update(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->

			if(params.name){

				specificationInstance.name = params.name

				def filterName = specificationInstance.name.replaceAll(" ", "_").toLowerCase()
                specificationInstance.filterName = filterName

				def catalogSelectedIdsArray = []
				if(params.catalogIds){
					catalogSelectedIdsArray = params.catalogIds.split(',').collect{it as int}
				}

				if(!catalogSelectedIdsArray && !specificationInstance?.catalogs){
					flash.message = "You must assign at least one catalog to continue saving the specification"
					redirect(action: "edit", id: specificationInstance.id)
					return
				}

				if(specificationInstance.catalogs){
				    specificationInstance.catalogs.each { catalog ->
                        def products = Product.createCriteria().list{
                            catalogs{
                                idEq(catalog.id)
                            }
                        }

                        products.each { product ->
                            def productSpecifications = ProductSpecification.findAllByProductAndSpecification(product, specificationInstance)
                            productSpecifications.each { it ->
                                product.removeFromProductSpecifications(it)
                                it.delete(flush:true)
                            }
                        }
				    }
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


    @Secured(['ROLE_ADMIN'])
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


    @Secured(['ROLE_ADMIN'])
    def manage_positions(){
        authenticatedAdmin { adminAccount ->
            def specifications = Specification.list()
            [ specifications: specifications ]
        }
    }

    
    @Secured(['ROLE_ADMIN'])
    def update_positions(){    
        authenticatedAdmin { adminAccount ->
			if(!params.positions){
				flash.message = "Something went wrong while saving positions ..."
				redirect(action:'manage_positions')
				return
			}
			
			def positions = params.positions.split(',').collect{it as int}
			
			if(!positions){
				flash.message = "Something went wrong while saving positions ..."
				redirect(action:'manage_positions')
				return
			}
			
			positions.eachWithIndex(){ specificationId, position ->
				def specification = Specification.get(specificationId)
				specification.position = position
				specification.save(flush:true)
			}
			
			flash.message = "Successfully updated positions"
			redirect(action : 'manage_positions')
        }
    }


    @Secured(['ROLE_ADMIN'])
	def add_option(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
        	if(params.name){
				def existingOption = SpecificationOption.findByNameAndSpecification(params.name, specificationInstance)
                
                if(existingOption){
                    flash.optionMessage = "Option with the same name already exists"
                }else{
                    def option = new SpecificationOption()
				    option.name = params.name
				    option.specification = specificationInstance
                    option.position = 0
                    option.save(flush:true)
				    flash.message = "Successfully created option"
                }

			}else{
				flash.optionMessage = "Name cannot be blank"
			}
			redirect(action:'edit', id : specificationInstance.id)
		}
	}


    @Secured(['ROLE_ADMIN'])
	def edit_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			[ specificationOption : specificationOptionInstance ]
		}
	}



    @Secured(['ROLE_ADMIN'])
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



    @Secured(['ROLE_ADMIN'])
	def delete_option(){
		authenticatedAdminSpecificationOption { adminAccount, specificationOptionInstance ->
			def specificationInstance = specificationOptionInstance.specification;
            def productSpecifications = ProductSpecification.findAllBySpecificationAndSpecificationOption(specificationInstance, specificationOptionInstance)
            
            productSpecifications.each{
                def product = it.product
                product.removeFromProductSpecifications(it)
                it.delete(flush:true)
            }
			
            specificationOptionInstance.delete(flush:true)
			redirect(action: "edit", id: specificationInstance.id)
		}
	}



    @Secured(['ROLE_ADMIN'])
    def manage_option_positions(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
            def specificationOptions = SpecificationOption.findAllBySpecification(specificationInstance)
            specificationOptions = specificationOptions.sort{ it.name }
            specificationOptions = specificationOptions.sort{ it.position }
            [ specificationInstance: specificationInstance, specificationOptions: specificationOptions ]
        }
    }



    @Secured(['ROLE_ADMIN'])
    def update_option_positions(Long id){
		authenticatedAdminSpecification { adminAccount, specificationInstance ->
			if(!params.positions){
				flash.message = "Something went wrong while saving positions ..."
				redirect(action: 'manage_option_positions', id: id)
				return
			}
			
			def positions = params.positions.split(',').collect{it as int}
			
			if(!positions){
				flash.message = "Something went wrong while saving positions ..."
				redirect(action: 'manage_option_positions', id: id)
				return
			}
			
			positions.eachWithIndex(){ specificationOptionId, position ->
				def specificationOption = SpecificationOption.get(specificationOptionId)
				specificationOption.position = position
				specificationOption.save(flush:true)
			}
			
			flash.message = "Successfully updated positions"
			redirect(action: 'manage_option_positions', id: id)
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
    
    
    def getCatalogHtmlOptions(specificationInstance){
        def catalogOptions = ""
        def catalogs = specificationInstance.catalogs.sort{ it.name }
        catalogs.each{ catalog ->
            catalogOptions += "<option value=\"${catalog.id}\">${catalog.name}</option>"
        }
        return catalogOptions
    }


}