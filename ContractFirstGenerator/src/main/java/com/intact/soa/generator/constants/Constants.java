package com.intact.soa.generator.constants;

public class Constants {

    private Constants() {
    }

    public static final byte[] buffer = new byte[2048];

    public static final String POM_FILENAME = "pom";
    public static final String RAW_PROJECT_NAME = "RawWebservice";
    public static final String POM_TEMPLATE_FILENAME = "pom_template";
    public static final String XSD_TEMPLATE_FILENAME = "xsd_template";
    public static final String WSDL_TEMPLATE_FILENAME = "wsdl_template";
    public static final String JAVA_TEMPLATE_FILENAME = "java_template";
    public static final String JAVA_CONFIG_TEMPLATE_FILENAME = "java_config_template";
    public static final String PROPERTIES_TEMPLATE_FILENAME = "properties_template";
    public static final String SOAPUI_PROJECT_TEMPLATE_FILENAME = "project_template";
    public static final String SOAPUI_INTERFACE_TEMPLATE_FILENAME = "interface_template";
    public static final String SOAPUI_DEFAULT_REQUEST_TEMPLATE_FILENAME = "request_template";
    public static final String SOAPUI_TESTSUITE_TEMPLATE_FILENAME = "testsuite_template";
    public static final String EXAMPLE_ENDPOINT_XSD_TEMPLATE_FILENAME = "ExampleEndpoint";
    public static final String EXAMPLE_ENDPOINT_IMPL_TEMPLATE_FILENAME = "ExampleEndpointPortImpl";
    public static final String UBI360_MANAGER_TEMPLATE_FILENAME = "Ubi360Manager";
    public static final String UBI360_MANAGER_IMPL_TEMPLATE_FILENAME = "Ubi360ManagerImpl";
    public static final String JAVA_CONFIG_ENDPOINT_TEMPLATE_FILENAME = "java_config_endpoint_template";
    public static final String WSDL_TYPES_TEMPLATE_FILENAME = "wsdl_types_template";
    public static final String WSDL_MESSAGE_TEMPLATE_FILENAME = "wsdl_message_template";
    public static final String WSDL_PORTTYPES_TEMPLATE_FILENAME = "wsdl_porttypes_template";
    public static final String WSDL_BINDINGS_TEMPLATE_FILENAME = "wsdl_bindings_template";
    public static final String WSDL_SERVICES_TEMPLATE_FILENAME = "wsdl_services_template";
    public static final String JUNIT_FILE_TEMPLATE_FILENAME = "junit_class_template";
    public static final String JUNIT_CONFIG_TEMPLATE_FILENAME = "junit_config_template";

    public static final String TEMPLATES_FOLDER = "templates";
    public static final String TEMPLATES_WSDL_PARTS_FOLDER = "templates\\wsdl_parts";
    public static final String TEMPLATES_EXAMPLE_PARTS_FOLDER = "templates\\example_parts";
    public static final String TEMPLATES_JUNIT_PARTS_FOLDER = "templates\\junit_parts";
    public static final String TEMPLATES_SOAPUI_PARTS_FOLDER = "templates\\soapui_parts";
    public static final String GENERATED_FILES_FOLDER = "generated_files";
    public static final String WSDL_DESTINATION_FOLDER = "src\\main\\resources\\wsdl\\v1";
    public static final String XSD_DESTINATION_FOLDER = "src\\main\\resources\\xsd\\v1";
    public static final String JAVA_DESTINATION_FOLDER = "src\\main\\java\\net\\intact\\webservice";
    public static final String COMPONENT_DESTINATION_FOLDER = "src\\main\\java\\net\\intact\\component";
    public static final String JAVA_CONFIG_DESTINATION_FOLDER = "src\\main\\java\\net\\intact\\config";
    public static final String PROPERTIES_DESTINATION_FOLDER = "src\\main\\resources";
    public static final String TEST_PROPERTIES_DESTINATION_FOLDER = "src\\test\\resources";
    public static final String TEST_JAVA_DESTINATION_FOLDER = "src\\test\\java\\net\\intact\\junit\\webservice";
    public static final String TEST_CONFIG_DESTINATION_FOLDER = "src\\test\\java\\net\\intact\\config";
    public static final String SOAPUI_DESTINATION_FOLDER = "soapUI";

    public static final String XML_EXTENTION = ".xml";
    public static final String WSDL_EXTENTION = ".wsdl";
    public static final String XSD_EXTENTION = ".xsd";
    public static final String JAVA_EXTENTION = ".java";
    public static final String ZIP_EXTENTION = ".zip";
    public static final String PROPERTIES_EXTENTION = ".properties";

    public static final String REQUEST = "Request";
    public static final String RESPONSE = "Response";

