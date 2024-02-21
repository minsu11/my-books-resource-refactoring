package store.mybooks.resource.author.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import store.mybooks.resource.author.mapper.AuthorMapper;
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
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private static final Integer id = 1;
    private static final String name = "author";
    private static final String content = "author_content";

    @BeforeEach
    void setUp() {
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

        verify(authorRepository, times(1)).findAllBy(pageable);

    }

    @Test
    @DisplayName("저자 추가")
    void givenAuthorNameAndContent_whenCreateAuthor_thenSaveAuthorAndReturnAuthorCreateResponse() {
        AuthorCreateRequest createRequest = new AuthorCreateRequest(name, content);
        AuthorCreateResponse createResponse = new AuthorCreateResponse();
        createResponse.setName(createRequest.getName());
        createResponse.setContent(createRequest.getContent());

        given(authorRepository.save(any())).willReturn(author);
        when(authorMapper.createResponse(any(Author.class))).thenReturn(createResponse);

        authorService.createAuthor(createRequest);

        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorMapper, times(1)).createResponse(any(Author.class));
    }

    @Test
    @DisplayName("저자 수정")
    void givenAuthorIdAndAuthorModifyRequest_whenModifyAuthor_thenModifyAuthorAndReturnAuthorModifyResponse() {
        AuthorModifyRequest modifyRequest = new AuthorModifyRequest("changeName", "changeContent");
        AuthorModifyResponse modifyResponse = new AuthorModifyResponse();
        modifyResponse.setChangedName(modifyRequest.getChangeName());
        modifyResponse.setChangedContent(modifyRequest.getChangeContent());

        given(authorRepository.findById(id)).willReturn(Optional.of(author));
        when(authorMapper.modifyResponse(any(Author.class))).thenReturn(modifyResponse);

        authorService.modifyAuthor(id, modifyRequest);

        verify(authorRepository, times(1)).findById(id);
        verify(authorMapper, times(1)).modifyResponse(any(Author.class));
    }

    @Test
    @DisplayName("저자 삭제")
    void givenAuthorId_whenDeleteAuthor_thenDeleteAuthorAndReturnAuthorDeleteResponse() {
        AuthorDeleteResponse deleteResponse = new AuthorDeleteResponse();
        deleteResponse.setName(name);

        given(authorRepository.findById(id)).willReturn(Optional.of(author));

        doNothing().when(authorRepository).deleteById(id);
        when(authorMapper.deleteResponse(any(Author.class))).thenReturn(deleteResponse);

        authorService.deleteAuthor(id);

        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).deleteById(id);
        verify(authorMapper, times(1)).deleteResponse(any(Author.class));
    }
}