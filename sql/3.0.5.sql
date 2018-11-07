alter table product
 	add "sales_price" numeric(19,2);

alter table shopping_cart_item 
 	add "checkout_price" numeric(19,2);
 
alter table shopping_cart_item  
 	add "regular_price" numeric(19,2);
 
alter table shopping_cart_item_option  
	add "checkout_price" numeric(19,2); 