package greenfield

import grails.plugin.springsecurity.annotation.Secured
import org.greenfield.Account

class StoreController {

 	@Secured(['permitAll'])
    def index() {
    	def accounts = Account.list()
    	[accounts : accounts]
    }

}
