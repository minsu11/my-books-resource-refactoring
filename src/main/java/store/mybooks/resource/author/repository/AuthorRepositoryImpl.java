package store.mybooks.resource.author.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.author.entity.Author;
import store.mybooks.resource.author.entity.QAuthor;

/**
 * packageName    : store.mybooks.resource.author.repository <br/>
 * fileName       : AuthorRepositoryImpl<br/>
 * author         : newjaehun <br/>
 * date           : 2/26/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/26/24        newjaehun       최초 생성<br/>
 */
public class AuthorRepositoryImpl extends QuerydslRepositorySupport implements AuthorRepositoryCustom {
    public AuthorRepositoryImpl() {
        super(Author.class);
    }

    QAuthor author = QAuthor.author;

    @Override
    public List<AuthorGetResponse> getAllAuthors() {
        return from(author)
                .select(Projections.constructor(AuthorGetResponse.class, author.id, author.name, author.content))
                .fetch();
    }

    @Override
    public Page<AuthorGetResponse> getAllPagedAuthors(Pageable pageable) {
        List<AuthorGetResponse> lists = from(author)
                .select(Projections.constructor(AuthorGetResponse.class,
                        author.id, author.name, author.content))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = from(author).fetchCount();

        return new PageImpl<>(lists, pageable, total);
    }

    @Override
    public AuthorGetResponse getAuthorInfo(Integer authorId) {

        return from(author)
                .where(author.id.eq(authorId))
                .select(Projections.constructor(AuthorGetResponse.class, author.id, author.name, author.content))
                .fetchOne();
    }
}