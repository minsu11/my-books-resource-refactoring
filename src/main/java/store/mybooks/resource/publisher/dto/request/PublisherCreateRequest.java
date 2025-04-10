package store.mybooks.resource.publisher.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 50)
    private String name;
}
