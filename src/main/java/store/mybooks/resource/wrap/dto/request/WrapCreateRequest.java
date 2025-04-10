package store.mybooks.resource.wrap.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.wrap.dto.request<br>
 * fileName       : WrapCreateRequaer<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    : 등록할 포장지 DTO
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class WrapCreateRequest {
    @Size(min = 2, max = 20)
    @NotBlank
    private String name;

    @Positive
    @Max(100000)
    private Integer cost;
}
