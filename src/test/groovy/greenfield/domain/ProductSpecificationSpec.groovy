package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Layout
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.Specification
import org.greenfield.ProductSpecification

import org.greenfield.common.DomainMockHelper

class ProductSpecificationSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain ProductSpecification
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
	    
		def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)
		
		def specification = DomainMockHelper.getMockSpecification()
		specification.save(flush:true)
		
		def productSpecification = DomainMockHelper.getMockProductSpecification()
		productSpecification.save(flush:true)

	    expect:	    
		Layout.count() == 1
	    Catalog.count() == 1
	    Product.count() == 1
	    Specification.count() == 1
	    ProductSpecification.count() == 1
	}
	
}