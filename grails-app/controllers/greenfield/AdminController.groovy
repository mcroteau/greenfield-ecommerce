package greenfield

import greenfield.common.BaseController
import grails.converters.*
import java.util.GregorianCalendar
import java.text.NumberFormat
import java.text.DecimalFormat

import org.greenfield.common.ShoppingCartStatus

import org.greenfield.Account
import org.greenfield.Product
import org.greenfield.Catalog
import org.greenfield.ShoppingCart 
import org.greenfield.Transaction

import org.greenfield.log.PageViewLog
import org.greenfield.log.ProductViewLog
import org.greenfield.log.CatalogViewLog
import org.greenfield.log.SearchLog

import grails.plugin.springsecurity.annotation.Secured


@Mixin(BaseController)
class AdminController {
	
	def currencyService
	
	//@Secured(['ROLE_ADMIN'])
	@Secured(['permitAll'])
    def index() { 
		authenticatedAdmin { adminAccount ->
		
			def currency = currencyService.getCurrency()
			def currencySymbol = currencyService.getCurrencySymbol()
			
			def startDate 
			def endDate
			
			if(params.startDate && params.endDate){
				try{
					startDate = Date.parse("MM/dd/yyyy", params.startDate)
					endDate = Date.parse("MM/dd/yyyy", params.endDate)
				}catch (Exception e){
					flash.message = "Incorrect date format, please specify dates as mm/dd/yyy"
					startDate = new Date() - 30
					endDate = new Date()
				}
				
				if(!startDate || !endDate){
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
				startDate = null
				endDate = null
			}else{
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
			
		
			
			[ adminAccount : adminAccount, storeStatistics: storeStatistics, startDate : formattedStartDate, endDate : formattedEndDate, currency: currency, currencySymbol: currencySymbol]
	
		
		}
	}
	
	
	
	def data(){
		authenticatedAdmin { adminAccount ->
			def data = [:]
			
			def formattedData = [:]
			formattedData.rows = []
			
			def response = [:]
			def startDate = null
			def endDate = null
			
			if(params.startDate && params.endDate){
				try{
					startDate = Date.parse("MM/dd/yyyy", params.startDate)
					endDate = Date.parse("MM/dd/yyyy", params.endDate)
				}catch (Exception e){
					response.error = "Unable to parse dates"
				}
			}
			
			def rows = []
			if(params.type){			
				switch(params.type){
					case "products" :
						data = getProductViewsStatistics(startDate, endDate)
						rows = formatProductsData(data)
						break;
					case "pages" :
						data = getPageViewStatistics(startDate, endDate)
						rows = formatPagesData(data)
						break;
					case "catalogs" :
						data = getCatalogViewsStatistics(startDate, endDate)
						rows = formatCatalogsData(data)
						break;
					case "searches" :
						data = getSearchQueriesStatistics(startDate, endDate)
						rows = formatSearchesData(data)
						break;
					default : 
						response.error = "Type of data requested not found"
						break;
				}
			}else{
				response.error = "Type of data must be specified"
			}
			

			formattedData.rows = rows
			response.data = formattedData
			
			render response as JSON
		}
	}
	
	
	def formatProductsData(data){
		def products = []
		data.each{ p ->
			def product = [:]
			product.title = p.value.product
			product.value = p.value.count
			products.add(product)
		}
		return products
	}
	
	
	def formatPagesData(data){
		def pages = []
		data.each{ p ->
			def page = [:]
			page.title = p.value.page
			page.value = p.value.count
			pages.add(page)
		}
		return pages
	}
	
	
	def formatCatalogsData(data){
		def catalogs = []
		data.each{ p ->
			def catalog = [:]
			catalog.title = p.value.catalog
			catalog.value = p.value.count
			catalogs.add(catalog)
		}
		return catalogs
	}
	
	
	
	def formatSearchesData(data){
		def searches = []
		data.each{ p ->
			def search = [:]
			search.title = p.key
			search.value = p.value.count
			searches.add(search)
		}
		return searches
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
		String averageOrder = "0"
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
		def chartData = [:]
		if(startDate && endDate){
			orders = Transaction.findAllByOrderDateBetween(startDate, endDate, [ sort : "orderDate", order:"asc" ])
		}else{
			orders = Transaction.list([sort : "orderDate", order:"asc" ])
		}
		
		
		def previousDate = null
		orders.eachWithIndex(){ order, index ->
			sales += order.total
			
			def date = order.orderDate.clearTime().format("MM/dd/yyyy")
			
			if(date != previousDate){
				chartData[date] = order.total
			}else{
				chartData[date] += order.total
			}

			previousDate = date
			
		}
		
		
		orderCount = orders?.size()
		

		DecimalFormat df = new DecimalFormat("###,###.##"); 
		df.setMinimumFractionDigits(2)

		if(sales && orderCount){
			def unformattedAverage = Math.round(sales/orderCount * 100)/100
			averageOrder = df.format(unformattedAverage)
		}
		
		stats.orders = orders
		stats.chartData = chartData
		stats.checkoutCarts = checkoutCarts
		stats.shoppingCarts = shoppingCarts
		stats.checkoutRate = checkoutRate
		

		stats.sales = df.format(sales)
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
			productViewLogs = ProductViewLog.list()
		}
		
		
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
			pageViewLogs = PageViewLog.list()
		}
		
		
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
			catalogViewLogs = CatalogViewLog.list()
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
			searchLogs = SearchLog.list()
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
