package danal.batch.restaurant.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Getter
@Setter
@ToString
public class DataSourceConfigProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private HikariConfig hikari = new HikariConfig();

    @Getter
    @Setter
    public static class HikariConfig {
        private int connectionTimeout;
        private int maximumPoolSize;
        private int minimumIdle;
        private int idleTimeout;
        private int maxLifetime;
    }
}
