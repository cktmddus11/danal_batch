package danal.batch.restaurant.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableBatchProcessing
@Configuration
public class BatchConfig extends DefaultBatchConfiguration {
    @Value("${danal.batch.thread-pool-size:10}")
    private int threadPoolSize;


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher,
                                                                     JobExplorer jobExplorer,
                                                                     JobRepository jobRepository,
                                                                     BatchProperties properties) {
        // 실행할 JOB 이름을 읽어 동적으로 지정.
        JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobNames = properties.getJob().getName();
        if (StringUtils.isNotBlank(jobNames)) {
            runner.setJobName(jobNames);
        }
        return runner;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(threadPoolSize);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("batch-task-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
                jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        try {
            jobLauncher.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jobLauncher;
    }

}
