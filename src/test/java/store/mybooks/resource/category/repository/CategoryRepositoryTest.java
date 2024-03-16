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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.book.dto.response.BookBriefResponseIncludePublishDate;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookcategory.entity.BookCategory;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.category.dto.response.CategoryGetResponse;
import store.mybooks.resource.category.dto.response.CategoryGetResponseForQuerydsl;
import store.mybooks.resource.category.entity.Category;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
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

    Book appleBook;

    Book bananaBook;

    @BeforeEach
    void setup() {
        Category grandParentCategory = new Category(null, "firstCategory");
        actualGrandParentCategory = categoryRepository.save(grandParentCategory);

        Category parentCategory = new Category(grandParentCategory, "secondCategory");
        actualParentCategory = categoryRepository.save(parentCategory);

        Category childCategory = new Category(parentCategory, "thirdCategory");
        actualChildCategory = categoryRepository.save(childCategory);

        BookStatus noSale = new BookStatus("판매종료");
        testEntityManager.persist(noSale);

        BookStatus noStock = new BookStatus("재고없음");
        testEntityManager.persist(noStock);

        Publisher publisher = new Publisher("작가");
        testEntityManager.persist(publisher);

        book = testEntityManager.persist(Book.builder()
                .bookStatus(noSale)
                .publisher(publisher)
                .name("도서")
                .isbn("1234567890123")
                .publishDate(LocalDate.of(2024, 1, 1))
                .page(100)
                .index("index")
                .explanation("content")
                .originalCost(20000)
                .saleCost(16000)
                .discountRate(20)
                .stock(5)
                .createdDate(LocalDate.now())
                .viewCount(1)
                .isPackaging(true)
                .build());

        appleBook = testEntityManager.persist(Book.builder()
                .bookStatus(noSale)
                .publisher(publisher)
                .name("사과 도서")
                .isbn("1111111111111")
                .publishDate(LocalDate.of(2024, 1, 2))
                .page(143)
                .index("index")
                .explanation("content")
                .originalCost(5000)
                .saleCost(4000)
                .discountRate(20)
                .stock(10)
                .createdDate(LocalDate.now())
                .viewCount(10)
                .isPackaging(false)
                .build());

        bananaBook = testEntityManager.persist(Book.builder()
                .bookStatus(noStock)
                .publisher(publisher)
                .name("바나나 도서")
                .isbn("2222222222222")
                .publishDate(LocalDate.of(2024, 1, 12))
                .page(189)
                .index("index")
                .explanation("content")
                .originalCost(16000)
                .saleCost(12000)
                .discountRate(25)
                .stock(0)
                .createdDate(LocalDate.now())
                .viewCount(100)
                .isPackaging(true)
                .build());

        BookCategory bookCategory = new BookCategory(
                new BookCategory.Pk(book.getId(), actualChildCategory.getId()),
                book,
                actualChildCategory
        );

        testEntityManager.persist(bookCategory);


        testEntityManager.persist(new BookCategory(
                new BookCategory.Pk(appleBook.getId(), actualParentCategory.getId()),
                appleBook,
                actualParentCategory
        ));

        testEntityManager.persist(new BookCategory(
                new BookCategory.Pk(bananaBook.getId(), actualGrandParentCategory.getId()),
                bananaBook,
                grandParentCategory
        ));
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

    @Test
    @DisplayName("카테고리 아이디의 최상위 카테고리 아이디 찾기")
    void givenCategoryId_whenFindHighestCategoryId_thenReturnRootCategoryId() {
        Integer highestCategoryIdOfLevelTwoCategory =
                categoryRepository.findHighestCategoryId(actualParentCategory.getId());
        Integer highestCategoryIdOfLevelThreeCategory =
                categoryRepository.findHighestCategoryId(actualChildCategory.getId());

        assertThat(highestCategoryIdOfLevelTwoCategory).isNotNull().isEqualTo(actualGrandParentCategory.getId());
        assertThat(highestCategoryIdOfLevelThreeCategory).isNotNull().isEqualTo(actualGrandParentCategory.getId());
    }

    @Test
    @DisplayName("카테고리 화면에서 보여줄 카테고리에 속하는 도서 조회")
    void givenCategoryIdAndPageable_whenGetBooksForCategoryView_thenReturnPageOfBookBriefResponseIncludePublishDate() {
        ImageStatus thumbnail = new ImageStatus(ImageStatusEnum.THUMBNAIL.getName());
        ImageStatus review = new ImageStatus(ImageStatusEnum.REVIEW.getName());

        testEntityManager.persist(thumbnail);
        testEntityManager.persist(review);

        Image grapeImage = new Image(
                "path",
                "grapeImage",
                "png",
                book,
                null,
                review
        );

        Image appleImage = new Image(
                "path",
                "appleImage",
                "png",
                appleBook,
                null,
                thumbnail
        );

        Image bananaImage = new Image(
                "path",
                "bananaImage",
                "png",
                bananaBook,
                null,
                thumbnail
        );

        testEntityManager.persist(grapeImage);
        testEntityManager.persist(appleImage);
        testEntityManager.persist(bananaImage);

        Pageable pageable = PageRequest.of(0, 3);

        Page<BookBriefResponseIncludePublishDate> actualPage =
                categoryRepository.getBooksForCategoryView(actualGrandParentCategory.getId(), pageable);

        assertThat(actualPage).isNotNull();
        assertThat(actualPage.getContent()).isNotNull().hasSize(1);
        List<BookBriefResponseIncludePublishDate> actualList = actualPage.getContent();
        assertThat(actualList.get(0).getId()).isNotNull().isEqualTo(bananaBook.getId());
        assertThat(actualList.get(0).getImageResponse().getPath()).isNotNull().isEqualTo(bananaImage.getPath());
        assertThat(actualList.get(0).getImageResponse().getFileName()).isNotNull().isEqualTo(bananaImage.getFileName());
        assertThat(actualList.get(0).getImageResponse().getExtension()).isNotNull()
                .isEqualTo(bananaImage.getExtension());
    }
}
