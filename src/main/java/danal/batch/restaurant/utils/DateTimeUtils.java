package danal.batch.restaurant.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
public class DateTimeUtils {
    public static LocalDate parse(String dateStr) {
        return parse(dateStr, List.of("yyyy-MM-dd", "yyyy.MM.dd"));
    }

    // 여러 포맷을 시도하면서 변환
    public static LocalDate parse(String dateStr, List<String> formats) {
        if(StringUtils.isBlank(dateStr)){
            return null;
        }
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
                // 포맷이 맞지 않으면 다음 포맷 시도
                log.warn(">> 날짜 포맷 탐색 실패 : {}", format);
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateStr);
    }
}
