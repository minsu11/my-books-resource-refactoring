package store.mybooks.resource.user_grade_name.enumeration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * packageName    : store.mybooks.resource.user_grade_name.enumeration
 * fileName       : UserGradeNameEnumTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */

public class UserGradeNameEnumTest {

    @Test
    public void testUserGradeNameEnumToString() {
        assertEquals("일반", UserGradeNameEnum.NORMAL.toString());
        assertEquals("골드", UserGradeNameEnum.GOLD.toString());
        assertEquals("플레티넘", UserGradeNameEnum.PLATINUM.toString());
        assertEquals("로얄", UserGradeNameEnum.ROYAL.toString());
    }
}
