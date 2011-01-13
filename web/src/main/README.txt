web/src/main/README.txt
~~~~~~~~~~~~~~~~~~~~~~~

This directory contains the web layer application source and the things needed to build it in a build system.
The purpose of its sub-directories:

java/ 
	Contains the web java source files, Tapestry template files, and any properties files that are not user-configurable.
	Its contents will be compiled and packaged into the WAR's WEB-INF/classes/. 
lib-compile/
	Contains jar files needed during compile and runtime. 
	They must be included in the packaged deployables.
	To populate or refresh the directory run the get-dependent-files target in the project's build.xml Ant file.
lib-provided/
	Contains jar files needed only during compile.
	They are not needed at runtime because runtime will be in a container (usually JBoss/Tomcat) that would provide these jars.
	To populate or refresh the directory run the get-dependent-files target in the project's build.xml Ant file.
lib-runtime/
	Contains jar files needed only at runtime.
	They are not needed during compile, usually because they are connectors from the code to the runtime resources.
	To populate or refresh the directory, run the get-dependent-files target in the project's build.xml Ant file.
webapp/
	Contains files that are to be packaged into the WAR.
