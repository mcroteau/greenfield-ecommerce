package org.greenfield

import org.greenfield.BaseController
import grails.converters.*

import org.greenfield.common.ShoppingCartStatus

import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.CatalogViewLog
import org.greenfield.log.SearchLog


@Mixin(BaseController)
class AdminController {

    def index() { 
		authenticatedAdmin { adminAccount ->
		
			def startDate = new Date() 
			def endDate = new Date() - 5
			
			def generalStats = getGeneralStats()
			def pageStats = getPageViewStatistics(startDate, endDate)
			def productStats = getProductViewsStatistics(startDate, endDate)
			def catalogStats = getCatalogViewsStatistics(startDate, endDate)
			def searchStats = getSearchQueriesStatistics(startDate, endDate)
			def salesStats = getSalesStatistics(startDate, endDate)
	
			def storeStatistics = [:]
			
			storeStatistics.generalStats = generalStats
			storeStatistics.pageStats = pageStats
			storeStatistics.productStats = productStats
			storeStatistics.catalogStats = catalogStats
			storeStatistics.searchStats = searchStats
			storeStatistics.salesStats = salesStats
			
			[ adminAccount : adminAccount, storeStatistics: storeStatistics ]
		}
	}
	
	def getGeneralStats(){
		def generalStats = [:]
		
		generalStats.products = Product.count()
		generalStats.outOfStock = Product.countByQuantity(0)
		generalStats.catalogs = Catalog.count()
		generalStats.customers = Account.count()
		generalStats.abandonedCarts = ShoppingCart.countByStatus(ShoppingCartStatus.ACTIVE.description())
		
		return generalStats
	}
	
	
	def getSalesStatistics(startDate, endDate){
		def stats = [:]
		
		def shoppingCarts
		def checkoutCarts
		if(startDate && endDate){
			shoppingCarts = ShoppingCart.countByDateCreatedBetween(endDate, startDate)
			checkoutCarts = ShoppingCart.countByStatusAndDateCreatedBetween(ShoppingCartStatus.TRANSACTION.description(), endDate, startDate)
		}else{
			shoppingCarts = ShoppingCart.count()
			checkoutCarts = ShoppingCart.countByStatus(ShoppingCartStatus.TRANSACTION.description())
		}
		def checkoutRate = Math.round((checkoutCarts/shoppingCarts) * 100)
		
		def orders
		if(startDate && endDate){
			orders = Transaction.findAllByOrderDateBetween(endDate, startDate)
		}else{
			orders = Transaction.findAll()
		}
		
		def sales = 0
		orders.each(){ order->
			sales += order.total
		}
		
		def orderCount = orders.size()
		def averageOrder = sales/orderCount
		
		
		stats.checkoutCarts = checkoutCarts
		stats.shoppingCarts = shoppingCarts
		stats.checkoutRate = checkoutRate
		
		stats.sales = sales
		stats.orderCount = orderCount
		stats.averageOrder = averageOrder
		
		return stats
	}
	
	
	
	
	def getPageViewStatistics(startDate, endDate){
		def stats = [:]
		def pageViewLogs
		if(startDate && endDate){
			pageViewLogs = PageViewLog.findAllByDateCreatedBetween(endDate, startDate)
		}else{
			pageViewLogs = PageViewLog.findAll()
		}
		
		println "PageViewLogs : " + pageViewLogs.size()
		
		pageViewLogs.each(){ pageLog ->
			if(stats[pageLog.page.id]){
				stats[pageLog.page.id].count += 1
			}else{
				stats[pageLog.page.id] = [:]
				stats[pageLog.page.id].count = 1
				stats[pageLog.page.id].page = pageLog.page.title
			}
		}
		
		return stats.sort(){ -it.value.count }			
	}
	
	
	
	
	
	def getProductViewsStatistics(startDate, endDate){
		def stats = [:]
		def productViewLogs
		if(startDate && endDate){
			productViewLogs = ProductViewLog.findAllByDateCreatedBetween(endDate, startDate)
		}else{
			productViewLogs = ProductViewLog.findAll()
		}
		
		println "ProductViewLogs : " + productViewLogs.size()
		
		productViewLogs.each(){ productLog ->
			if(stats[productLog.product.id]){
				stats[productLog.product.id].count += 1
			}else{
				stats[productLog.product.id] = [:]
				stats[productLog.product.id].count = 1
				stats[productLog.product.id].product = productLog.product.name
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	
	
	
	def getCatalogViewsStatistics(startDate, endDate){
		def stats = [:]
		def catalogViewLogs
		if(startDate && endDate){
			catalogViewLogs = CatalogViewLog.findAllByDateCreatedBetween(endDate, startDate)
		}else{
			catalogViewLogs = CatalogViewLog.findAll()
		}
		
		catalogViewLogs.each(){ catalogLog ->
			if(stats[catalogLog.catalog.id]){
				stats[catalogLog.catalog.id].count += 1
			}else{
				stats[catalogLog.catalog.id] = [:]
				stats[catalogLog.catalog.id].count = 1
				stats[catalogLog.catalog.id].catalog = catalogLog.catalog.name
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
	
	
	
	
	def getSearchQueriesStatistics(startDate, endDate){
		def stats = [:]
		def searchLogs
		if(startDate && endDate){
			searchLogs = SearchLog.findAllByDateCreatedBetween(endDate, startDate)
		}else{
			searchLogs = SearchLog.findAll()
		}
		
		searchLogs.each(){ searchLog ->
			if(stats[searchLog.query]){
				stats[searchLog.query].count += 1
			}else{
				stats[searchLog.query] = [:]
				stats[searchLog.query].count = 1
			}
		}
		
		return stats.sort(){ -it.value.count }
	}
	
}
