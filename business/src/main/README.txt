business/src/main/README.txt
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This directory contains the business layer application source and the things needed to build it in a build system.
The purpose of its sub-directories:

java/ 
	Contains the business java source files and properties files that are not configurable.
lib-compile/
	Contains jar files needed during compile and runtime. 
	They must be included in the packaged deployables.
	To populate or refresh the directory run the get-dependent-files target in the project's build.xml Ant file.
lib-provided/
	Contains jar files needed only during compile.
	They are not needed at runtime because runtime will be in a container (usually JBoss) that would provide these jars.
	To populate or refresh the directory run the get-dependent-files target in the project's build.xml Ant file.
lib-runtime/
	Contains jar files needed only at runtime.
	They are not needed during compile, usually because they are connectors from the code to the runtime resources.
	To populate or refresh the directory, run the get-dependent-files target in the project's build.xml Ant file.
resources/
	Contains files that are to be packaged into the JAR.
