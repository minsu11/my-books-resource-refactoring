package store.mybooks.resource.author.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.entity.Author;

/**
 * packageName    : store.mybooks.resource.author.repository
 * fileName       : AuthorRepositoryTest
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
    private Author author1;
    private Author author2;
    private Author author3;

    @BeforeEach
    public void setUp() {
        author1 = new Author(1, "authorName1", "author1_content", LocalDate.now());
        author2 = new Author(2, "authorName2", "author2_content", LocalDate.now());
        author3 = new Author(3, "authorName3", "author2_content", LocalDate.now());
        authorRepository.save(author1);
        authorRepository.save(author2);
    }

    @Test
    @DisplayName("전체 저자 조회(리스트)")
    void whenFindAllAuthors_thenReturnAllAuthorsGetResponseList() {
        List<AuthorGetResponse> result = authorRepository.getAllAuthors();

        assertThat(result.get(0).getName()).isEqualTo(author1.getName());
        assertThat(result.get(1).getName()).isEqualTo(author2.getName());
    }

    @Test
    @DisplayName("전체 저자 조회(페에지)")
    void givenAuthorList_whenFindPagedAllAuthors_thenReturnAllAuthorsGetResponse() {
        Pageable pageable = PageRequest.of(0, 2);

        Page<AuthorGetResponse> result = authorRepository.getAllPagedAuthors(pageable);

        assertThat(result.getContent().get(0).getName()).isEqualTo(author1.getName());
        assertThat(result.getContent().get(1).getName()).isEqualTo(author2.getName());
    }

    @Test
    @DisplayName("저자명 중복일 경우")
    void givenExistsAuthorName_whenExistsByName_thenReturnTrue() {

        Assertions.assertTrue(authorRepository.existsByName(author1.getName()));
    }

    @Test
    @DisplayName("저자명 중복이 아닐일 경우")
    void givenNotExistsAuthorName_whenExistsByName_thenReturnTrue() {
        Assertions.assertFalse(authorRepository.existsByName(author3.getName()));
    }
}
