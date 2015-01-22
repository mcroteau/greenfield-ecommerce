package org.greenfield.log

import org.greenfield.Account

class LoginLog {

	Date date
	Account account
	String ipAddress

    static constraints = {
		id generator: 'sequence', params:[sequence:'ID_LOGIN_LOG_PK_SEQ']
    }
	
}
