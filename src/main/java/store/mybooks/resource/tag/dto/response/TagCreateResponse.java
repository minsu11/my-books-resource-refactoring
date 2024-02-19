package store.mybooks.resource.tag.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.tag.dto.response
 * fileName       : TagCreateResponse
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@Getter
@Builder
public class TagCreateResponse {
    private String name;
}
