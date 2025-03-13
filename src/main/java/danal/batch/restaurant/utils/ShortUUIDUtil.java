package danal.batch.restaurant.utils;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class ShortUUIDUtil {

    /**
     * 11자리 대문자로 변환된 짧은 UUID 생성
     * @return 11자리의 대문자로 된 고유 문자열
     */
    public static String generateShortUUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        // Base64 URL 인코딩 후, 패딩 제거 및 11자리로 잘라서 반환 (대문자로 변환)
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(byteBuffer.array())
                .substring(0, 11)
                .toUpperCase();
    }

    public static void main(String[] args) {
        // 테스트 실행
        for (int i = 0; i < 5; i++) {
            System.out.println(generateShortUUID());
        }
    }
}
