package danal.batch.restaurant.dataloader.job.repository;

import danal.batch.restaurant.config.JdbcConfig;
import danal.batch.restaurant.dataloader.domain.vo.RestaurantVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ResturantInfoRepository {

    @Qualifier(JdbcConfig.NAMED_JDBC_TEMPLATE)
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void insertRestaurantInfo(List<RestaurantVo> restaurantVoList) {
        if (CollectionUtils.isEmpty(restaurantVoList)) {
            return;
        }

        // 배치 파라미터 설정
        SqlParameterSource[] batchParams = restaurantVoList.stream()
                .map(restaurantInfo -> new MapSqlParameterSource()
                        .addValue("id", restaurantInfo.getId())
                        .addValue("managementNumber", restaurantInfo.getManagementNumber())
                        .addValue("number", restaurantInfo.getNumber())
                        .addValue("serviceName", restaurantInfo.getServiceName())
                        .addValue("serviceId", restaurantInfo.getServiceId())
                        .addValue("cityCode", restaurantInfo.getCityCode())
                        .addValue("licenseDate", restaurantInfo.getLicenseDate())
                        .addValue("licenseCancelDate", restaurantInfo.getLicenseCancelDate())
                        .addValue("businessStatusCode", restaurantInfo.getBusinessStatusCode())
                        .addValue("businessStatus", restaurantInfo.getBusinessStatus())
                        .addValue("detailedBusinessStatusCode", restaurantInfo.getDetailedBusinessStatusCode())
                        .addValue("detailedBusinessStatus", restaurantInfo.getDetailedBusinessStatus())
                        .addValue("closureDate", restaurantInfo.getClosureDate())
                        .addValue("suspensionStartDate", restaurantInfo.getSuspensionStartDate())
                        .addValue("suspensionEndDate", restaurantInfo.getSuspensionEndDate())
                        .addValue("reopeningDate", restaurantInfo.getReopeningDate())
                        .addValue("phoneNumber", restaurantInfo.getPhoneNumber())
                        .addValue("area", restaurantInfo.getArea())
                        .addValue("postalCode", restaurantInfo.getPostalCode())
                        .addValue("address", restaurantInfo.getAddress())
                        .addValue("roadAddress", restaurantInfo.getRoadAddress())
                        .addValue("roadPostalCode", restaurantInfo.getRoadPostalCode())
                        .addValue("businessName", restaurantInfo.getBusinessName())
                        .addValue("lastModifiedTime", restaurantInfo.getLastModifiedTime())
                        .addValue("dataUpdateType", restaurantInfo.getDataUpdateType())
                        .addValue("dataUpdateDate", restaurantInfo.getDataUpdateDate())
//                        .addValue("businessType", restaurantInfo.getBusinessType())
//                        .addValue("coordinateX", restaurantInfo.getCoordinateX())
//                        .addValue("coordinateY", restaurantInfo.getCoordinateY())
//                        .addValue("sanitaryBusinessType", restaurantInfo.getSanitaryBusinessType())
//                        .addValue("maleEmployeeCount", restaurantInfo.getMaleEmployeeCount())
//                        .addValue("femaleEmployeeCount", restaurantInfo.getFemaleEmployeeCount())
//                        .addValue("surroundingClassification", restaurantInfo.getSurroundingClassification())
//                        .addValue("gradeClassification", restaurantInfo.getGradeClassification())
//                        .addValue("waterFacilityType", restaurantInfo.getWaterFacilityType())
//                        .addValue("totalEmployeeCount", restaurantInfo.getTotalEmployeeCount())
//                        .addValue("headOfficeEmployeeCount", restaurantInfo.getHeadOfficeEmployeeCount())
//                        .addValue("factoryOfficeEmployeeCount", restaurantInfo.getFactoryOfficeEmployeeCount())
//                        .addValue("factorySalesEmployeeCount", restaurantInfo.getFactorySalesEmployeeCount())
//                        .addValue("factoryProductionEmployeeCount", restaurantInfo.getFactoryProductionEmployeeCount())
//                        .addValue("buildingOwnershipType", restaurantInfo.getBuildingOwnershipType())
//                        .addValue("depositAmount", restaurantInfo.getDepositAmount())
//                        .addValue("monthlyRent", restaurantInfo.getMonthlyRent())
//                        .addValue("multiUseBusinessYn", restaurantInfo.getMultiUseBusinessYn())
//                        .addValue("facilityTotalScale", restaurantInfo.getFacilityTotalScale())
//                        .addValue("traditionalDesignationNumber", restaurantInfo.getTraditionalDesignationNumber())
//                        .addValue("traditionalMainFood", restaurantInfo.getTraditionalMainFood())
//                        .addValue("website", restaurantInfo.getWebsite())
                )
                .toArray(SqlParameterSource[]::new);

        // 배치 삽입 실행
        namedParameterJdbcTemplate.batchUpdate(insertRestaurantInfoSql(), batchParams);
    }

    public String insertRestaurantInfoSql() {
        return "INSERT INTO restaurant.restaurant_info (" +
                "id, management_number, number, service_name, service_id, city_code, " +
                "license_date, license_cancel_date, business_status_code, business_status, " +
                "detailed_business_status_code, detailed_business_status, closure_date, " +
                "suspension_start_date, suspension_end_date, reopening_date, phone_number, " +
                "area, postal_code, address, road_address, road_postal_code, business_name, " +
                "last_modified_time, data_update_type, data_update_date, business_type, " +
                "coordinate_x, coordinate_y, sanitary_business_type, male_employee_count, " +
                "female_employee_count, surrounding_classification, grade_classification, " +
                "water_facility_type, total_employee_count, head_office_employee_count, " +
                "factory_office_employee_count, factory_sales_employee_count, " +
                "factory_production_employee_count, building_ownership_type, deposit_amount, " +
                "monthly_rent, multi_use_business_yn, facility_total_scale, " +
                "traditional_designation_number, traditional_main_food, website) " +
                "VALUES (" +
                ":id, :managementNumber, :number, :serviceName, :serviceId, :cityCode, " +
                ":licenseDate, :licenseCancelDate, :businessStatusCode, :businessStatus, " +
                ":detailedBusinessStatusCode, :detailedBusinessStatus, :closureDate, " +
                ":suspensionStartDate, :suspensionEndDate, :reopeningDate, :phoneNumber, " +
                ":area, :postalCode, :address, :roadAddress, :roadPostalCode, :businessName, " +
                ":lastModifiedTime, :dataUpdateType, :dataUpdateDate, :businessType, " +
                ":coordinateX, :coordinateY, :sanitaryBusinessType, :maleEmployeeCount, " +
                ":femaleEmployeeCount, :surroundingClassification, :gradeClassification, " +
                ":waterFacilityType, :totalEmployeeCount, :headOfficeEmployeeCount, " +
                ":factoryOfficeEmployeeCount, :factorySalesEmployeeCount, " +
                ":factoryProductionEmployeeCount, :buildingOwnershipType, :depositAmount, " +
                ":monthlyRent, :multiUseBusinessYn, :facilityTotalScale, " +
                ":traditionalDesignationNumber, :traditionalMainFood, :website)";
    }
}
