package store.mybooks.resource.user_status.enumeration;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * packageName    : store.mybooks.resource.user_status.enumeration
 * fileName       : UserStatusEnumTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */


public class UserStatusEnumTest {

    @Test
    @DisplayName("userStatusEnum 값 테스트")
    public void testUserStatusEnumToString() {
        assertEquals("활성", UserStatusEnum.ACTIVE.toString());
        assertEquals("휴면", UserStatusEnum.INACTIVE.toString());
        assertEquals("탈퇴", UserStatusEnum.RESIGN.toString());
    }
}