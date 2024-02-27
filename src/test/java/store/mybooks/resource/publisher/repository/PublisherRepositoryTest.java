package store.mybooks.resource.publisher.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.publisher.repository
 * fileName       : PublisherRepositoryTest
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@DataJpaTest
class PublisherRepositoryTest {
    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher1;
    private Publisher publisher2;

    @BeforeEach
    public void setUp() {
        publisher1 = new Publisher(1, "publisherName1", LocalDate.now());
        publisher2 = new Publisher(2, "publisherName2", LocalDate.now());
        publisherRepository.save(publisher1);
        publisherRepository.save(publisher2);
    }

    @Test
    @DisplayName("전체 출판사 조회(리스트)")
    void whenFindAllBy_thenReturnPublisherGetResponseList() {
        List<PublisherGetResponse> result = publisherRepository.findAllBy();

        assertThat(result.get(0).getName()).isEqualTo(publisher1.getName());
        assertThat(result.get(1).getName()).isEqualTo(publisher2.getName());
        assertThat(result).hasSize(2);
    }


    @Test
    @DisplayName("전체 출판사 조회(페이징)")
    void givenPageable_whenPagedFindAllBy_thenReturnPublisherGetResponsePage() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<PublisherGetResponse> result = publisherRepository.getPagedBy(pageable);

        assertThat(result.getContent().get(0).getName()).isEqualTo(publisher1.getName());
        assertThat(result.getContent().get(1).getName()).isEqualTo(publisher2.getName());
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("출판사명 중복일 경우")
    void givenExistPublisherName_whenExistsByName_thenReturnTrue() {
        Publisher publisher = new Publisher(1, "publisherName1", LocalDate.now());
        publisherRepository.save(publisher);

        Assertions.assertTrue(publisherRepository.existsByName(publisher.getName()));
    }

    @Test
    @DisplayName("출판사명 중복이 아닐 경우")
    void givenNotExistPublisherName_whenExistsByName_thenReturnFalse() {
        Assertions.assertFalse(publisherRepository.existsByName("test"));
    }
}