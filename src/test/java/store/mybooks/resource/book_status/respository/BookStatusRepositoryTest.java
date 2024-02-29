package store.mybooks.resource.book_status.respository;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import store.mybooks.resource.book_status.dto.response.BookStatusGetResponse;
import store.mybooks.resource.book_status.entity.BookStatus;

/**
 * packageName    : store.mybooks.resource.book_status.respository <br/>
 * fileName       : BookStatusRepositoryTest<br/>
 * author         : newjaehun <br/>
 * date           : 2/23/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/23/24        newjaehun       최초 생성<br/>
 */
@DataJpaTest
class BookStatusRepositoryTest {
    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Test
    @DisplayName("전체 도서상태 조회")
    void whenFindAllBookStatuses_thenReturnBookStatusGetResponseList() {
        BookStatus bookStatus1 = new BookStatus("판매중");
        BookStatus bookStatus2 = new BookStatus("판매종료");
        bookStatusRepository.save(bookStatus1);
        bookStatusRepository.save(bookStatus2);

        List<BookStatusGetResponse> bookStatusGetResponseList = bookStatusRepository.findAllBy();

        Assertions.assertEquals(2, bookStatusGetResponseList.size());
    }
}