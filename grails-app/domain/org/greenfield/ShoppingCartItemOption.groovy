package org.greenfield

class ShoppingCartItemOption {
	
	ShoppingCartItemOption(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Variant variant
	BigDecimal checkoutPrice
	
	ShoppingCartItem shoppingCartItem
	static belongsTo = [ ShoppingCartItem ]
		
    static constraints = {
		uuid(nullable:true)
		checkoutPrice(nullable:false, defaultValue:0)
		id generator: 'sequence', params:[sequence:'ID_SHOPPING_CART_ITEM_OPTION_PK_SEQ']
    }
	
}
