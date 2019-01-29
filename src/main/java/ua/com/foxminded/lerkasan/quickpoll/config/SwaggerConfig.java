package ua.com.foxminded.lerkasan.quickpoll.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket v1Api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.com.foxminded.lerkasan.quickpoll.v1.controller"))
                .paths(PathSelectors.regex("(?!/error).+"))
                .paths(PathSelectors.regex("(/api/v1/).+"))
                .build()
                .apiInfo(apiInfo()).groupName("v1")
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket v2Api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.com.foxminded.lerkasan.quickpoll.v2.controller"))
                .paths(PathSelectors.regex("(?!/error).+"))
                .paths(PathSelectors.regex("(/api/v2/).+"))
                .build()
                .apiInfo(apiInfo()).groupName("v2")
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket v3Api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.com.foxminded.lerkasan.quickpoll.v3.controller"))
                .paths(PathSelectors.regex("(?!/error).+"))
                .paths(PathSelectors.regex("(/api/v3/).+"))
                .build()
                .apiInfo(apiInfo()).groupName("v3")
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Quickpoll RESTful API",
                "RESTful API for Quickpoll",
                "1.0",
                "Terms of service",
                new Contact("Valeriia Lapytska", "www.example.com", "lerkasan@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}