package com.high.highblog.config;

import com.high.highblog.helper.FileHelper;
import com.high.highblog.model.entity.File;
import liquibase.pro.packaged.B;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@Configuration
@EnableSwagger2
@EnableAsync(proxyTargetClass = true)
public class WebConfig
        implements WebMvcConfigurer {

    private final ApplicationConfigProperties.FileUpload fileUploadConfigProperties;
    private final ApplicationConfigProperties.ResourceTemplate resourceTemplateConfigProperties;
    private final ApplicationConfigProperties.Cors corsConfigProperties;
    private final ResourceLoader resourceLoader;

    public WebConfig(final ApplicationConfigProperties applicationConfigProperties,
                     final ResourceLoader resourceLoader) {
        this.fileUploadConfigProperties = applicationConfigProperties.getFileUpload();
        this.resourceTemplateConfigProperties = applicationConfigProperties.getResourceTemplate();
        this.corsConfigProperties = applicationConfigProperties.getCors();
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
                .allowedOriginPatterns("*")
                .maxAge(3600);
    }

    @Bean(name="taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(3);
        threadPoolTaskExecutor.setQueueCapacity(600);
        threadPoolTaskExecutor.afterPropertiesSet();
        log.info("ThreadPoolTaskExecutor set");
        return new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor);
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
