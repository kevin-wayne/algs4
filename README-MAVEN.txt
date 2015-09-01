This file provides brief instructions on how to add this repository to
a Maven build, from Eclipse, NetBeans, IntelliJ, or the command line.
Specifically, it provides instructions on creating a version of algs4.jar
that you can use as a dependency in your projects.

These instructions assume that you already have installed Java 7
(JDK 1.7) or above.


Using Maven in Eclipse for Java Developers
------------------------------------------
[untested instructions from http://stackoverflow.com/questions/8620127/maven-in-eclipse-step-by-step-installation]
To install the Maven plugin for Eclipse (M2Eclipse):
  * Open Eclipse.
  * Go to Help -> Eclipse Marketplace.
  * Search for Maven.
  * Click "Install" button at "Maven Integration for Eclipse" section.
  * Follow the instruction step by step.,


[this could use a bit more explanation, with reference to algs4 repository and
 where algs4-<version>.jar ends up ]
To view source files and run this project, open this project with your favorite IDE,
using "open project" or "import project wizards."

In any case open or import this project "As Maven Project".



Using Maven in IntelliJ IDEA
----------------------------


Using Maven in Netbeans
-----------------------


Using Maven from the Windows Command Prompt
-------------------------------------------
Download and install Maven by following the instructions at
https://maven.apache.org.

Locate the installation directory for Maven, e.g., C:\<appache-maven-x-y-z>

Locate the installation directory for Java, e.g., C:\Program Files\Java\<JDK-x-y-z>

Set the following environment variables:

set JAVA_HOME=C:\Program Files\Java\<JDK-x-y-z>
set PATH=%JAVA_HOME%\bin;%PATH%
set M2_HOME=C:\<appache-maven-x-y-z>
set PATH=%M2_HOME%\bin;%PATH%

To create the algs4-<version>.jar package and install it in the local
repository for reuse from other projects, change to the directory of
the algs4 repository and run Maven.

cd <algs4 directory>
mvn clean install

Maven will put algs4-<version>.jar in the directory <algs4 directory>/target.
You can use this jar as a dependency in your projects.



Using Maven from the Linux / Mac OS X bash shell
------------------------------------------------
Download and install Maven, either by using your favorite package
manager  (such as apt-get) or by following the instructions at
https://maven.apache.org.

Locate the installation directory for Maven, e.g., /my/maven/<appache-maven-x-y-z>

Locate the installation directory for Java, e.g., /my/java/<JDK-x-y-z->

Set the following environment variables:

export JAVA_HOME=/my/java/<JDK-x-y-z>
export PATH=$JAVA_HOME/bin:$PATH
export M2_HOME=/my/maven/<appache-maven-x-y-z>
export PATH=$M2_HOME/bin:$PATH

To create the algs4-<version>.jar package and install it in the local
repository for reuse from other projects, change to the directory of
the algs4 repository and run Maven.

cd <algs4 directory>
mvn clean install

Maven will put algs4-<version>.jar in the directory <algs4 directory>/target.
You can use this jar as a dependency in your projects.
