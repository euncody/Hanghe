package com.example.ecommerce.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("e-Commerce API 문서")
                    .description("e-Commerce 사용자 API 문서입니다.")
                    .version("1.0.0")
            )
    }
}