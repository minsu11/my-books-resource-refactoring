package store.mybooks.resource.book_tag.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.book_status.entity.BookStatus;
import store.mybooks.resource.book_tag.dto.request.BookTagCreateRequest;
import store.mybooks.resource.book_tag.repository.BookTagRepository;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.tag.entity.Tag;
import store.mybooks.resource.tag.repository.TagRepository;

/**
 * packageName    : store.mybooks.resource.book_tag.service
 * fileName       : BookTagServiceTest
 * author         : damho-lee
 * date           : 2/29/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/29/24          damho-lee          최초 생성
 */
@ExtendWith(MockitoExtension.class)
class BookTagServiceTest {
    @Mock
    BookTagRepository bookTagRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    BookTagService bookTagService;

    @Test
    @DisplayName("createBookTag 테스트")
    void givenBookTagCreateRequest_whenCreateBookTag_thenCreateBookTag() {
        List<Integer> tagIdList = new ArrayList<>();
        tagIdList.add(1);
        tagIdList.add(2);
        tagIdList.add(3);
        Tag firstTag = new Tag(1, "firstTag", LocalDate.now());
        Tag secondTag = new Tag(1, "secondTag", LocalDate.now());
        Tag thirdTag = new Tag(1, "thirdTag", LocalDate.now());
        BookStatus bookStatus = new BookStatus("판매중");
        Publisher publisher = new Publisher(1, "출판사1", LocalDate.now());
        Book book =
                new Book(1L, bookStatus, publisher, "도서1", "1234567898764", LocalDate.of(2024, 1, 1), 100, "인덱스1",
                        "내용1", 20000, 16000, 20, 5, 0, true, LocalDate.now());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(tagRepository.findById(1)).thenReturn(Optional.of(firstTag));
        when(tagRepository.findById(2)).thenReturn(Optional.of(secondTag));
        when(tagRepository.findById(3)).thenReturn(Optional.of(thirdTag));
        when(bookTagRepository.save(any())).thenReturn(null);
        BookTagCreateRequest bookCategoryCreateRequest = new BookTagCreateRequest(1L, tagIdList);

        bookTagService.createBookTag(bookCategoryCreateRequest);

        verify(bookTagRepository, times(3)).save(any());
    }

    @Test
    @DisplayName("deleteBookTag 테스트")
    void givenBookId_whenDeleteBookTag_thenDeleteBookTag() {
        when(bookTagRepository.existsByPk_BookId(anyLong())).thenReturn(true);
        doNothing().when(bookTagRepository).deleteByPk_BookId(anyLong());
        bookTagService.deleteBookTag(1L);
        verify(bookTagRepository, times(1)).deleteByPk_BookId(1L);
    }
}