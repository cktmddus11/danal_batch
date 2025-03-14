package danal.batch.restaurant.dataloader.job.step.item;

import danal.batch.restaurant.listener.CustomItemReaderListener;
import danal.batch.restaurant.meta.consts.BatchConstStrings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep.ITEM_READER_NAME;
import static danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep.STEP_NAME;

@RequiredArgsConstructor
@Slf4j
@Component
public class RestaurantCsvFileReader {

    @Value("${danal.batch.input.csv-file}")
    private String csvFilePath;

    private final ResourceLoader resourceLoader;

    private final CustomItemReaderListener<Map<String, String>> itemReaderListener;

    @Bean(ITEM_READER_NAME + BatchConstStrings.READER)
    @StepScope // Step 실행 시점에 Bean이 생성, JobParameter 주입필요시
    public FlatFileItemReader<Map<String, String>> flatFileReader() {
        log.debug(">>> {} - Reader Start!", STEP_NAME);
        //LineTokenizer 설정 (CSV 컬럼 구분)
        DelimitedLineTokenizer tokenizer = getDelimitedLineTokenizer();
        //FieldSetMapper 설정 (FieldSet → Map 변환)
        DefaultLineMapper<Map<String, String>> lineMapper = getMapDefaultLineMapper(tokenizer);

        return new FlatFileItemReaderBuilder<Map<String, String>>()
                .name(ITEM_READER_NAME+ BatchConstStrings.READER)
                .resource( new ClassPathResource("data/csv/restaurant_temp_data.csv"))
                .encoding(StandardCharsets.UTF_8.name())
                .linesToSkip(1)
                .lineMapper(lineMapper)
                .build();
    }

    /*
    * FieldSetMapper 설정 (FieldSet → Map 변환)
    * */
    private static DefaultLineMapper<Map<String, String>> getMapDefaultLineMapper(DelimitedLineTokenizer tokenizer) {
        DefaultLineMapper<Map<String, String>> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        // FieldSetMapper 구현
        lineMapper.setFieldSetMapper(new FieldSetMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapFieldSet(FieldSet fieldSet) {
                // 각 필드를 Map으로 변환
                Map<String, String> resultMap = new HashMap<>();
                for (String name : FIELD_NAMES) {
                    resultMap.put(name, fieldSet.readString(name));
                }
                return resultMap;
            }
        });
        return lineMapper;
    }

    /*
    * LineTokenizer 설정 (CSV 컬럼 구분)
    */
    private static DelimitedLineTokenizer getDelimitedLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames(FIELD_NAMES);
        tokenizer.setStrict(false); // strict 모드를 false로 설정하여 파일이 없으면 예외 발생하지 않음
        return tokenizer;
    }

    private static final String[] FIELD_NAMES = new String[]{
            "번호", "개방서비스명", "개방서비스아이디", "개방자치단체코드", "관리번호", "인허가일자",
            "인허가취소일자", "영업상태구분코드", "영업상태명", "상세영업상태코드", "상세영업상태명",
            "폐업일자", "휴업시작일자", "휴업종료일자", "재개업일자", "소재지전화", "소재지면적",
            "소재지우편번호", "소재지전체주소", "도로명전체주소", "도로명우편번호", "사업장명",
            "최종수정시점", "데이터갱신구분", "데이터갱신일자", "업태구분명", "좌표정보x(epsg5174)",
            "좌표정보y(epsg5174)", "위생업태명", "남성종사자수", "여성종사자수", "영업장주변구분명",
            "등급구분명", "급수시설구분명", "총직원수", "본사직원수", "공장사무직직원수", "공장판매직직원수",
            "공장생산직직원수", "건물소유구분명", "보증액", "월세액", "다중이용업소여부",
            "시설총규모", "전통업소지정번호", "전통업소주된음식", "홈페이지"
    };
}
