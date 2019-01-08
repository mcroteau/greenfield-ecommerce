package greenfield

import grails.plugin.springsecurity.annotation.Secured
import grails.converters.*

import org.greenfield.Country
import org.greenfield.State

import org.greenfield.Account
import org.greenfield.Transaction

public class DataController {
	
	private final String SETTINGS_FILE = "settings.properties"
	private final String STORE_PRIVATE_KEY = "store.key"
	
	@Secured(['permitAll'])	
	def states(){
		
		def country
		if(params.country && params.country != "undefined"){
			country = Country.get(params.country)
		}
		
		def states = [:]
		if(country){
			states = State.findAllByCountry(country)
		}
		
		render states as JSON
	}
	
	@Secured(['permitAll'])
	def accounts(){
		
		def key = params.k
		
		if(!key){
			render "Please provide private key in order to access accounts..."
			return
		}

		Properties prop = new Properties();
		File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
		FileInputStream inputStream = new FileInputStream(propertiesFile)
		prop.load(inputStream);
		
		def privateKey = prop.getProperty(STORE_PRIVATE_KEY);
		
		if(key != privateKey){
			render "Key does not match, please make sure key set is correct..."
			return
		}
		
		def data =[:]
		
		def accounts = []
		def accountInstanceList = Account.findAllByHasAdminRole(false)
		
		accountInstanceList.each() { acc ->
			def account = [:]
	        account.email = acc.email
			account.name = acc.name
			account.address1 = acc.address1
			account.address2 = acc.address2
			account.city = acc.city
			account.country = acc.country?.name
			account.state = acc.state?.name
			account.zip = acc.zip
			account.phone = acc.phone
			account.ipAddress = acc.ipAddress
			
			def ordersCount = Transaction.countByAccount(acc)
			def orders = Transaction.findAllByAccount(acc)
			def ordersValue = 0
			
			orders.each(){ order ->
				ordersValue += order.total
			}
			account.ordersCount = ordersCount
			account.ordersValue = ordersValue
			
			accounts.add(account)
		}
		
		data.count = accountInstanceList.size()
		data.accounts = accounts
		
		render data as JSON
		
	}

}