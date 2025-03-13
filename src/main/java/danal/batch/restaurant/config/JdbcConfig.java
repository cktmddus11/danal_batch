package danal.batch.restaurant.config;

import danal.batch.restaurant.meta.consts.DbConstStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcConfig {
    public static final String JDBC_TEMPLATE = "restaurant" + DbConstStrings.JDBC_TEMPLATE;
    public static final String NAMED_JDBC_TEMPLATE = "restaurant" + DbConstStrings.NAMED_JDBC_TEMPLATE;


    @Bean(JDBC_TEMPLATE)
    public JdbcTemplate jdbcTemplate(@Qualifier(DataSourceConfig.DS_BEAN_NAME) DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(NAMED_JDBC_TEMPLATE)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
            @Qualifier(DataSourceConfig.DS_BEAN_NAME) DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
