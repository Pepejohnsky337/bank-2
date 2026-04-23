package com.bank.publicinfo.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class CustomInfoContributor implements InfoContributor {

    private final BuildProperties buildProperties;
    private final long startupTime;
    private final String contextPath;

    public CustomInfoContributor(
            BuildProperties buildProperties,
            @Value("${server.servlet.context-path:/}") String contextPath
    ) {
        this.buildProperties = buildProperties;
        this.startupTime = System.currentTimeMillis();
        this.contextPath = contextPath;
    }

    @Override
    public void contribute(Info.Builder builder) {
        final String formattedStartupTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(startupTime), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        builder
                .withDetail("app-name", "public-info-app")
                .withDetail("artifactId", buildProperties.getArtifact())
                .withDetail("startup-time", formattedStartupTime)
                .withDetail("version", buildProperties.getVersion())
                .withDetail("context-path", contextPath);
    }
}
