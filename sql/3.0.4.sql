alter table shopping_cart
	alter column account_id drop not null;

alter table account 
	alter column username type character varying(255);