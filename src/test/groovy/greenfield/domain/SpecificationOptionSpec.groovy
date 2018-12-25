package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Catalog
import org.greenfield.Layout
import org.greenfield.Product
import org.greenfield.Specification
import org.greenfield.SpecificationOption

import org.greenfield.common.DomainMockHelper

class SpecificationOptionSpec extends spock.lang.Specification implements DataTest {

	void setupSpec(){
        mockDomain SpecificationOption
	}

	void "test basic persistence mocking"() {
	    setup:
		def specification = DomainMockHelper.getMockSpecification()
		specification.save(flush:true)
		
		def specificationOption = DomainMockHelper.getMockSpecificationOption(specification)
		specificationOption.save(flush:true)

	    expect:
	    Specification.count() == 1	
	    SpecificationOption.count() == 1	
	}
	
}