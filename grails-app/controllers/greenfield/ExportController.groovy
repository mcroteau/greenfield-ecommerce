package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

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

import org.greenfield.Transaction
import org.greenfield.Page
import org.greenfield.Upload
import org.greenfield.Layout

import org.greenfield.log.CatalogViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.SearchLog

import groovy.json.*
import groovy.json.JsonOutput
import grails.converters.JSON
import groovy.json.JsonOutput

import java.io.InputStream
import java.io.ByteArrayInputStream

@Mixin(BaseController)
class ExportController {
	
 	@Secured(['ROLE_ADMIN'])
	def view_export(){
		//TODO:add numbers of data to be exported
	}

	
 	@Secured(['ROLE_ADMIN'])
	def export_data(){
		/**
			data : {
				accounts : []
				catalogs : []
				products : []
				productOptions : []
				optionVariants : []
				specifications : []
				specificationOptions : []
				productSpecifications : []
				orders : []
				shoppingCarts : []
				layout : []
				pages : []
			}
		**/
		
		def data = [:]
		
		if(params.exportAccounts == "on"){
			def accounts = Account.list()
			accounts = formatAccounts(accounts)
			data['accounts'] = accounts
		}
		
		/** TODO:might be unecessary, create permissions on account creation and transaction creation
		if(params.exportPermissions == "on"){
			def permissions = Permission.list()
			permissions = formatPermissions(permissions)
			data['permissions'] = permissions
		}
		**/
		
		if(params.exportCatalogs == "on"){
			def catalogs = Catalog.findAllByToplevel(true)
			catalogs = formatCatalogs(catalogs)
			data['catalogs'] = catalogs
		}
		
		if(params.exportProducts == "on"){
			def products = Product.list()
			products = formatProducts(products)
			data['products'] = products
		}
		
		if(params.exportProductOptions == "on"){
			
			def productOptions = ProductOption.list()
			productOptions = formatProductOptions(productOptions)
			
			def variants = Variant.list()
			variants = formatVariants(variants)
			
			data['productOptionData'] = [:]
			data['productOptionData']['productOptions'] = productOptions
			data['productOptionData']['optionVariants'] = variants
		}
		
		
		if(params.exportSpecifications == "on"){
			def specifications = Specification.list()
			specifications = formatSpecifications(specifications)
			
			def specificationOptions = SpecificationOption.list()
			specificationOptions = formatSpecificationOptions(specificationOptions)
			
			def productSpecifications = ProductSpecification.list()
			productSpecifications = formatProductSpecifications(productSpecifications)
			
			data['specificationData'] = [:]
			data['specificationData']['specifications'] = specifications
			data['specificationData']['specificationOptions'] = specificationOptions
			data['specificationData']['productSpecifications'] = productSpecifications
			
		}
		
		if(params.exportAdditionalPhotos == "on"){
			def additionalPhotos = AdditionalPhoto.list()
			additionalPhotos = formatAdditionalPhotos(additionalPhotos)
			data['additionalPhotos'] = additionalPhotos
		}
		
		
		if(params.exportShoppingCarts == "on"){
			def shoppingCarts = ShoppingCart.list()
			shoppingCarts = formatShoppingCarts(shoppingCarts)
			
			def shoppingCartItems = ShoppingCartItem.list()
			shoppingCartItems = formatShoppingCartItems(shoppingCartItems)
			
			def shoppingCartItemOptions = ShoppingCartItemOption.list()
			shoppingCartItemOptions = formatShoppingCartItemOptions(shoppingCartItemOptions)
			
			data['shoppingCartData'] = [:]
			data['shoppingCartData']['shoppingCarts'] = shoppingCarts
			data['shoppingCartData']['shoppingCartItems'] = shoppingCartItems
			data['shoppingCartData']['shoppingCartItemOptions'] = shoppingCartItemOptions
		
		}
		
		if(params.exportOrders == "on"){
			def transactions = Transaction.list()
			transactions = formatTransactions(transactions)
			data['orders'] = transactions
		}
		
		if(params.exportPages == "on"){
			def pages = Page.list()
			pages = formatPages(pages)
			data['pages'] = pages
		}
		
		if(params.exportUploads == "on"){
			def uploads = Upload.list()
			uploads = formatUploads(uploads)
			data['uploads'] = uploads
		}
		
		if(params.exportLayout == "on"){
			def layout = Layout.findByName("STORE_LAYOUT")
			layout = formatLayout(layout)
			data['layout'] = layout
		}
		
		if(params.exportLogs == "on"){
			def catalogViewLogs = CatalogViewLog.list()
			catalogViewLogs = formatCatalogViewLogs(catalogViewLogs)
			
			def productViewLogs = ProductViewLog.list()
			productViewLogs = formatProductViewLogs(productViewLogs)
			
			def pageViewLogs = PageViewLog.list()
			pageViewLogs = formatPageViewLogs(pageViewLogs)
			
			def searchLogs = SearchLog.list()
			searchLogs = formatSearchLogs(searchLogs)
			
			data['logs'] = [:]
			data['logs']['catalogViewLogs'] = catalogViewLogs
			data['logs']['productViewLogs'] = productViewLogs
			data['logs']['pageViewLogs'] = pageViewLogs
			data['logs']['searchLogs'] = searchLogs
		}
		
		
		def json = formatJson(data)
		InputStream is = new ByteArrayInputStream(json.getBytes());

		render(file: is, fileName: "greenfield-data.json")	
	}
	
	
	def formatSearchLogs(unformattedSearchLogs){
		def searchLogs = []
		
		unformattedSearchLogs.each(){ sl ->
			def searchLog = [:]
			
			searchLog['query'] = sl.query
			searchLog['account'] = sl?.account ? sl.account.uuid : null
			searchLog['dateCreated'] = sl.dateCreated
			searchLog['lastUpdated'] = sl.lastUpdated
			
			searchLogs.add(searchLog)
		}
		
		return searchLogs
	}
	
	
	def formatPageViewLogs(unformattedPageViewLogs){
		def pageViewLogs = []
		
		unformattedPageViewLogs.each(){ pvl ->
			def pageViewLog = [:]
			
			pageViewLog['page'] = pvl.page.uuid
			pageViewLog['account'] = pvl?.account ? pvl.account.uuid : null
			pageViewLog['dateCreated'] = pvl.dateCreated
			pageViewLog['lastUpdated'] = pvl.lastUpdated
			
			pageViewLogs.add(pageViewLog)
		}
		
		return pageViewLogs
	}
	
	
	def formatProductViewLogs(unformattedProductViewLogs){
		def productViewLogs = []
		
		unformattedProductViewLogs.each(){ pvl ->
			def productViewLog = [:]
			
			productViewLog['product'] = pvl.product.uuid
			productViewLog['account'] = pvl?.account ? pvl.account.uuid : null
			productViewLog['dateCreated'] = pvl.dateCreated
			productViewLog['lastUpdated'] = pvl.lastUpdated
			
			productViewLogs.add(productViewLog)
		}
		
		return productViewLogs
	}
	
	
	def formatCatalogViewLogs(unformattedCatalogViewLogs){
		def catalogViewLogs = []
		
		unformattedCatalogViewLogs.each(){ cvl ->
			def catalogViewLog = [:]
			
			catalogViewLog['catalog'] = cvl.catalog.uuid
			catalogViewLog['account'] = cvl?.account ? cvl.account.uuid : null
			catalogViewLog['dateCreated'] = cvl.dateCreated
			catalogViewLog['lastUpdated'] = cvl.lastUpdated
			
			catalogViewLogs.add(catalogViewLog)
		}
		
		return catalogViewLogs
	}
	
	
	def formatLayout(l){
		def layout = [:]
		layout['name'] = l.name
		layout['content'] = l.content
		layout['dateCreated'] = l.dateCreated
		layout['lastUpdated'] = l.lastUpdated
		
		return layout
	}
	
	
	def formatUploads(unformattedUploads){
		def uploads = []
		
		unformattedUploads.each(){ u ->
			def upload = [:]
			upload['url'] = u.url
			upload['dateCreated'] = u.dateCreated
			upload['lastUpdated'] = u.lastUpdated
			
			uploads.add(upload)
		}
		
		return uploads
	}
	
	
	
