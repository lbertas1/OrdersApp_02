Multi-module application for making orders, includes order class and client.

Application characteristics:
- application written in java 15
- multi-module application divided into: persistence, service, ui, written in java
Persistence is a module that contains models, service contains business logic, and ui is used to communication.
- the application uses the gson library to convert data from and to json format.
- the data can come either from a json file or a plain text txt file.
The application join data from several txt files.
- the ui module uses the spark framework to generate http requests, 
and also allows communication with the user through consoles and convenient menu.
- the application has been tested using the JUnit 5 library. 
The project also uses the extension tool to generate test data from a json file.
- the application is available on both github and dockerhub:.