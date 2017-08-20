package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account
import org.greenfield.Permission
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.ProductOption
import org.greenfield.Variant

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
				permissions : []
				catalogs : []
				products : []
				productOptions : []
				optionVariants : []
				specifications : []
				specificationOptions : []
				productSpecifications : []
				orders : []
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
			data['productOptions'] = productOptions
		}
		
		if(params.exportOptionVariants == "on"){
			def variants = Variant.list()
			println new JSON(variants)
			variants = formatVariants(variants)
			data['optionVariants'] = variants
		}
		
		def json = formatJson(data)
		InputStream is = new ByteArrayInputStream(json.getBytes());

		render(file: is, fileName: "greenfield-data.json")	
	}
	
	
	def formatVariants(unformattedVariants){
		def variants = []
		unformattedVariants.each() { it ->
			def variant = [:]
			variant['name'] = it.name
			variant['price'] = it.price
			variant['position'] = it.position
			variant['imageUrl'] = it.imageUrl
			variant['productOption'] = it.productOption.name
			variant['product'] = it.productOption.product.name
			variants.add(variant)
		}
		return variants
	}
	
	
	def formatProductOptions(unformattedProductOptions){
		def productOptions = []
		unformattedProductOptions.each(){ it ->
			def productOption = [:]
			productOption['name'] = it.name
			productOption['product'] = it.product.name
			productOptions.add(productOption)
		}
		return productOptions
	}
	
	
	def formatProducts(unformattedProducts){
		def products = []
		unformattedProducts.each(){ it ->
			def product = [:]
			/** fields
			name
			description
			quantity
			price
			imageUrl
			detailsImageUrl
			disabled
			length
			width
			height
			weight
			productNo
			dateCreated
			lastUpdated
			catalogs
			**/
			product['name'] = it.name
			product['description'] = it.description
			product['quantity'] = it.quantity
			product['price'] = it.price
			product['imageUrl'] = it.imageUrl
			product['detailsImageUrl'] = it.detailsImageUrl
			product['disabled'] = it.disabled
			product['length'] = it.length
			product['width'] = it.width
			product['height'] = it.height
			product['weight'] = it.weight
			product['productNo'] = it.productNo
			
			product['dateCreated'] = it.dateCreated
			product['lastUpdated'] = it.lastUpdated
			
			product['catalogs'] = []
			
			it.catalogs.each(){ itx ->
				println itx
				product['catalogs'].add(itx.name)
			}
			
			products.add(product)
		}
		
		return products
	}
	
	
	def formatAccounts(unformattedAccounts){
		def accounts = []
		
		unformattedAccounts.each(){ it ->
			def account = [:]
			//account['id'] = it.id
			account['email'] = it.email
		    account['username'] = it.username
		    account['password'] = it.password
			account['name'] = it.name
			
			account['address1'] = (it?.address1 ? it.address1 : "")
			account['address2'] = (it?.address2 ? it.address2 : "")
			account['city'] = (it?.city ? it.city : "")
			account['state'] = (it?.state ? it.state.id : "")
			account['zip'] = (it?.zip ? it.zip : "")

			account['phone'] = (it?.phone ? it.phone : "")
			
			account['ipAddress'] = (it?.ipAddress ? it.ipAddress : "")

			account['enabled'] = it.enabled
			account['accountExpired'] = it.accountExpired
			account['accountLocked'] = it.accountLocked
			account['passwordExpired'] = it.passwordExpired
			account['hasAdminRole'] = it.hasAdminRole
			account['addressVerified'] = it.addressVerified
			account['dateCreated'] = it.dateCreated
			account['lastUpdated'] = it.lastUpdated
			
			accounts.add(account)
		}
		
		return accounts
	}
	
	
	
	def formatPermissions(unformattedPermissions){
		def permissions = []
	
		unformattedPermissions.each(){ it ->
			def permission = [:]
			def account = it.account
			permission['account'] = account.username
			permission['permission'] = it.permission
			permissions.add(permission)		
		}
		
		return permissions
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
		data['name'] = catalog.name
		data['description'] = catalog?.description ? catalog.description : ""
		data['toplevel'] = catalog.toplevel
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