	def formatPages(unformattedPages){
		def pages = []
		
		unformattedPages.each(){ p ->
			def page = [:]
			page['uuid'] = p.uuid
			page['title'] = p.title
			page['content'] = p.content
			page['dateCreated'] = p.dateCreated
			page['lastUpdated'] = p.lastUpdated
			
			pages.add(page)
		}
		
		return pages
	}
	
	
	
	def formatTransactions(unformattedTransactions){
		def transactions = []
		
		unformattedTransactions.each(){ t ->
			def transaction = [:]
			
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
			
			transactions.add(transaction)
		}
		
		return transactions
	}
	
	
	def formatShoppingCartItemOptions(unformattedShoppingCartItemOptions){
		def shoppingCartItemOptions = []
		
		unformattedShoppingCartItemOptions.each(){ scio ->
			def shoppingCartItemOption = [:]
			shoppingCartItemOption['variant'] = scio.variant.uuid
			shoppingCartItemOption['shoppingCartItem'] = scio.shoppingCartItem.uuid
			
			shoppingCartItemOptions.add(shoppingCartItemOption)
		}
		
		return shoppingCartItemOptions
	}
	
	
	def formatShoppingCartItems(unformattedShoppingCartItems){
		def shoppingCartItems = []
		
		unformattedShoppingCartItems.each(){ sci ->
			def shoppingCartItem = [:]
			shoppingCartItem['uuid'] = sci.uuid
			shoppingCartItem['quantity'] = sci.quantity
			shoppingCartItem['product'] = sci.product.uuid
			shoppingCartItem['shoppingCart'] = sci.shoppingCart.uuid
			
			shoppingCartItems.add(shoppingCartItem)
		}
		
		return shoppingCartItems
	}
	
	
	
