package de.kamiql.core.source.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class WebApp {
    private ConfigurableApplicationContext context;

    public void start() {
        context = new SpringApplicationBuilder(MySpringBootApplication.class)
                .properties("server.port=8080")
                .run();
    }

    public void stop() {
        if (context != null) {
            context.close();
        }
    }
}
