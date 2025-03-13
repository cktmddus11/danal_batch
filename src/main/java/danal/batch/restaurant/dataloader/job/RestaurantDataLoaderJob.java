package danal.batch.restaurant.dataloader.job;

import danal.batch.restaurant.dataloader.job.listener.DataLoaderJobExecutionListener;
import danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep;
import danal.batch.restaurant.meta.consts.BatchConstStrings;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RestaurantDataLoaderJob {
    public static final String BATCH_NAME = "restaurant-data-loader";
    private final RestaurantDataLoaderStep dataLoaderStep;

    @Bean(BATCH_NAME + BatchConstStrings.JOB)
    public Job job(JobRepository jobRepository,
                   DataLoaderJobExecutionListener jobExecutionListener) throws Exception {
        return new JobBuilder(BATCH_NAME, jobRepository)
               // .preventRestart() //  작업 재시작 방지. TODO 추후 삭제 => 실행시 고유 아이디를 주니까 필요없는거 같음.
                .incrementer(new RunIdIncrementer()) // 배치 작업(Job)의 실행 ID를 자동으로 증가시

                .listener(jobExecutionListener)
//                .start(truncateStep())
                .flow(dataLoaderStep.step())
                .end()
                .build();
    }


}
