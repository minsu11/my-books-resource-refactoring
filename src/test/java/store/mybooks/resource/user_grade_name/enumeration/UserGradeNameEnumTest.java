package store.mybooks.resource.user_grade_name.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

class UserGradeNameEnumTest {

    @Test
    @DisplayName("UserGradeName Enum 값 테스트")
     void testUserGradeNameEnumToString() {
        assertEquals("일반", UserGradeNameEnum.NORMAL.toString());
        assertEquals("골드", UserGradeNameEnum.GOLD.toString());
        assertEquals("플레티넘", UserGradeNameEnum.PLATINUM.toString());
        assertEquals("로얄", UserGradeNameEnum.ROYAL.toString());
    }
}
