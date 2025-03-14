package danal.batch.restaurant.dataloader.job.listener;

import danal.batch.restaurant.dataloader.domain.entity.RestaurantEntity;
import danal.batch.restaurant.utils.MaskingUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantDataLoaderStepSkipListener implements SkipListener<Map<String, String>, RestaurantEntity> {
    // 스레드 안전한 Queue
    private final Queue<Map<String, String>> skippedItems = new ConcurrentLinkedQueue<>();

    @Override
    public void onSkipInRead(Throwable t) {
        log.debug(">>> Item skipped in read: {}", t.getMessage());
    }

    @Override
    public void onSkipInProcess(Map<String, String> item, Throwable t) {
        skippedItems.add(item);

        log.debug(">>> Item skipped in process: {} due to {}",item, t.getMessage());
        String errorKey = t.getClass().getSimpleName();

        // 안전하게 로그 기록 (민감 정보 마스킹)
        log.error(">>> 항목 처리 오류: {}, 항목: {}, 오류: {}",
                errorKey,
                MaskingUtils.sanitizeData(item),
                t.getMessage());
    }

    @Override
    public void onSkipInWrite(RestaurantEntity item, Throwable t) {
//        skippedItems.add(item);
        log.debug(">>> Item skipped in process: {} due to {}",item.toString(), t.getMessage());

    }

}
