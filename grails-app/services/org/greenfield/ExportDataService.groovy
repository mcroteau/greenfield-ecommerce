package org.greenfield

import grails.transaction.Transactional
import org.greenfield.Account
import org.greenfield.Permission
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.ProductOption
import org.greenfield.Variant

import org.greenfield.Specification
import org.greenfield.SpecificationOption
import org.greenfield.ProductSpecification

import org.greenfield.AdditionalPhoto

import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCartItemOption
import org.greenfield.common.ShoppingCartStatus

import org.greenfield.Transaction
import org.greenfield.Page
import org.greenfield.Upload
import org.greenfield.Layout

import org.greenfield.log.CatalogViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.SearchLog
import org.greenfield.log.LoginLog

import groovy.json.*
import groovy.json.JsonOutput
import grails.converters.JSON
import groovy.json.JsonOutput


@Transactional
class ExportDataService {

	def export(params){
	
	
		def data = [:]
		
		if(params.exportAccounts == "on"){
			println "exporting accounts..."
			def accounts = Account.list()
			accounts = formatAccounts(accounts)
			data['accounts'] = accounts
		}
		
		if(params.exportCatalogs == "on"){
			println "exporting catalogs..."
			def catalogs = Catalog.findAllByToplevel(true)
			catalogs = formatCatalogs(catalogs)
			data['catalogs'] = catalogs
		}
		
		if(params.exportProducts == "on"){
			println "exporting products..."
			def products = Product.list()
			products = formatProducts(products)
			data['products'] = products
		}
		
		if(params.exportProductOptions == "on"){
			println "exporting product options..."
			def productOptions = ProductOption.list()
			productOptions = formatProductOptions(productOptions)
			data['productOptions'] = productOptions
		}
		
		if(params.exportSpecifications == "on"){
			println "exporting specifications..."
			def specifications = Specification.list()
			specifications = formatSpecifications(specifications)
			
			def productSpecifications = ProductSpecification.list()
			productSpecifications = formatProductSpecifications(productSpecifications)
			
			data['specifications'] = specifications
			data['productSpecifications'] = productSpecifications
			
		}
		
		if(params.exportAdditionalPhotos == "on"){
			println "exporting additional photos..."
			def additionalPhotos = AdditionalPhoto.list()
			additionalPhotos = formatAdditionalPhotos(additionalPhotos)
			data['additionalPhotos'] = additionalPhotos
		}
		
		if(params.exportShoppingCarts == "on"){
			println "exporting shopping carts..."
			def shoppingCarts = ShoppingCart.list()
			shoppingCarts = formatShoppingCarts(shoppingCarts)
			data['shoppingCarts'] = shoppingCarts
		}
		
		if(params.exportOrders == "on"){
			println "exporting orders..."
			def transactions = Transaction.list()
			transactions = formatTransactions(transactions)
			data['orders'] = transactions
		}
		
		if(params.exportPages == "on"){
			println "exporting pages..."
			def pages = Page.list()
			pages = formatPages(pages)
			data['pages'] = pages
		}
		
		if(params.exportUploads == "on"){
			println "exporting uploads..."
			def uploads = Upload.list()
			uploads = formatUploads(uploads)
			data['uploads'] = uploads
		}
		
		if(params.exportLayout == "on"){
			println "exporting layout..."
			def layout = Layout.findByName("STORE_LAYOUT")
			layout = formatLayout(layout)
			data['layout'] = layout
		}
		
		if(params.exportLogs == "on"){
			println "exporting logs..."
			def catalogViewLogs = CatalogViewLog.list()
			catalogViewLogs = formatCatalogViewLogs(catalogViewLogs)
			
			def productViewLogs = ProductViewLog.list()
			productViewLogs = formatProductViewLogs(productViewLogs)
			
			def pageViewLogs = PageViewLog.list()
			pageViewLogs = formatPageViewLogs(pageViewLogs)
			
			def searchLogs = SearchLog.list()
			searchLogs = formatSearchLogs(searchLogs)
			
			//TODO: add LoginLogs
			//def loginLogs = LoginLog.list()
			//loginLogs = formatLoginLogs(loginLogs)
			def total = CatalogViewLog.count() + ProductViewLog.count() + PageViewLog.count() + SearchLog.count()
			
			data['logs'] = [:]
			data['logs']['total'] = total
			data['logs']['catalogViewLogs'] = catalogViewLogs
			data['logs']['productViewLogs'] = productViewLogs
			data['logs']['pageViewLogs'] = pageViewLogs
			data['logs']['searchLogs'] = searchLogs
		}
		
		
		def json = formatJson(data)
		
		return json
	}
	
	
	
