package store.mybooks.resource.book_status.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.book_status.dto.response.BookStatusGetResponse;
import store.mybooks.resource.book_status.respository.BookStatusRepository;

/**
 * packageName    : store.mybooks.resource.book_status.service <br/>
 * fileName       : BookStatusServiceTest<br/>
 * author         : newjaehun <br/>
 * date           : 2/23/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/23/24        newjaehun       최초 생성<br/>
 */
@ExtendWith(MockitoExtension.class)
class BookStatusServiceTest {
    @Mock
    private BookStatusRepository bookStatusRepository;
    @InjectMocks
    private BookStatusService bookStatusService;

    @Test
    @DisplayName("전체 도서상태 조회")
    void whenFindAllBookStatuses_thenReturnBookStatusGetResponseList() {
        List<BookStatusGetResponse> bookStatusRepositoryList = Arrays.asList(new BookStatusGetResponse() {
            @Override
            public String getId() {
                return "판매중";
            }
        }, new BookStatusGetResponse() {
            @Override
            public String getId() {
                return "판매중단";
            }
        });


        when(bookStatusRepository.findAllBy()).thenReturn(bookStatusRepositoryList);
        List<BookStatusGetResponse> result = bookStatusService.getAllBookStatus();

        assertEquals("판매중", result.get(0).getId());
        assertEquals("판매중단", result.get(1).getId());

        verify(bookStatusRepository, times(1)).findAllBy();
    }
}