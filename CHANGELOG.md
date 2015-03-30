# Greenfield Changelog

This is a changelog for Greenfield developers.  All future changes to this project will be listed here.


alter table catalog add column toplevel boolean;
update catalog set toplevel = true where id > 0;	

alter table product drop column catalog_id;


## Greenfield 1.1.2

### New Features

* Added a Health Check endpoint




##Greenfield 1.1.1

###Updates
* Added AdminMenuFilters to highlight active menu option
* Small admin style improvements

### Bug Fixes
* Moved development data generation from service to plain groovy class
* Increased size of input boxes for Stripe settings
* Removed old .backup files





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