	def formatAccounts(unformattedAccounts){
		def accounts = [:]
		accounts['count'] = unformattedAccounts?.size()
		accounts['data'] = []
		
		unformattedAccounts.each(){ ac ->
			def account = [:]
			account['uuid'] = ac.uuid
			account['email'] = ac.email
		    account['username'] = ac.username
		    account['password'] = ac.password
			account['name'] = ac.name
			
			account['address1'] = (ac?.address1 ? ac.address1 : "")
			account['address2'] = (ac?.address2 ? ac.address2 : "")
			account['city'] = (ac?.city ? ac.city : "")
			account['state'] = (ac?.state ? ac.state.id : "")
			account['zip'] = (ac?.zip ? ac.zip : "")

			account['phone'] = (ac?.phone ? ac.phone : "")
			
			account['ipAddress'] = (ac?.ipAddress ? ac.ipAddress : "")

			account['enabled'] = ac.enabled
			account['accountExpired'] = ac.accountExpired
			account['accountLocked'] = ac.accountLocked
			account['passwordExpired'] = ac.passwordExpired
			account['hasAdminRole'] = ac.hasAdminRole
			account['addressVerified'] = ac.addressVerified
			account['dateCreated'] = ac.dateCreated
			account['lastUpdated'] = ac.lastUpdated
			
			accounts['data'].add(account)
		}
		
		return accounts
	}
	
	
	
	
	def formatCatalogs(unformattedCatalogs){
		def catalogs = [:]
		catalogs['count'] = Catalog.count()
		catalogs['data'] = []
		
		unformattedCatalogs.each(){ catalog ->
			def data = populateCatalogData(catalog)
			catalogs['data'].add(data)
		}
		
		return catalogs
	}
	
	
	def getSubcatalogs(subcatalogs, catalog){
		catalog.subcatalogs.each(){ itx ->
			def data = populateCatalogData(itx)
			subcatalogs.add(data)
		}
		return subcatalogs
	}
	
	
	def populateCatalogData(catalog){
		def data = [:]
		data['uuid'] = catalog.uuid
		data['name'] = catalog.name
		data['description'] = catalog?.description ? catalog.description : ""
		//TODO:might not need
		//data['toplevel'] = catalog.toplevel
		data['position'] = catalog.position
		data['parentCatalog'] = catalog.parentCatalog ? catalog.parentCatalog.name : null
		data['subcatalogs'] = []
		
		if(catalog.subcatalogs){
			data['subcatalogs'] = getSubcatalogs([], catalog)
		}
		
		return data		
	}
	
	
	
	def formatProducts(unformattedProducts){
		def products = [:]
		products['count'] = unformattedProducts?.size()
		products['data'] = []
		
		unformattedProducts.each(){ p ->
			def product = [:]

			product['uuid'] = p.uuid
			product['name'] = p.name
			product['description'] = p.description
			product['quantity'] = p.quantity
			product['price'] = p.price
			product['salesPrice'] = p.salesPrice
			product['imageUrl'] = p.imageUrl
			product['detailsImageUrl'] = p.detailsImageUrl
			product['disabled'] = p.disabled
			product['length'] = p.length
			product['width'] = p.width
			product['height'] = p.height
			product['weight'] = p.weight
			product['productNo'] = p.productNo
			
			product['dateCreated'] = p.dateCreated
			product['lastUpdated'] = p.lastUpdated
			
			product['catalogs'] = []
			
			p.catalogs.each(){ c ->
				product['catalogs'].add(c.uuid)
			}
			
			products['data'].add(product)
		}
		
		return products
	}
	
	
	

