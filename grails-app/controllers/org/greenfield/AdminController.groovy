package org.greenfield

import org.greenfield.BaseController
import grails.converters.*
import java.util.GregorianCalendar

import org.greenfield.common.ShoppingCartStatus

import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.CatalogViewLog
import org.greenfield.log.SearchLog


@Mixin(BaseController)
class AdminController {

    def index() { 
		authenticatedAdmin { adminAccount ->
		
			def startDate 
			def endDate
			
			if(params.startDate && params.endDate){
				println "****   parsing dates   *****"
				try{
					startDate = Date.parse("MM/dd/yyyy", params.startDate)
					endDate = Date.parse("MM/dd/yyyy", params.endDate)
				}catch (Exception e){
					flash.message = "Incorrect date format, please specify dates as mm/dd/yyy"
					startDate = new Date() - 30
					endDate = new Date()
				}
				
				if(!startDate || !endDate){
					println "*****    invalid dates    ****"
					flash.message = "Date Range must have correct dates formatted as mm/dd/yyyy"
					startDate = new Date() - 30
					endDate = new Date()
				}
				
				if(!endDate.after(startDate)){
					flash.message = "Start Date must be before End Date"
					startDate = new Date() - 30
					endDate = new Date()
				}
			
			}else if(params.allData){
				//set dates to null
				println "****    retrieve all data    ****"
				startDate = null
				endDate = null
			}else{
				println "****   set date range   ****"
				//last month
				startDate = new Date() - 30
				endDate = new Date() 
			}
			
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
			
			def formattedStartDate = "--"
			def formattedEndDate = "--"
			if(startDate && endDate){
				formattedStartDate = startDate.format('MM/dd/yyyy')
				formattedEndDate = endDate.format('MM/dd/yyyy')
			}
			
			[ adminAccount : adminAccount, storeStatistics: storeStatistics, startDate : formattedStartDate, endDate : formattedEndDate ]
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
		

		def sales = 0
		def orderCount = 0
		def averageOrder = 0
		def checkoutCarts = 0
		def shoppingCarts = 0
		def checkoutRate = 0
		
		def dates = ""
		def totals = ""
		
		if(startDate && endDate){
			shoppingCarts = ShoppingCart.countByDateCreatedBetween(startDate, endDate)
			checkoutCarts = ShoppingCart.countByStatusAndDateCreatedBetween(ShoppingCartStatus.TRANSACTION.description(), startDate, endDate)
		}else{
			shoppingCarts = ShoppingCart.count()
			checkoutCarts = ShoppingCart.countByStatus(ShoppingCartStatus.TRANSACTION.description())
		}
		
		if(checkoutCarts && shoppingCarts){
			checkoutRate = Math.round((checkoutCarts/shoppingCarts) * 100)
		}
		
		def orders
		if(startDate && endDate){
			orders = Transaction.findAllByOrderDateBetween(startDate, endDate)
		}else{
			orders = Transaction.findAll()
		}
		
		orders.eachWithIndex(){ order, index ->
			sales += order.total
		}
		
		
		orderCount = orders?.size()
		
		if(sales && orderCount){
			averageOrder = Math.round(sales/orderCount * 100)/100
		}
		
		
		stats.orders = orders
		stats.checkoutCarts = checkoutCarts
		stats.shoppingCarts = shoppingCarts
		stats.checkoutRate = checkoutRate
		
		stats.sales = sales
		stats.orderCount = orderCount
		stats.averageOrder = averageOrder
		
		
		return stats
	}
	
	
	
	
	
	
	def getProductViewsStatistics(startDate, endDate){
		def stats = [:]
		def productViewLogs
		if(startDate && endDate){
			productViewLogs = ProductViewLog.findAllByDateCreatedBetween(startDate, endDate)
		}else{
			productViewLogs = ProductViewLog.findAll()
		}
		
		println "ProductViewLogs : " + productViewLogs.size()
		
		productViewLogs?.each(){ productLog ->
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
	
	
	
	
	
	def getPageViewStatistics(startDate, endDate){
		def stats = [:]
		def pageViewLogs
		if(startDate && endDate){
			pageViewLogs = PageViewLog.findAllByDateCreatedBetween(startDate, endDate)
		}else{
			pageViewLogs = PageViewLog.findAll()
		}
		
		println "PageViewLogs : " + pageViewLogs.size()
		
		pageViewLogs?.each(){ pageLog ->
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
	
	
	
	
	
	
	
	
	def getCatalogViewsStatistics(startDate, endDate){
		def stats = [:]
		def catalogViewLogs
		if(startDate && endDate){
			catalogViewLogs = CatalogViewLog.findAllByDateCreatedBetween(startDate, endDate)
		}else{
			catalogViewLogs = CatalogViewLog.findAll()
		}
		
		catalogViewLogs?.each(){ catalogLog ->
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
			searchLogs = SearchLog.findAllByDateCreatedBetween(startDate, endDate)
		}else{
			searchLogs = SearchLog.findAll()
		}
		
		searchLogs?.each(){ searchLog ->
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
