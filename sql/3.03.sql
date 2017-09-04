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
 add "position" integer NOT NULL DEFAULT 0;