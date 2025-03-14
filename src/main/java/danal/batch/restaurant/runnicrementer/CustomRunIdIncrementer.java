package danal.batch.restaurant.runnicrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public class CustomRunIdIncrementer extends RunIdIncrementer {
    private static final String RUN_ID_KEY = "run.id";
    private static final String TIMESTAMP_KEY = "timestamp";

    @Override
    public JobParameters getNext(JobParameters parameters) {
        // 현재 파라미터에서 run.id 값을 가져옴
        Long runId = parameters == null || parameters.getLong(RUN_ID_KEY) == null
                ? 0L : parameters.getLong(RUN_ID_KEY);

        // run.id 증가
        runId++;

        // 새 파라미터 생성
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addLong(RUN_ID_KEY, runId);
        builder.addLong(TIMESTAMP_KEY, System.currentTimeMillis());

        return builder.toJobParameters();
    }
}