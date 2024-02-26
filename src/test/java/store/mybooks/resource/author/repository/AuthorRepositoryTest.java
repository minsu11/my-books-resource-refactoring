package store.mybooks.resource.author.repository;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
