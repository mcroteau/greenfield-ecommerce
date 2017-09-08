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
import org.greenfield.log.LoginLog

import groovy.json.*
import groovy.json.JsonOutput
import grails.converters.JSON
import groovy.json.JsonOutput

import java.io.InputStream
import java.io.ByteArrayInputStream

import org.greenfield.ExportDataService

@Mixin(BaseController)
class ExportController {
	
	def exportDataService
	def missingUuidHelperService
	
	@Secured(['ROLE_ADMIN'])
	def resolve_missing_uuids(){
		println "resolve missing uuids..."
		missingUuidHelperService.correctMissingUuids()
		def json = [:]
		def shoppingCarts = ShoppingCart.list()
		render shoppingCarts as JSON
	}
	
 	@Secured(['ROLE_ADMIN'])
	def view_export(){
		//TODO:add numbers of data to be exported
	}

 	@Secured(['ROLE_ADMIN'])
	def export_data(){

		def json = exportDataService.export(params)
		InputStream is = new ByteArrayInputStream(json.getBytes());

		render(file: is, fileName: "greenfield-data.json")	
	}
	
}