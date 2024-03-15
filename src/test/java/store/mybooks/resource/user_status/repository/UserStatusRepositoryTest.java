package store.mybooks.resource.user_status.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.entity.UserStatus;

/**
 * packageName    : store.mybooks.resource.user_status.repository
 * fileName       : UserStatusRepositoryTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */
@DataJpaTest
class UserStatusRepositoryTest {


    @Autowired
    UserStatusRepository userStatusRepository;


    @BeforeEach
    void setUp() {

        UserStatus userStatus1 = new UserStatus("test1");
        UserStatus userStatus2 = new UserStatus("test2");
        UserStatus userStatus3 = new UserStatus("test3");
        userStatusRepository.save(userStatus1);
        userStatusRepository.save(userStatus2);
        userStatusRepository.save(userStatus3);
    }

    @Test
    @DisplayName("UserStatusId 를 이용해 queryById 실행시 UserStatusGetResponse 반환")
    void givenUserStatusId_whenCallQueryById_thenReturnUserStatusGetResponse() {

        UserStatusGetResponse userStatusGetResponse1 = userStatusRepository.queryById("test1");
        UserStatusGetResponse userStatusGetResponse2 = userStatusRepository.queryById("test2");
        UserStatusGetResponse userStatusGetResponse3 = userStatusRepository.queryById("test3");

        assertEquals("test1", userStatusGetResponse1.getId());
        assertEquals("test2", userStatusGetResponse2.getId());
        assertEquals("test3", userStatusGetResponse3.getId());
    }

    @Test
    @DisplayName("QueryAllBy 실행시 List<UserStatusGetResponse> 반환")
    void whenCallQueryAllBy_thenReturnUserStatusGetResponseList() {

        assertEquals(3,userStatusRepository.queryAllBy().size());

        UserStatus userStatus = new UserStatus("test4");
        userStatusRepository.save(userStatus);
        assertEquals(4,userStatusRepository.queryAllBy().size());

    }


}