package store.mybooks.resource.book.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.book.dto.response.BookCreateResponse;
import store.mybooks.resource.book.dto.response.BookModifyResponse;
import store.mybooks.resource.book.entity.Book;

/**
 * packageName    : store.mybooks.resource.book.mapper <br/>
 * fileName       : BookMapper<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookMapper {
    BookCreateResponse createResponse(Book book);


    BookModifyResponse modifyResponse(Book book);
}
