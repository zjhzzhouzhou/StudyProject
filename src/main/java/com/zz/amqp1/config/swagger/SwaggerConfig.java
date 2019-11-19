package com.zz.amqp1.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description: swagger 配置
 * User: zhouzhou
 * Date: 2019-03-28
 * Time: 8:43 AM
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .withMethodAnnotation(ApiOperation.class)).build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("hahahah")
                .description("健康导航 api")
                .termsOfServiceUrl("浙江融创")
                .version("1.0.0")
                .build();
    }

}
