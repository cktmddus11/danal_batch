package danal.batch.restaurant.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableConfigurationProperties(BatchProperties.class)
@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfig extends DefaultBatchConfiguration {
    @Value("${danal.batch.thread-pool-size:10}")
    private int threadPoolSize;

    @Qualifier(DataSourceConfig.DS_BEAN_NAME)
    private final DataSource dataSource;


    @Qualifier(DataSourceConfig.TX_MANAGER_BEAN_NAME)
    private final PlatformTransactionManager transactionManager;


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
    @Override
    public JobRepository jobRepository() throws BatchConfigurationException {
        try {
            JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
            factory.setDataSource(dataSource);
            factory.setTransactionManager(transactionManager);
            factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
            factory.setTablePrefix("BATCH_");
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create JobRepository", e);
        }
    }

    @Bean
    @Override
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        try {
            TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
            jobLauncher.setJobRepository(jobRepository);
//            jobLauncher.setTaskExecutor(taskExecutor());
            jobLauncher.afterPropertiesSet();
            return jobLauncher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 추후 데이터 소스 확장을 위해 데이터 소스를 직접 지정함.
    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }
}
