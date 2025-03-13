package danal.batch.restaurant.dataloader.domain.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RestaurantVo {
    private String id; // 자동 생성되는 고유 ID

    private Integer number; // 번호

    private String serviceName; // 개방서비스명

    private String serviceId; // 개방서비스아이디

    private String cityCode; // 개방자치단체코드

    private String managementNumber; // 관리번호

    private String licenseDate; // 인허가일자

    private String licenseCancelDate; // 인허가취소일자

    private String businessStatusCode; // 영업상태구분코드

    private String businessStatus; // 영업상태명

    private String detailedBusinessStatusCode; // 상세영업상태코드

    private String detailedBusinessStatus; // 상세영업상태명

    private LocalDate closureDate; // 폐업일자

    private LocalDate suspensionStartDate; // 휴업시작일자

    private LocalDate suspensionEndDate; // 휴업종료일자

    private String reopeningDate; // 재개업일자

    private String phoneNumber; // 소재지전화

    private Double area; // 소재지면적

    private String postalCode; // 소재지우편번호

    private String address; // 소재지전체주소

    private String roadAddress; // 도로명전체주소

    private String roadPostalCode; // 도로명우편번호

    private String businessName; // 사업장명

    private String lastModifiedTime; // 최종수정시점

    private String dataUpdateType; // 데이터갱신구분

    private String dataUpdateDate; // 데이터갱신일자

    private String businessType; // 업태구분명

    private String coordinateX; // 좌표정보x(epsg5174)

    private String coordinateY; // 좌표정보y(epsg5174)

    private String sanitaryBusinessType; // 위생업태명

    private Integer maleEmployeeCount; // 남성종사자수

    private Integer femaleEmployeeCount; // 여성종사자수

    private String surroundingClassification; // 영업장주변구분명

    private String gradeClassification; // 등급구분명

    private String waterFacilityType; // 급수시설구분명

    private Integer totalEmployeeCount; // 총직원수

    private Integer headOfficeEmployeeCount; // 본사직원수

    private Integer factoryOfficeEmployeeCount; // 공장사무직직원수

    private Integer factorySalesEmployeeCount; // 공장판매직직원수

    private Integer factoryProductionEmployeeCount; // 공장생산직직원수

    private String buildingOwnershipType; // 건물소유구분명

    private Double depositAmount; // 보증액

    private Double monthlyRent; // 월세액

    private String multiUseBusinessYn; // 다중이용업소여부

    private Double facilityTotalScale; // 시설총규모

    private String traditionalDesignationNumber; // 전통업소지정번호

    private String traditionalMainFood; // 전통업소주된음식

    private String website; // 홈페이지

    // 데이터 무결성 검증 메서드
    public boolean isValid() {
        return managementNumber != null && !managementNumber.trim().isEmpty();
    }
}