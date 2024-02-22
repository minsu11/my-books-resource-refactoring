package store.mybooks.resource.publisher.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.publisher.mapper
 * fileName       : PublisherMapper
 * author         : newjaehun
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        newjaehun       최초 생성
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PublisherMapper {
    PublisherCreateResponse createResponse(Publisher publisher);

    PublisherModifyResponse modifyResponse(Publisher publisher);

    PublisherDeleteResponse deleteResponse(Publisher publisher);
}
