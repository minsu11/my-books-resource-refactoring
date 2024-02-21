package store.mybooks.resource.publisher.repository;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    @DisplayName("전체 출판사 조회")
    void givenPageable_whenPagedFindAllBy_thenReturnPublisherGetResponsePage() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<PublisherGetResponse> result = publisherRepository.findAllBy(pageable);

        Assertions.assertEquals(publisher1.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(publisher1.getName(), result.getContent().get(0).getName());
        Assertions.assertEquals(publisher2.getId(), result.getContent().get(1).getId());
        Assertions.assertEquals(publisher2.getName(), result.getContent().get(1).getName());
        Assertions.assertEquals(2, result.getSize());
    }

    @Test
    @Order(2)
    @DisplayName("출판사명 중복일 경우")
    void givenExistPublisherName_whenExistsByName_thenReturnTrue() {
        Publisher publisher = new Publisher(1, "publisherName1", LocalDate.now());
        publisherRepository.save(publisher);

        Assertions.assertTrue(publisherRepository.existsByName(publisher.getName()));
    }

    @Test
    @Order(3)
    @DisplayName("출판사명 중복이 아닐 경우")
    void givenNotExistPublisherName_whenExistsByName_thenReturnFalse() {
        Assertions.assertFalse(publisherRepository.existsByName("test"));
    }
}