	def formatShoppingCarts(unformattedShoppingCarts){
		def shoppingCarts = []

		unformattedShoppingCarts.each(){ sc ->
			def shoppingCart = [:]
			shoppingCart['uuid'] = sc.uuid
			shoppingCart['status'] = sc.status
			shoppingCart['taxes'] = sc.taxes
			shoppingCart['shipping'] = sc.shipping
			shoppingCart['subtotal'] = sc.subtotal
			shoppingCart['total'] = sc.total
		
			shoppingCart['account'] = sc.account.uuid
		
		  	shoppingCart['dateCreated'] = sc.dateCreated
			shoppingCart['lastUpdated'] = sc.lastUpdated
	
			shoppingCart['shipmentId'] = sc.shipmentId
			shoppingCart['shipmentDays'] = sc.shipmentDays
			shoppingCart['shipmentCarrier'] = sc.shipmentCarrier
			shoppingCart['shipmentService'] = sc.shipmentService
			shoppingCart['shipmentRateId'] = sc.shipmentRateId
		
			shoppingCarts.add(shoppingCart)
		}
		
		return shoppingCarts
	}
	
	
	def formatAdditionalPhotos(unformattedAdditionalPhotos){
		def additionalPhotos = []
		
		unformattedAdditionalPhotos.each(){ ap ->
			def additionalPhoto = [:]
			additionalPhoto['name'] = ap.name
			additionalPhoto['imageUrl'] = ap.imageUrl
			additionalPhoto['detailsImageUrl'] = ap.detailsImageUrl
	
			additionalPhoto['dateCreated'] = ap.dateCreated
			additionalPhoto['lastUpdated'] = ap.lastUpdated
	
			additionalPhoto['product'] = ap.product.uuid
			
			additionalPhotos.add(additionalPhoto)
		}
		
		return additionalPhotos
	}
	
	
	def formatProductSpecifications(unformattedProductSpecifications){
		def productSpecifications = []
		unformattedProductSpecifications.each(){ ps ->
			def productSpecification = [:]
			productSpecification['product'] = ps.product.uuid
			productSpecification['specificationOption'] = ps.specificationOption.uuid
			productSpecification['specification'] = ps.specification.uuid
			productSpecification['dateCreated'] = ps.dateCreated
			productSpecification['lastUpdated'] = ps.lastUpdated
			
			productSpecifications.add(productSpecification)
		}
		
		return productSpecifications
	}
	
	
	def formatSpecificationOptions(unformattedSpecificationOptions){
		def specificationOptions = []
		unformattedSpecificationOptions.each(){ so ->
			def specificationOption = [:]
			specificationOption['uuid'] = so.uuid
			specificationOption['name'] = so.name
		    specificationOption['position'] = so.position
			specificationOption['dateCreated'] = so.dateCreated
			specificationOption['lastUpdated'] = so.lastUpdated
			specificationOption['specification'] = so.specification.uuid
			
			specificationOptions.add(specificationOption)
		}
	}
	
	
	def formatSpecifications(unformattedSpecifications){
		def specifications = []
		unformattedSpecifications.each(){ sp ->
			def specification = [:]
			if(sp.catalogs){
				specification['uuid'] = sp.uuid
				specification['name'] = sp.name
				specification['filterName'] = sp.filterName
		    	specification['position'] = sp.position
				specification['dateCreated'] = sp.dateCreated
				specification['lastUpdated'] = sp.lastUpdated
				
				specification['catalogs'] = []
				
				sp.catalogs.each(){ c ->
					specification['catalogs'].add(c.uuid)
				}
				
				specifications.add(specification)
			}			
		}
		
		return specifications
	}
	
	
	
