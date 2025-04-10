package store.mybooks.resource.review.dto.reqeust;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.review.dto.reqeust<br>
 * fileName       : ReviewModifyRequest<br>
 * author         : masiljangajji<br>
 * date           : 3/17/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/17/24        masiljangajji       최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModifyRequest {

    @NotNull
    private Integer rate;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    private String content;

}
