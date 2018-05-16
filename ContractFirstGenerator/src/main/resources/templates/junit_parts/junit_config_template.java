package net.intact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

TEST_CONFIG_IMPORTS
@Configuration
@ComponentScan({ "net.intact" })
public class TestConfig {

TEST_CONFIG_BEANS
}
