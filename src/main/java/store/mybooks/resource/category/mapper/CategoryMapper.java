package store.mybooks.resource.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
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
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {
    CategoryCreateResponse createResponse(Category category);

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    @Mapping(source = "parentCategory.name", target = "parentCategoryName")
    CategoryModifyResponse modifyResponse(Category category);

    CategoryDeleteResponse deleteResponse(Category category);
}
