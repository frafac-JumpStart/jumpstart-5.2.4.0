README-BUSINESS-DOMAIN
~~~~~~~~~~~~~~~~~~~~~~

SecurityFinderService implements several methods, including:

	User authenticateUser(String loginId, String password) throws BusinessException

It finds the User domain object with the loginId, then it asks the User to authenticate 
with the passsword.  If successful, it returns the User object.

Domain Classes and Services Together?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Naturally this "domain" package contains domain objects, also called entities:

	User
	UserPassword
	Role
	UserRole

but why does it also contain services, which we implement as session beans:

	SecurityFinderService
	SecurityManagerService

It's for a purely pragmatic reason - it's a way of giving the services sole access to
certain methods in the domain objects. We give those methods package scope and put the
services into the same package. 

An example is User.setPassword(...).  We give it package scope so it can be invoked by the 
services and so it cannot be invoked in the web layer when the object is detached.

Actually, what we'd really like is the ability to scope it to a layer.  The method could
be available within the business layer but nowhere else.  Perhaps in future Java will 
introduce such a scope.

Where's the XML?
~~~~~~~~~~~~~~~~

There isn't any.  Deployment descriptors and mapping files have been replaced by EJB3 
annotations.

The session beans publish their existence with annotations like these:

	@Stateless
	@Local(ISecurityFinderServiceLocal.class)
	@Remote(ISecurityFinderServiceRemote.class)

And the entity beans publish their existence and do their data mapping with annotations like:

	@Entity
	@Column

Where are the DAOs?
~~~~~~~~~~~~~~~~~~~

Well, there aren't any DAOs!  Thanks to the Java Persistence API (or JPA), DAOs are
often not necessary and JumpStart doesn't use them.

Instead, our "finder" services query the domain objects with javax.persistence.Query, and 
our "manager" services manipulate the domain objects with javax.persistence.EntityManager.

But why not do that in a DAO?  Because it's an unnecessary layer.  The DAO was invented to
hide where and how the data is stored.  It hides the data mapping, and it hides whether you are using
Hibernate, Kodo/JDO, TopLink, or whatever. But now JPA does that, so JPA is the DAO.

JPA hides the data mapping in the classes that you mark with @Entity (eg. User), and it hides 
the runtime implementation until, well, runtime.  JPA is a standard API that was developed by the 
very same people who make Hibernate, JDO, TopLink, and others.

You might still use a DAO when the data cannot be accessed via JPA, eg. rather than being in an
accessible database it is available only through other means such as an API to an external service.

But in our case the DAO layer is redundant so it's out! Less layers means a simpler system, and 
that's a plus.

UML
~~~

A class diagram is contained in the security.zargo file which can be edited with the free tool ArgoUML 
(http://argouml.tigris.org/). The diagram has also been exported (using the tool) to the web app images 
directory as security_classses.png.
