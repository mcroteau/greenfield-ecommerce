package org.greenfield

class ShoppingCartItem {
	
	ShoppingCartItem(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	int quantity
	
	Product product
	ShoppingCart shoppingCart

	BigDecimal regularPrice
	BigDecimal checkoutPrice

	static belongsTo = [ ShoppingCart ]
	
	static hasMany = [ shoppingCartItemOptions : ShoppingCartItemOption ]
	
	static mapping = {
	    sort "product"
	}
		
    static constraints = {
		uuid(nullable:true)
		quantity(nullable:true)
		checkoutPrice(nullable:true, defaultValue:0)
		regularPrice(nullable:true, defaultValue:0)
		id generator: 'sequence', params:[sequence:'ID_SHOPPING_CART_ITEM_PK_SEQ']
    }
}
