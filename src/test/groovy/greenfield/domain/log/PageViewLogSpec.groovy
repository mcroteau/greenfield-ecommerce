package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Account
import org.greenfield.Layout
import org.greenfield.Page
import org.greenfield.log.PageViewLog

import org.greenfield.common.DomainMockHelper

class PageViewLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain PageViewLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def page = DomainMockHelper.getMockPage(layout)
		page.save(flush:true)
		
		def pageViewLog = DomainMockHelper.getMockPageViewLog(account, page)
		pageViewLog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    Layout.count() == 1
	    Page.count() == 1
	    PageViewLog.count() == 1
	}
	

}