package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Catalog
import org.greenfield.Layout
import org.greenfield.Product
import org.greenfield.Specification

import org.greenfield.common.DomainMockHelper

class SpecificationSpec extends spock.lang.Specification implements DataTest {

	void setupSpec(){
        mockDomain org.greenfield.Specification
	}

	void "test basic persistence mocking"() {
	    setup:
		def specification = DomainMockHelper.getMockSpecification()
		specification.save(flush:true)

	    expect:
	    Specification.count() == 1	
	}
	
}