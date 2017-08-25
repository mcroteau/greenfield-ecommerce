package org.greenfield.log

import org.greenfield.Account

class LoginLog {
	
	LoginLog(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Account account
	String ipAddress
	Date date	
		
	Date dateCreated
	Date lastUpdated

    static constraints = {
		uuid(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_LOGIN_LOG_PK_SEQ']
    }
	
}
