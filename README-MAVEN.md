How to use this project in Maven
--------------------------------

To import current project in your favorite IDE (such as Eclipse, NetBeans, or IntelliJ) you need following:

1) Install Java JDK.
   http://www.oracle.com/technetwork/java/javase/downloads/index.html
   Note the installation directory, known as JAVA_HOME, e.g., C:\Program Files\Java\`<JDK-x-y-z>` on Windows.

2) Install your favorite IDE.
       Eclipse IDE for Java Developers: https://eclipse.org/downloads/
       NetBeans IDE: https://netbeans.org
       IntelliJ IDEA: https://www.jetbrains.com/idea/

3) Install Maven.
   https://maven.apache.org/
   (optional step, because most modern IDE have an embedded Maven installation)
   Note the installation directory, known as M2_HOME, e.g,. C:\`<appache-maven-x-y-z>` on Windows.


Install Java (obligatory)
-------------------------

Download Java JDK:  http://www.oracle.com/technetwork/java/javase/downloads/index.html

Note, where Java landed after installation.



Using Maven from from your favorite IDE
---------------------------------------

To view source files and run this project, open this project with your favorite IDE,
using "open project" or "import project wizards."

In any case open or import this project "As Maven Project".




Using Maven from the command line
---------------------------------

Add Java to your system path
  %JAVA_HOME%\bin on Windows
  $JAVA_HOME\bin on Linux or OS X



Open Windows command line, Linux shell, or Mac OS X Terminal.

Specify following parameters:

On Windows:

set JAVA_HOME=C:\Program Files\Java\\`<JDK-x-y-z>`
set PATH=%JAVA_HOME%\bin;%PATH%
set M2_HOME=C:\\`<appache-maven-x-y-z>`
set PATH=%M2_HOME%\bin;%PATH%

On Linux or Mac OS X (bash):

export JAVA_HOME=/my/java/`<JDK-x-y-z>`
export PATH=$JAVA_HOME/bin:$PATH
export M2_HOME=/my/maven/`<appache-maven-x-y-z>`
export PATH=$M2_HOME/bin:$PATH

Maven will store repositories on your file system.
   Windows : C:\Users\\`<username>`\\.m2\repository
   Linux : /home/`<username>`/.m2/repository




Change to directory of the project and run build on Windows or Linux in the same command line/shell:

cd `<algs4 directory>`
mvn clean install

In your `<algs4 directory>`/target you will find a algs4-`<version>`.jar

You can use this jar as dependency in your projects (according to GPLv3 license).
