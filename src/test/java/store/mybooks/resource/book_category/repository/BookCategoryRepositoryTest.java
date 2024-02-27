//package store.mybooks.resource.book_category.repository;
//
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import store.mybooks.resource.book_category.dto.response.CategoryGetResponse;
//
///**
// * packageName    : store.mybooks.resource.book_category.repository
// * fileName       : BookCategoryRepositoryTest
// * author         : damho-lee
// * date           : 2/22/24
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2/22/24          damho-lee          최초 생성
// */
//@DataJpaTest
//class BookCategoryRepositoryTest {
//    @Autowired
//    BookCategoryRepository bookCategoryRepository;
//
//    @Autowired
//    TestEntityManager testEntityManager;
//
//    @BeforeEach
//    void setup() {
//
//    }
//
//    @Test
//    void test() {
//        List<CategoryGetResponse> categoryGetResponseList = bookCategoryRepository.getCategoryListByBookId(1);
//        System.out.println("categorySize : " + categoryGetResponseList.size());
//    }
//}