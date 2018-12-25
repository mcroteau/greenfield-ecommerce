package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Layout
import org.greenfield.Catalog

import org.greenfield.common.DomainMockHelper

class CatalogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain Layout
        mockDomain Catalog
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
		
	    expect:
	    Catalog.count() == 1
	}
	

}