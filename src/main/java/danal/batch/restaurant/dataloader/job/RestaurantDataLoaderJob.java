package danal.batch.restaurant.dataloader.job;

import danal.batch.restaurant.dataloader.job.listener.DataLoaderJobExecutionListener;
import danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep;
import danal.batch.restaurant.meta.consts.BatchStrings;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RestaurantDataLoaderJob {
    public static final String BATCH_NAME = "restaurant-data-loader";
    public final DataLoaderJobExecutionListener jobExecutionListener;

    public final RestaurantDataLoaderStep dataLoaderStep;

    @Bean(BATCH_NAME + BatchStrings.JOB)
    public Job job(JobRepository jobRepository) throws Exception {
        return new JobBuilder(BATCH_NAME, jobRepository)
                .preventRestart()
                .listener(jobExecutionListener)
                .flow(dataLoaderStep.step())
                .end()
                .build();
    }


}
