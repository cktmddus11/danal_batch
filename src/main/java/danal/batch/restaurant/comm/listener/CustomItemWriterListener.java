package danal.batch.restaurant.comm.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

/**
 * 리스너를 그대로 사용하던지 상속받아서 custom함수를 구현해도됨.
 * * 기본 역할은 로깅하는 역할을 포함하고 있음.
 *
 * @param <T>
 */
@Slf4j
public abstract class CustomItemWriterListener<T> implements ItemWriteListener<T> {

    protected void customBeforeWrite(Chunk<? extends T> items) {
    }

    ;

    protected void customAfterWrite(Chunk<? extends T> items) {
    }

    ;

    @Override
    public void beforeWrite(Chunk<? extends T> items) {
        log.debug("=== ItemWriter 시작: {}건 ===", items.size());
        this.customBeforeWrite(items);
    }

    @Override
    public void afterWrite(Chunk<? extends T> items) {
        log.debug("=== ItemWriter 완료: {}건 ===", items.size());
        this.customAfterWrite(items);
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends T> items) {
        log.debug("=== ItemWriter 오류 발생: {} ===", exception.getMessage());
    }
}
