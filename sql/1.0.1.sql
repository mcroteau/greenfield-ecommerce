alter table layout add column default_layout boolean;
alter table page add column layout_id bigint;
alter table product add column layout_id bigint;
alter table catalog add column layout_id bigint;
update layout set default_layout = FALSE where name = 'STORE_LAYOUT';
--TODO:test this -- alter table variant alter column price type numeric(19,2);
--set layout for products, catalogs, pages
--update base layout wrapper to contain css and js
--update transaction table
