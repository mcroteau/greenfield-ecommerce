alter table catalog add column toplevel boolean;
update catalog set toplevel = true where id > 0;	

alter table product drop column catalog_id;