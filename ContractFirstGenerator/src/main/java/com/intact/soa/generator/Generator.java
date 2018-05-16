package com.intact.soa.generator;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import com.intact.soa.generator.utils.GeneratorUtils;

@SpringBootApplication
public class Generator {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    @Autowired
    private GeneratorUtils generatorUtils;

    public static void main(String[] args) {
        SpringApplication.run(Generator.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (validateArgs(args)) {
                generatorUtils.initializeValues();

                generatorUtils.createStructure();

                generatorUtils.createPom();
                generatorUtils.createWsdl();
                generatorUtils.createXsd();
                generatorUtils.createJavaImpl();
                generatorUtils.createConfig();
                generatorUtils.createProperties();

                generatorUtils.createExampleEndpointFiles();
                generatorUtils.createTestFiles();
                generatorUtils.createSoapuiProject();
            } else {
                logger.warn("Nothing executed, verify your input arguments");
            }
        };
    }

    private Boolean validateArgs(String[] args) {
        if (args.length == 0) {
            return !(Arrays.asList(projectsPath, groupId, artifactId, namespace, serviceName).stream().anyMatch(Objects::isNull) || CollectionUtils.isEmpty(endpoints));
        } else if (args.length == 7) {
            if(Arrays.stream(args).anyMatch(Objects::isNull))
                return false;
            initializeArguments(args);
            return true;
        } else {
            return false;
        }
    }

    private void initializeArguments(String[] args) {
        groupId             = args[0];
        artifactId          = args[1];
        namespace           = args[2];
        serviceName         = args[3];
        endpoints           = Arrays.asList(args[4].replaceAll("\\s+", "").split(","));
        projectsPath        = args[5];
        addExampleEndpoint  = Boolean.valueOf(args[6]);
    }

}
