package store.mybooks.resource.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import store.mybooks.resource.author.dto.request.AuthorCreateRequest;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorDeleteResponse;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.repository.AuthorRepository;

/**
 * packageName    : store.mybooks.resource.author.service
 * fileName       : AuthorServiceTest
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private static final Integer id = 1;
    private static final String name = "author";
    private static final String content = "author_content";

    @BeforeEach
    void setUp(){
        author = new Author(id, name, content, LocalDate.now());
    }

    @Test
    @DisplayName("전체 저자 조회")
    void givenAuthorListAndPageable_whenFindAllAuthors_thenReturnPageAuthorsGetResponseList() {
        Pageable pageable = PageRequest.of(0, 2);
        List<AuthorGetResponse> authorGetResponseList = Arrays.asList(
                new AuthorGetResponse() {
                    @Override
                    public Integer getId() {
                        return id;
                    }
                    @Override
                    public String getName() {
                        return name;
                    }
                    @Override
                    public String getContent() {
                        return content;
                    }
                }, new AuthorGetResponse() {
                    @Override
                    public Integer getId() {
                        return 2;
                    }
                    @Override
                    public String getName() {
                        return "author2";
                    }
                    @Override
                    public String getContent() {
                        return "author2_content";
                    }
                }
        );
        Page<AuthorGetResponse> pageGetResponse =
                new PageImpl<>(authorGetResponseList, pageable, authorGetResponseList.size());
        when(authorRepository.findAllBy(pageable)).thenReturn(pageGetResponse);
        assertThat(authorService.getAllAuthors(pageable)).isEqualTo(pageGetResponse);

        verify(authorRepository,times(1)).findAllBy(pageable);

    }
    @Test
    @DisplayName("저자 추가")
    void givenAuthorNameAndContent_whenCreateAuthor_thenSaveAuthorAndReturnAuthorCreateResponse() {
        AuthorCreateRequest createRequest = new AuthorCreateRequest(name, content);

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorCreateResponse createResponse = authorService.createAuthor(createRequest);

        assertThat(createResponse.getName()).isEqualTo(author.getName());
        assertThat(createResponse.getContent()).isEqualTo(author.getContent());

        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    @DisplayName("저자 수정")
    void givenAuthorIdAndAuthorModifyRequest_whenModifyAuthor_thenModifyAuthorAndReturnAuthorModifyResponse() {
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest("changeName", "changeContent");
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorModifyResponse modifyResponse = authorService.modifyAuthor(author.getId(), modifyRequest);


        assertThat(modifyResponse.getChangeName()).isEqualTo(modifyRequest.getChangeName());
        assertThat(modifyResponse.getChangeContent()).isEqualTo(modifyRequest.getChangeContent());

        verify(authorRepository,times(1)).findById(author.getId());
    }

    @Test
    @DisplayName("저자 삭제")
    void givenAuthorId_whenDeleteAuthor_thenDeleteAuthorAndReturnAuthorDeleteResponse() {
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        doNothing().when(authorRepository).deleteById(id);

        AuthorDeleteResponse deleteResponse = authorService.deleteAuthor(id);

        assertThat(deleteResponse.getName()).isEqualTo(author.getName());

        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).deleteById(id);
    }
}