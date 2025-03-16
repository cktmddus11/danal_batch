package danal.batch.restaurant.dataloader.job.step;

import danal.batch.restaurant.dataloader.job.listener.CustomStepExecutionListener;
import danal.batch.restaurant.dataloader.job.partitioner.RangePartitioner;
import danal.batch.restaurant.comm.meta.consts.BatchConstStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import static danal.batch.restaurant.dataloader.job.step.RestaurantChunkStep.WORKER_STEP_NAME;


@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantDataLoaderStep {
    public static final String MASTER_STEP_NAME = "restaurant-data-loader-chunk-master";

    @Value("${danal.batch.chunk-size}")
    private int chunkSize;

    @Value("${danal.batch.skip-limit}")
    private int skipLimit;


    private final JobRepository jobRepository;

    private final CustomStepExecutionListener stepExecutionListener;

    private final static int GRID_SIZE = 5;

    @Qualifier(BatchConstStrings.TASK_EXECUTOR)
    private final TaskExecutor taskExecutor;


    @Value("${danal.batch.input.csv-file}")
    private String csvFilePath; // TODO 잡 파람으로 변경

//    private final RestaurantChunkStep restaurantChunkStep;

    /**
     * CSV 파일 라인 수를 세어 파티셔너 생성
     */
    @JobScope
    @Bean(MASTER_STEP_NAME + "Partitioner")
    public Partitioner partitioner() {
        long totalCount = 0;
        try (LineNumberReader reader = new LineNumberReader(new FileReader(csvFilePath))) {
            // 파일의 끝까지 빠르게 스킵
            reader.skip(Long.MAX_VALUE);
            // 헤더 한 줄 제외
            totalCount = reader.getLineNumber() - 1;
            log.info(">>> CSV totalCount: {}", totalCount);
        } catch (IOException e) {
            log.error(">>> CSV 파일 읽기 오류: {}", e.getMessage(), e);
            throw new RuntimeException("CSV 파일 라인 수 세는 중 오류 발생", e);
        }
        return new RangePartitioner(totalCount, GRID_SIZE);
    }

    /**
     * 마스터 스텝 정의 - 파티셔닝을 위한 설정
     * 주요 변경: 의존성 주입을 통해 Bean 메소드 직접 호출 방지
     */
    @Bean(MASTER_STEP_NAME + BatchConstStrings.STEP)
    @JobScope
    public Step masterStep(
            @Qualifier(WORKER_STEP_NAME + BatchConstStrings.STEP) Step workerStep,
            @Qualifier(MASTER_STEP_NAME + "Partitioner") Partitioner partitioner
    ) {
        return new StepBuilder(MASTER_STEP_NAME, jobRepository)
                .partitioner(WORKER_STEP_NAME, partitioner)  // 의존성 주입된 partitioner 사용
                .step(workerStep)  // 의존성 주입된 workerStep 사용
                .gridSize(GRID_SIZE)
                .taskExecutor(taskExecutor)
                .listener(stepExecutionListener)
                .build();
    }
}