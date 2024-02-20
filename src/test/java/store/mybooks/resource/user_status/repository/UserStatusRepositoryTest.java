package store.mybooks.resource.user_status.repository;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.exception.UserStatusNotExistException;

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
    @DisplayName("id로 유저상태 조회시 올바르게 조회되는지 테스트")
    void givenUserStatusId_whenCallQueryById_thenReturnUserStatusGetResponse() {

        UserStatusGetResponse userStatusGetResponse1 = userStatusRepository.queryById("test1");
        UserStatusGetResponse userStatusGetResponse2 = userStatusRepository.queryById("test2");
        UserStatusGetResponse userStatusGetResponse3 = userStatusRepository.queryById("test3");

        assertEquals("test1", userStatusGetResponse1.getId());
        assertEquals("test2", userStatusGetResponse2.getId());
        assertEquals("test3", userStatusGetResponse3.getId());
    }

    @Test
    @DisplayName("UserStatus 전체 조회시 List<getResponse> 형태로 올바르게 조회되는지 테스트")
    void whenCallQueryAllBy_thenReturnUserStatusGetResponseList() {

        assertEquals(userStatusRepository.queryAllBy().size(), 3);

        UserStatus userStatus = new UserStatus("test4");
        userStatusRepository.save(userStatus);
        assertEquals(userStatusRepository.queryAllBy().size(), 4);

    }


}