    public static final String POM_METACONTENT_GROUP_ID = "REPLACE_GROUP_ID";
    public static final String POM_METACONTENT_ARTIFACT_ID = "REPLACE_ARTIFACT_ID";
    public static final String POM_METACONTENT_WSDL_FILENAME = "REPLACE_WSDL_FILENAME";
    public static final String POM_METACONTENT_SERVICENAME = "REPLACE_SERVICENAME";
    public static final String POM_METACONTENT_DATA_BASE_DEPENDENCY = "DATA_BASE_DEPENDENCY";

    public static final String XSD_METACONTENT_ENDPOINT_NAMESPACE = "ENDPOINT_NAMESPACE";
    public static final String XSD_METACONTENT_ENDPOINT_REQUEST = "ENDPOINT_REQUEST";
    public static final String XSD_METACONTENT_ENDPOINT_RESPONSE = "ENDPOINT_RESPONSE";

    public static final String WSDL_METACONTENT_DEFINITION = "WSDL_DEFINITION";
    public static final String WSDL_METACONTENT_TYPES = "WSDL_TYPES";
    public static final String WSDL_METACONTENT_MESSAGES = "WSDL_MESSAGES";
    public static final String WSDL_METACONTENT_PORTTYPES = "WSDL_PORTTYPES";
    public static final String WSDL_METACONTENT_BINDINGS = "WSDL_BINDINGS";
    public static final String WSDL_METACONTENT_SERVICES = "WSDL_SERVICES";

    public static final String WSDL_METACONTENT_XS_IMPORT = "WSDL_XS_IMPORT";
    public static final String WSDL_METACONTENT_XS_NAMESPACE = "WSDL_XS_NAMESPACE";

    public static final String WSDL_METACONTENT_MESSAGE_REQUEST_NAME = "WSDL_MESSAGE_REQUEST_NAME";
    public static final String WSDL_METACONTENT_PART_REQUEST_ELEMENT = "WSDL_PART_REQUEST_ELEMENT";
    public static final String WSDL_METACONTENT_MESSAGE_RESPONSE_NAME = "WSDL_MESSAGE_RESPONSE_NAME";
    public static final String WSDL_METACONTENT_PART_RESPONSE_ELEMENT = "WSDL_PART_RESPONSE_ELEMENT";

    public static final String WSDL_METACONTENT_PORTYPE_NAME = "WSDL_PORTTYPE_NAME";
    public static final String WSDL_METACONTENT_PORTYPE_OPERATION = "WSDL_PORTTYPE_OPERATION";
    public static final String WSDL_METACONTENT_PORTYPE_MESSAGE_REQUEST = "WSDL_PORTTYPE_MESSAGE_REQUEST";
    public static final String WSDL_METACONTENT_PORTYPE_REQUEST = "WSDL_PORTTYPE_REQUEST";
    public static final String WSDL_METACONTENT_PORTYPE_URL_REQUEST = "WSDL_PORTTYPE_URL_REQUEST";
    public static final String WSDL_METACONTENT_PORTYPE_MESSAGE_RESPONSE = "WSDL_PORTTYPE_MESSAGE_RESPONSE";
    public static final String WSDL_METACONTENT_PORTYPE_RESPONSE = "WSDL_PORTTYPE_RESPONSE";
    public static final String WSDL_METACONTENT_PORTYPE_URL_RESPONSE = "WSDL_PORTTYPE_URL_RESPONSE";

    public static final String WSDL_METACONTENT_BINDINGS_NAME = "WSDL_BINDINGS_NAME";
    public static final String WSDL_METACONTENT_BINDINGS_PORTTYPE = "WSDL_BINDINGS_PORTTYPE";
    public static final String WSDL_METACONTENT_BINDINGS_OPERATION = "WSDL_BINDINGS_OPERATION";
    public static final String WSDL_METACONTENT_BINDINGS_OPERATION_URL = "WSDL_BINDINGS_OPERATION_URL";
    public static final String WSDL_METACONTENT_BINDINGS_REQUEST = "WSDL_BINDINGS_REQUEST";
    public static final String WSDL_METACONTENT_BINDINGS_RESPONSE = "WSDL_BINDINGS_RESPONSE";

    public static final String WSDL_METACONTENT_SERVICES_NAME = "WSDL_SERVICES_NAME";
    public static final String WSDL_METACONTENT_SERVICES_BINDING = "WSDL_SERVICES_BINDING";
    public static final String WSDL_METACONTENT_SERVICES_PORT = "WSDL_SERVICES_PORT";
    public static final String WSDL_METACONTENT_SERVICES_URL = "WSDL_SERVICES_URL";

    public static final String JAVA_METACONTENT_SERVICE_NAME = "SERVICE_NAME";
    public static final String JAVA_METACONTENT_PACKAGE_NAME = "PACKAGE_NAME";
    public static final String JAVA_METACONTENT_ENDPOINT_NAME = "ENDPOINT_NAME";
    public static final String JAVA_METACONTENT_ENDPOINT_LOWER_NAME = "ENDPOINT_LOWER_NAME";
    public static final String JAVA_METACONTENT_ENDPOINT_METHOD_NAME = "ENDPOINT_METHOD_NAME";

