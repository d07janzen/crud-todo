package com.davidjanzen.crudtodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CrudTodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudTodoApplication.class, args);
	}

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.davidjanzen.crudtodo.controller"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Todo API").description("Documentation Todo API v1.0").build());
	}
}
