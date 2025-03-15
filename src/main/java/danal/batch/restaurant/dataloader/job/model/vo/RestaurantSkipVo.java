package danal.batch.restaurant.dataloader.job.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RestaurantSkipVo {
    private String id; // 자동 생성되는 고유 ID

    private ChunkType chunkType;
    private String errMsg;
    private String errType; // 에러 Exception 명
    private String errItemString;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public enum ChunkType {
        READER,
        PROCESSOR,
        WRITER
    }
}
