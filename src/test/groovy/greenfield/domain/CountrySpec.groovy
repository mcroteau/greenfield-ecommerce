package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Country

import org.greenfield.common.DomainMockHelper

class CountrySpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain Country
	}

	void "test basic persistence mocking"() {
	    setup:
		def country = DomainMockHelper.getMockCountry()
		country.save(flush:true)
		
	    expect:
	    Country.count() == 1
	}
	

}