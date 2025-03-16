package danal.batch.restaurant.comm.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

/**
 * 리스너를 그대로 사용하던지 상속받아서 custom함수를 구현해도됨.
 * 기본 역할은 로깅하는 역할을 포함하고 있음.
 *
 * @param <T>
 */
@Slf4j
public abstract class CustomItemProcessorListener<T> implements ItemProcessListener<T, T> {
    protected void customBeforeProcessor(T item) {
    }


    protected void customAfterProcessor(T item, T result) {
    }


    // ItemProcessor 로깅
    @Override
    public void beforeProcess(T item) {
        log.debug("=== ItemProcessor 시작 ===");
        this.customBeforeProcessor(item);
    }

    @Override
    public void afterProcess(T item, T result) {
        log.debug("=== ItemProcessor 완료: {} -> {} ===", item.toString(), result);
        this.customAfterProcessor(item, result);
    }

    @Override
    public void onProcessError(T item, Exception ex) {
        log.error("=== ItemProcessor 오류 발생: {} ===", ex.getMessage());
    }


}
