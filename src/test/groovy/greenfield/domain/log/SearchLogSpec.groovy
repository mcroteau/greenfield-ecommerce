package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Account
import org.greenfield.Layout
import org.greenfield.log.SearchLog

import org.greenfield.common.DomainMockHelper

class SearchLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain SearchLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def searchlog = DomainMockHelper.getMockSearchLog(account)
		searchlog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    SearchLog.count() == 1
	}

}