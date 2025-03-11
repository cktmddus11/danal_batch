package danal.batch.restaurant.dataloader.job.model.type;


import lombok.Getter;

@Getter
public enum DataLoaderStatusCode {
    SUCCESS,
    FAIL,
    PROCESSING,
}
