package store.mybooks.resource.book_category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.book_category.dto.response.BookCategoryCreateResponse;
import store.mybooks.resource.book_category.dto.response.BookCategoryDeleteResponse;
import store.mybooks.resource.book_category.entity.BookCategory;

/**
 * packageName    : store.mybooks.resource.book_category.mapper
 * fileName       : BookCategoryMapper
 * author         : damho-lee
 * date           : 2/22/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/22/24          damho-lee          최초 생성
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookCategoryMapper {
    @Mapping(source = "category.name", target = "name")
    @Mapping(source = "book.name", target = "title")
    BookCategoryCreateResponse createResponse(BookCategory bookCategory);

    @Mapping(source = "book.name", target = "title")
    BookCategoryDeleteResponse deleteResponse(BookCategory bookCategory);
}
