package danal.batch.restaurant.dataloader.job.parameter;

import lombok.Getter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class RestaurantJobParameter {

    private String csvFilePath;

    @Value("#{jobParameters[csvFilePath]}")
    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
}
