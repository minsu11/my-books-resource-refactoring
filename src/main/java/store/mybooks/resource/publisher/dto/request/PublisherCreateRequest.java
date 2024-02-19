package store.mybooks.resource.publisher.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class PublisherCreateRequest {
    @NotBlank
    private String name;
}
