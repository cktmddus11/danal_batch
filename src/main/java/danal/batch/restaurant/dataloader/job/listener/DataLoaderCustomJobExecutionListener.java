package danal.batch.restaurant.dataloader.job.listener;

import danal.batch.restaurant.listener.CustomJobExecutionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@Slf4j

@RequiredArgsConstructor
@Component
public class DataLoaderCustomJobExecutionListener extends CustomJobExecutionListener {

    @Value("${danal.batch.input.csv-file}")
    private String csvFilePath; // TODO 잡파라미터로 처리


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
