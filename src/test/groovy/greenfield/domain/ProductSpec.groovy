package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Product

import org.greenfield.common.DomainMockHelper

class ProductSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Product
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		def catalog = DomainMockHelper.getMockCatalog()
		catalog.save(flush:true)
	    def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)

	    expect:
	    Product.count() == 1
	}
	
}