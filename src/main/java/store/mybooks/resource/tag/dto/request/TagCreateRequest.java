package store.mybooks.resource.tag.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.tag.dto.request
 * fileName       : TagCreateRequest
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateRequest {
    @NotBlank
    @Size(min = 1, max = 20)
    private String name;
}
