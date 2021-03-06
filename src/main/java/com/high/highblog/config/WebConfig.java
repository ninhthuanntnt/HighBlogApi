package com.high.highblog.config;

import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.entity.File;
import liquibase.pro.packaged.B;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class WebConfig
        implements WebMvcConfigurer {

    private final ApplicationConfigProperties.FileUpload fileUploadConfigProperties;
    private final ApplicationConfigProperties.ResourceTemplate resourceTemplateConfigProperties;
    private final ResourceLoader resourceLoader;

    public WebConfig(final ApplicationConfigProperties applicationConfigProperties,
                     final ResourceLoader resourceLoader) {
        this.fileUploadConfigProperties = applicationConfigProperties.getFileUpload();
        this.resourceTemplateConfigProperties = applicationConfigProperties.getResourceTemplate();
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public String registrationEmailTemplate() {
        Resource resource = resourceLoader.getResource(resourceTemplateConfigProperties.getRegistrationEmailTemplate());
        return FileHelper.asString(resource);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imageRootDir = fileUploadConfigProperties.getRootDir() + "/" + fileUploadConfigProperties
                .getImagesSubDir();
        registry.addResourceHandler("static/**",
                                    fileUploadConfigProperties.getImagesSubDir() + "/**")
                .addResourceLocations("classpath:static/",
                                      "file:" + imageRootDir + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("https://developer.mozilla.org")
                .maxAge(3600);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
