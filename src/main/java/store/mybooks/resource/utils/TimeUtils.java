package store.mybooks.resource.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * packageName    : store.mybooks.resource.user.utils<br>
 * fileName       : TimeUtils<br>
 * author         : masiljangajji<br>
 * date           : 3/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/13/24        masiljangajji       최초 생성
 */
public class TimeUtils {

    private TimeUtils() {
    }

    public static LocalDate nowDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
