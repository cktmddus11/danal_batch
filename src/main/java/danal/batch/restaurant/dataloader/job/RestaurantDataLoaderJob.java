package danal.batch.restaurant.dataloader.job;

import danal.batch.restaurant.dataloader.job.listener.DataLoaderCustomJobExecutionListener;
import danal.batch.restaurant.dataloader.job.parameter.RestaurantJobParameter;
import danal.batch.restaurant.dataloader.job.step.RestaurantChunkStep;
import danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep;
import danal.batch.restaurant.dataloader.job.step.RestaurantTruncateTableStep;
import danal.batch.restaurant.dataloader.job.valid.RestaurantJobParameterValidator;
import danal.batch.restaurant.meta.consts.BatchConstStrings;
import danal.batch.restaurant.runnicrementer.CustomRunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep.MASTER_STEP_NAME;


@RequiredArgsConstructor
@Configuration
public class RestaurantDataLoaderJob {
    public static final String BATCH_NAME = "restaurant-data-loader";

  //  private final RestaurantJobParameterValidator jobParameterValidator;

    private final RestaurantTruncateTableStep truncateTableStep;
    private final RestaurantDataLoaderStep dataLoaderStep;

//    @Qualifier(MASTER_STEP_NAME + BatchConstStrings.STEP)
    private final RestaurantChunkStep restaurantChunkStep;

    @Qualifier(MASTER_STEP_NAME + "Partitioner")
    private final Partitioner partitioner;

    @Bean(BATCH_NAME + BatchConstStrings.JOB)
    public Job job(JobRepository jobRepository,
                   DataLoaderCustomJobExecutionListener jobExecutionListener) throws Exception {
        return new JobBuilder(BATCH_NAME, jobRepository)
               // .preventRestart() //  작업 재시작 방지. TODO 추후 삭제 => 실행시 고유 아이디를 주니까 필요없는거 같음.
              //  .validator(jobParameterValidator)
                .incrementer(new CustomRunIdIncrementer()) // 배치 작업(Job)의 실행 ID를 자동으로 증가시

                .listener(jobExecutionListener)
                .start(truncateTableStep.step())
                .next(dataLoaderStep.masterStep(restaurantChunkStep.workerStep(),partitioner))
                // TODO SKIP처리된 ROW 저장 : 실패사유, 원본데이터 등
                .build();
    }



}
