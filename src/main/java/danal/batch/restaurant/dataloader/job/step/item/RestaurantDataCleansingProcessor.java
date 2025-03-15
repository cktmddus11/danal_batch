package danal.batch.restaurant.dataloader.job.step.item;


import danal.batch.restaurant.dataloader.job.model.vo.RestaurantVo;
import danal.batch.restaurant.utils.DateTimeUtils;
import danal.batch.restaurant.utils.MaskingUtils;
import danal.batch.restaurant.utils.UniqueIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;


@Slf4j

@RequiredArgsConstructor
@Component
public class RestaurantDataCleansingProcessor implements ItemProcessor<Map<String, String>, RestaurantVo> {

    @Override
    public RestaurantVo process(Map<String, String> item) throws Exception {
        log.debug(">>> item id : {}, {}", item.get("번호"), MaskingUtils.sanitizeData(item).toString());

        RestaurantVo restaurantVo = RestaurantVo.builder()
                .id(UniqueIdUtil.generateShortUUID())
                .number(parseIntSafely(item.get("번호"), "번호"))
                .serviceName(item.get("개방서비스명"))
                .serviceId(item.get("개방서비스아이디"))
                .cityCode(item.get("개방자치단체코드"))
                .managementNumber(item.get("관리번호"))
                .licenseDate(item.get("인허가일자"))
                .licenseCancelDate(item.get("인허가취소일자"))
                .businessStatusCode(item.get("영업상태구분코드"))
                .businessStatus(item.get("영업상태명"))
                .detailedBusinessStatusCode(item.get("상세영업상태코드"))
                .detailedBusinessStatus(item.get("상세영업상태명"))

                .closureDate(DateTimeUtils.parse(item.get("폐업일자")))
                .suspensionStartDate(DateTimeUtils.parse(item.get("휴업시작일자")))
                .suspensionEndDate(DateTimeUtils.parse(item.get("휴업종료일자")))

                .reopeningDate(item.get("재개업일자"))
                .phoneNumber(item.get("소재지전화"))
                .area(parseDoubleSafely(item.get("소재지면적"), "소재지면적"))
                .postalCode(item.get("소재지우편번호"))
                .address(item.get("소재지전체주소"))
                .roadAddress(item.get("도로명전체주소"))
                .roadPostalCode(item.get("도로명우편번호"))
                .businessName(item.get("사업장명"))
                .lastModifiedTime(item.get("최종수정시점"))
                .dataUpdateType(item.get("데이터갱신구분"))
                .dataUpdateDate(item.get("데이터갱신일자"))
                .businessType(item.get("업태구분명"))
                .coordinateX(item.get("좌표정보x(epsg5174)"))
                .coordinateY(item.get("좌표정보y(epsg5174)"))
                .sanitaryBusinessType(item.get("위생업태명"))
                .maleEmployeeCount(parseIntSafely(item.get("남성종사자수"), "남성종사자수"))
                .femaleEmployeeCount(parseIntSafely(item.get("여성종사자수"), "여성종사자수"))
                .surroundingClassification(item.get("영업장주변구분명"))
                .gradeClassification(item.get("등급구분명"))
                .waterFacilityType(item.get("급수시설구분명"))
                .totalEmployeeCount(parseIntSafely(item.get("총직원수"), "총직원수"))
                .headOfficeEmployeeCount(parseIntSafely(item.get("본사직원수"), "본사직원수"))
                .factoryOfficeEmployeeCount(parseIntSafely(item.get("공장사무직직원수"), "공장사무직직원수"))
                .factorySalesEmployeeCount(parseIntSafely(item.get("공장판매직직원수"), "공장판매직직원수"))
                .factoryProductionEmployeeCount(parseIntSafely(item.get("공장생산직직원수"), "공장생산직직원수"))
                .buildingOwnershipType(item.get("건물소유구분명"))
                .depositAmount(parseDoubleSafely(item.get("보증액"), "보증액"))
                .monthlyRent(parseDoubleSafely(item.get("월세액"), "월세액"))
                .multiUseBusinessYn(item.get("다중이용업소여부"))
                .facilityTotalScale(parseDoubleSafely(item.get("시설총규모"), "시설총규모"))
                .traditionalDesignationNumber(item.get("전통업소지정번호"))
                .traditionalMainFood(item.get("전통업소주된음식"))
                .website(item.get("홈페이지"))

                .createBy("BATCh")
                .createAt(LocalDateTime.now())
                .build();

        // 데이터 무결성 검증
        if (!restaurantVo.isValid()) {
            log.warn(">>> 유효하지 않은 레스토랑 데이터 항목: {}", MaskingUtils.sanitizeData(item));
            throw new RuntimeException("유효하지 않은 레스토랑 데이터");
        }

        return restaurantVo;
    }

    // 정수형 안전 파싱 (null이나 빈 문자열, 숫자가 아닌 값에 대한 처리)
    private Integer parseIntSafely(String value, String fieldName) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.warn(">>> {} 필드의 값 '{}' 파싱 오류", fieldName, value);
            return null;
        }
    }

    // 실수형 안전 파싱
    private Double parseDoubleSafely(String value, String fieldName) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            log.warn(">>> {} 필드의 값 '{}' 파싱 오류", fieldName, value);
            return null;
        }
    }
}
