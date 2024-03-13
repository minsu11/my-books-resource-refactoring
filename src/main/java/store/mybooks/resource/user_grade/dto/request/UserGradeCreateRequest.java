package store.mybooks.resource.user_grade.dto.request;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer minCost;

    @NotNull
    @Positive
    private Integer maxCost;

    @NotNull
    @Positive
    private Integer rate;

    @NotNull
    private LocalDate createdDate;

}
