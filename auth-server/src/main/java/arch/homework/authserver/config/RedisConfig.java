package arch.homework.authserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ConfigurationProperties("redis.properties")
@Configuration
@Data
public class RedisConfig {

    @NotEmpty
    private String host;

    @NotNull
    private int port = 6379;

    @NotNull
    private String password;

}
