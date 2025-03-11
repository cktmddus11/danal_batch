package danal.batch.restaurant.dataloader.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass // 해당 클래스를 상속받은 엔티티들이 필드를 공유하도록 설정
@EntityListeners(AuditingEntityListener.class) // Spring Data JPA의 Auditing
public class BaseEntity {
    @CreatedDate // 엔티티 생성 시 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티 수정 시 자동 업데이트
    private LocalDateTime updatedAt;

    @CreatedBy // 생성자 자동 저장 (SecurityContext 이용)
    private String createdBy;

    @LastModifiedBy // 수정자 자동 저장 (SecurityContext 이용)
    private String updatedBy;
}
