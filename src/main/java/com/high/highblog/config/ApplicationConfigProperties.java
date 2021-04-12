package com.high.highblog.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "application")
@NoArgsConstructor
@Getter
@Setter
public class ApplicationConfigProperties {

    private Jwt jwt;
    private FileUpload fileUpload;
    private ResourceTemplate resourceTemplate;
    private Cors cors;
    private Paypal paypal;

    @Getter
    @Setter
    public static class FileUpload {
        private Set<String> allowedImageExtensions;
        private String rootDir;
        private String imagesSubDir;
    }

    @Getter
    @Setter
    public static class Jwt {
        private String secretKey;
        private String issuer;
        private Long expiration;
        private Long refreshExpiration;
    }

    @Getter
    @Setter
    public static class ResourceTemplate {
        private String registrationEmailTemplate;
    }

    @Getter
    @Setter
    public static class Cors {
        private String allowedOrigin;
    }

    @Getter
    @Setter
    public static class Paypal{
        private String clientId;
        private String clientSecret;
        private String environment;
    }
}