	def formatProductOptions(unformattedProductOptions){
		def productOptions = [:]
		productOptions['count'] = unformattedProductOptions?.size()
		productOptions['data'] = []
		
		unformattedProductOptions.each(){ po ->
			def productOption = [:]
			
			productOption['uuid'] = po.uuid
			productOption['name'] = po.name
			productOption['product'] = po.product.uuid
			
			productOption['variants'] = []
			
			if(po.variants){
				po.variants.each(){ v ->
					def variant = [:]
					variant['uuid'] = v.uuid
					variant['name'] = v.name
					variant['price'] = v.price
					variant['position'] = v.position
					variant['imageUrl'] = v.imageUrl
					variant['productOption'] = po.uuid
					
					productOption['variants'].add(variant)
				}
			}	
			productOptions['data'].add(productOption)
		}
		return productOptions
	}
	
	
	
	
	def formatSpecifications(unformattedSpecifications){
		def specifications = [:]
		specifications['count'] = unformattedSpecifications?.size()
		specifications['data'] = []
		
		unformattedSpecifications.each(){ sp ->
			
			def specification = [:]
			if(sp.catalogs){
				
				specification['uuid'] = sp.uuid
				specification['name'] = sp.name
				specification['filterName'] = sp.filterName
		    	specification['position'] = sp.position
				specification['dateCreated'] = sp.dateCreated
				specification['lastUpdated'] = sp.lastUpdated
				
				specification['specificationOptions'] = []
				
				if(sp.specificationOptions){
					sp.specificationOptions.each(){ spo ->
						
						def specificationOption = [:]
						specificationOption['uuid'] = spo.uuid
						specificationOption['name'] = spo.name
				    	specificationOption['position'] = spo.position
						specificationOption['dateCreated'] = spo.dateCreated
						specificationOption['lastUpdated'] = spo.lastUpdated
						specificationOption['specification'] = sp.uuid
			        	
						specification['specificationOptions'].add(specificationOption)
					}
				}
				
				specification['catalogs'] = []
				
				sp.catalogs.each(){ c ->
					specification['catalogs'].add(c.uuid)
				}
				
				specifications['data'].add(specification)
			}			
		}
		
		return specifications
	}
	
	
	def formatProductSpecifications(unformattedProductSpecifications){
		def productSpecifications = [:]
		productSpecifications['count'] = unformattedProductSpecifications?.size()
		productSpecifications['data'] = []
		
		unformattedProductSpecifications.each(){ ps ->
			def productSpecification = [:]
			productSpecification['uuid'] = ps.uuid
			productSpecification['product'] = ps.product.uuid
			productSpecification['specificationOption'] = ps.specificationOption.uuid
			productSpecification['specification'] = ps.specification.uuid
			productSpecification['dateCreated'] = ps.dateCreated
			productSpecification['lastUpdated'] = ps.lastUpdated
			
			productSpecifications['data'].add(productSpecification)
		}
		
		return productSpecifications
	}

	
	
