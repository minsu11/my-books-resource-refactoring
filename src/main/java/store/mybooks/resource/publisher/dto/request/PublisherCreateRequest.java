package store.mybooks.resource.publisher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.publisher.dto.request
 * fileName       : PublisherCreateRequest
 * author         : newjaehun
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        newjaehun       최초 생성
 */
@Getter
@AllArgsConstructor
public class PublisherCreateRequest {
    private String name;
}
