package danal.batch.restaurant.dataloader.job.listener;

import danal.batch.restaurant.listener.CustomItemReaderListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RestaurantDataLoaderItemReaderListener extends CustomItemReaderListener<Map<String, String>> {
    @Override
    protected void customBeforeReader() {
        super.customBeforeReader();
    }

    @Override
    protected void customAfterReader(Map<String, String> item) {
        super.customAfterReader(item);
    }
}