	def formatAdditionalPhotos(unformattedAdditionalPhotos){
		def additionalPhotos = [:]
		additionalPhotos['count'] = unformattedAdditionalPhotos?.size()
		additionalPhotos['data'] = []
		
		unformattedAdditionalPhotos.each(){ ap ->
			def additionalPhoto = [:]
			additionalPhoto['uuid'] = ap.uuid
			additionalPhoto['name'] = ap.name
			additionalPhoto['imageUrl'] = ap.imageUrl
			additionalPhoto['detailsImageUrl'] = ap.detailsImageUrl
	
			additionalPhoto['dateCreated'] = ap.dateCreated
			additionalPhoto['lastUpdated'] = ap.lastUpdated
	
			additionalPhoto['product'] = ap.product.uuid
			
			additionalPhotos['data'].add(additionalPhoto)
		}
		
		return additionalPhotos
	}
	
	
	
	
	def formatShoppingCarts(unformattedShoppingCarts){
		def shoppingCarts = [:]
		shoppingCarts['count'] = unformattedShoppingCarts?.size()
		shoppingCarts['data'] = []
		
		unformattedShoppingCarts.each(){ sc ->
			
			def shoppingCart = [:]
			shoppingCart['uuid'] = sc.uuid
			shoppingCart['status'] = sc.status
			shoppingCart['taxes'] = sc.taxes
			shoppingCart['shipping'] = sc.shipping
			shoppingCart['subtotal'] = sc.subtotal
			shoppingCart['total'] = sc.total
		    
			if(sc.account){
				shoppingCart['account'] = sc.account.uuid
			}
			
			shoppingCart['shipmentId'] = sc.shipmentId
			shoppingCart['shipmentDays'] = sc.shipmentDays
			shoppingCart['shipmentCarrier'] = sc.shipmentCarrier
			shoppingCart['shipmentService'] = sc.shipmentService
			shoppingCart['shipmentRateId'] = sc.shipmentRateId
		    
		  	shoppingCart['dateCreated'] = sc.dateCreated
			shoppingCart['lastUpdated'] = sc.lastUpdated
	        	
			shoppingCart['shoppingCartItems'] = []	
			
			if(sc.shoppingCartItems){
				sc?.shoppingCartItems.each(){ sci ->
				
					def shoppingCartItem = [:]
					shoppingCartItem['uuid'] = sci.uuid
					shoppingCartItem['quantity'] = sci.quantity
					shoppingCartItem['product'] = sci.product.uuid

					if(sc.status == ShoppingCartStatus.TRANSACTION.description()){
						shoppingCartItem['regularPrice'] = sci.product.price
						if(sci.product.salesPrice){
							shoppingCartItem['checkoutPrice'] = sci.product.salesPrice
						}else{
							shoppingCartItem['checkoutPrice'] = sci.product.price
						}
					}

					shoppingCartItem['shoppingCart'] = sci.shoppingCart.uuid
					
					shoppingCartItem['shoppingCartItemOptions'] = []
					
					if(sci.shoppingCartItemOptions){
						sci.shoppingCartItemOptions.each(){ scio ->
							def shoppingCartItemOption = [:]
							shoppingCartItemOption['uuid'] = scio.uuid
							shoppingCartItemOption['variant'] = scio.variant.uuid
							shoppingCartItemOption['checkouPrice'] = scio.checkouPrice
							shoppingCartItemOption['shoppingCartItem'] = scio.shoppingCartItem.uuid
			    	
							shoppingCartItem['shoppingCartItemOptions'].add(shoppingCartItemOption)
						}
					}
			    	
					shoppingCart['shoppingCartItems'].add(shoppingCartItem)
				}
			}
			
			shoppingCarts['data'].add(shoppingCart)
	
		}
		
		return shoppingCarts
	}
	
	
	
	
	def formatTransactions(unformattedTransactions){
		def transactions = [:]
		transactions['count'] = unformattedTransactions?.size()
		transactions['data'] = []
		
		unformattedTransactions.each(){ t ->
			def transaction = [:]
			transaction['uuid'] = t.uuid
			transaction['total'] = t.total
			transaction['subtotal'] = t.subtotal
			transaction['shipping'] = t.shipping
			transaction['taxes'] = t.taxes
	
			transaction['status'] = t.status
			transaction['orderDate'] = t.orderDate
	
			transaction['chargeId'] = t.chargeId
			transaction['postageId'] = t.postageId
			transaction['postageUrl'] = t.postageUrl
	
			transaction['dateCreated'] = t.dateCreated
			transaction['lastUpdated'] = t.lastUpdated

			transaction['shipName'] = t.shipName
			transaction['shipAddress1'] = t.shipAddress1
			transaction['shipAddress2'] = t.shipAddress2
			transaction['shipCity'] = t.shipCity
			transaction['shipState'] = t?.shipState?.id
			transaction['shipZip'] = t.shipZip
	
			transaction['billName'] = t.billName
			transaction['billAddress1'] = t.billAddress1
			transaction['billAddress2'] = t.billAddress2
			transaction['billCity'] = t.billCity
			transaction['billState'] = t?.billState?.id
			transaction['billZip'] = t.billZip
	
			transaction['account'] = t.account.uuid
			transaction['shoppingCart'] = t.shoppingCart.uuid
			
			transactions['data'].add(transaction)
		}
		
		return transactions
	}
	
	
	
	
	def formatPages(unformattedPages){
		def pages = [:]
		pages['count'] = unformattedPages?.size()
		pages['data'] = []
		
		unformattedPages.each(){ p ->
			def page = [:]
			page['uuid'] = p.uuid
			page['title'] = p.title
			page['content'] = p.content
			page['dateCreated'] = p.dateCreated
			page['lastUpdated'] = p.lastUpdated
			
			pages['data'].add(page)
		}
		
		return pages
	}
	
	
	def formatUploads(unformattedUploads){
		def uploads = [:]
		uploads['count'] = unformattedUploads?.size()
		uploads['data'] = []
		
		unformattedUploads.each(){ u ->
			def upload = [:]
			upload['uuid'] = u.uuid
			upload['url'] = u.url
			upload['dateCreated'] = u.dateCreated
			upload['lastUpdated'] = u.lastUpdated
			
			uploads['data'].add(upload)
		}
		
		return uploads
	}
	
	
	
