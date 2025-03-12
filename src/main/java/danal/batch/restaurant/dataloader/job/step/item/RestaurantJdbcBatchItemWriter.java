package danal.batch.restaurant.dataloader.job.step.item;

import danal.batch.restaurant.config.DataSourceConfig;
import danal.batch.restaurant.dataloader.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep.STEP_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantJdbcBatchItemWriter {

    @Qualifier(DataSourceConfig.DS_BEAN_NAME)
    private final DataSource dataSource;
    @Bean
    public JdbcBatchItemWriter<Restaurant> jdbcBatchItemWriter() {
        log.info(">>> {} - Writer Start!", STEP_NAME);

        return new JdbcBatchItemWriterBuilder<Restaurant>()
                .dataSource(dataSource)
                .sql(sql)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>()) //  Java 객체와 SQL 파라미터 간의 매핑을 자동화
                .assertUpdates(false) // false 설정 시 UPDATE 문이 영향을 미치는 행이 없어도 예외를 발생시키지 않음
                .build();
    }
    private static String sql =
            """
            INSERT INTO restaurant (
                management_number, number, service_name, service_id, city_code, 
                license_date, license_cancel_date, business_status_code, business_status, 
                detailed_business_status_code, detailed_business_status, closure_date, 
                suspension_start_date, suspension_end_date, reopening_date, phone_number, 
                area, postal_code, address, road_address, road_postal_code, business_name, 
                last_modified_time, data_update_type, data_update_date, business_type, 
                coordinate_x, coordinate_y, sanitary_business_type, male_employee_count, 
                female_employee_count, surrounding_classification, grade_classification, 
                water_facility_type, total_employee_count, head_office_employee_count, 
                factory_office_employee_count, factory_sales_employee_count, 
                factory_production_employee_count, building_ownership_type, deposit_amount, 
                monthly_rent, multi_use_business_yn, facility_total_scale, 
                traditional_designation_number, traditional_main_food, website
            ) 
            VALUES (
                :managementNumber, :number, :serviceName, :serviceId, :cityCode, 
                :licenseDate, :licenseCancelDate, :businessStatusCode, :businessStatus, 
                :detailedBusinessStatusCode, :detailedBusinessStatus, :closureDate, 
                :suspensionStartDate, :suspensionEndDate, :reopeningDate, :phoneNumber, 
                :area, :postalCode, :address, :roadAddress, :roadPostalCode, :businessName, 
                :lastModifiedTime, :dataUpdateType, :dataUpdateDate, :businessType, 
                :coordinateX, :coordinateY, :sanitaryBusinessType, :maleEmployeeCount, 
                :femaleEmployeeCount, :surroundingClassification, :gradeClassification, 
                :waterFacilityType, :totalEmployeeCount, :headOfficeEmployeeCount, 
                :factoryOfficeEmployeeCount, :factorySalesEmployeeCount, 
                :factoryProductionEmployeeCount, :buildingOwnershipType, :depositAmount, 
                :monthlyRent, :multiUseBusinessYn, :facilityTotalScale, 
                :traditionalDesignationNumber, :traditionalMainFood, :website
            ) 
            ON DUPLICATE KEY UPDATE 
                number = VALUES(number), 
                service_name = VALUES(service_name), 
                service_id = VALUES(service_id), 
                city_code = VALUES(city_code), 
                license_date = VALUES(license_date), 
                license_cancel_date = VALUES(license_cancel_date), 
                business_status_code = VALUES(business_status_code), 
                business_status = VALUES(business_status), 
                detailed_business_status_code = VALUES(detailed_business_status_code), 
                detailed_business_status = VALUES(detailed_business_status), 
                closure_date = VALUES(closure_date), 
                suspension_start_date = VALUES(suspension_start_date), 
                suspension_end_date = VALUES(suspension_end_date), 
                reopening_date = VALUES(reopening_date), 
                phone_number = VALUES(phone_number), 
                area = VALUES(area), 
                postal_code = VALUES(postal_code), 
                address = VALUES(address), 
                road_address = VALUES(road_address), 
                road_postal_code = VALUES(road_postal_code), 
                business_name = VALUES(business_name), 
                last_modified_time = VALUES(last_modified_time), 
                data_update_type = VALUES(data_update_type), 
                data_update_date = VALUES(data_update_date), 
                business_type = VALUES(business_type), 
                coordinate_x = VALUES(coordinate_x), 
                coordinate_y = VALUES(coordinate_y), 
                sanitary_business_type = VALUES(sanitary_business_type), 
                male_employee_count = VALUES(male_employee_count), 
                female_employee_count = VALUES(female_employee_count), 
                surrounding_classification = VALUES(surrounding_classification), 
                grade_classification = VALUES(grade_classification), 
                water_facility_type = VALUES(water_facility_type), 
                total_employee_count = VALUES(total_employee_count), 
                head_office_employee_count = VALUES(head_office_employee_count), 
                factory_office_employee_count = VALUES(factory_office_employee_count), 
                factory_sales_employee_count = VALUES(factory_sales_employee_count), 
                factory_production_employee_count = VALUES(factory_production_employee_count), 
                building_ownership_type = VALUES(building_ownership_type), 
                deposit_amount = VALUES(deposit_amount), 
                monthly_rent = VALUES(monthly_rent), 
                multi_use_business_yn = VALUES(multi_use_business_yn), 
                website = VALUES(website)""" ;
}