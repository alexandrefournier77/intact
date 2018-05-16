Contract First Generator
===================

This tool will automaticaly generate a new contract first webservice project. The generated project will inherit from [Spring Boot Webservice parent POM](https://githubifc.iad.ca.inet/SOA/parent-pom-springboot/tree/master/Webservice) and so it will be based on Spring Boot 1.5.12


How to
-------------

1. Edit the ContractFirstGenerator.properties file:
   - DESTINATION_FOLDER - The destination folder where the new project will be created
   - ADD_EXAMPLE_ENDPOINT - A boolean that will define if an example endpoint should be also generated
2. Execute the generate.bat file and follow the given instructions
3. Import the project on Eclipse and deploy using one of the following options:
   - Right click on ServiceApp.java and choose Run As > Java Application (This will launch a embedded Tomcat)
   - Add project to Tomcat and start server


Parameters description
-------------

#### Group ID

Insert the Group ID that represents the domain where the service will be available
> Ex: intact.soa.client.entity.soa-service


#### Artifact ID

Insert the Artifact ID that will identify your service
> Ex: soa-service-test


#### Namespace

Insert the namespace for your service
> Ex: http://soa.intact.net/client/entity/


#### Service name

Insert the name of your service
> Ex: ServiceTest


#### Service endpoints

Insert all the endpoints your service will be serving separated by a comma
> Ex: FirstEndpoint, SecoundEndpoint, ThirdEndpoint, FourthEndpoint, FifthEndpoint