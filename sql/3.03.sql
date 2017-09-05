update account set has_admin_role = true where username = 'admin'

alter table account add column uuid character varying(255);
alter table additional_photo add column uuid character varying(255);
alter table catalog add column uuid character varying(255);
alter table catalog_view_log add column uuid character varying(255);
alter table country add column uuid character varying(255);
alter table layout add column uuid character varying(255);
alter table login_log add column uuid character varying(255);
alter table page add column uuid character varying(255);
alter table page_view_log add column uuid character varying(255);
alter table permission add column uuid character varying(255);
alter table product add column uuid character varying(255);
alter table product_catalog add column uuid character varying(255);
alter table product_option add column uuid character varying(255);
alter table product_specification add column uuid character varying(255);
alter table product_view_log add column uuid character varying(255);
alter table role add column uuid character varying(255);
alter table search_log add column uuid character varying(255);
alter table shopping_cart add column uuid character varying(255);
alter table shopping_cart_item add column uuid character varying(255);
alter table shopping_cart_item_option add column uuid character varying(255);
alter table specification add column uuid character varying(255);
alter table specification_catalog add column uuid character varying(255);
alter table specification_option add column uuid character varying(255);
alter table state add column uuid character varying(255);
alter table transaction add column uuid character varying(255);
alter table upload add column uuid character varying(255);
alter table variant add column uuid character varying(255);


alter table product_option
 add "position" integer DEFAULT 0;

update product_option set position = 0 where position is null;
	
	
alter table account add catalog_views integer default 0;
alter table account add product_views integer default 0;
alter table account add page_views integer default 0;
alter table account add searches integer default 0;
alter table account add orders integer default 0;

update account set catalog_views = 0 where catalog_views is null;
update account set product_views = 0 where product_views is null;
update account set page_views = 0 where page_views is null;
update account set searches = 0 where searches is null;
update account set orders = 0 where orders is null;
	
	







	