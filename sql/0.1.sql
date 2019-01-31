alter table layout add column default_layout boolean;
alter table page add column layout_id bigint;
alter table product add column layout_id bigint;
alter table catalog add column layout_id bigint;
update layout set default_layout = FALSE where name = 'STORE_LAYOUT';
alter table product add column purchaseable boolean;
update account set email_opt_in = true where id > 0;
update product set purchaseable = false where id > 0;