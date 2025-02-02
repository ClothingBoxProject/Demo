package com.first.demo.config;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.load();

        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            environment.getSystemProperties().put(key, value);
        });
    }
}
