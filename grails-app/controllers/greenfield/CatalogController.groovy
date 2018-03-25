package greenfield

import org.springframework.dao.DataIntegrityViolationException
import greenfield.common.BaseController

import org.greenfield.log.CatalogViewLog

import org.greenfield.Account
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.Specification

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class CatalogController {

	def numberSpaces = 1
	def applicationService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]


    @Secured(['ROLE_ADMIN'])
    def index() {
        redirect(action: "list", params: params)
    }


    @Secured(['permitAll'])
	def products(Long id){
		def catalogInstance = Catalog.findById(id)
		if(!catalogInstance){
			flash.message = "Catalog cannot be found"
			redirect(uri: '/',)	
			return
		}

		def max = 12
		def offset = params.offset ? params.offset : 0
		def sort = params.sort ? params.sort : "name"
		//def desc = 
		
		if(!catalogInstance){	
			flash.message = "Unable to find Catalog..."
			redirect(controller : 'store', action : 'index')
		}
		
		def products
		def productsTotal

		def populateProductsAndTotal = {
			productsTotal = Product.createCriteria().count{
				and{
					eq("disabled", false)
					gt("quantity", 0)
					catalogs {
			    		idEq(id)
			 		}
				}
			}
			/**
			install_name_tool -change libdb2.dylib /Users/mcroteau/Library/Python/2.7/lib/python/site-packages/clidriver/lib/libdb2.dylib ibm_db.so
			
			install_name_tool -change libdb2.dylib /usr/local/lib/python3.6/site-packages/ibm_db-2.0.8-py3.6-macosx-10.11-x86_64.egg/clidriver/lib ibm_db.so
			
			**/
			
			products = Product.createCriteria().list(max: max, offset: offset, sort: sort){
				and{
					eq("disabled", false)
					gt("quantity", 0)
					catalogs {
			    		idEq(id)
			 		}
				}
			}
		}

		if(isFilterRequest(params)){

			//println "*** filter request ***"
            def combinations = []

            Collection<?> keys = params.keySet()
            for (Object param : keys) {

                def optionIdsString = params.get(param)
                if(param != "action" &&
                        param != "controller" &&
                        param != "id" &&
                        param != "offset" &&
                        param != "max" &&
                        optionIdsString){
                    def optionIds = optionIdsString.split("-")
                    combinations.push(optionIds)
                }
            }

            combinations = combinations.combinations()
            combinations.unique()

            if(combinations){
            	//println "combinations : " + combinations
	            products = []
	            def countTotal = 0
	            combinations.each { ids ->
	                def ps = Product.executeQuery '''
	                    select prd from Product as prd
	                        join prd.productSpecifications as sp
	                        join sp.specificationOption as opt
	                        join prd.catalogs as c
	                    where c.id = :catalogId
	                    and opt.id in :ids
	                    group by prd
	                    having count(prd) = :count
	                    and
	                    disabled = false
	                    and
	                    quantity > 0''', [ids: ids.collect { it.toLong() }, count: ids.size().toLong(), catalogId: id]

	                if(ps){
	                    products.addAll(ps)
	                }
	            }
            	
	        	productsTotal = products.size() ? products.size() : 0
	        	products = products.drop(offset.toInteger()).take(max.toInteger())
	        
	        }else{
	        	//println "*** no combinations " + id + " ***"
		        populateProductsAndTotal()
	        }

        }else{
        	//println "*** not filter request ***"
			populateProductsAndTotal()
			
		}
		
        logCatalogView(catalogInstance)
        products.sort{ it.name }
		
		[products : products,  productsTotal: productsTotal, catalogInstance: catalogInstance, offset : offset, max : max ]
	}




	def logCatalogView(catalogInstance){
		def catalogViewLog = new CatalogViewLog()
		catalogViewLog.catalog = catalogInstance
		
		def accountInstance
		if(principal?.username){
			accountInstance = Account.findByUsername(principal?.username)
			catalogViewLog.account = accountInstance
		}
		
		catalogViewLog.save(flush:true)
		
		if(accountInstance){
			accountInstance.catalogViews = CatalogViewLog.countByAccount(accountInstance)
			accountInstance.save(flush:true)
		}	
	}
	


    def isFilterRequest(params){
    	//TODO: resolved issue, everything was going to "isFilterRequest" 
    	//format must have been a new parameter that was added
    	//println "*******************************"
    	//println "params size = " + params.size()
    	//println params
    	//println "*******************************"
        if(!params || 
        		params.size() == 4 ||
                (params.size() == 6 && paginationParams(params))){
            return false
        }
        return true
    }


    def paginationParams(params){
        if(params.offset && params.max){
            return true
        }
        return false;
    }

	
	def catalog_products(String name){
		if(params.name){
			def catalog = Catalog.findByName(params.name)
			def products = Product.findAllByCatalogAndDisabled(catalog, false)
			request.products = products
			render view:'products'
		}
	}


	
	@Secured(['ROLE_ADMIN'])
    def list(Integer max) {
		authenticatedAdmin { adminAccount -> 
			
			def catalogInstanceList = Catalog.list([sort: "position", order: "asc"])
			def catalogsList = []
			
			catalogInstanceList.each { catalog ->
				def catalogData = [:]
				def catalogPath = ""
				if(catalog.parentCatalog){
					catalogPath = getFullCatalogPath(catalog)
				}else{
					catalogPath = "Top Level"
				}
				
				catalogData.id = catalog.id
				catalogData.path = catalogPath
				catalogData.name = catalog.name
				catalogData.position = catalog.position
				catalogData.productsCount = getCatalogProductsCount(catalog)
				catalogsList.add(catalogData)
			}

			
        	[ catalogsList: catalogsList ]
    	}
	}


	def getCatalogProductsCount(catalogInstance){
		def productsCount = Product.createCriteria().count{
			and{
				catalogs {
		    		idEq(catalogInstance.id)
		 		}
			}
		}
		return productsCount
	}
	
	
	
	def getFullCatalogPath(catalog){
		def path = new StringBuffer()
		path.append(catalog.name)
		if(catalog?.parentCatalog){
			path.insert(0, getFullCatalogPath(catalog.parentCatalog) + "&nbsp;&nbsp;&#xBB;&nbsp;&nbsp;")
		}
		return path.toString()
	}
	
	
	@Secured(['ROLE_ADMIN'])
    def create() {
		authenticatedAdmin { adminAccount -> 
			
			numberSpaces = 1
			def catalogOptions = getCatalogOptions()
			
	    	[ catalogInstance: new Catalog(params), catalogOptions: catalogOptions ]
    	}
	}



	
	@Secured(['ROLE_ADMIN'])	
    def show(Long id) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->	
			
			numberSpaces = 1
			def catalogOptions = getCatalogOptionsWithCatalog(catalogInstance)
			
    	    [ catalogInstance: catalogInstance, catalogOptions: catalogOptions ]			
		}
    }




	@Secured(['ROLE_ADMIN'])
    def edit(Long id) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->	
			
			numberSpaces = 1
			def catalogOptions = getCatalogOptionsWithCatalog(catalogInstance)
			
            def c = Specification.createCriteria()
            def specifications = c.list {
                catalogs {
                    idEq(catalogInstance.id)
                }
            }
    	    [ catalogInstance: catalogInstance, catalogOptions: catalogOptions, specifications: specifications ]
		}
    }
	
	
	@Secured(['ROLE_ADMIN'])
    def save() {
		authenticatedAdmin { adminAccount ->
    	    
			def catalogInstance = new Catalog(params)
			
			def parentCatalog
			if(params.location){
				catalogInstance.toplevel = false
				parentCatalog = Catalog.get(params.location)
				if(!parentCatalog){
					flash.message = "Something went wrong while saving the Catalog.  Please try again.  Be sure to select a valid location"
    	        	render(view: "create", model: [catalogInstance: catalogInstance])
    	        	return
				}
			}else{
				catalogInstance.toplevel = true
			}
			
    	    if (!catalogInstance.save(flush: true)) {
				flash.error = "Something went wrong while saving catalog. Please try again."
    	        render(view: "create", model: [catalogInstance: catalogInstance])
    	        return
    	    }
    		
			
			if(parentCatalog){
				catalogInstance.parentCatalog = parentCatalog
				catalogInstance.save(flush:true)
				parentCatalog.addToSubcatalogs(catalogInstance)
				parentCatalog.save(flush:true)
			}		
    	    
			
			flash.message = "Successfully saved catalog"
    	    redirect(action: "show", id: catalogInstance.id)
    	}
    }
	
	
	

	@Secured(['ROLE_ADMIN'])
    def update(Long id, Long version) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->	
    	
        	catalogInstance.properties = params
			
			def parentCatalog
			if(params.location){
				catalogInstance.toplevel = false
				parentCatalog = Catalog.get(params.location)
				if(!parentCatalog){
					flash.message = "Something went wrong while saving the Catalog.  Please try again.  Be sure to select a valid location"
    	        	render(view: "create", model: [catalogInstance: catalogInstance])
    	        	return
				}
				catalogInstance.parentCatalog = parentCatalog
				catalogInstance.save(flush:true)
				parentCatalog.addToSubcatalogs(catalogInstance)
				parentCatalog.save(flush:true)
			}else{
				catalogInstance.toplevel = true
				if(catalogInstance.parentCatalog){
					parentCatalog = catalogInstance.parentCatalog
					parentCatalog.removeFromSubcatalogs(catalogInstance)
					parentCatalog.save(flush:true)
					catalogInstance.parentCatalog = null
				}
			}
			
        	
        	if (!catalogInstance.save(flush: true)) {
				flash.error = "Something went wrong while trying to update. Please try again"
        	    render(view: "edit", model: [catalogInstance: catalogInstance])
        	    return
        	}
        	flash.message = "Successfully updated product"
        	redirect(action: "show", id: catalogInstance.id)
		}
	}
	



	@Secured(['ROLE_ADMIN'])
    def delete(Long id) {
		authenticatedAdminCatalog { adminAccount, catalogInstance ->
    	    try {
				CatalogViewLog.executeUpdate("delete CatalogViewLog c where c.catalog = :catalog", [catalog : catalogInstance])
                
                //delete all specifications, specification options and product specifications
			    def specifications = Specification.createCriteria().list{
                    catalogs{
                        idEq(catalogInstance.id)
                    }
                }
                
                specifications.each{ specification ->
                    specification.removeFromCatalogs(catalogInstance)
                    specification.save(flush:true)
                }
                
    	        catalogInstance.delete(flush: true)
    	        flash.message = "Successfully deleted the catalog"
    	        redirect(action: "list")
                
    	    }catch (DataIntegrityViolationException e) {
    	        flash.message = "Something went wrong while deleting catalog. <br/>Please make sure no products currently belong to this catalog. <br/>In addition make sure this catalog has no subcatalogs"
				e.printStackTrace()
    	        redirect(action: "show", id: id)
    	    }
    	}
	}
	
	
    @Secured(['ROLE_ADMIN'])
    def manage_positions(Long id){
		authenticatedAdminCatalog { adminAccount, catalogInstance ->
			def catalogs = []
			if(catalogInstance.toplevel){
				catalogs = Catalog.findAllByToplevel(true)
			}else{
				def parentCatalog = catalogInstance.parentCatalog
				catalogs = parentCatalog.subcatalogs
				catalogs.sort{ it.position }
			}
			[ catalogInstance: catalogInstance, catalogs: catalogs ]
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
			
			positions.eachWithIndex(){ catalogId, position ->
				def catalog = Catalog.get(catalogId)
				catalog.position = position
				catalog.save(flush:true)
			}
			
			flash.message = "Successfully updated positions"
			redirect(action : 'manage_positions', id : params.catalogId)
        }
    }



    @Secured(['ROLE_ADMIN'])
	def menu_view(){
		def catalogMenuString = "<ul class=\"admin-catalog-menu\">"
		def toplevelCatalogs = Catalog.findAllByToplevel(true)
		toplevelCatalogs.each{ catalog ->
			def count = getCatalogProductsCount(catalog)
			catalogMenuString += "<li>${catalog.name}"
			catalogMenuString += "<span class=\"catalog-admin-links\">"
			catalogMenuString += "(<a href=\"/${applicationService.getContextName()}/catalog/manage_positions/${catalog.id}\">Update Position</a>"
			catalogMenuString += "&nbsp;|&nbsp;"
			catalogMenuString += "<a href=\"/${applicationService.getContextName()}/catalog/edit/${catalog.id}\">Edit</a>)"
			catalogMenuString += "&nbsp;&nbsp;&nbsp;&nbsp;${count} products</span>"
			if(catalog.subcatalogs){
				def subcatalogMenuString = getAllSubcatalogLists(catalog)
				catalogMenuString += subcatalogMenuString
			}
			catalogMenuString += "</li>"
		}
		catalogMenuString += "</ul>"
		
		[ catalogMenuString : catalogMenuString ]
	}
	
    
	
	def getAllSubcatalogLists(catalog){
		def subcatalogsMenu = "<ul class=\"admin-subcatalog-menu\">"
		catalog.subcatalogs.sort { it.id }
		catalog.subcatalogs.each{ subcatalog ->
			def count = getCatalogProductsCount(subcatalog)
			subcatalogsMenu += "<li>${subcatalog.name}"
			subcatalogsMenu += "<span class=\"catalog-admin-links\">"
			subcatalogsMenu += "(<a href=\"/${applicationService.getContextName()}/catalog/manage_positions/${subcatalog.id}\">Update Position</a>"
			subcatalogsMenu += "&nbsp;|&nbsp;"
			subcatalogsMenu += "<a href=\"/${applicationService.getContextName()}/catalog/edit/${subcatalog.id}\">Edit</a>)"
			subcatalogsMenu += "&nbsp;&nbsp;&nbsp;&nbsp;${count} products</span>"
			if(subcatalog.subcatalogs){
				subcatalogsMenu += getAllSubcatalogLists(subcatalog)
			}
			subcatalogsMenu += "</li>"
		}
		subcatalogsMenu += "</ul>"
		
		return subcatalogsMenu
	}
    
	
    
	def getCatalogOptionsWithCatalog(catalogInstance){
		def catalogOptions = ""
		def toplevelCatalogs = Catalog.findAllByToplevel(true)
		toplevelCatalogs.each{ catalog ->
			if(catalogInstance != catalog){
				catalogOptions += "<option value=\"${catalog.id}\">${catalog.name}</option>"
				if(catalog.subcatalogs){
					def optionsString = getAllSubcatalogOptions(catalog)
					catalogOptions += optionsString
				}
			}
		}
		return catalogOptions
	}
	
	
	
	def getCatalogOptions(){
		def catalogOptions = ""
		def toplevelCatalogs = Catalog.findAllByToplevel(true)
		toplevelCatalogs.each{ catalog ->
			catalogOptions += "<option value=\"${catalog.id}\">${catalog.name}</option>"
			if(catalog.subcatalogs){
				def optionsString = getAllSubcatalogOptions(catalog)
				catalogOptions += optionsString
			}
		}
		return catalogOptions
	}
	
	
	
	def getAllSubcatalogOptions(catalog){
		def subcatalogs = ""
		catalog.subcatalogs.each{ subcatalog ->
			def spaceString = ""
			for(def m = 0; m < numberSpaces; m++){
				spaceString += "|&nbsp;&nbsp;&nbsp;&nbsp;"
			}
			subcatalogs += "<option value=\"${subcatalog.id}\">${spaceString}${subcatalog.name}</option>"
			if(subcatalog.subcatalogs){
				numberSpaces++
				def subOptionsString = getAllSubcatalogOptions(subcatalog)
				subcatalogs += subOptionsString
			}
		}
		
		if(numberSpaces > 1){
			--numberSpaces
		}
		return subcatalogs
	}
	
	
}