	def formatVariants(unformattedVariants){
		def variants = []
		unformattedVariants.each() { v ->
			def variant = [:]
			variant['uuid'] = v.uuid
			variant['name'] = v.name
			variant['price'] = v.price
			variant['position'] = v.position
			variant['imageUrl'] = v.imageUrl
			variant['productOption'] = v.productOption.uuid
			variants.add(variant)
		}
		return variants
	}
	
	
	def formatProductOptions(unformattedProductOptions){
		def productOptions = []
		unformattedProductOptions.each(){ po ->
			def productOption = [:]
			
			productOption['uuid'] = po.uuid
			productOption['name'] = po.name
			productOption['product'] = po.product.uuid
			
			productOptions.add(productOption)
		}
		return productOptions
	}
	
	
	def formatProducts(unformattedProducts){
		def products = []
		unformattedProducts.each(){ p ->
			def product = [:]

			product['uuid'] = p.uuid
			product['name'] = p.name
			product['description'] = p.description
			product['quantity'] = p.quantity
			product['price'] = p.price
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
				//def catalog = [:]
				//catalog['uuid'] = c.uuid
				//catalog['name'] = c.name
				//product['catalogs'].add(catalog)
				product['catalogs'].add(c.uuid)
			}
			
			products.add(product)
		}
		
		return products
	}
	
	
	def formatAccounts(unformattedAccounts){
		def accounts = []
		
		unformattedAccounts.each(){ ac ->
			def account = [:]
			//account['id'] = it.id
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
			
			accounts.add(account)
		}
		
		return accounts
	}
	
	
	
	def formatCatalogs(unformattedCatalogs){
		def catalogs = []
		
		unformattedCatalogs.each(){ catalog ->
			def data = populateCatalogData(catalog)
			catalogs.add(data)
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
	
	
	def formatJson(data){
		def jsonData = new JSON(data)
		def jsonString = jsonData.toString()
		def jsonOutput = new JsonOutput()
		return jsonOutput.prettyPrint(jsonString)
	}
	
}