package danal.batch.restaurant.dataloader;

import danal.batch.restaurant.config.JdbcConfig;
import danal.batch.restaurant.dataloader.job.RestaurantDataLoaderJob;
import danal.batch.restaurant.dataloader.job.model.vo.RestaurantVo;
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
 * 배치 동작 및 데이터 정합성 확인
 * 설정 프로퍼티에 있는 값읽어서 테스트
 *
 * [옵션]
 * skip limit 10 
 * chunk 10
 * file : data/csv/restaurant_temp_data.csv 
 *   skip 대상 2건 파일
 */
@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
public class RestaurantDataLoaderJobTests {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier(RestaurantDataLoaderJob.BATCH_NAME + BatchConstStrings.JOB)
    Job job;

    @Autowired
    @Qualifier(JdbcConfig.JDBC_TEMPLATE)
    JdbcTemplate jdbcTemplate;

/*    private JobParameters jobParameters;*/

    final static String filePath = "data/csv/restaurant_temp_data.csv";

  /*  @BeforeEach
    void setUp() {
        jobParameters = new JobParametersBuilder()
                .addString("csvFilePath", filePath)  // 공통 Job 파라미터 설정
                .toJobParameters();
    }
*/
    @Test
    @DisplayName("기본 작업 완료 테스트")
    void testJobExecution() throws Exception {
        // 작업 실행
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        // 상태 확인
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Test
    @DisplayName("데이터 처리 및 저장 검증")
    void testDataProcessingAndStorage() throws Exception {
        // 작업 실행
        jobLauncherTestUtils.launchJob();

        // 데이터베이스에 저장된 레코드 수 확인
        // 정상데이터인 13개만 저장되어야함.
        final int fileSuccessRecordCount = 13;
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM restaurant.restaurant_info", Integer.class);
        assertTrue(count == fileSuccessRecordCount, "데이터베이스에 레코드가 저장되어야 합니다");

        // 특정 레코드 검증 
        // case1) 정상 데이터가 저장됐는지 확인
        final String management_number = "5370000-101-2019-00001";
        Map<String, Object> record = jdbcTemplate.queryForMap(
                "SELECT * FROM restaurant.restaurant_info WHERE management_number = ?", management_number);
        assertEquals("경상남도 거제시 아주동 1575 아주e편한세상1단지", record.get("address"));

        // case2) 실패 데이터가 저장되지 않았는지 확인
        // 15번 데이터는 management_number 중복 키로 skip되어야한다.
        final String number = "15";
        Map<String, Object> record2 = null;
        try {
            record2 = jdbcTemplate.queryForMap(
                    "SELECT * FROM restaurant.restaurant_info WHERE number = ?", number);
        } catch (EmptyResultDataAccessException e) {
            record2 = new HashMap<>();
        }

        // Optional을 사용하여 처리
        Optional<Map<String, Object>> optionalRecord = Optional.ofNullable(record2);
        // record2가 비어 있는지 확인
        assertTrue(optionalRecord.orElse(new HashMap<>()).isEmpty(), "Expected an empty map, but got: " + optionalRecord);
    }


    @Test
    @DisplayName("데이터 무결성 검증")
    void testDataIntegrity() throws Exception {
        // 작업 실행
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // 스킵된 레코드 확인
        int skipCount = 0;
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            skipCount += stepExecution.getReadSkipCount() +
                    stepExecution.getProcessSkipCount() +
                    stepExecution.getWriteSkipCount();
        }

        // 테스트 데이터에 의도적으로 포함된 잘못된 레코드 수와 비교
        assertEquals(2, skipCount, "잘못된 형식의 레코드는 건너뛰어야 합니다");
    }

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
        assertEquals(2, jobExecution.getStepExecutions().iterator().next().getReadSkipCount());
    }
}
