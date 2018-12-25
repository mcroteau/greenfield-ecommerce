package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Account
import org.greenfield.log.LoginLog

import org.greenfield.common.DomainMockHelper

class LoginLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain LoginLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def loginLog = DomainMockHelper.getMockLoginLog(account)
		loginLog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    LoginLog.count() == 1
	}

}