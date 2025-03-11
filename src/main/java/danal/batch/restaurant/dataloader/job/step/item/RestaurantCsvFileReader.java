package danal.batch.restaurant.dataloader.job.step.item;

import danal.batch.restaurant.meta.consts.BatchStrings;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static danal.batch.restaurant.dataloader.job.step.RestaurantDataLoaderStep.ITEM_READER_NAME;

@Slf4j
@Component
@StepScope
public class RestaurantCsvFileReader extends FlatFileItemReader<Map<String, String>> {

    @Value("${danal.batch.input.csv-file}")
    private String csvFilePath;

    private final ResourceLoader resourceLoader;

    public RestaurantCsvFileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean(ITEM_READER_NAME + BatchStrings.READER)
    @StepScope
    public FlatFileItemReader<Map<String, String>> flatFileReader() {
        DelimitedLineTokenizer tokenizer = getDelimitedLineTokenizer();

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

        return new FlatFileItemReaderBuilder<Map<String, String>>()
                .name("restaurantItemReader")
                .resource(resourceLoader.getResource("file:" + csvFilePath))
                .encoding(StandardCharsets.UTF_8.name())
                .linesToSkip(1)
                .lineMapper(lineMapper)
                .build();
    }

    private static DelimitedLineTokenizer getDelimitedLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames(FIELD_NAMES);
        tokenizer.setStrict(false);
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
