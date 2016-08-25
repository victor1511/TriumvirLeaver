Steps to install identityiq.jar in the local maven repository

* Download the Apache Maven binaries
* Put the identityiq.jar in the bin folder of your maven installation
* Press Shift + Right Click and select "Open command window here"
* Copy and paste the following command in the console:
  mvn install:install-file -Dfile=./identityiq.jar -DgroupId=iiq -DartifactId=identityiq -Dversion=7.0 -Dpackaging=jar
* Press Enter


Before using the "ImportFileCreator" utility

* Go to Run >> Run Configurations...
* Below "Java Application" click the profile for made for your application
* Go to the "Classpath" tab
* Click on "User Entries"
* Click on "Advanced..."
* Click on "Add Folder"
* Select the bin folder of your project (where the .class files are stored)
* Press "Ok"


Set JDK as runtime enviroment
* Go to Windows >> Preferences >> Java >> Installed JREs 
* Select your JRE 1.7 and click "Edit"
* Select the JRE folder of your JDK installation (i.e. C:\Program Files\Java\jdk1.7.0_79\jre)
* Click Finish
* Click Ok

