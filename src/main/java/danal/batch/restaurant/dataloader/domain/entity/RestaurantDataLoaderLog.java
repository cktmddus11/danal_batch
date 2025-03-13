package danal.batch.restaurant.dataloader.domain.entity;


import danal.batch.restaurant.dataloader.job.model.type.DataLoaderStatusCode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurant_data_loader_log")
public class RestaurantDataLoaderLog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 자동 생성되는 고유 ID

    @Column(name = "file_name", length = 200)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "load_stus_cd", length = 10)
    private DataLoaderStatusCode loadStusCd;


}
