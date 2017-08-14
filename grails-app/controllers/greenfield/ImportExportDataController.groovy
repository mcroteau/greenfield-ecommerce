package greenfield

import greenfield.common.BaseController
import grails.plugin.springsecurity.annotation.Secured

import org.greenfield.Account
import org.greenfield.Permission
import org.greenfield.Catalog

@Mixin(BaseController)
class ImportExportDataController {
	
 	@Secured(['ROLE_ADMIN'])
	def export_data_view(){
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
				variants : []
				specifications : []
				specificationOptions : []
				productSpecifications : []
				orders : []
				layout : []
				pages : []
			}
		**/
		
		def accounts = Account.list()
		def permissions = Permission.list()
		def catalogs = Catalog.list()
		
		/**
		def products = Product.list()
		def productOptions = ProductOption.list()
		def variants = Variant.list()
		def specifications = Specification.list()
		def specificationOptions = SpecificationOption.list()
		**/
		
		redirect(action:'export_data_view')
	}
	
}