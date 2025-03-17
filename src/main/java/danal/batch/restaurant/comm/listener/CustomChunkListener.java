package danal.batch.restaurant.comm.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomChunkListener implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context) { // StepExecution은 빈이 아니라 실행 중에 동적으로 주어지는 값이므로 ChunkContext에서 가져와야함
        StepExecution stepExecution = context.getStepContext().getStepExecution();
        log.debug("=== Chunk 시작 ===");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        StepExecution stepExecution = context.getStepContext().getStepExecution();
        log.debug("=== Chunk 완료 : [{}] (처리된 아이템: {}) ===",
                stepExecution.getStepName(),
                context.getStepContext().getStepExecution().getWriteCount());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        StepExecution stepExecution = context.getStepContext().getStepExecution();
        log.error("=== [{}] Chunk 오류 발생 ===", stepExecution.getStepName());
    }
}


