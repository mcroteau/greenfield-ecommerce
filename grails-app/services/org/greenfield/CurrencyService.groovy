package org.greenfield
 
import java.util.Properties;
import grails.util.Holders
 

class CurrencyService {

	def grailsApplication
	
	private final String SETTINGS_FILE = "settings.properties"
	private final String STORE_CURRENCY = "store.currency"
	
	
	
	def getCurrency(){
		
		if(!grailsApplication){
			grailsApplication = Holders.grailsApplication
		}
		
		Properties prop = new Properties();
		
		try{
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			def currency = "USD"
			if(prop.getProperty(STORE_CURRENCY)){
				currency = prop.getProperty(STORE_CURRENCY)
			}
			
			return currency.toUpperCase()
			
		}catch(Exception e){
			e.printStackTrace()
		}
	}
	
	
	def getCurrencySymbol(){
		if(!grailsApplication){
			grailsApplication = Holders.grailsApplication
		}
		Properties prop = new Properties();
		
		try{
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			def currency = "USD"
			if(prop.getProperty(STORE_CURRENCY)){
				currency = prop.getProperty(STORE_CURRENCY)
			}
			
			def currencySymbol
			
			if(currency){
				switch(currency){
					case "USD" : 
						currencySymbol = "\$"
						break
					case "GBP":
						currencySymbol = "£"
						break
					case "NZD" : 
						currencySymbol = "\$"
						break
					case "CAD" : 
						currencySymbol = "C \$"
						break
					case "EUR":
						currencySymbol = "€"
						break
					case "HKD":
						currencySymbol = "HK\$"
						break
					case "BRL":
						currencySymbol = "R\$"
						break
					case "INR":
						currencySymbol = "₹"
						break
					default : 
						currencySymbol = "Needs Configuration"
						break
				}
				
				return currencySymbol
			}else{
				return "currency isn't set..."
			}
			
		}catch(Exception e){
			e.printStackTrace()
			return "currency isn't set..."
		}
	}
	
	
	def format(string){
		def currency = getCurrency()
		if(prepend(currency)){
			string = string + " " + getCurrencySymbol()
		}else{
			string = getCurrencySymbol() + " " + string
		}
	}
	
	
	def prepend(currency){
		if(currency == "EUR"){
			return true
		}
	}
	
	
	/**TODO:for a later date for international shipping**/
	def getCountryCode(){
		
		if(!grailsApplication){
			grailsApplication = Holders.grailsApplication
		}
		Properties prop = new Properties();
		
		try{
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			def countryCode = "us"
			if(prop.getProperty(STORE_COUNTRY_CODE)){
				countryCode = prop.getProperty(STORE_COUNTRY_CODE)
			}
			
			return countryCode

		}catch(Exception e){
			e.printStackTrace()
			return "country isn't set..."
		}
	}
	
}