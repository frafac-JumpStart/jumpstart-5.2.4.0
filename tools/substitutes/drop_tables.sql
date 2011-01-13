set referential_integrity false;  -- hsqldb
-- set foreign_key_checks = false;  -- mysql

drop table if exists UserRole cascade;
drop table if exists Users cascade;
drop table if exists Role cascade;
drop table if exists UserPassword cascade;

drop table if exists Student cascade;
drop table if exists Teacher cascade;
drop table if exists Department cascade;
drop table if exists Building cascade;

set referential_integrity true;  -- hsqldb
-- set foreign_key_checks = true;  -- mysql
