package store.mybooks.resource.publisher.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.publisher.dto.response
 * fileName       : PublisherModifyResponse
 * author         : newjaehun
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        newjaehun       최초 생성
 */
@Builder
@Getter
public class PublisherModifyResponse {
    private String name;
}
