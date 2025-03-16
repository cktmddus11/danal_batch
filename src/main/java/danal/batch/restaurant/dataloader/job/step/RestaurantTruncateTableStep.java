package danal.batch.restaurant.dataloader.job.step;

import danal.batch.restaurant.config.DataSourceConfig;
import danal.batch.restaurant.dataloader.job.repository.ResturantInfoRepository;
import danal.batch.restaurant.dataloader.job.listener.CustomStepExecutionListener;
import danal.batch.restaurant.meta.consts.BatchConstStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantTruncateTableStep {
    public static final String STEP_NAME = "restaurant-truncate-table";

    @Qualifier(DataSourceConfig.TX_MANAGER_BEAN_NAME)
    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    private final ResturantInfoRepository repository;


    @Bean(STEP_NAME + BatchConstStrings.STEP)
    @JobScope // Step 실행 시점에 Bean이 생성
    public Step step() {
        return new StepBuilder(STEP_NAME + BatchConstStrings.STEP, jobRepository)
                .tasklet((stepContribution, context) -> {
                    repository.truncateResturantInfo();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(new CustomStepExecutionListener())
                .allowStartIfComplete(true)
                .build();
    }


}
