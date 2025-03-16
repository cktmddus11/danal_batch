package danal.batch.restaurant.dataloader.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("=== STEP 시작: {} === ", stepExecution.getStepName());
//        ExecutionContext executionContext = stepExecution.getExecutionContext();
//        this.startIndex = executionContext.containsKey("startIndex") ?
//                executionContext.getLong("startIndex") : null;
//        this.endIndex = executionContext.containsKey("endIndex") ?
//                executionContext.getLong("endIndex") : null;
//        log.info(">>> {}, {}",
//                executionContext.getString("startIndex"),
//                executionContext.getString("endIndex"));

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("=== STEP 종료: {} === ", stepExecution.getStepName());
        return stepExecution.getExitStatus();
    }
}
