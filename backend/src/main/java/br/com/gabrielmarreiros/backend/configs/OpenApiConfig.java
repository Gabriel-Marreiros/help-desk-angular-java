package br.com.gabrielmarreiros.backend.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.version}")
    private String APP_VERSION;

    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                .info(
                    new Info()
                        .title("Help Desk Angular & Java - Backend")
                        .description("O Help Desk Angular & Java é uma aplicação criada para gerenciar a criação e atribuição de chamados. Além de possibilitar a criação, exclusão e atualização de usuários com diferentes perfis, como: técnico, cliente e administrador. Os clientes podem abrir chamados para os técnicos e os administradores conseguem inativar tecnicos e clientes.")
                        .version(APP_VERSION)
                        .contact(
                            new Contact()
                                .name("Gabriel Marreiros")
                                .url("https://www.gabrielmarreiros.com.br")
                                .email("gabriel.s.marreiros@hotmail.com")
                        )
                );
    }
}