    public static final String JAVA_CONFIG_METACONTENT_ENDPOINTS_DEFINITION_IMPORTS = "ENDPOINTS_DEFINITION_IMPORTS";
    public static final String JAVA_CONFIG_METACONTENT_ENDPOINTS_IMPLEMENTATION_IMPORTS = "ENDPOINTS_IMPLEMENTATION_IMPORTS";
    public static final String JAVA_CONFIG_METACONTENT_ENDPOINTS_CONFIG = "ENDPOINTS_CONFIG";
    public static final String JAVA_CONFIG_METACONTENT_ENDPOINT_NAME = "ENDPOINT_NAME";
    public static final String JAVA_CONFIG_METACONTENT_LOWER_ENDPOINT_NAME = "ENDPOINT_LOWER_NAME";

    public static final String PROPERTIES_METACONTENT_ARTIFACTID = "ARTIFACTID";

    public static final String EXAMPLE_ENDPOINT_XSD_METACONTENT_NAMESPACE = "NAMESPACE";

    public static final String EXAMPLE_ENDPOINT_IMPL_METACONTENT_PACKAGE = "EXAMPLE_ENDPOINT_PACKAGE";

    public static final String JUNIT_IMPL_METACONTENT_PACKAGE = "PACKAGE";
    public static final String JUNIT_IMPL_METACONTENT_ENDPOINT = "ENDPOINT_NAME";
    public static final String JUNIT_IMPL_METACONTENT_ENDPOINT_LOWER = "LOWER_ENDPOINT_NAME";
    public static final String JUNIT_IMPL_METACONTENT_ENDPOINT_LOWERCAMEL = "LOWERCAMEL_ENDPOINT_NAME";

    public static final String JUNIT_CONFIG_METACONTENT_IMPORTS = "TEST_CONFIG_IMPORTS";
    public static final String JUNIT_CONFIG_METACONTENT_BEANS = "TEST_CONFIG_BEANS";

    public static final String SOAPUI_METACONTENT_UUID1 = "UUID1";
    public static final String SOAPUI_METACONTENT_UUID2 = "UUID2";
    public static final String SOAPUI_METACONTENT_SERVICE_NAME = "SERVICE_NAME";
    public static final String SOAPUI_METACONTENT_INTERFACE = "INTERFACE";
    public static final String SOAPUI_METACONTENT_TESTCASE = "TESTCASE";
    
    public static final String SOAPUI_INTERFACE_METACONTENT_UUID1 = "UUID1";
    public static final String SOAPUI_INTERFACE_METACONTENT_UUID2 = "UUID2";
    public static final String SOAPUI_INTERFACE_METACONTENT_UUID3 = "UUID3";
    public static final String SOAPUI_INTERFACE_METACONTENT_ENDPOINT_NAME = "ENDPOINT_NAME";
    public static final String SOAPUI_INTERFACE_METACONTENT_ARTIFACT_ID = "ARTIFACT_ID";
    public static final String SOAPUI_INTERFACE_METACONTENT_SERVICE_NAME = "SERVICE_NAME";
    public static final String SOAPUI_INTERFACE_METACONTENT_SERVICE_NAMESPACE = "SERVICE_NAMESPACE";
    public static final String SOAPUI_INTERFACE_METACONTENT_ENDPOINT_LOWERCAMEL_NAME = "ENDPOINT_LOWERCAMEL_NAME";
    public static final String SOAPUI_INTERFACE_METACONTENT_DEFAULT_REQUEST = "DEFAULT_REQUEST";

    public static final String SOAPUI_TESTSUITE_METACONTENT_UUID1 = "UUID1";
    public static final String SOAPUI_TESTSUITE_METACONTENT_UUID2 = "UUID2";
    public static final String SOAPUI_TESTSUITE_METACONTENT_UUID3 = "UUID3";
    public static final String SOAPUI_TESTSUITE_METACONTENT_UUID4 = "UUID4";
    public static final String SOAPUI_TESTSUITE_METACONTENT_UUID5 = "UUID5";
    public static final String SOAPUI_TESTSUITE_METACONTENT_ARTIFACT_ID = "ARTIFACT_ID";
    public static final String SOAPUI_TESTSUITE_METACONTENT_ENDPOINT_NAME = "ENDPOINT_NAME";
    public static final String SOAPUI_TESTSUITE_METACONTENT_ENDPOINT_LOWERCAMEL_NAME = "ENDPOINT_LOWERCAMEL_NAME";
    public static final String SOAPUI_TESTSUITE_METACONTENT_SERVICE_NAMESPACE = "SERVICE_NAMESPACE";
    public static final String SOAPUI_TESTSUITE_METACONTENT_SERVICE_NAME = "SERVICE_NAME";
    public static final String SOAPUI_TESTSUITE_METACONTENT_DEFAULT_REQUEST = "DEFAULT_REQUEST";

    public static final String EXAMPLE_ENDPOINT_NAME = "ExampleEndpoint";
}
