// Classe de configuração do Swagger (OpenAPI).
// Responsável por gerar a documentação visual e interativa da API REST,
// permitindo a exploração e o teste dos endpoints diretamente pelo navegador.

package com.improel.samp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SAMP API - Sistema de Apontamento de Produção")
                        .version("1.0.0")
                        .description("API REST para o chão de fábrica e painel do gestor da Improel.")
                        .contact(new Contact()
                                .name("Suporte Improel")
                                .email("suporte@improel.com")));
    }
}
