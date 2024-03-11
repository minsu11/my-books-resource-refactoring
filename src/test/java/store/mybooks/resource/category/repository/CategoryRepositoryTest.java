package store.mybooks.resource.category.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookcategory.entity.BookCategory;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForQuerydsl;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.category.repository
 * fileName       : CategoryRepositoryTest
 * author         : damho
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TestEntityManager testEntityManager;

    Category actualGrandParentCategory;

    Category actualParentCategory;

    Category actualChildCategory;

    Book book;

    @BeforeEach
    void setup() {
        Category grandParentCategory = new Category(null, "firstCategory");
        actualGrandParentCategory = categoryRepository.save(grandParentCategory);

        Category parentCategory = new Category(grandParentCategory, "secondCategory");
        actualParentCategory = categoryRepository.save(parentCategory);

        Category childCategory = new Category(parentCategory, "thirdCategory");
        actualChildCategory = categoryRepository.save(childCategory);

        BookStatus bookStatus = new BookStatus("판매중");
        testEntityManager.persist(bookStatus);

        Publisher publisher = new Publisher("작가");
        testEntityManager.persist(publisher);

        book = testEntityManager.persist(Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name("도서")
                .isbn("1234567890123")
                .publishDate(LocalDate.of(2024, 1, 1))
                .page(100)
                .index("index")
                .content("content")
                .originalCost(20000)
                .saleCost(16000)
                .discountRate(20)
                .stock(5)
                .createdDate(LocalDate.now())
                .build());

        BookCategory bookCategory = new BookCategory(
                new BookCategory.Pk(book.getId(), actualChildCategory.getId()),
                book,
                actualChildCategory
        );
        testEntityManager.persist(bookCategory);
    }

    @Test
    @DisplayName("최상위 카테고리들 가져오기")
    void givenCategory_whenFindHighestCategoryList_thenReturnHighestCategoryGetResponseList() {
        List<CategoryGetResponse> highestCategoryList = categoryRepository.findAllByParentCategoryIsNull();
        assertThat(highestCategoryList).hasSize(1);

        CategoryGetResponse parentActual = highestCategoryList.get(0);
        assertThat(parentActual.getName()).isEqualTo("firstCategory");
        assertThat(parentActual.getParentCategory()).isNull();
    }

    @Test
    @DisplayName("부모 카테고리 id 로 카테고리들 가져오기")
    void givenCategory_whenFindCategoryListByParentCategoryId_thenReturnCategoryGetResponseList() {
        List<CategoryGetResponse> childCategoryList =
                categoryRepository.findAllByParentCategory_Id(actualGrandParentCategory.getId());

        assertThat(childCategoryList).isNotNull().hasSize(1);
        assertThat(childCategoryList.get(0).getName()).isEqualTo("secondCategory");
        assertThat(childCategoryList.get(0).getParentCategory().getId()).isEqualTo(actualGrandParentCategory.getId());
    }

    @Test
    @DisplayName("도서 상세페이지에서 보여줄 카테고리 리스트 반환 메서드 테스트")
    void givenBookId_whenFindFullCategoryForBookViewByBookId_thenReturnListOfCategoryGetResponseForQuerydsl() {
        List<CategoryGetResponseForQuerydsl> actualList =
                categoryRepository.findFullCategoryForBookViewByBookId(book.getId());

        assertThat(actualList).isNotNull().hasSize(1);
        assertThat(actualList.get(0).getId()).isEqualTo(actualChildCategory.getId());
        assertThat(actualList.get(0).getName1()).isEqualTo(actualGrandParentCategory.getName());
        assertThat(actualList.get(0).getName2()).isEqualTo(actualParentCategory.getName());
        assertThat(actualList.get(0).getName3()).isEqualTo(actualChildCategory.getName());
    }
}
