package store.mybooks.resource.author.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.author.dto.response.AuthorCreateResponse;
import store.mybooks.resource.author.dto.response.AuthorDeleteResponse;
import store.mybooks.resource.author.dto.response.AuthorModifyResponse;
import store.mybooks.resource.author.entity.Author;

/**
 * packageName    : store.mybooks.resource.author.mapper
 * fileName       : AuthorMapper
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthorMapper {
    AuthorCreateResponse createResponse(Author author);


    @Mapping(source = "name", target = "changedName")
    @Mapping(source = "content", target = "changedContent")
    AuthorModifyResponse modifyResponse(Author author);

    AuthorDeleteResponse deleteResponse(Author author);

}