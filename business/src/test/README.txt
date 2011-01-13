business/src/test/README.txt
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This directory contains business layer tests and the things needed to run them in a build system.
The purpose of its sub-directories:

conf-*/
	Contains items that are used to configure the test environment.
data/
	Scripts and HSQLDB files for setting up initial database data. Use as described in 
	http://jumpstart.doublenegative.com.au/installation.html and http://jumpstart.doublenegative.com.au/tips.html .
java/ 
	Contains the test classes.  Ideally the package structure emulates the structure of business/src/main/java/.
lib-test/
	Contains jar files needed only during tests.
	The jars enable the test environment to interact with the application which would be running in a container (locally or remote).
	To populate or refresh this directory, run the get-dependent-files target in the project's build.xml Ant file.
lib-test-openejb-local/
	Contains jar files needed only during tests with OpenEJB as the container, running locally (in-line).
	Use as described in http://jumpstart.doublenegative.com.au/installation.html and http://jumpstart.doublenegative.com.au/tips.html .
	To populate or refresh this directory, run the get-dependent-files target in the project's build.xml Ant file.
