package lab.base.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * SpringDoc API文档相关配置
 * Created by macro on 2022/3/4.
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI restfulOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info().title("Spring Boot3 Restful LAB API")
                        .description("LAB Detail APi")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Wiki Documentation")
                        .url("https://springdoc.org/v2"));
        // oauth2.0 password
        openAPI.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme());
        //全局安全校验项，也可以在对应的controller上加注解SecurityRequirement
        openAPI.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
        return openAPI;

    }

    private SecurityScheme securityScheme() {
        SecurityScheme securityScheme = new SecurityScheme();
        //类型
        securityScheme.setType(SecurityScheme.Type.APIKEY);
        //请求头的name
        securityScheme.setName(HttpHeaders.AUTHORIZATION);
        //token所在未知
        securityScheme.setIn(SecurityScheme.In.HEADER);
        return securityScheme;
    }

}