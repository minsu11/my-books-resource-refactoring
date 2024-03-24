package store.mybooks.resource.pointhistory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

/**
 * packageName    : store.mybooks.resource.pointhistory.dto.response
 * fileName       : PointResponseForUser
 * author         : damho-lee
 * date           : 3/25/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/25/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class PointResponseForUser {
    Integer remainPoint;
    Page<PointHistoryResponse> pointHistoryResponsePage;
}
