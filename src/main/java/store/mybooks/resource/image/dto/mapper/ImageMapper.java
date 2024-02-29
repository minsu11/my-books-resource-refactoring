package store.mybooks.resource.image.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.entity.Image;

/**
 * packageName    : store.mybooks.resource.image.dto <br/>
 * fileName       : ImageMapper<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ImageMapper {
    ImageRegisterResponse mapToResponse(Image image);
}
