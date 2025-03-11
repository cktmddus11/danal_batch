package danal.batch.restaurant.dataloader.job.step;

import danal.batch.restaurant.config.DataSourceConfig;
import danal.batch.restaurant.dataloader.domain.Restaurant;
import danal.batch.restaurant.dataloader.job.step.item.RestaurantCsvFileReader;
import danal.batch.restaurant.dataloader.job.step.item.RestaurantDataCleansingProcessor;
import danal.batch.restaurant.listener.RestaurantDataLoaderStepSkipListener;
import danal.batch.restaurant.meta.consts.BatchStrings;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

import static danal.batch.restaurant.dataloader.job.RestaurantDataLoaderJob.BATCH_NAME;

@RequiredArgsConstructor
@Component
public class RestaurantDataLoaderStep {
    public static final String STEP_NAME = "restaurant-data-loader-chunk";
    public static final String ITEM_READER_NAME = "restaurant-csv-file";
//    public static final String ITEM_PROCESSOR_NAME = "restaurant-data-cleansing";
//    public static final String ITEM_WRITER_NAME = "restaurant_writer";


    @Value("${danal.batch.chunk-size}")
    private int chunkSize;

    @Value("${danal.batch.skip-limit}")
    private int skipLimit;

    @Qualifier(DataSourceConfig.TX_MANAGER_BEAN_NAME)
    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    private final RestaurantCsvFileReader csvFileReader;
    private final RestaurantDataCleansingProcessor dataCleansingProcessor;
    private final RestaurantDataLoaderStepSkipListener skipListener;

    @Bean(STEP_NAME + BatchStrings.STEP)
    @JobScope
    public Step step() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Map<String, String>, Restaurant>chunk(chunkSize, transactionManager)
                .reader(csvFileReader.flatFileReader())
                .processor(dataCleansingProcessor)
//                .writer(restaurantItemWriter())
                .faultTolerant()
                    .skipLimit(skipLimit)
                    .skip(Exception.class)
                    .listener(skipListener)
//                    .taskExecutor(taskExecutor)
//                    .throttleLimit(10)
                .build();

    }


}
