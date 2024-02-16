package store.mybooks.resource.publisher.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.publisher.dto.response
 * fileName       : PublisherCreateResponse
 * author         : newjaehun
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        newjaehun       최초 생성
 */
@Builder
@Getter
public class PublisherCreateResponse {
    private String name;
}
