package danal.batch.restaurant.comm.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public abstract class CustomJobExecutionListener implements JobExecutionListener {
    protected abstract void customBeforeJob(JobExecution jobExecution);

    protected abstract void customAfterJob(JobExecution jobExecution);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("========= Job 시작: Job {}, JOB_ID : {} =========", jobExecution.getJobInstance().getJobName(), jobExecution.getId());
        log.info(">>> 파라미터: {}", jobExecution.getJobParameters());
        jobExecution.getExecutionContext().put("startTime", System.currentTimeMillis());

        this.customBeforeJob(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            // 종료 시간 계산
            long startTime = jobExecution.getExecutionContext().getLong("startTime");
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("========= Job 종료 {} =========", jobExecution.getJobInstance().getJobName());
            log.info(">>> 총 소요 시간: {} 초", duration / 1000.0);
        } else {
            log.error(">>> 작업 실패. 상태: {} ", jobExecution.getStatus());
            log.error(">>> 종료 메시지: {}", jobExecution.getExitStatus().getExitDescription());
        }
        this.customAfterJob(jobExecution);
    }
}
