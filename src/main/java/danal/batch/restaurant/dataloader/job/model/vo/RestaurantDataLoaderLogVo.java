package danal.batch.restaurant.dataloader.job.model.vo;


import danal.batch.restaurant.dataloader.domain.entity.BaseEntity;
import danal.batch.restaurant.dataloader.job.model.type.DataLoaderStatusCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 읽은 파일 로깅
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RestaurantDataLoaderLogVo  {
    private String id; // 자동 생성되는 고유 ID
    private String fileName;
    private DataLoaderStatusCode loadStusCd;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
