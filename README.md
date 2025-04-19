# **IngSW22-PF-Eriantys**

[![Release](https://img.shields.io/badge/release-v1.1.0-blue)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/releases)
[![Server](https://img.shields.io/badge/server-v1.1.0-blue)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/releases)
[![Client](https://img.shields.io/badge/client-v1.1.0-blue)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/releases)
[![Java JDK](https://img.shields.io/badge/java%20jdk-21-brightgreen)](https://docs.oracle.com/en/java/javase/21/)
[![License](https://img.shields.io/badge/license-GPL--2.0-orange)](https://github.com/massimopavoni/IngSW22-PF-Eriantys/blob/master/LICENSE)

Repository for **Software Engineering** exam final test.

The java project aims to provide a Java implementation of the tabletop game **[Eriantys](https://www.craniocreations.it/prodotto/eriantys/)**, by **[Cranio Creations](https://www.craniocreations.it/)**.

## **Server**

Server application must be run through command line with the command `java -jar Eriantys-Server-[version].jar (-[argument]=[value])*`.<br>
The program always runs on `localhost` (`127.0.0.1`); port forwarding, paired with a static ip, can be used to make the server accessible from the internet.

Available optional arguments (to be used for defaults overriding):
|Argument|Type|Default|Description|
|----|----|----|----|
|port|integer|50666|server socket listening port|
|maxconn|integer|4|maximum simultaneous connections|
|polling|integer|200|polling interval for input directive execution (in ms)
|reslocations|string|bundled file location|location of file with paths to server configurations|

> Server resource locations specifies where JSON files for various configurations should be.<br>
The structure to be followed for them is visible inside the resources' directory model and server configuration json files.<br>
Changing the locations and contents of those files is possible, and can be used for testing purposes and/or to bring various modifications to the core mechanics or data of the game.<br><br>
*It is strongly advised to check the usage of json files in the project sources, to make sure that you're following the constraints enforced by game rules and corresponding code implementation.*

## **Client**

Client application must be run through command line with the command `java -jar Eriantys-Client-[version].jar`.<br>
The program asks for server address and port (default `localhost:50666`), then allows to choose a preferred user interface (`cli/gui`).