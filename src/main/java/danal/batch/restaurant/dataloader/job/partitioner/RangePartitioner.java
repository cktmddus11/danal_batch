package danal.batch.restaurant.dataloader.job.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RangePartitioner implements Partitioner {
    private final long totalCount;
    private final int gridSize;

    public RangePartitioner(long totalCount, int gridSize) {
        this.totalCount = totalCount;
        this.gridSize = gridSize;
    }


    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        log.info(">>> partition");
        Map<String, ExecutionContext> partitions = new HashMap<>();

//        // stepExecution을 통해 jobExecutionContext에서 totalCount 가져오기
//        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
  //      long totalCount = stepExecution.getJobExecution().getExecutionContext().getLong("totalCount");
        long chunkSize = totalCount / gridSize;
        long remainder = totalCount % gridSize;
        // ex) 200개
        // chunksize 20 = 200 / 10
        // remainder 나머지 0 200개 % 10

        long start = 1;
        for (int i = 0; i < gridSize; i++) {
            long end = start + chunkSize - 1;
            if (i == gridSize - 1) {
                end += remainder; // 마지막 파티션에 나머지 몰아주기
            }

            ExecutionContext context = new ExecutionContext();
            context.putLong("startIndex", start);
            context.putLong("endIndex", end);
            partitions.put("partition" + i, context);

            log.info(">>> Partition " + i + " : " + start + " ~ " + end);
            log.info(">>> 파티셔닝 완료: {} 개의 파티션 생성", partitions.size());
            start = end + 1;
        }

        return partitions;
    }
}