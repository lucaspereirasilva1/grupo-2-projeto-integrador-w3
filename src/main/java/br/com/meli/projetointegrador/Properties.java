package br.com.meli.projetointegrador;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "br.com.meli.projetointegrador")
@Data
public class Properties {

    private String jwtSecret;
    private Integer jwtExpirationMs;

}
