package danal.batch.restaurant.dataloader.job.step;

import danal.batch.restaurant.config.DataSourceConfig;
import danal.batch.restaurant.dataloader.job.listener.CustomStepExecutionListener;
import danal.batch.restaurant.dataloader.job.listener.RestaurantDataLoaderStepSkipListener;
import danal.batch.restaurant.dataloader.job.model.vo.RestaurantVo;
import danal.batch.restaurant.dataloader.job.step.item.RestaurantDataCleansingProcessor;
import danal.batch.restaurant.dataloader.job.step.item.RestaurantJdbcBatchItemWriter;
import danal.batch.restaurant.listener.CustomChunkListener;
import danal.batch.restaurant.meta.consts.BatchConstStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantChunkStep {
    public static final String WORKER_STEP_NAME = "restaurant-data-loader-chunk-worker";
    public static final String ITEM_READER_NAME = "restaurant-csv-file";

    @Qualifier(ITEM_READER_NAME + BatchConstStrings.READER)
    private final SynchronizedItemStreamReader<Map<String, String>> csvFileReader;

    private final JobRepository jobRepository;

    private final RestaurantDataCleansingProcessor dataCleansingProcessor;
    private final RestaurantJdbcBatchItemWriter restaurantJdbcBatchItemWriter;

    private final RestaurantDataLoaderStepSkipListener skipListener;
    private final CustomChunkListener chunkListener;

    @Value("${danal.batch.chunk-size}")
    private int chunkSize;

    @Value("${danal.batch.skip-limit}")
    private int skipLimit;

    @Qualifier(DataSourceConfig.TX_MANAGER_BEAN_NAME)
    private final PlatformTransactionManager transactionManager;


    @Qualifier(BatchConstStrings.TASK_EXECUTOR)
    private final TaskExecutor taskExecutor;

    /**
     *
     * 워커 스텝 정의 - 실제 데이터 처리 담당
     */
    @Bean(WORKER_STEP_NAME + BatchConstStrings.STEP)
    @JobScope
    public Step workerStep() {
        return new StepBuilder(WORKER_STEP_NAME, jobRepository)
                .<Map<String, String>, RestaurantVo>chunk(chunkSize, transactionManager)
                .reader(csvFileReader)  // @StepScope로 생성된 reader가 자동으로 stepExecutionContext에 접근
                .processor(dataCleansingProcessor)
                .writer(restaurantJdbcBatchItemWriter.jdbcBatchItemWriter())
                .allowStartIfComplete(true)
              //  .listener(chunkListener) // chunk 리스너
                .faultTolerant()
                    .skipLimit(skipLimit)
                    .skip(Exception.class)
                    .listener(skipListener)
                    .processorNonTransactional()
                 //   .taskExecutor(taskExecutor)
                .build();
    }
}
