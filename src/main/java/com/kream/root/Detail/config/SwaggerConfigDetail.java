package com.kream.root.Detail.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@OpenAPIDefinition( //해당 Swagger 페이지가 무엇을 나타내는지 알려주기 위해 작성
//        info =  @Info(title = "kream Datail Page", //어떤 API 명세서를 위한 Swagger 페이지인지
//                description = "kream Detail swagger",
//                version = "v1"))
//@Configuration
//public class SwaggerConfigDetail {
//    @Bean
//    public GroupedOpenApi detailApi() {
//        return GroupedOpenApi.builder()
//                .group("DetailController")
//                .pathsToMatch("/detail/**") //Rest Controller로 연결할 path를 작성
//                .build();
//        }
//}