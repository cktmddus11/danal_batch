package danal.batch.restaurant.dataloader.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_info")
public class RestaurantEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11)
    private String id; // 자동 생성되는 고유 ID

    @Column(name = "number", length = 100)
    private Integer number; // 번호

    @Column(name = "service_name", length = 100)
    private String serviceName; // 개방서비스명

    @Column(name = "service_id", length = 100)
    private String serviceId; // 개방서비스아이디

    @Column(name = "city_code", length = 20)
    private String cityCode; // 개방자치단체코드

    @Column(name = "management_number")
    private String managementNumber; // 관리번호

    @Column(name = "license_date")
    private String licenseDate; // 인허가일자

    @Column(name = "license_cancel_date")
    private String licenseCancelDate; // 인허가취소일자

    @Column(name = "business_status_code", length = 20)
    private String businessStatusCode; // 영업상태구분코드

    @Column(name = "business_status", length = 50)
    private String businessStatus; // 영업상태명

    @Column(name = "detailed_business_status_code", length = 10)
    private String detailedBusinessStatusCode; // 상세영업상태코드

    @Column(name = "detailed_business_status", length = 10)
    private String detailedBusinessStatus; // 상세영업상태명

    @Column(name = "closure_date")
    private LocalDate closureDate; // 폐업일자

    @Column(name = "suspension_start_date")
    private LocalDate suspensionStartDate; // 휴업시작일자

    @Column(name = "suspension_end_date")
    private LocalDate suspensionEndDate; // 휴업종료일자

    @Column(name = "reopening_date")
    private String reopeningDate; // 재개업일자

    @Column(name = "phone_number", length = 50)
    private String phoneNumber; // 소재지전화

    @Column(name = "area")
    private Double area; // 소재지면적

    @Column(name = "postal_code", length = 20)
    private String postalCode; // 소재지우편번호

    @Column(name = "address", length = 200)
    private String address; // 소재지전체주소

    @Column(name = "road_address", length = 200)
    private String roadAddress; // 도로명전체주소

    @Column(name = "road_postal_code", length = 20)
    private String roadPostalCode; // 도로명우편번호

    @Column(name = "business_name", length = 100)
    private String businessName; // 사업장명

    @Column(name = "last_modified_time")
    private String lastModifiedTime; // 최종수정시점

    @Column(name = "data_update_type", length = 50)
    private String dataUpdateType; // 데이터갱신구분

    @Column(name = "data_update_date")
    private String dataUpdateDate; // 데이터갱신일자

    @Column(name = "business_type", length = 100)
    private String businessType; // 업태구분명

    @Column(name = "coordinate_x")
    private String coordinateX; // 좌표정보x(epsg5174)

    @Column(name = "coordinate_y")
    private String coordinateY; // 좌표정보y(epsg5174)

    @Column(name = "sanitary_business_type", length = 100)
    private String sanitaryBusinessType; // 위생업태명

    @Column(name = "male_employee_count")
    private Integer maleEmployeeCount; // 남성종사자수

    @Column(name = "female_employee_count")
    private Integer femaleEmployeeCount; // 여성종사자수

    @Column(name = "surrounding_classification", length = 100)
    private String surroundingClassification; // 영업장주변구분명

    @Column(name = "grade_classification", length = 50)
    private String gradeClassification; // 등급구분명

    @Column(name = "water_facility_type", length = 50)
    private String waterFacilityType; // 급수시설구분명

    @Column(name = "total_employee_count")
    private Integer totalEmployeeCount; // 총직원수

    @Column(name = "head_office_employee_count")
    private Integer headOfficeEmployeeCount; // 본사직원수

    @Column(name = "factory_office_employee_count")
    private Integer factoryOfficeEmployeeCount; // 공장사무직직원수

    @Column(name = "factory_sales_employee_count")
    private Integer factorySalesEmployeeCount; // 공장판매직직원수

    @Column(name = "factory_production_employee_count")
    private Integer factoryProductionEmployeeCount; // 공장생산직직원수

    @Column(name = "building_ownership_type", length = 50)
    private String buildingOwnershipType; // 건물소유구분명

    @Column(name = "deposit_amount")
    private Double depositAmount; // 보증액

    @Column(name = "monthly_rent")
    private Double monthlyRent; // 월세액

    @Column(name = "multi_use_business_yn", length = 1)
    private String multiUseBusinessYn; // 다중이용업소여부

    @Column(name = "facility_total_scale")
    private Double facilityTotalScale; // 시설총규모

    @Column(name = "traditional_designation_number", length = 50)
    private String traditionalDesignationNumber; // 전통업소지정번호

    @Column(name = "traditional_main_food", length = 100)
    private String traditionalMainFood; // 전통업소주된음식

    @Column(name = "website", length = 200)
    private String website; // 홈페이지

    // 데이터 무결성 검증 메서드
    public boolean isValid() {
        return managementNumber != null && !managementNumber.trim().isEmpty();
    }
}