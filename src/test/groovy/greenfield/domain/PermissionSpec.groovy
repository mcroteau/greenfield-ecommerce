package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Account
import org.greenfield.Permission

import org.greenfield.common.DomainMockHelper

class PermissionSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Permission
	}

	void "test basic persistence mocking"() {
	    setup:
		def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def permission = DomainMockHelper.getMockPermission(account)
		permission.save(flush:true)

	    expect:
	    Account.count() == 1
	    Permission.count() == 1
	}
	
}