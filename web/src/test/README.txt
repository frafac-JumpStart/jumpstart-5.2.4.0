web/src/test/README.txt
~~~~~~~~~~~~~~~~~~~~~~~

This directory contains the things needed to run JumpStart directly from the IDE.
It could also contain web layer tests (eg. using Selenium) and the things needed to run them in a build system, but it doesn't.

conf/
	Describes the environment for testing JumpStart while it is running in-line.
lib-dev-jetty-openejb-local/
	Contains jar files needed only for running JumpStart with jetty and openejb locally, ie. in-line.
	To populate or refresh this directory, run the get-dependent-files target in the project's build.xml Ant file.
