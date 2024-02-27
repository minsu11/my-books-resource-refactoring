package store.mybooks.resource.wrap.dto;

import org.mapstruct.Mapper;
import store.mybooks.resource.wrap.dto.response.WrapCreateResponse;
import store.mybooks.resource.wrap.dto.response.WrapModifyResponse;
import store.mybooks.resource.wrap.entity.Wrap;

/**
 * packageName    : store.mybooks.resource.wrap.dto<br>
 * fileName       : WrapMapper<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@Mapper(componentModel = "spring")
public interface WrapMapper {
    WrapCreateResponse mapToWrapCreateResponse(Wrap wrap);

    WrapModifyResponse mapToWrapModifyResponse(Wrap wrap);
}
