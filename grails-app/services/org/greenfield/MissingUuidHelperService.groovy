package org.greenfield

import grails.transaction.Transactional

import org.greenfield.Account
import org.greenfield.AdditionalPhoto
import org.greenfield.Catalog
import org.greenfield.Country
import org.greenfield.Layout
import org.greenfield.Page
import org.greenfield.Permission
import org.greenfield.Product
import org.greenfield.ProductOption
import org.greenfield.ProductSpecification
import org.greenfield.Role
import org.greenfield.ShoppingCart
import org.greenfield.ShoppingCartItem
import org.greenfield.ShoppingCartItemOption
import org.greenfield.Specification
import org.greenfield.SpecificationOption
import org.greenfield.State
import org.greenfield.Transaction
import org.greenfield.Upload
import org.greenfield.Variant

import org.greenfield.log.CatalogViewLog
import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.SearchLog

import java.util.UUID

@Transactional
class MissingUuidHelperService {


    def correctMissingUuids() { 

		println "***************************************"
		println "***     Resolving Missing UUIDs     ***"
		println "***************************************"
		
		
		def accounts = Account.findAllByUuidIsNull()
		if(accounts){
			println "Accounts : ${accounts.size()}"
			accounts.each(){ ac ->
				ac.uuid = UUID.randomUUID().toString()
				ac.save(flush:true)
			}
		}
		
		def additionalPhotos = AdditionalPhoto.findAllByUuidIsNull()
		if(additionalPhotos){
			println "AdditionalPhotos : ${additionalPhotos.size()}"
			additionalPhotos.each(){ ap ->
				ap.uuid = UUID.randomUUID().toString()
				ap.save(flush:true)
			}
		}
		
		def catalogs = Catalog.findAllByUuidIsNull()
		if(catalogs){
			println "Catalogs : ${catalogs.size()}"
			catalogs.each(){ c ->
				c.uuid = UUID.randomUUID().toString()
				c.save(flush:true)
			}
		}
		
		def countries = Country.findAllByUuidIsNull()
		if(countries){
			println "Countries : ${countries.size()}"
			countries.each(){ cvl ->
				cvl.uuid = UUID.randomUUID().toString()
				cvl.save(flush:true)
			}
		}
		
		def layout = Layout.findAllByUuidIsNull()
		if(layout){
			println "Layout : ${layout.size()}"
			layout.each(){ l ->
				l.uuid = UUID.randomUUID().toString()
				l.save(flush:true)
			}
		}
		
		def pages = Page.findAllByUuidIsNull()
		if(pages){
			println "Pages : ${pages.size()}"
			pages.each(){ p ->
				p.uuid = UUID.randomUUID().toString()
				p.save(flush:true)
			}
		}
		
		/** TODO: revist, possibly remove unnecessary to have uuid like AccountRole
		permissions are created from scratch on import, no uuid needed
		def permissions = Permission.findAllByUuidIsNull()
		if(permissions){
			permissions.each(){ p ->
				p.uuid = UUID.randomUUID().toString()
				p.save(flush:true)
			}
		}
		**/
		
		def products = Product.findAllByUuidIsNull()
		if(products){
			println "Products : ${products.size()}"
			products.each(){ p ->
				p.uuid = UUID.randomUUID().toString()
				p.save(flush:true)
			}
		}
		
		def productOptions = ProductOption.findAllByUuidIsNull()
		if(productOptions){
			println "ProductOptions : ${productOptions.size()}"
			productOptions.each(){ po ->
				po.uuid = UUID.randomUUID().toString()
				po.save(flush:true)
			}
		}
		
		def productSpecifications = ProductSpecification.findAllByUuidIsNull()
		if(productSpecifications){
			println "ProductSpecifications : ${productSpecifications.size()}"
			productSpecifications.each(){ ps ->
				ps.uuid = UUID.randomUUID().toString()
				ps.save(flush:true)
			}
		}
		
		/** TODO: revisit, like Permission and AccountRole**/
		def roles = Role.findAllByUuidIsNull()
		if(roles){
			println "Roles : ${roles.size()}"
			roles.each(){ r ->
				r.uuid = UUID.randomUUID().toString()
				r.save(flush:true)
			}
		}
		
		def shoppingCarts = ShoppingCart.findAllByUuidIsNull()
		if(shoppingCarts){
			println "ShoppingCarts : ${shoppingCarts.size()}"
			shoppingCarts.each(){ sc ->
				sc.uuid = UUID.randomUUID().toString()
				sc.save(flush:true)
			}
		}
		
		def shoppingCartItems = ShoppingCartItem.findAllByUuidIsNull()
		if(shoppingCartItems){
			println "ShoppingCartItems : ${shoppingCartItems.size()}"
			shoppingCartItems.each(){ sci ->
				sci.uuid = UUID.randomUUID().toString()
				sci.save(flush:true)
			}
		}
		
		def shoppingCartItemOptions = ShoppingCartItemOption.findAllByUuidIsNull()
		if(shoppingCartItemOptions){
			println "ShoppingCartItemOptions : ${shoppingCartItemOptions.size()}"
			shoppingCartItemOptions.each(){ scio ->
				scio.uuid = UUID.randomUUID().toString()
				scio.save(flush:true)
			}
		}
		
		def specifications = Specification.findAllByUuidIsNull()
		if(specifications){
			println "Specifications : ${specifications.size()}"
			specifications.each(){ sp ->
				sp.uuid = UUID.randomUUID().toString()
				sp.save(flush:true)
			}
		}
		
		def specificationOptions = SpecificationOption.findAllByUuidIsNull()
		if(specificationOptions){
			println "SpecificationOptions : ${specificationOptions.size()}"
			specificationOptions.each(){ spo ->
				spo.uuid = UUID.randomUUID().toString()
				spo.save(flush:true)
			}
		}
		
		def states = State.findAllByUuidIsNull()
		if(states){
			println "States : ${states.size()}"
			states.each(){ s ->
				s.uuid = UUID.randomUUID().toString()
				s.save(flush:true)
			}
		}
		
		def transactions = Transaction.findAllByUuidIsNull()
		if(transactions){
			println "Orders : ${transactions.size()}"
			transactions.each(){ t ->
				t.uuid = UUID.randomUUID().toString()
				t.save(flush:true)
			}
		}
		
		def uploads = Upload.findAllByUuidIsNull()
		if(uploads){
			println "Uploads : ${uploads.size()}"
			uploads.each(){ u ->
				u.uuid = UUID.randomUUID().toString()
				u.save(flush:true)
			}
		}
		
		def variants = Variant.findAllByUuidIsNull()
		if(variants){
			println "Variants : ${variants.size()}"
			variants.each(){ v ->
				v.uuid = UUID.randomUUID().toString()
				v.save(flush:true)
			}
		}
		
		def catalogViewLogs = CatalogViewLog.findAllByUuidIsNull()
		if(catalogViewLogs){
			println "CatalogViewLogs : ${catalogViewLogs.size()}"
			catalogViewLogs.each(){ cvl ->
				cvl.uuid = UUID.randomUUID().toString()
				cvl.save(flush:true)
			}
		}
		
		def productViewLogs = ProductViewLog.findAllByUuidIsNull()
		if(productViewLogs){
			println "ProductViewLogs : ${productViewLogs.size()}"
			productViewLogs.each(){ pvl ->
				pvl.uuid = UUID.randomUUID().toString()
				pvl.save(flush:true)
			}
		}
		
		def pageViewLogs = PageViewLog.findAllByUuidIsNull()
		if(pageViewLogs){
			println "PageViewLogs : ${pageViewLogs.size()}"
			pageViewLogs.each(){ pvl ->
				pvl.uuid = UUID.randomUUID().toString()
				pvl.save(flush:true)
			}
		}
		
		def searchLogs = SearchLog.findAllByUuidIsNull()
		if(searchLogs){
			println "SearchLogs : ${searchLogs.size()}"
			searchLogs.each(){ sl ->
				sl.uuid = UUID.randomUUID().toString()
				sl.save(flush:true)
			}
		}
		
		//TODO: add LoginLog
		

		println "***************************************"
		println "***       UUID Check Complete       ***"
		println "***************************************"
		
		
	}
	
	

}
