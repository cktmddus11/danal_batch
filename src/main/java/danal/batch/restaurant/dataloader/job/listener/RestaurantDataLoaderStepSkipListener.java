package danal.batch.restaurant.dataloader.job.listener;

import danal.batch.restaurant.dataloader.job.model.vo.RestaurantSkipVo;
import danal.batch.restaurant.dataloader.job.model.vo.RestaurantVo;
import danal.batch.restaurant.utils.MaskingUtils;
import danal.batch.restaurant.utils.UniqueIdUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantDataLoaderStepSkipListener implements SkipListener<Map<String, String>, RestaurantVo> {
    // 스레드 안전한 Queue
    private final Queue<RestaurantSkipVo> skippedItems = new ConcurrentLinkedQueue<>();

    @Override
    public void onSkipInRead(Throwable t) {
        log.info(">>> Item skipped in read: {}", t.getMessage());

        skippedItems.add(RestaurantSkipVo.builder()
                .id(UniqueIdUtil.generateShortUUID())
                .errMsg(t.getMessage())
                .chunkType(RestaurantSkipVo.ChunkType.READER)

                .createdBy("BATCH")
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Override
    public void onSkipInProcess(Map<String, String> item, Throwable t) {
        String errorKey = t.getClass().getSimpleName();

        log.info(">>> Item skipped in process: {}, 항목: {}, 오류: {}",
                errorKey,
                MaskingUtils.sanitizeData(item),
                t.getMessage());

        skippedItems.add(RestaurantSkipVo.builder()
                .id(UniqueIdUtil.generateShortUUID())
                .chunkType(RestaurantSkipVo.ChunkType.PROCESSOR)

                .errMsg(t.getMessage())
                .errItemString(MaskingUtils.sanitizeData(item).toString())
                .createdBy("BATCH")
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Override
    public void onSkipInWrite(RestaurantVo item, Throwable t) {

        log.info(">>> Item skipped in write: {} due to {}", item.toString(), t.getMessage());

        skippedItems.add(RestaurantSkipVo.builder()
                .id(UniqueIdUtil.generateShortUUID())
                .chunkType(RestaurantSkipVo.ChunkType.WRITER)

                .errMsg(t.getMessage())
                .errItemString(item.toString())
                .createdBy("BATCH")
                .createdAt(LocalDateTime.now())
                .build());
    }

}
