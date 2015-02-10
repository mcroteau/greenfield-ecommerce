# Greenfield Changelog

This is a changelog for Greenfield developers.  All future changes to this project will be listed here.


## Greenfield 1.1.0

### New Features

* Application Dashboard
	* # Products
	* # Products out of stock
	* # Catalogs
	* # Customers
	* # Abandoned Carts (Total for life of Application)
	* Date Range Statistics
		* Gross Sales $
		* Total Orders
		* Average Order $
		* Checkout Rate % (Shopping Carts that converted to checkouts)
		* Top Products Viewed
		* Top Pages Viewed
		* Top Search Strings
		* Top Catalogs Viewed
		
* Updated Administration Styles
* Added alert to checkout screen if Stripe Credentials not configured in Application
* Added `DevelopmentDataService`.  Uncomment `developmentDataService` and `developmentDataService.generate()` in `BootStrap.groovy` to create mock development data on startup.  
