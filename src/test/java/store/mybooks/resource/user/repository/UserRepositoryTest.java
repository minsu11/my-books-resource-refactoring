package store.mybooks.resource.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.netflix.discovery.converters.Auto;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user.repository
 * fileName       : UserRepositoryTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */


@DataJpaTest
class UserRepositoryTest {


    @Autowired
    UserRepository userRepository;
    @Autowired
    UserStatusRepository userStatusRepository;
    @Autowired
    UserGradeRepository userGradeRepository;

    Long id;

    @BeforeEach
    void setUp() {

        UserStatus userStatus = new UserStatus("test");
        UserGrade userGrade = new UserGrade();
        userStatusRepository.save(userStatus);
        userGradeRepository.save(userGrade);

        User user1 =
                new User("test1@naver.com", LocalDate.now(), "test1", "test1", false, "test1", userStatus, userGrade);
        User user2 =
                new User("test2@naver.com", LocalDate.now(), "test2", "test2", false, "test2", userStatus, userGrade);
        User resultUser = userRepository.save(user1);

        id = resultUser.getId();
        userRepository.save(user2);
    }


    @Test
    @DisplayName("email 을 이용해 User 를 조회")
    void givenUserEmail_whenCallFindByEmail_thenReturnUserCreateResponse() {

        User user1 = userRepository.findByEmail("test1@naver.com").get();
        User user2 = userRepository.findByEmail("test2@naver.com").get();

        assertEquals("test1@naver.com", user1.getEmail());
        assertEquals("test2@naver.com", user2.getEmail());
    }


    @Test
    @DisplayName("UserId 를 이용해 User 를 조회")
    void givenUserId_whenCallQueryById_thenReturnUserGetResponse() {

        UserGetResponse userGetResponse = userRepository.queryById(id).get();

        assertEquals("test1@naver.com", userGetResponse.getEmail());
        assertEquals("test1", userGetResponse.getPhoneNumber());
        assertEquals("test1", userGetResponse.getName());
    }

    @Test
    @DisplayName("Pageable 을 이용해 전체 User 를 Pagination")
    void givenPageable_whenCallQueryAllBy_thenReturnUserGetResponsePage() {

        Page<UserGetResponse> page = userRepository.queryAllBy(PageRequest.of(0, 10));



        assertEquals(1, page.getTotalPages());
        assertEquals(2, page.getTotalElements());

    }


}