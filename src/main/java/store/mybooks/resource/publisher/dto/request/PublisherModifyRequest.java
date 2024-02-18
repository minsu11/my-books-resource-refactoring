package store.mybooks.resource.publisher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.publisher.dto.request
 * fileName       : PublisherModifyRequest
 * author         : newjaehun
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        newjaehun       최초 생성
 */
@Getter
@AllArgsConstructor
public class PublisherModifyRequest {
    private String changeName;
}
