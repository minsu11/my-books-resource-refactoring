package store.mybooks.resource.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.review.dto.response<br>
 * fileName       : ReviewRateResponse<br>
 * author         : masiljangajji<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/19/24        masiljangajji       최초 생성
 */

@Getter
@AllArgsConstructor
public class ReviewRateResponse {

    private Long totalCount;

    private Double averageRate;

}
