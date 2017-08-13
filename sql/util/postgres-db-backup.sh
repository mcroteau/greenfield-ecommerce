#!/bin/bash

dbname=postgresql://postgres:password@host:5432/database-name
file=backups/database-name-$(date +%Y-%m-%d).tar

/Library/PostgreSQL/9.6/bin/pg_dump --dbname=$dbname --format=tar --file=$file

