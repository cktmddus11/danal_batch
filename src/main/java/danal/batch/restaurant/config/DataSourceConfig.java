package danal.batch.restaurant.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DataSourceConfig {

    private final DataSourceConfigProperties dataSourceConfigProperties;

    public static final String DS_BEAN_NAME = "RESTAURANT_DATASOURCE";
    public static final String TX_MANAGER_BEAN_NAME = "RESTAURANT_DB_TX_MANAGER";

    @Primary
    @Bean(DS_BEAN_NAME)
    public DataSource dataSource() {
        log.info("{} DataSource build with properties: {}", DS_BEAN_NAME, dataSourceConfigProperties);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceConfigProperties.getUrl());
        dataSource.setUsername(dataSourceConfigProperties.getUsername());
        dataSource.setPassword(dataSourceConfigProperties.getPassword());
        dataSource.setDriverClassName(dataSourceConfigProperties.getDriverClassName());

        // HikariCP 설정
        dataSource.setConnectionTimeout(dataSourceConfigProperties.getHikari().getConnectionTimeout());
        dataSource.setMaximumPoolSize(dataSourceConfigProperties.getHikari().getMaximumPoolSize());
        dataSource.setMinimumIdle(dataSourceConfigProperties.getHikari().getMinimumIdle());
        dataSource.setIdleTimeout(dataSourceConfigProperties.getHikari().getIdleTimeout());
        dataSource.setMaxLifetime(dataSourceConfigProperties.getHikari().getMaxLifetime());

        return dataSource;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("danal.batch.restaurant.**.domain"); // TODO 모듈 구조 변경시 변경 필요
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(TX_MANAGER_BEAN_NAME)
    public PlatformTransactionManager platformTransactionManager(@Qualifier(DS_BEAN_NAME) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
