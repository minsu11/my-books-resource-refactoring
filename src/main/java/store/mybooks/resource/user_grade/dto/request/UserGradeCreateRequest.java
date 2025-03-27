package store.mybooks.resource.user_grade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.user_grade.dto.request<br>
 * fileName       : UserGradeCreateRequest<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserGradeCreateRequest {

    @NotNull
    @Positive
    private Integer rate;

    @NotBlank
    private String userGradeNameId;


}
