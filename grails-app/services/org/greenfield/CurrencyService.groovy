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
			switch(currency){
				case "USD" : 
					currencySymbol = "\$"
					break
				case "GBP":
					currencySymbol = "£"
					break
				case "EUR":
					currencySymbol = "€"
					break
				case "BRL":
					currencySymbol = "R\$"
					break
				default : 
					currencySymbol = "\$"
					break
			}
			
			return currencySymbol
			
		}catch(Exception e){
			e.printStackTrace()
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
	
	
}