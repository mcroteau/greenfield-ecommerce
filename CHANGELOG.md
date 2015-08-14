# Greenfield Changelog

This is a changelog for Greenfield developers.  All future changes to this project will be listed here.



## Greenfield 2.0.2

####Updates

* Updated search results to include # of results
* Added pagination to search results
* Created separate admin email for new registrations
* Added Selenium tests for Easypost api calls for shipping calculation and label generation
* Small modifications & style changes


####Bug Fixes

* Secured product import
* Updated store front layout to render CONTEXT_NAME tag in footer
* Refresh product page maintains catalog breadcrumbs
* Modified administration template to render sub gsp page title
* Test cleanup removing old/stale tests





## Greenfield 2.0.1

####Updates

* Resolved EasyPost dependency issue - Added latest EasyPost jar to project lib directory
* Updated Selenium tests - Added shipping settings test
* Upgraded Stripe dependency to latest release





## Greenfield 2.0.0

#### New Features

* Multi-Catalog - Products can now belong to multiple catalogs
* Sub-Catalogs - Catalogs can now have sub catalogs
* Breadcrumbs - Added breadcrumb navigation on catalog and product views


####Updates

* Refactored Product Import - Now have to manually add products to catalogs after import
* Updated Catalog Results Information - Limited number of pages to display, updated results counter


####Database Changes

You will need to run sql commands in `2.0.0.sql` script located in root sql directory





## Greenfield 1.1.2

#### New Features

* Added a Health Check endpoint






##Greenfield 1.1.1

####Updates
* Added AdminMenuFilters to highlight active menu option
* Small admin style improvements

#### Bug Fixes
* Moved development data generation from service to plain groovy class
* Increased size of input boxes for Stripe settings
* Removed old .backup files





## Greenfield 1.1.0

#### New Features

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
