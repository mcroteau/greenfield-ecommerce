package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Layout
import org.greenfield.Page

import org.greenfield.common.DomainMockHelper

class PageSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Page
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
	    
		def page = DomainMockHelper.getMockPage(layout)
		page.save(flush:true)

	    expect:
	    Layout.count() == 1
	    Page.count() == 1
	}
	
}