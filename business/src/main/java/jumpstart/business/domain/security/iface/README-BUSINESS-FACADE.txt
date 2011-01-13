README-BUSINESS-FACADE
~~~~~~~~~~~~~~~~~~~~~~

This directory holds the external interface of the "security" domain.   The business facade is 
described by 

	ISecurityFinderServiceLocal.java
	ISecurityFinderServiceRemote.java
	ISecurityManagerServiceLocal.java
	ISecurityManagerServiceRemote.java
	
Notice they are all POJOs.  With EJB3 there's no reference to EJBObject, EJBLocalObject,
EJBLocalHome, etc, etc.

In ISecurityFinderServiceRemote you will see the method we use to authenticate the user:

	User authenticateUser(String loginId, String password) throws BusinessException;

Where are the DTOs?
~~~~~~~~~~~~~~~~~~~

Well, there are some DTOs (eg. UserSearchFields) but not many, because JPA, the Java
Persistence API, has made them less necessary.

How's that?  Well, since people's reluctance to share the domain model across layers has 
diminished, the designers of JPA decided to allow domain objects, or "entities", 
to be shared across the layers!  It's called "detaching" the objects, and it's much simpler 
than shuffling data in and out of DTOs, particularly when the DTO has to represent an 
extensive object tree.

Looking at our "finder" service (ISecurityFinderServiceRemote) you can see that all the methods 
return domain objects:

	User findUser(Long id) throws BusinessException;
	User findUser(String loginId) throws BusinessException;
	List<User> findUsers();
	List<User> findUsers(UserSearchFields searchFields, SearchOptions searchOptions);
	User authenticateUser(String loginId, String password) throws BusinessException;

and in our "manager" service (ISecurityManagerServiceRemote) you can see methods that receive 
domain objects:

	User createUser(User user, String password) throws ServiceException;
	void changeUser(User user) throws ServiceException;
	void deleteUser(User user) throws ServiceException;

In all of that, only one DTO is used - UserSearchFields.

Next
~~~~

README-BUSINESS-DOMAIN.  In Eclipse you can find it easily with Ctrl-Shift-R (or Cmd-Shift-R on the Mac).
