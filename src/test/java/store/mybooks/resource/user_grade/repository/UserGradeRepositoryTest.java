package store.mybooks.resource.user_grade.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_grade_name.repository.UserGradeNameRepository;

/**
 * packageName    : store.mybooks.resource.user_grade.repository
 * fileName       : UserGradeRepositoryTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */

@DataJpaTest
class UserGradeRepositoryTest {

    @Autowired
    UserGradeRepository userGradeRepository;

    @Autowired
    UserGradeNameRepository userGradeNameRepository;

    LocalDate localDate;

    Integer userGradeId;

    @BeforeEach
    void setUp() {

        localDate = LocalDate.now();

        UserGradeName userGradeName1 = new UserGradeName("일반");
        UserGradeName userGradeName2 = new UserGradeName("로얄");

        userGradeNameRepository.save(userGradeName1);
        userGradeNameRepository.save(userGradeName2);

        UserGrade userGrade1 = new UserGrade(1, userGradeName1, 0, 1000, 3, localDate, true);
        UserGrade userGrade2 = new UserGrade(2, userGradeName1, 0, 1000, 3, localDate, false);
        UserGrade userGrade3 = new UserGrade(3, userGradeName2, 0, 1000, 3, localDate, true);

        userGradeId = userGradeRepository.save(userGrade1).getId();
        userGradeRepository.save(userGrade2);
        userGradeRepository.save(userGrade3);


    }

    @Test
    @DisplayName("id로 유저등급 조회시 올바르게 조회되는지 테스트")
    void givenUserGradeId_whenCallQueryById_thenReturnUserGradeGetResponse() {

        UserGradeGetResponse userGradeGetResponse = userGradeRepository.queryById(userGradeId);

        assertEquals("일반", userGradeGetResponse.getUserGradeNameId());
        assertEquals(3, userGradeGetResponse.getRate());
        assertEquals(localDate, userGradeGetResponse.getCreatedDate());
        assertEquals(1000, userGradeGetResponse.getMaxCost());
        assertEquals(0, userGradeGetResponse.getMinCost());
    }

    @Test
    @DisplayName("UserGradeName으로 사용가능한 UserGrade를 조회시 올바르게 조회되는지 테스트")
    void givenUserGradeName_whenCallFindByUserGradeNameIdAndIsAvailableIsTrue_thenReturnOptionalUserGrade() {

        UserGrade userGrade = userGradeRepository.findByUserGradeNameIdAndIsAvailableIsTrue("일반").get();

        assertEquals("일반", userGrade.getUserGradeName().getId());
        assertEquals(true, userGrade.getIsAvailable());
    }

    @Test
    void givenPageable_whenQueryAllBy_thenReturnUserGradeGetResponsePage() {

        Page<UserGradeGetResponse> page = userGradeRepository.queryAllBy(PageRequest.of(0, 10));

        assertEquals(3, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

    }


}