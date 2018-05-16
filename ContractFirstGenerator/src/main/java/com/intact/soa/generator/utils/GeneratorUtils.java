package com.intact.soa.generator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.intact.soa.generator.constants.Constants;

@Component
public class GeneratorUtils {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private HashMap<String, String> endpointsNamespace = new HashMap<>();
    
    // Generator params
    @Value("${default.projects.path}")
    private String projectsPath;
    @Value("${default.add.example.endpoint}")
    private Boolean addExampleEndpoint;

    // Service params
    @Value("${default.group.id}")
    private String groupId;
    @Value("${default.artifact.id}")
    private String artifactId;
    @Value("${default.namespace}")
    private String namespace;
    @Value("${default.service.name}")
    private String serviceName;
    @Value("#{'${default.endpoints}'.split(',')}")
    private List<String> endpoints;

    private GeneratorUtils() {
    }

    public void createPom() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.POM_FILENAME + Constants.XML_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.POM_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            List<String> fileContent = buffer.lines()
                .map(line -> line.replaceAll(Constants.POM_METACONTENT_GROUP_ID, groupId))
                .map(line -> line.replaceAll(Constants.POM_METACONTENT_ARTIFACT_ID, artifactId))
                .map(line -> line.replaceAll(Constants.POM_METACONTENT_WSDL_FILENAME, getLowerCamelCase(serviceName)))
                .map(line -> line.replaceAll(Constants.POM_METACONTENT_SERVICENAME, serviceName))
                .map(line -> line.replaceAll(Constants.POM_METACONTENT_DATA_BASE_DEPENDENCY, addExampleEndpoint ? getDataBaseDependency() : "" ))
                .collect(Collectors.toList());

