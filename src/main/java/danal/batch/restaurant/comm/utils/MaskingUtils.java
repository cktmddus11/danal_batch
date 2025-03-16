package danal.batch.restaurant.comm.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MaskingUtils {

    // 전화번호 마스킹 패턴 (ex: 01*-****-5678)
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{2,3}-\\d{3,4}-\\d{4}");

    /**
     * 데이터 맵에서 민감 정보를 마스킹 처리하여 로그에 안전하게 기록하도록 함
     */
    public static Map<String, String> sanitizeData(Map<String, String> data) {
        Map<String, String> sanitized = new HashMap<>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // 민감한 필드명이면 값을 마스킹
            if (key.contains("전화") || key.contains("phone")) {
                sanitized.put(key, maskPhoneNumber(value));
            } else if (key.contains("주소") || key.contains("address")) {
                sanitized.put(key, maskAddress(value));
            } else {
                sanitized.put(key, value);
            }
        }

        return sanitized;
    }

    /**
     * 전화번호 마스킹 처리
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return phoneNumber;
        }

        // 전화번호 패턴인 경우
        if (PHONE_PATTERN.matcher(phoneNumber).matches()) {
            String[] parts = phoneNumber.split("-");
            if (parts.length == 3) {
                return parts[0].substring(0, 2) + "*" +
                        "-****-" +
                        parts[2];
            }
        }

        // 패턴에 맞지 않으면 앞 2자리만 보이고 나머지 *로 처리
        if (phoneNumber.length() > 4) {
            return phoneNumber.substring(0, 2) + "*".repeat(phoneNumber.length() - 4) +
                    phoneNumber.substring(phoneNumber.length() - 2);
        }

        return "***";
    }

    /**
     * 주소 마스킹 처리
     */
    public static String maskAddress(String address) {
        if (address == null || address.isEmpty()) {
            return address;
        }

        String[] parts = address.split(" ");
        StringBuilder masked = new StringBuilder();

        // 시/도, 시/군/구까지만 표시하고 나머지는 **로 마스킹
        if (parts.length > 2) {
            masked.append(parts[0]).append(" ").append(parts[1]).append(" **");
        } else if (parts.length == 2) {
            masked.append(parts[0]).append(" **");
        } else {
            masked.append("**");
        }

        return masked.toString();
    }
}
