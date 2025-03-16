package danal.batch.restaurant.dataloader.job.step.item;

import danal.batch.restaurant.config.DataSourceConfig;
import danal.batch.restaurant.config.JdbcConfig;
import danal.batch.restaurant.dataloader.job.model.vo.RestaurantVo;
import danal.batch.restaurant.dataloader.job.repository.ResturantInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static danal.batch.restaurant.dataloader.job.step.RestaurantChunkStep.WORKER_STEP_NAME;


@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantJdbcBatchItemWriter {

    @Qualifier(DataSourceConfig.DS_BEAN_NAME)
    private final DataSource dataSource;

    private final ResturantInfoRepository repository;

    @Qualifier(JdbcConfig.NAMED_JDBC_TEMPLATE)
    private final  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Bean
    public JdbcBatchItemWriter<RestaurantVo> jdbcBatchItemWriter() {
        log.info(">>> {} - Writer Create!", WORKER_STEP_NAME);

        JdbcBatchItemWriter<RestaurantVo> writer = new JdbcBatchItemWriterBuilder<RestaurantVo>()
                .dataSource(dataSource)
                .sql(repository.insertRestaurantInfoSql())
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .assertUpdates(false) // 업데이트 확인 비활성화
                .build();

        writer.afterPropertiesSet(); // 초기화 확인
        return writer;
    }

}