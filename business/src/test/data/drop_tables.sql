set referential_integrity false;  -- hsqldb
-- set foreign_key_checks = false;  -- mysql

-- Tables used in domain "examples"
drop table if exists person_min cascade; -- not in JumpStart 3.14.0 onwards
drop table if exists Person cascade;
drop table if exists DateStuff cascade;

-- Tables used in domain "security"
drop table if exists user_role cascade; -- not in JumpStart 3.14.0 onwards
drop table if exists UserRole cascade;
drop table if exists user_min cascade; -- not in JumpStart 3.14.0 onwards
drop table if exists Users cascade;
drop table if exists role cascade;
drop table if exists Role cascade;
drop table if exists user_password_min cascade; -- not in JumpStart 3.14.0 onwards
drop table if exists UserPassword cascade;

-- Tables used in domain "workout"
drop table if exists Student cascade;
drop table if exists Teacher cascade;
drop table if exists Department cascade;
drop table if exists Building cascade;
drop table if exists Room cascade;
drop table if exists StringThing cascade;

set referential_integrity true;  -- hsqldb
-- set foreign_key_checks = true;  -- mysql

commit;
