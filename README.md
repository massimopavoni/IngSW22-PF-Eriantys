# **IngSW22-PF-Eriantys**

[![Release](https://img.shields.io/badge/release-v1.0.0--alpha.8-red)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/releases)
[![Server](https://img.shields.io/badge/server-v1.0.8-blue)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/releases)
[![Client](https://img.shields.io/badge/client-none-lightgray)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/releases)
[![Java JDK](https://img.shields.io/badge/java%20jdk-17-brightgreen)](https://docs.oracle.com/en/java/javase/17/)
[![License](https://img.shields.io/badge/license-GPL--3.0-orange)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/blob/master/LICENSE)

Repository for **Software Engineering** exam final test.

The java project aims to simulate a Java implementation of the tabletop game **[Eriantys](https://www.craniocreations.it/prodotto/eriantys/)**, by **[Cranio Creations](https://www.craniocreations.it/)**.

## **Server**

Server application runs always on `localhost` (`127.0.0.1`) and must be run through command line with the command `java -jar Eriantys-Server-[version].jar (-[argument]=[value])*`.

Available optional arguments (to be used for defaults overriding):
|Argument|Type|Default|Description|
|----|----|----|----|
|port|integer|50666|server socket listening port|
|maxconn|integer|4|maximum simultaneous connections|
|polling|integer|200|polling interval for input directive execution (in ms)
|reslocations|string|bundled file location|location of file with paths to server configurations|

> Server resource locations specifies where JSON files for various configurations should be.<br>
The structure to be followed for these is visible inside the resources directory model and server configuration json files.<br>
Changing the locations and contents of those files is possible, and can be used for testing purposes and/or to bring various modifications to the core mechanics or data of the game.<br><br>
*It is strongly advised to check the usage of json files in the project sources, to make sure that you're following the constraints enforced by game rules and corresponding code implementation.*
