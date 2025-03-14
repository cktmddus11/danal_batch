package danal.batch.restaurant.dataloader.job.listener;

import danal.batch.restaurant.listener.CustomJobExecutionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class DataLoaderCustomJobExecutionListener extends CustomJobExecutionListener {

    @Override
    protected void customBeforeJob(JobExecution jobExecution) {

    }

    @Override
    protected void customAfterJob(JobExecution jobExecution) {
        // 실패 시 처리
        if (jobExecution.getStatus() == BatchStatus.FAILED) {

        }
    }
}
