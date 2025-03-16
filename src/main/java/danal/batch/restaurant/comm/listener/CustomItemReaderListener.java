package danal.batch.restaurant.comm.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

/**
 * 리스너를 그대로 사용하던지 상속받아서 custom함수를 구현해도됨.
 * 기본 역할은 로깅하는 역할을 포함하고 있음.
 *
 * @param <T>
 */
@Slf4j
public abstract class CustomItemReaderListener<T> implements ItemReadListener<T> {
    protected void customBeforeReader() {
    }

    protected void customAfterReader(T item) {
    }

    // ItemReader 로깅
    @Override
    public void beforeRead() {
        log.debug("=== ItemReader 시작 ===");
        this.customBeforeReader();
    }

    @Override
    public void afterRead(T item) {
        log.debug("=== ItemReader 완료 ===");
        this.customAfterReader(item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.error("=== ItemReader 오류 발생: {} ===", ex.getMessage());
    }

}
