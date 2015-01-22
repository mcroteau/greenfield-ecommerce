package org.greenfield

class ShoppingCartItemOption {

	Variant variant
	
	ShoppingCartItem shoppingCartItem
	static belongsTo = [ ShoppingCartItem ]
		
    static constraints = {
		id generator: 'sequence', params:[sequence:'ID_SHOPPING_CART_ITEM_OPTION_PK_SEQ']
    }
	
}
