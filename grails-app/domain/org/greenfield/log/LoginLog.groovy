package org.greenfield.log

import org.greenfield.Account

class LoginLog {

	Account account
	String ipAddress
	Date date	
		
	Date dateCreated
	Date lastUpdated

    static constraints = {
		id generator: 'sequence', params:[sequence:'ID_LOGIN_LOG_PK_SEQ']
    }
	
}
