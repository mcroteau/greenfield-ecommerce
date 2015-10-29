-- Table: page_view_log

CREATE TABLE page_view_log
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  account_id bigint,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL,
  page_id bigint NOT NULL,
  CONSTRAINT page_view_log_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fvxwbu6xlbf0ispu7ku2wgahd FOREIGN KEY (page_id)
      REFERENCES page (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_slo4p90v75xp3h8yt89wd52nc FOREIGN KEY (account_id)
      REFERENCES account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE page_view_log
  OWNER TO postgres;






-- Table: product_view_log

CREATE TABLE product_view_log
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  account_id bigint,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL,
  product_id bigint NOT NULL,
  CONSTRAINT product_view_log_pkey PRIMARY KEY (id),
  CONSTRAINT fk_1dd33fvslq551vniprefkft8o FOREIGN KEY (account_id)
      REFERENCES account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_mrp00gt8cdgu5r5nqqsefysl FOREIGN KEY (product_id)
      REFERENCES product (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE product_view_log
  OWNER TO postgres;

  
  
  
  
  
-- Table: catalog_view_log

CREATE TABLE catalog_view_log
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  account_id bigint,
  catalog_id bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL,
  CONSTRAINT catalog_view_log_pkey PRIMARY KEY (id),
  CONSTRAINT fk_j7kdhl3rtk9u3h6lunbi7fjru FOREIGN KEY (account_id)
      REFERENCES account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_smthwarrl4cpk57nfama0d0xb FOREIGN KEY (catalog_id)
      REFERENCES catalog (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE catalog_view_log
  OWNER TO postgres;
  
  
  
  


-- Table: search_log

CREATE TABLE search_log
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  account_id bigint,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL,
  query character varying(255) NOT NULL,
  CONSTRAINT search_log_pkey PRIMARY KEY (id),
  CONSTRAINT fk_qecau2sbdv9o10a19l2lvvqgc FOREIGN KEY (account_id)
      REFERENCES account (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE search_log
  OWNER TO postgres;
  
  
