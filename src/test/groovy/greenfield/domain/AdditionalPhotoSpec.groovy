package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Layout
import org.greenfield.Catalog
import org.greenfield.Product
import org.greenfield.AdditionalPhoto

import org.greenfield.common.DomainMockHelper

class AdditionalPhotoSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain Layout
        mockDomain Catalog
        mockDomain Product
        mockDomain AdditionalPhoto
	}


	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)

		def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)
		
		def additionalPhoto = DomainMockHelper.getMockAdditionalPhoto(product)
		additionalPhoto.save(flush:true)
		
	    expect:
	    AdditionalPhoto.count() == 1
	}
	

}