package store.mybooks.resource.user_address.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.entity.UserAddress;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade.repository.UserGradeRepository;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.repository.UserGradeNameRepository;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.repository.UserStatusRepository;

/**
 * packageName    : store.mybooks.resource.user_address.repository
 * fileName       : UserAddressRepositoryTest
 * author         : masiljangajji
 * date           : 2/21/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/21/24        masiljangajji       최초 생성
 */
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class UserAddressRepositoryTest {


    @Autowired
    UserAddressRepository userAddressRepository;

    @Autowired
    UserGradeNameRepository userGradeNameRepository;

    @Autowired
    UserGradeRepository userGradeRepository;

    @Autowired
    UserStatusRepository userStatusRepository;

    @Autowired
    UserRepository userRepository;

    Long userId;
    Long addressId;

    @BeforeEach
    void setUp() {

        UserStatus userStatus = new UserStatus("test");
        UserGradeName userGradeName = new UserGradeName("test");
        userStatusRepository.save(userStatus);
        userGradeNameRepository.save(userGradeName);

        UserGrade userGrade = new UserGrade(1, 1, 1, LocalDate.now(), userGradeName);
        userGradeRepository.save(userGrade);

        User user = new User("test@naver.com", LocalDate.now(), "test", "test", false, "test", userStatus, userGrade);
        User resultUser = userRepository.save(user);
        userId = resultUser.getId();

        UserAddress userAddress1 = new UserAddress(user, "test1", "test1", "test1", 1, "test1");
        UserAddress userAddress2 = new UserAddress(user, "test2", "test2", "test2", 2, "test2");
        addressId = userAddressRepository.save(userAddress1).getId();
        userAddressRepository.save(userAddress2);
    }

    @Test
    @DisplayName("UserAddressId , UserId 로 queryByIdAndUserId 실행시 UserAddressGetResponse 반환")
    void givenUserAddressIdAndUserId_whenQueryByIdAndUserId_thenReturnUserAddressGetResponse() {

        UserAddressGetResponse result = userAddressRepository.queryByIdAndUserId(addressId, userId).get();

        assertEquals(result.getAlias(), "test1");
        assertEquals(result.getDetail(), "test1");
        assertEquals(result.getReference(), "test1");
        assertEquals(result.getRoadName(), "test1");
        assertEquals(result.getNumber(), 1);
    }

    @Test
    @DisplayName("UserId 로 queryAllByUserId 실행시 List<UserAddressGetResponse> 반환")
    void givenUserId_whenCallQueryAllByUserId_thenReturnUserAddressGetResponseList() {

        List<UserAddressGetResponse> responseList = userAddressRepository.queryAllByUserId(userId);

        assertEquals(2, responseList.size());
        assertEquals("test1", responseList.get(0).getAlias());
        assertEquals("test2", responseList.get(1).getAlias());
    }

    @Test
    @DisplayName("Pageable 로 queryAllBy 실행시 Page<UserAddressGetResponse>반환")
    void givenPageable_whenCallQueryAllBy_thenReturnUserAddressGetResponsePage() {

        Page<UserAddressGetResponse> page = userAddressRepository.queryAllBy(PageRequest.of(0, 10));

        assertEquals(page.getTotalPages(), 1);
        assertEquals(page.getTotalElements(), 2);
    }

    @Test
    @DisplayName("UserId 로 countByUserId 실행시 User 의 전체 주소 개수 반환")
    void givenUserId_whenCallCountByUserId_thenReturnTotalAddressCount() {

        assertEquals(2, userAddressRepository.countByUserId(userId));
    }


}