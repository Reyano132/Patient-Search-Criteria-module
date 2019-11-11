![alt text](https://camo.githubusercontent.com/93680c923c12178e9fa6b523b1bbb644d32f4039/68747470733a2f2f74616c6b2e6f70656e6d72732e6f72672f75706c6f6164732f64656661756c742f6f726967696e616c2f32582f662f663165633537396230333938636230346338306135346335366461323139623234343066653234392e6a7067)

Patient Search Criteria Module
==========================

Description
-----------
This module provides the different patient search criteria which are used in core apps module

Building from Source
--------------------
You will need to have Java 1.8+ and Maven 3.x+ installed.  Use the command 'mvn package' to 
compile and package the module.  The .omod file will be in the omod/target folder.

Alternatively you can add the snippet provided in the [Creating Modules](https://wiki.openmrs.org/x/cAEr) page to your 
omod/pom.xml and use the mvn command:

    mvn package -P deploy-web -D deploy.path="../../openmrs-1.8.x/webapp/src/main/webapp"

It will allow you to deploy any changes to your web 
resources such as jsp or js files without re-installing the module. The deploy path says 
where OpenMRS is deployed.

Installation
------------
1. Build the module to produce the .omod file.
2. Use the OpenMRS Administration > Manage Modules screen to upload and install the .omod file.

If uploads are not allowed from the web (changable via a runtime property), you can drop the omod
into the ~/.OpenMRS/modules folder.  (Where ~/.OpenMRS is assumed to be the Application 
Data Directory that the running openmrs is currently using.)  After putting the file in there 
simply restart OpenMRS/tomcat and the module will be loaded and started.
