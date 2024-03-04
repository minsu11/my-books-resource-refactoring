package store.mybooks.resource.wrap.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.wrap.dto.response.WrapPageResponse;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.entity.Wrap;

/**
 * packageName    : store.mybooks.resource.wrap.repository<br>
 * fileName       : WrapRepositoryTest<br>
 * author         : minsu11<br>
 * date           : 2/28/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/28/24        minsu11       최초 생성<br>
 */
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WrapRepositoryTest {
    @Autowired
    WrapRepository wrapRepository;

    @BeforeEach
    void setUp() {
        Wrap wrap1 = new Wrap(1, "test1", 100, LocalDate.of(1212, 12, 12), true);
        Wrap wrap2 = new Wrap(2, "test2", 1000, LocalDate.of(1212, 12, 12), true);
        wrapRepository.save(wrap1);
        wrapRepository.save(wrap2);
    }

    @Test
    @Order(1)
    @DisplayName("id를 통해 사용 중인 포장지 Optional DTO 반환")
    void givenIntegerId_whenFindWrapResponseById_thenReturnOptionalWrapResponse() {
        String expectedName = "test1";
        Integer expectedCost = 100;
        Optional<WrapResponse> wrapResponse = wrapRepository.findWrapResponseById(1);

        Assertions.assertEquals(expectedName, wrapResponse.get().getName());
        Assertions.assertEquals(expectedCost, wrapResponse.get().getCost());
        Assertions.assertTrue(wrapResponse.get().getIsAvailable());
    }

    @Test
    @Order(2)
    @DisplayName("사용 중인 포장지 목록 찾기")
    void given_whenGetWrapResponseList_thenReturnWrapResponseList() {
        List<WrapResponse> expectedList = List.of(new WrapResponse("test1", 100, true),
                new WrapResponse("test2", 1000, true));

        List<WrapResponse> actualList = wrapRepository.getWrapResponseList();

        Assertions.assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        Assertions.assertEquals(expectedList.get(0).getCost(), actualList.get(0).getCost());
        Assertions.assertEquals(expectedList.get(0).getIsAvailable(), actualList.get(0).getIsAvailable());

        Assertions.assertEquals(expectedList.get(1).getName(), actualList.get(1).getName());
        Assertions.assertEquals(expectedList.get(1).getCost(), actualList.get(1).getCost());
        Assertions.assertEquals(expectedList.get(1).getIsAvailable(), actualList.get(1).getIsAvailable());
    }

    @Test
    @Order(3)
    @DisplayName("이름으로 사용 중인 포장지의 유무 확인")
    void givenWrapName_whenNonNull_thenReturnBoolean() {
        Assertions.assertTrue(wrapRepository.existWrap("test1"));
        Assertions.assertTrue(wrapRepository.existWrap("test2"));
        Assertions.assertFalse(wrapRepository.existWrap("test34"));
    }

    @Test
    @Order(4)
    @DisplayName("페이징된 값 가져오기")
    void givenPageable_whenGEtQueryDsl_thenReturnWrapPageResponsePage() {
        List<WrapPageResponse> expectedList = List.of(new WrapPageResponse(7, "test1", 100, true),
                new WrapPageResponse(8, "test2", 1000, true));
        Pageable pageable = PageRequest.of(0, 5);
        Page<WrapPageResponse> expected = new PageImpl<>(expectedList, pageable, 5);

        Page<WrapPageResponse> actual = wrapRepository.getPageBy(pageable);

        Assertions.assertEquals(expected.getContent().get(0).getId(), actual.getContent().get(0).getId());
        Assertions.assertEquals(expected.getContent().get(0).getName(), actual.getContent().get(0).getName());
        Assertions.assertEquals(expected.getContent().get(0).getCost(), actual.getContent().get(0).getCost());
        Assertions.assertEquals(expected.getContent().get(0).getIsAvailable(), actual.getContent().get(0).getIsAvailable());
        Assertions.assertEquals(expected.getContent().get(1).getId(), actual.getContent().get(1).getId());
        Assertions.assertEquals(expected.getContent().get(1).getName(), actual.getContent().get(1).getName());
        Assertions.assertEquals(expected.getContent().get(1).getCost(), actual.getContent().get(1).getCost());
        Assertions.assertEquals(expected.getContent().get(1).getIsAvailable(), actual.getContent().get(1).getIsAvailable());
    }
}