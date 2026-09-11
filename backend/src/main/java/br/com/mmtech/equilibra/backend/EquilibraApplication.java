package br.com.mmtech.equilibra.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@SpringBootApplication
@EnableAsync
@Slf4j
public class EquilibraApplication {

    public static void main(String[] args) {
        var app = SpringApplication.run(EquilibraApplication.class, args);
        Environment environment = app.getEnvironment();
        logApplicationStartup(environment);
    }

    private static void logApplicationStartup(Environment environment) {
        String protocol = "http";
        if (environment.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        String activeProfiles = environment.getActiveProfiles().length == 0
                ? Arrays.toString(environment.getDefaultProfiles())
                : String.join(", ", environment.getActiveProfiles());
        String mensagemStartup = """
                Application '%s' is running! Access URLs:
                Local: %s://localhost:%s%s
                External: %s://%s:%s%s
                Profile(s): %s
                """.formatted(environment.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                activeProfiles);
        log.info(mensagemStartup);
    }

}
