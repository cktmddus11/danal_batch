-- 데이터베이스 생성
CREATE
DATABASE IF NOT EXISTS restaurant_info CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE restaurant;

-- 레스토랑 테이블 생성
CREATE TABLE IF NOT EXISTS restaurant_info (
    id VARCHAR(11) PRIMARY KEY,
    number VARCHAR(50) UNIQUE,
    management_number VARCHAR(50) UNIQUE,
    service_name VARCHAR(100),
    service_id VARCHAR(100),
    city_code VARCHAR(20),
    license_date VARCHAR(20),
    license_cancel_date VARCHAR(20),
    business_status_code VARCHAR(20),
    business_status VARCHAR(50),
    detailed_business_status_code VARCHAR(20),
    detailed_business_status VARCHAR(50),
    closure_date VARCHAR(20),
    suspension_start_date VARCHAR(20),
    suspension_end_date VARCHAR(20),
    reopening_date VARCHAR(20),
    phone_number VARCHAR(50),
    area DOUBLE,
    postal_code VARCHAR(20),
    address VARCHAR(200),
    road_address VARCHAR(200),
    road_postal_code VARCHAR(20),
    business_name VARCHAR(100),
    last_modified_time VARCHAR(30),
    data_update_type VARCHAR(50),
    data_update_date VARCHAR(20),
    business_type VARCHAR(100),
    coordinate_x VARCHAR(50),
    coordinate_y VARCHAR(50),
    sanitary_business_type VARCHAR(100),
    male_employee_count INT,
    female_employee_count INT,
    surrounding_classification VARCHAR(100),
    grade_classification VARCHAR(50),
    water_facility_type VARCHAR(50),
    total_employee_count INT,
    head_office_employee_count INT,
    factory_office_employee_count INT,
    factory_sales_employee_count INT,
    factory_production_employee_count INT,
    building_ownership_type VARCHAR(50),
    deposit_amount DOUBLE,
    monthly_rent DOUBLE,
    multi_use_business_yn VARCHAR(1),
    facility_total_scale DOUBLE,
    traditional_designation_number VARCHAR(50),
    traditional_main_food VARCHAR(100),
    website VARCHAR(200),
    created_at   DATETIME     NOT NULL,                   -- 생성일
    updated_at   DATETIME,                                -- 수정일
    created_by   VARCHAR(255),                            -- 생성자
    updated_by   VARCHAR(255),                            -- 수정자

    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--
--
-- CREATE TABLE restaurant_data_loader_log
-- (
--     id           VARCHAR(11) PRIMARY KEY,       -- 고유 ID, 자동 생성
--     file_name    VARCHAR(255) NOT NULL,                   -- 파일명
--     load_stus_cd VARCHAR(50)  NOT NULL,                   -- 데이터 로드 상태 코드 (enum을 VARCHAR로 저장)
--     created_at   DATETIME     NOT NULL,                   -- 생성일
--     updated_at   DATETIME,                                -- 수정일
--     created_by   VARCHAR(255),                            -- 생성자
--     updated_by   VARCHAR(255)                            -- 수정자
-- --     FOREIGN KEY (created_by) REFERENCES users (username), -- 예시로 users 테이블과 연결
-- --     FOREIGN KEY (updated_by) REFERENCES users (username)  -- 예시로 users 테이블과 연결
-- );
--
-- CREATE TABLE batch_failure_log
-- (
--     id            VARCHAR(11) PRIMARY KEY, -- 고유 ID
--     job_name      VARCHAR(255) NOT NULL,             -- 배치 작업 이름
--     step_name     VARCHAR(255) NOT NULL,             -- 배치 스텝 이름
--     job_id        BIGINT NOT NULL,
--     item_id       BIGINT,                            -- 실패한 항목의 ID (필요에 따라 추가)
--     item_data     TEXT,                              -- 실패한 항목의 데이터 (JSON이나 원본 데이터 저장 가능)
--     error_message VARCHAR(255),                      -- 오류 메시지
--     skip_reason   VARCHAR(255),                      -- 스킵된 이유
--     created_at   DATETIME     NOT NULL               -- 생성일
-- );