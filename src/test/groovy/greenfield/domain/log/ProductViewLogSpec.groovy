package greenfield.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import org.greenfield.Account
import org.greenfield.Layout
import org.greenfield.Product
import org.greenfield.log.ProductViewLog

import org.greenfield.common.DomainMockHelper

class ProductViewLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain ProductViewLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def product = DomainMockHelper.getMockProduct(layout)
		product.save(flush:true)
		
		def productViewLog = DomainMockHelper.getMockProductViewLog(account, product)
		productViewLog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    Layout.count() == 1
	    Product.count() == 1
	    ProductViewLog.count() == 1
	}
	

}