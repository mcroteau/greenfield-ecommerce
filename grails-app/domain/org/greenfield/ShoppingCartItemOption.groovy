package org.greenfield

class ShoppingCartItemOption {
	
	ShoppingCartItemOption(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	Variant variant
	
	ShoppingCartItem shoppingCartItem
	static belongsTo = [ ShoppingCartItem ]
		
    static constraints = {
		uuid(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_SHOPPING_CART_ITEM_OPTION_PK_SEQ']
    }
	
}
