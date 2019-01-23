package ua.com.foxminded.lerkasan.quickpoll.controller;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class SwaggerController {

    // https://dzone.com/articles/overcoming-swagger-annotation-overload-by-switchin
    // https://github.com/swagger-api/swagger-ui/issues/4744
    @GetMapping(path = "/v2/api-docs", produces = APPLICATION_JSON_VALUE)
    public Resource apiDocs() {
        return new ClassPathResource("static/swagger-votes.yaml");
    }
}