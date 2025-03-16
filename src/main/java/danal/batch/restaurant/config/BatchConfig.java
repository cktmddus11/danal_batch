package danal.batch.restaurant.config;

import danal.batch.restaurant.comm.meta.consts.BatchConstStrings;
import lombok.RequiredArgsConstructor;
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

    @Bean(BatchConstStrings.TASK_EXECUTOR)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 스레드로 청크 기반 작업을 병렬로 처리하기 때문에, task thread 수와 HikariCP 커넥션 수를 맞춰서 사용.
        executor.setCorePoolSize(threadPoolSize); // 기본 유지 스레드 수 - 실행 작업이 없어도 최소 10개의 스레드가 살아있음.
        executor.setMaxPoolSize(threadPoolSize); //  풀의 최대 스레드 수

        // 작업 대기 큐의 크기
        executor.setQueueCapacity(200); //  10개의 스레드가 모두 사용중일 때 추가로 들어오는 작업은 이 큐에 최대 50개까지 대기
                                        // (corePoolSize * 청크사이즈 * 2) 이상으로 설정하지만 개발환경을 고려하여 100 ~ 500 으로 설정
        
        executor.setThreadNamePrefix("batch-task-");  // 스레드 prefix ex) batch-task-1...

        executor.setWaitForTasksToCompleteOnShutdown(true); // 스프링 컨텍스트가 종료될 때 모든 작업이 끝날때까지 기다리고 종료
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
           //factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
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
