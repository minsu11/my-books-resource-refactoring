package store.mybooks.resource.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;
import store.mybooks.resource.category.dto.response.CategoryDeleteResponse;
import store.mybooks.resource.category.dto.response.CategoryModifyResponse;
import store.mybooks.resource.category.entity.Category;

/**
 * packageName    : store.mybooks.resource.category.mapper
 * fileName       : CategoryMapper
 * author         : damho-lee
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24          damho-lee          최초 생성
 */
@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryCreateResponse createResponse(Category category);

    CategoryModifyResponse modifyResponse(Category category);

    CategoryDeleteResponse deleteResponse(Category category);
}
