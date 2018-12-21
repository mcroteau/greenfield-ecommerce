package org.greenfield

class Transaction {
	
	Transaction(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	BigDecimal total
	BigDecimal subtotal
	BigDecimal shipping
	BigDecimal taxes
	
	String status
	Date orderDate
	
	String chargeId
	String postageId
	String postageUrl
	
	
	Date dateCreated
	Date lastUpdated

	String shipName
	String shipAddress1
	String shipAddress2
	String shipCity
	State shipState
	Country shipCountry
	String shipZip
	
	/** TODO : CONSIDER REMOVING **/
	String billName
	String billAddress1
	String billAddress2
	String billCity
	State billState
	String billZip
	
	String gateway
	
	
	Account account
	ShoppingCart shoppingCart
	
	static belongsTo = [ Account, ShoppingCart]
	
	
	static mapping = {
	    sort "orderDate" : "desc"
	}
	
    static constraints = {
		uuid(nullable:true)
		status(nullable:false)
		total(nullable:false)
		subtotal(nullable:false)
		shipping(nullable:false)
		taxes(nullable:false)
		orderDate(nullable:false)
		chargeId(nullable:true)
		postageId(nullable:true)
		postageUrl(nullable:true)
		shipName(nullable:false)
		shipAddress1(nullable:false)
		shipAddress2(nullable:true)
		shipCity(nullable:true)
		shipState(nullable:true)
		shipCountry(nullable:true)
		shipZip(nullable:false)
		billName(nullable:true)
		billAddress1(nullable:true)
		billAddress2(nullable:true)
		billCity(nullable:true)
		billState(nullable:true)
		billZip(nullable:true)
		gateway(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_TRANSACTION_PK_SEQ']
	}
	
}
