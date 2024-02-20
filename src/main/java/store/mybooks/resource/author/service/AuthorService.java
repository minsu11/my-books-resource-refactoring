package store.mybooks.resource.author.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.author.dto.request.AuthorCreateRequest;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorDeleteResponse;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.exception.AuthorAlreadyExistException;
import store.mybooks.resource.author.exception.AuthorNotExistException;
import store.mybooks.resource.author.mapper.AuthorMapper;
import store.mybooks.resource.author.repository.AuthorRepository;

/**
 * packageName    : store.mybooks.resource.author.service
 * fileName       : AuthorService
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<AuthorGetResponse> getAllAuthors(Pageable pageable) {
        return authorRepository.findByAll(pageable);
    }

    @Transactional
    public AuthorCreateResponse createAuthor(AuthorCreateRequest createRequest) {
        Author author = new Author(createRequest.getName(), createRequest.getContent());
        if (authorRepository.existsByName(author.getName())) {
            throw new AuthorAlreadyExistException(author.getName());
        }
        return AuthorMapper.INSTANCE.createResponse(authorRepository.save(author));
    }

    @Transactional
    public AuthorModifyResponse modifyAuthor(Integer authorId, AuthorModifyRequest modifyRequest) {
        Author author =
                authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistException(authorId));
        if (authorRepository.existsByName(modifyRequest.getChangeName())) {
            throw new AuthorAlreadyExistException(author.getName());
        }
        author.setByModifyRequest(modifyRequest);
        return AuthorMapper.INSTANCE.modifyResponse(author);
    }

    @Transactional
    public AuthorDeleteResponse deleteAuthor(Integer authorId) {
        Author author =
                authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotExistException(authorId));
        authorRepository.deleteById(authorId);
        return AuthorMapper.INSTANCE.deleteResponse(author);
    }
}
