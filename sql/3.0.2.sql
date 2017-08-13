
-- 3.0.2 added catalog position 

alter table catalog
 add "position" integer NOT NULL DEFAULT 0;
 
 