            // Write the new pom.xml
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating pom.xml", e);
        }
    }

    public String getDataBaseDependency() {
        return new StringBuilder()
                .append("    <dependencies>").append(System.lineSeparator())
                .append("        <dependency>").append(System.lineSeparator())
                .append("            <groupId>org.springframework.boot</groupId>").append(System.lineSeparator())
                .append("            <artifactId>spring-boot-starter-data-jpa</artifactId>").append(System.lineSeparator())
                .append("        </dependency>").append(System.lineSeparator())
                .append("        <dependency>").append(System.lineSeparator())
                .append("            <groupId>com.oracle</groupId>").append(System.lineSeparator())
                .append("            <artifactId>ojdbc6</artifactId>").append(System.lineSeparator())
                .append("            <version>11.2.0.4</version>").append(System.lineSeparator())
                .append("        </dependency>").append(System.lineSeparator())
                .append("    </dependencies>").append(System.lineSeparator())
            .toString();
    }

    public void createXsd() {
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            if (!entry.getKey().equals(Constants.EXAMPLE_ENDPOINT_NAME)) {
                // Set the output file
                Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.XSD_DESTINATION_FOLDER + File.separator + entry.getKey() + Constants.XSD_EXTENTION);

                // Read from the template file
                Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.XSD_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
                try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    List<String> fileContent = buffer.lines()
                            .map(line -> line.replaceAll(Constants.XSD_METACONTENT_ENDPOINT_NAMESPACE, namespace + serviceName + "/v1/" + entry.getKey()))
                            .map(line -> line.replaceAll(Constants.XSD_METACONTENT_ENDPOINT_REQUEST, entry.getKey() + Constants.REQUEST))
                            .map(line -> line.replaceAll(Constants.XSD_METACONTENT_ENDPOINT_RESPONSE, entry.getKey() + Constants.RESPONSE))
                            .collect(Collectors.toList());

                    // Write the new XSD file
                    Files.write(path, fileContent);

                    logger.info("File {} successfuly generated!", path.getFileName());
                } catch (IOException e) {
                    logger.error("Error occured while generating xsd file", e);
                }
            }
        }
    }

    public void createWsdl() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.WSDL_DESTINATION_FOLDER + File.separator + getLowerCamelCase(serviceName) + Constants.WSDL_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.WSDL_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_DEFINITION, getWsdlDefinitionContent(namespace, serviceName)))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_TYPES, getWsdlTypesContent(namespace, serviceName)))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_MESSAGES, getWsdlMessagesContent()))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTTYPES, getWsdlPortTypesContent(namespace, serviceName)))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS, getWsdlBindingsContent(namespace, serviceName)))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_SERVICES, getWsdlServicesContent(serviceName)))
                    .collect(Collectors.toList());

            // Write the new WSDL file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.wsdl.", serviceName);
        }
    }

    private String getWsdlDefinitionContent(String serviceNamespace, String serviceName) {
        StringBuilder sb = new StringBuilder();
        String completeNamespace = serviceNamespace + serviceName;

        sb.append("\t").append("xmlns:tns").append("\t\t").append("= \"").append(completeNamespace).append("\"").append(System.lineSeparator());

        // Create list of definitions
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            sb.append("\t").append("xmlns:").append(entry.getValue()).append("\t\t");
            sb.append("= \"").append(completeNamespace).append("/v1/").append(entry.getKey()).append("\"").append(System.lineSeparator());
        }

        sb.append("\t").append("targetNamespace").append("\t").append("= \"").append(completeNamespace).append("\"").append(System.lineSeparator());
        sb.append("\t").append("name").append("\t\t\t").append("= \"").append(serviceName).append("\"");

        return sb.toString();
    }

    private String getWsdlTypesContent(String serviceNamespace, String serviceName) {
        StringBuilder sb = new StringBuilder();

        // Create list of imports
        StringBuilder sbi = new StringBuilder();
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            sbi.append(sbi.length() > 0 ? System.lineSeparator() : "");
            sbi.append("\t\t\t<xs:import ");
            sbi.append("namespace=\"").append(serviceNamespace).append(serviceName).append("/v1/").append(entry.getKey()).append("\" ");
            sbi.append("schemaLocation=\"").append("../../xsd/v1/").append(entry.getKey()).append(Constants.XSD_EXTENTION);
            sbi.append("\"/>");
        }

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_WSDL_PARTS_FOLDER + File.separator + Constants.WSDL_TYPES_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            buffer.lines()
                .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_XS_NAMESPACE, serviceNamespace + serviceName))
                .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_XS_IMPORT, sbi.toString()))
                .forEach(line -> sb.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            logger.error("Error occured while generating WSDL types content.", e);
        }
        return sb.toString();
    }

    private String getWsdlMessagesContent() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_WSDL_PARTS_FOLDER + File.separator + Constants.WSDL_MESSAGE_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                // Replace content
                buffer.lines()
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_MESSAGE_REQUEST_NAME, entry.getKey() + Constants.REQUEST))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PART_REQUEST_ELEMENT, entry.getValue() + ":" + entry.getKey() + Constants.REQUEST))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_MESSAGE_RESPONSE_NAME, entry.getKey() + Constants.RESPONSE))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PART_RESPONSE_ELEMENT, entry.getValue() + ":" + entry.getKey() + Constants.RESPONSE))
                    .forEach(line -> sb.append(line).append(System.lineSeparator()));
            } catch (IOException e) {
                logger.error("Error while generating wsdl message content.", e);
            }
        }

        return sb.toString();
    }

    private String getWsdlPortTypesContent(String serviceNamespace, String serviceName) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            String url = serviceNamespace + serviceName + "/" + entry.getKey() + "/";

            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_WSDL_PARTS_FOLDER + File.separator + Constants.WSDL_PORTTYPES_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                // Replace content
                buffer.lines()
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_NAME, entry.getKey()))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_OPERATION, getLowerCamelCase(entry.getKey())))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_MESSAGE_REQUEST, entry.getKey() + Constants.REQUEST))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_REQUEST, getLowerCamelCase(entry.getKey()) + Constants.REQUEST))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_URL_REQUEST, url + entry.getKey() + Constants.REQUEST))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_MESSAGE_RESPONSE, entry.getKey() + Constants.RESPONSE))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_RESPONSE, getLowerCamelCase(entry.getKey()) + Constants.RESPONSE))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_PORTYPE_URL_RESPONSE, url + entry.getKey() + Constants.RESPONSE))
                    .forEach(line -> sb.append(line).append(System.lineSeparator()));
            } catch (IOException e) {
                logger.error("Error while building wsdl port type content.", e);
            }
        }

        return sb.toString();
    }

    private String getWsdlBindingsContent(String serviceNamespace, String serviceName) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_WSDL_PARTS_FOLDER + File.separator + Constants.WSDL_BINDINGS_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                // Replace content
                buffer.lines()
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS_NAME, entry.getKey() + "Binding"))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS_PORTTYPE, entry.getKey()))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS_OPERATION, getLowerCamelCase(entry.getKey())))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS_OPERATION_URL, serviceNamespace + serviceName + "/" + entry.getKey()))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS_REQUEST, getLowerCamelCase(entry.getKey()) + Constants.REQUEST))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_BINDINGS_RESPONSE, getLowerCamelCase(entry.getKey()) + Constants.RESPONSE))
                    .forEach(line -> sb.append(line).append(System.lineSeparator()));
            } catch (IOException e) {
                logger.error("Error while building wsdl binding content.", e);
            }
        }

        return sb.toString();
    }

    private String getWsdlServicesContent(String serviceName) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_WSDL_PARTS_FOLDER + File.separator + Constants.WSDL_SERVICES_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                // Replace content
                buffer.lines()
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_SERVICES_NAME, entry.getKey()))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_SERVICES_BINDING, entry.getKey() + "Binding"))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_SERVICES_PORT, entry.getKey() + "Port"))
                    .map(line -> line.replaceAll(Constants.WSDL_METACONTENT_SERVICES_URL, "http://localhost:8080/" + serviceName + "/" + entry.getKey()))
                    .forEach(line -> sb.append(line).append(System.lineSeparator()));
            } catch (IOException e) {
                logger.error("Error while building wsdl port type content.", e);
            }
        }

        return sb.toString();
    }

    public void createJavaImpl() {
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            if (!entry.getKey().equals(Constants.EXAMPLE_ENDPOINT_NAME)) {
                // Set the output file
                Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.JAVA_DESTINATION_FOLDER + File.separator + entry.getKey() + "PortImpl" + Constants.JAVA_EXTENTION);

                // Read from the template file
                Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.JAVA_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
                try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                    // Replace content
                    List<String> fileContent = buffer.lines()
                        .map(line -> line.replaceAll(Constants.JAVA_METACONTENT_SERVICE_NAME, serviceName.toLowerCase()))
                        .map(line -> line.replaceAll(Constants.JAVA_METACONTENT_PACKAGE_NAME, getPackage(namespace)))
                        .map(line -> line.replaceAll(Constants.JAVA_METACONTENT_ENDPOINT_NAME, entry.getKey()))
                        .map(line -> line.replaceAll(Constants.JAVA_METACONTENT_ENDPOINT_LOWER_NAME, entry.getKey().toLowerCase()))
                        .map(line -> line.replaceAll(Constants.JAVA_METACONTENT_ENDPOINT_METHOD_NAME, getLowerCamelCase(entry.getKey())))
                        .collect(Collectors.toList());

                    // Write the new JAVA file
                    Files.write(path, fileContent);

                    logger.info("File {} successfuly generated!", path.getFileName());
                } catch (IOException e) {
                    logger.error("Error occured while generating pom.xml.", e);
                }
            }
        }
    }

    public void createConfig() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.JAVA_CONFIG_DESTINATION_FOLDER + File.separator + "ServiceConfig" + Constants.JAVA_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.JAVA_CONFIG_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.JAVA_CONFIG_METACONTENT_ENDPOINTS_DEFINITION_IMPORTS, getConfigEndpointsDefinitionImports(namespace, serviceName)))
                    .map(line -> line.replaceAll(Constants.JAVA_CONFIG_METACONTENT_ENDPOINTS_IMPLEMENTATION_IMPORTS, getConfigEndpointsimplementationImports()))
                    .map(line -> line.replaceAll(Constants.JAVA_CONFIG_METACONTENT_ENDPOINTS_CONFIG, getConfigEndpoint()))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating ServiceConfig.java.", e);
        }
    }

    private String getConfigEndpointsDefinitionImports(String serviceNamespace, String serviceName) {
        StringBuilder sb = new StringBuilder();

        endpointsNamespace.entrySet().stream()
            .forEach(entry -> sb.append("import ").append(getPackage(serviceNamespace)).append(".").append(serviceName.toLowerCase()).append(".").append(entry.getKey()).append(";").append(System.lineSeparator()));

        return sb.toString();
    }

    private String getConfigEndpointsimplementationImports() {
        StringBuilder sb = new StringBuilder();

        endpointsNamespace.entrySet().stream()
            .forEach(entry -> sb.append("import net.intact.webservice.").append(entry.getKey()).append("PortImpl;").append(System.lineSeparator()) );

        return sb.toString();
    }

    private String getConfigEndpoint() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.JAVA_CONFIG_ENDPOINT_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                // Replace content
                buffer.lines()
                    .map(line -> line.replaceAll(Constants.JAVA_CONFIG_METACONTENT_ENDPOINT_NAME, entry.getKey()))
                    .map(line -> line.replaceAll(Constants.JAVA_CONFIG_METACONTENT_LOWER_ENDPOINT_NAME, getLowerCamelCase(entry.getKey())))
                    .forEach(line -> sb.append(line).append(System.lineSeparator()));
            } catch (IOException e) {
                logger.error("Error while generating wsdl message content.", e);
            }
        }

        return sb.toString();
    }

    public void createProperties() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.PROPERTIES_DESTINATION_FOLDER + File.separator + "application" + Constants.PROPERTIES_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.PROPERTIES_TEMPLATE_FILENAME + Constants.PROPERTIES_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.PROPERTIES_METACONTENT_ARTIFACTID, artifactId))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.", path.getFileName(), e);
        }
    }

    public void createExampleEndpointFiles() {
        if(addExampleEndpoint) {
            createExampleEndpointXsd();
            createExampleEndpointPortImpl();
            createUbi360Manager();
            createUbi360ManagerImpl();
        }
    }

    private void createExampleEndpointXsd() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.XSD_DESTINATION_FOLDER + File.separator + Constants.EXAMPLE_ENDPOINT_NAME + Constants.XSD_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_EXAMPLE_PARTS_FOLDER + File.separator + Constants.EXAMPLE_ENDPOINT_XSD_TEMPLATE_FILENAME + Constants.XSD_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.EXAMPLE_ENDPOINT_XSD_METACONTENT_NAMESPACE, namespace + serviceName))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.", path.getFileName(), e);
        }
    }

    private void createExampleEndpointPortImpl() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.JAVA_DESTINATION_FOLDER + File.separator + "ExampleEndpointPortImpl" + Constants.JAVA_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_EXAMPLE_PARTS_FOLDER + File.separator + Constants.EXAMPLE_ENDPOINT_IMPL_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.EXAMPLE_ENDPOINT_IMPL_METACONTENT_PACKAGE, getPackage(namespace) + "." + serviceName.toLowerCase()))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.", path.getFileName(), e);
        }
    }

    private void createUbi360Manager() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.COMPONENT_DESTINATION_FOLDER + File.separator + "Ubi360Manager" + Constants.JAVA_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_EXAMPLE_PARTS_FOLDER + File.separator + Constants.UBI360_MANAGER_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.EXAMPLE_ENDPOINT_IMPL_METACONTENT_PACKAGE, getPackage(namespace) + "." + serviceName.toLowerCase()))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.", path.getFileName(), e);
        }
    }

    private void createUbi360ManagerImpl() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.COMPONENT_DESTINATION_FOLDER + File.separator + "Ubi360ManagerImpl" + Constants.JAVA_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_EXAMPLE_PARTS_FOLDER + File.separator + Constants.UBI360_MANAGER_IMPL_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.EXAMPLE_ENDPOINT_IMPL_METACONTENT_PACKAGE, getPackage(namespace) + "." + serviceName.toLowerCase()))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.", path.getFileName(), e);
        }
    }

    public void createTestFiles() {
        createTestsConfigFile();
        createTestsJunitFile();
    }

    private void createTestsConfigFile() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.TEST_CONFIG_DESTINATION_FOLDER + File.separator + "TestConfig" + Constants.JAVA_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_JUNIT_PARTS_FOLDER + File.separator + Constants.JUNIT_CONFIG_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replace(Constants.JUNIT_CONFIG_METACONTENT_IMPORTS, getTestConfigImports(namespace, serviceName)))
                    .map(line -> line.replace(Constants.JUNIT_CONFIG_METACONTENT_BEANS, getTestConfigBeans()))
                    .collect(Collectors.toList());

            // Write the new JAVA file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.", path.getFileName(), e);
        }
    }

    public String getTestConfigImports(String namespace, String serviceName) {
        StringBuilder sb = new StringBuilder();

        // For each endpoint
        endpointsNamespace.entrySet().stream()
            .forEach(entry -> sb.append("import ").append(getPackage(namespace)).append(".").append(serviceName.toLowerCase()).append(".").append(entry.getKey()).append(";").append(System.lineSeparator()));

        // For each endpoint
        endpointsNamespace.entrySet().stream()
            .forEach(entry -> sb.append("import net.intact.webservice.").append(entry.getKey()).append("PortImpl;").append(System.lineSeparator()));

        return sb.toString();
    }

    public String getTestConfigBeans() {
        StringBuilder result = new StringBuilder();

        String template = new StringBuilder()
            .append("    @Bean").append(System.lineSeparator())
            .append("    public ENDPOINT_NAME getENDPOINT_NAME() {").append(System.lineSeparator())
            .append("        return new ENDPOINT_NAMEPortImpl();").append(System.lineSeparator())
            .append("    }").append(System.lineSeparator())
            .toString();

        // For each endpoint
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            result.append(template.replaceAll("ENDPOINT_NAME", entry.getKey()));
            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    private void createTestsJunitFile() {
        // For each endpoint
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            // Set the output file
            Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.TEST_JAVA_DESTINATION_FOLDER + File.separator + entry.getKey() + "Test" + Constants.JAVA_EXTENTION);

            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_JUNIT_PARTS_FOLDER + File.separator + Constants.JUNIT_FILE_TEMPLATE_FILENAME + Constants.JAVA_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                // Replace content
                List<String> fileContent = buffer.lines()
                        .map(line -> line.replaceAll(Constants.JUNIT_IMPL_METACONTENT_PACKAGE, getPackage(namespace) + "." + serviceName.toLowerCase()))
                        .map(line -> line.replaceAll(Constants.JUNIT_IMPL_METACONTENT_ENDPOINT_LOWERCAMEL, getLowerCamelCase(entry.getKey())))
                        .map(line -> line.replaceAll(Constants.JUNIT_IMPL_METACONTENT_ENDPOINT_LOWER, entry.getKey().toLowerCase()))
                        .map(line -> line.replaceAll(Constants.JUNIT_IMPL_METACONTENT_ENDPOINT, entry.getKey()))
                        .collect(Collectors.toList());

                // Write the new JAVA file
                Files.write(path, fileContent);

                logger.info("File {} successfuly generated!", path.getFileName());
            } catch (IOException e) {
                logger.error("Error occured while generating {}.", path.getFileName(), e);
            }
        }
    }

    public void createStructure() {
        // Get template zip
        Resource resource = new ClassPathResource(Constants.TEMPLATES_FOLDER + File.separator + Constants.RAW_PROJECT_NAME + Constants.ZIP_EXTENTION);

        // Unzip the template
        try (ZipInputStream zis = new ZipInputStream( resource.getInputStream() )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if(!addExampleEndpoint && isExampleFile(entry.getName())) {
                    continue;
                }

                final File file = new File(projectsPath + File.separator + entry.getName().replaceAll(Constants.RAW_PROJECT_NAME, artifactId));
                if (entry.isDirectory()) {
                    file.mkdirs();
                }
                else {
                    try (OutputStream out = new FileOutputStream(file)) {
                        file.getParentFile().mkdirs();
                        int length;
                        while ((length = zis.read(Constants.buffer)) >= 0) {
                            out.write(Constants.buffer, 0, length);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error when unzipping {} file", Constants.RAW_PROJECT_NAME + Constants.ZIP_EXTENTION, e);
        }
    }

    public void createSoapuiProject() {
        // Set the output file
        Path path = Paths.get(projectsPath + File.separator + artifactId + File.separator + Constants.SOAPUI_DESTINATION_FOLDER + File.separator + serviceName + "SoapuiProject" + Constants.XML_EXTENTION);

        // Read from the template file
        Resource resource = new ClassPathResource(Constants.TEMPLATES_SOAPUI_PARTS_FOLDER + File.separator + Constants.SOAPUI_PROJECT_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            // Replace content
            List<String> fileContent = buffer.lines()
                    .map(line -> line.replaceAll(Constants.SOAPUI_METACONTENT_UUID1, UUID.randomUUID().toString()))
                    .map(line -> line.replaceAll(Constants.SOAPUI_METACONTENT_UUID2, UUID.randomUUID().toString()))
                    .map(line -> line.replaceAll(Constants.SOAPUI_METACONTENT_SERVICE_NAME, serviceName))
                    .map(line -> line.replaceAll(Constants.SOAPUI_METACONTENT_INTERFACE, getSoapuiInterface(artifactId, namespace, serviceName)))
                    .map(line -> line.replaceAll(Constants.SOAPUI_METACONTENT_TESTCASE, getSoapuiTestCase(artifactId, namespace, serviceName)))
                    .collect(Collectors.toList());

            // Write the new WSDL file
            Files.write(path, fileContent);

            logger.info("File {} successfuly generated!", path.getFileName());
        } catch (IOException e) {
            logger.error("Error occured while generating {}.wsdl.", serviceName);
        }
    }

    private String getSoapuiInterface(String artifactId, String namespace, String serviceName) {
        StringBuilder sb = new StringBuilder();
        
        // For each endpoint
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            // Read from the template file
            Resource resource = new ClassPathResource(Constants.TEMPLATES_SOAPUI_PARTS_FOLDER + File.separator + Constants.SOAPUI_INTERFACE_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                List<String> fileContent = buffer.lines()
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_UUID1, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_UUID2, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_UUID3, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_ENDPOINT_NAME, entry.getKey()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_ARTIFACT_ID, artifactId))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_SERVICE_NAMESPACE, namespace))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_SERVICE_NAME, serviceName))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_ENDPOINT_LOWERCAMEL_NAME, getLowerCamelCase(entry.getKey())))
                        .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_DEFAULT_REQUEST, getSoapuiDefaultRequest(namespace, serviceName, entry.getKey())))
                        .collect(Collectors.toList());

                for (String line : fileContent) {
                    sb.append(line).append(System.lineSeparator());
                }
                sb.append(System.lineSeparator());
            } catch (IOException e) {
                logger.error("Error occured while generating WSDL types content.", e);
            }
        }

        return sb.toString();
    }

    private String getSoapuiTestCase(String artifactId, String namespace, String serviceName) {
        StringBuilder sb = new StringBuilder();

        // For each endpoint
        for (Map.Entry<String, String> entry : endpointsNamespace.entrySet()) {
            Resource resource = new ClassPathResource(Constants.TEMPLATES_SOAPUI_PARTS_FOLDER + File.separator + Constants.SOAPUI_TESTSUITE_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                List<String> fileContent = buffer.lines()
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_UUID1, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_UUID2, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_UUID3, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_UUID4, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_UUID5, UUID.randomUUID().toString()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_ARTIFACT_ID, artifactId))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_ENDPOINT_NAME, entry.getKey()))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_ENDPOINT_LOWERCAMEL_NAME, getLowerCamelCase(entry.getKey())))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_SERVICE_NAMESPACE, namespace))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_SERVICE_NAME, serviceName))
                        .map(line -> line.replaceAll(Constants.SOAPUI_TESTSUITE_METACONTENT_DEFAULT_REQUEST, getSoapuiDefaultRequest(namespace, serviceName, entry.getKey())))
                        .collect(Collectors.toList());

                for (String line : fileContent) {
                    sb.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                logger.error("Error occured while generating WSDL types content.", e);
            }
        }

        return sb.toString();
    }

    private String getSoapuiDefaultRequest(String namespace, String serviceName, String endpointName) {
        StringBuilder sb = new StringBuilder();

        Resource resource = new ClassPathResource(Constants.TEMPLATES_SOAPUI_PARTS_FOLDER + File.separator + Constants.SOAPUI_DEFAULT_REQUEST_TEMPLATE_FILENAME + Constants.XML_EXTENTION);
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            buffer.lines()
                .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_ENDPOINT_NAME, endpointName))
                .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_SERVICE_NAMESPACE, namespace))
                .map(line -> line.replaceAll(Constants.SOAPUI_INTERFACE_METACONTENT_SERVICE_NAME, serviceName))
                .forEach(line -> sb.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            logger.error("Error occured while generating WSDL types content.", e);
        }

        return sb.toString();
    }

    private Boolean isExampleFile(String file) {
        return file.endsWith("Ubi360Manager.java") ||
            file.endsWith("Ubi360ManagerImpl.java") ||
            file.endsWith("Request.java") ||
            file.endsWith("UbiClient.java") ||
            file.endsWith("ClientInfo.java") ||
            file.endsWith("UbiClientRepository.java");
    }

    private String getPackage(String namespace) {
        String domain = namespace.replace("http://", "").split("/")[0];
        LinkedList<String> path = new LinkedList<>(Arrays.asList(namespace.replace("http://", "").split("/")));
        path.removeFirst();

        List<String> domainList = Arrays.asList(domain.split("\\."));
        Collections.reverse(domainList);

        return String.join(".", domainList) + "." + String.join(".", path);
    }

    private String getInitials(String name) {
        StringBuilder result = new StringBuilder();
        name.chars().filter(Character::isUpperCase).forEach(c -> result.append((char) c));
        return result.toString().toLowerCase();
    }

    private String getLowerCamelCase(String name) {
        char[] c = name.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public void initializeValues() {
        // If adding example endpoint
        if(addExampleEndpoint) {
            endpoints.add(Constants.EXAMPLE_ENDPOINT_NAME);
        }

        // Check initials for each endpoint
        HashMap<String, List<String>> temp = new HashMap<>();
        for (String endpoint : endpoints) {
            String key = getInitials(endpoint);

            if (temp.containsKey(key)) {
                List<String> current = temp.get(key);
                current.add(endpoint);
                temp.put(key, current);
            } else {
                List<String> newEntry = new ArrayList<>();
                newEntry.add(endpoint);
                temp.put(key, newEntry);
            }
        }

        // Create a mapping with unique initials for each endpoint
        for (Map.Entry<String, List<String>> entry : temp.entrySet()) {
            int i = 1;
            for (String endpoint : entry.getValue()) {
                String initials = entry.getKey() + ((i > 1) ? i++ : "");
                endpointsNamespace.put(endpoint, initials);
            }
        }
    }

}
