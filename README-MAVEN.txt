This file provides brief instructions on how to add this repository to
a Maven build, from Eclipse, NetBeans, IntelliJ, or the command line.
Specifically, it provides instructions on creating a version of algs4.jar
that you can use as a dependency in your projects.

These instructions assume that you already have installed Java 7
(JDK 1.7) or above.


Using Maven in Eclipse for Java Developers
------------------------------------------
If m2e (Maven plugin) is not built into Eclipse, follow these steps to install the plugin:

  * Open Eclipse.
  * Go to Help -> Eclipse Marketplace.
  * Search for Maven.
  * Click "Install" button at "Maven Integration for Eclipse" or "m2e" section.
  * Follow the instruction step by step.,

Restart Eclipse after installing m2e.

Now you can import algs4 as "Maven Project" into Eclipse:

  * Open menu: File-> Import-> Maven-> Existing Maven Projects...
  * Choose directory of algs4.
  * Confirm import.

To complete dependencies resolution after import:
  * Right click on the project, choose Maven -> Update Project...
  * Confirm project update.

To build project in Eclipse:
Eclipse automatically builds the project every time it saved.
But if you want enforce build, do following:
  * Right click on the project in Eclipse.
  * Choose Run as... Maven build.

Maven will put algs4-<version>.jar in the directory <algs4 directory>/target.
You can use this jar as a dependency in your projects.


Using Maven in IntelliJ IDEA
----------------------------


Using Maven in Netbeans
-----------------------


Using Maven from the Windows Command Prompt
-------------------------------------------
Download and install Maven by following the instructions at
https://maven.apache.org.

Locate the installation directory for Maven, e.g., C:\<apache-maven-x-y-z>

Locate the installation directory for Java, e.g., C:\Program Files\Java\<JDK-x-y-z>

Set the following environment variables:

set JAVA_HOME=C:\Program Files\Java\<JDK-x-y-z>
set PATH=%JAVA_HOME%\bin;%PATH%
set M2_HOME=C:\<apache-maven-x-y-z>
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

Locate the installation directory for Maven, e.g., /my/maven/<apache-maven-x-y-z>

Locate the installation directory for Java, e.g., /my/java/<JDK-x-y-z->

Set the following environment variables:

export JAVA_HOME=/my/java/<JDK-x-y-z>
export PATH=$JAVA_HOME/bin:$PATH
export M2_HOME=/my/maven/<apache-maven-x-y-z>
export PATH=$M2_HOME/bin:$PATH

To create the algs4-<version>.jar package and install it in the local
repository for reuse from other projects, change to the directory of
the algs4 repository and run Maven.

cd <algs4 directory>
mvn clean install

Maven will put algs4-<version>.jar in the directory <algs4 directory>/target.
You can use this jar as a dependency in your projects.

