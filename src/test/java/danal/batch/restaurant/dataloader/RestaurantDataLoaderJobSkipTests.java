package danal.batch.restaurant.dataloader;

import danal.batch.restaurant.config.JdbcConfig;
import danal.batch.restaurant.dataloader.job.RestaurantDataLoaderJob;
import danal.batch.restaurant.dataloader.job.model.vo.RestaurantVo;
import danal.batch.restaurant.dataloader.job.step.item.RestaurantJdbcBatchItemWriter;
import danal.batch.restaurant.meta.consts.BatchConstStrings;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 배치 skip
 * 설정 프로퍼티에 있는 값읽어서 테스트
 *
 * [옵션]
 * skip limit 10
 * chunk 10
 * file : data/csv/restaurant_temp_skip10_data.csv
 *   skip 대상 11건 파일
 */
@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
public class RestaurantDataLoaderJobSkipTests {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier(RestaurantDataLoaderJob.BATCH_NAME + BatchConstStrings.JOB)
    Job job;

    @Autowired
    @Qualifier(JdbcConfig.JDBC_TEMPLATE)
    JdbcTemplate jdbcTemplate;

    //private JobParameters jobParameters;

    final static String filePath = "data/csv/restaurant_temp_skip10_data.csv";
    @Autowired
    private RestaurantJdbcBatchItemWriter restaurantJdbcBatchItemWriter;
 /*   @BeforeEach
    void setUp() {
        jobParameters = new JobParametersBuilder()
                .addString("csvFilePath", filePath)  // 공통 Job 파라미터 설정
                .toJobParameters();
    }*/

    @Test
    public void testSkipLimitExceeded() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // 결과 검증: 스킵 한도 초과로 인해 Job은 FAILED 상태
        assertEquals(BatchStatus.FAILED, jobExecution.getStatus());

        // 예외 메시지 확인
        String exitDescription = jobExecution.getExitStatus().getExitDescription();
        assertTrue(exitDescription.contains("Skip limit of '10' exceeded"),
                "Exit description should contain info about exceeded skip limit");

        // 스킵 카운트 확인
        int skipTotalCount = (int) jobExecution.getStepExecutions().iterator().next().getSkipCount();
        assertEquals(0, skipTotalCount); // skip limit을 초과하였기 때문에 모두 롤백되며 스킵카운트도 없다.
    }
}