	def formatLayout(l){
		def layout = [:]
		layout['uuid'] = l.uuid
		layout['content'] = l.content
		layout['dateCreated'] = l.dateCreated
		layout['lastUpdated'] = l.lastUpdated
		
		return layout
	}
	
	
	def formatCatalogViewLogs(unformattedCatalogViewLogs){
		def catalogViewLogs = [:]
		catalogViewLogs['count'] = unformattedCatalogViewLogs?.size()
		catalogViewLogs['data'] = []
		
		unformattedCatalogViewLogs.each(){ cvl ->
			def catalogViewLog = [:]
			
			catalogViewLog['uuid'] = cvl.uuid
			catalogViewLog['catalog'] = cvl.catalog.uuid
			catalogViewLog['account'] = cvl?.account ? cvl.account.uuid : null
			catalogViewLog['dateCreated'] = cvl.dateCreated
			catalogViewLog['lastUpdated'] = cvl.lastUpdated
			
			catalogViewLogs['data'].add(catalogViewLog)
		}
		
		return catalogViewLogs
	}
	
	
	
	def formatProductViewLogs(unformattedProductViewLogs){
		def productViewLogs = [:]
		productViewLogs['count'] = unformattedProductViewLogs?.size()
		productViewLogs['data'] = []
		
		unformattedProductViewLogs.each(){ pvl ->
			def productViewLog = [:]
			
			productViewLog['uuid'] = pvl.uuid
			productViewLog['product'] = pvl.product.uuid
			productViewLog['account'] = pvl?.account ? pvl.account.uuid : null
			productViewLog['dateCreated'] = pvl.dateCreated
			productViewLog['lastUpdated'] = pvl.lastUpdated
			
			productViewLogs['data'].add(productViewLog)
		}
		
		return productViewLogs
	}
	
	
	def formatPageViewLogs(unformattedPageViewLogs){
		def pageViewLogs = [:]
		pageViewLogs['count'] = unformattedPageViewLogs?.size()
		pageViewLogs['data'] = []
		
		unformattedPageViewLogs.each(){ pvl ->
			def pageViewLog = [:]
			
			pageViewLog['uuid'] = pvl.uuid
			pageViewLog['page'] = pvl.page.uuid
			pageViewLog['account'] = pvl?.account ? pvl.account.uuid : null
			pageViewLog['dateCreated'] = pvl.dateCreated
			pageViewLog['lastUpdated'] = pvl.lastUpdated
			
			pageViewLogs['data'].add(pageViewLog)
		}
		
		return pageViewLogs
	}
	
	
	
	def formatSearchLogs(unformattedSearchLogs){
		def searchLogs = [:]
		searchLogs['count'] = unformattedSearchLogs?.size()
		searchLogs['data'] = []
		
		unformattedSearchLogs.each(){ sl ->
			def searchLog = [:]
			
			searchLog['uuid'] = sl.uuid
			searchLog['query'] = sl.query
			searchLog['account'] = sl?.account ? sl.account.uuid : null
			searchLog['dateCreated'] = sl.dateCreated
			searchLog['lastUpdated'] = sl.lastUpdated
			
			searchLogs['data'].add(searchLog)
		}
		
		return searchLogs
	}
	
	
	
	
	
	def formatJson(data){
		def jsonData = new JSON(data)
		def jsonString = jsonData.toString()
		def jsonOutput = new JsonOutput()
		return jsonOutput.prettyPrint(jsonString)
	}
	
}
