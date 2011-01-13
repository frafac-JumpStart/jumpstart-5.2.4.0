-- ------------------------------------------------------------------------------------------------------------
-- INITIAL DATA FOR JUMPSTART
-- ------------------------------------------------------------------------------------------------------------

-- persons

delete from Person;

insert into Person(id, version, firstName, lastName, region, startDate)
	values (1, 0, 'Humpty', 'Dumpty', 'EAST_COAST', '2007-12-05');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (2, 0, 'Mary', 'Contrary', 'EAST_COAST', '2008-02-29');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (3, 0, 'Jack', 'Sprat', 'WEST_COAST', '2007-02-28');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (4, 0, 'Jill', 'Spill', 'WEST_COAST', '2008-02-29');
insert into Person(id, version, firstName, lastName, region, startDate)
	values (5, 0, 'Dishy', 'Spoon', 'EAST_COAST', '2008-02-29');

-- date stuff

delete from DateStuff;

insert into DateStuff(id, version, aDate, aTime, aTimestamp, 
			aDateTime, aDateTimeWithTZ, aDateTimeTZ, aDateMidnight, aDateMidnightWithTZ, aDateMidnightTZ,
			aLocalDateTime, aLocalDate, aLocalTimeAsTime, aLocalTimeAsMillis, aLocalTimeAsString)
	values (1, 0, '2001-07-31', '10:35:17', '2001-07-31 10:35:17', 
			'2001-07-31 10:35:17', '2001-07-31 10:35:17', 'GMT+10:00', '2001-07-31', '2001-07-31', 'GMT+10:00',
			'2001-07-31 10:35:17', '2001-07-31', '10:35:17', 2117000, '10:35:17.000');

-- users

delete from UserRole;
delete from Role;
delete from Users;
delete from UserPassword;

insert into UserPassword (id, version, password)
	values	(1, 0, 'secofr');
insert into Users (id, version, loginId, salutation, firstName, lastName, emailAddress, expiryDate, active, userPasswordId,
	pageStyle, dateInputPattern, dateViewPattern, dateListPattern)
	values	(1, 0, 'secofr', 'Ms', 'Security', 'Officer', 'secofr@thecompany.com', null, 1, 1,
		0, 'dd/MM/yyyy', 'dd MMM yyyy', 'yyyy-MM-dd');
insert into UserPassword (id, version, password)
	values	(2, 0, 'admin');
insert into Users (id, version, loginId, salutation, firstName, lastName, emailAddress, expiryDate, active, userPasswordId,
	pageStyle, dateInputPattern, dateViewPattern, dateListPattern)
	values	(2, 0, 'admin', '', 'The', 'Administrator', 'admin@thecompany.com', null, 1, 2,
		1, 'dd/MM/yyyy', 'dd MMM yyyy', 'yyyy-MM-dd');
insert into UserPassword (id, version, password)
	values	(3, 0, 'john');
insert into Users (id, version, loginId, salutation, firstName, lastName, emailAddress, expiryDate, active, userPasswordId,
	pageStyle, dateInputPattern, dateViewPattern, dateListPattern)
	values	(3, 0, 'john', 'Mr', 'John', 'Citizen', 'john@thecompany.com', '2010-12-31', 1, 3,
		1, 'dd/MM/yyyy', 'dd MMM yyyy', 'yyyy-MM-dd');
		
-- roles
		
insert into Role(id, name, version)
	values	(1, 'Security Officer', 0);
insert into Role(id, name, version)
	values	(2, 'Administration', 0);
insert into Role(id, name, version)
	values	(3, 'Accounts', 0);

-- user roles

insert into UserRole(id, userId, roleId, active, version)
	values	(1, 1, 1, 1, 0);
insert into UserRole(id, userId, roleId, active, version)
	values	(2, 2, 2, 1, 0);
insert into UserRole(id, userId, roleId, active, version)
	values	(3, 2, 3, 1, 0);
insert into UserRole(id, userId, roleId, active, version)
	values	(4, 3, 3, 1, 0);

commit;
