package store.mybooks.resource.pointrulename.enumulation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : store.mybooks.resource.pointrulename.enumulation<br>
 * fileName       : PointRuleNameEnum<br>
 * author         : minsu11<br>
 * date           : 3/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/20/24        minsu11       최초 생성<br>
 */
@Getter
@RequiredArgsConstructor
public enum PointRuleNameEnum {
    LOGIN_POINT("로그인 적립"),
    BOOK_POINT("상품 적립"),
    REVIEW_POINT("리뷰 작성 적립"),
    REVIEW_IMAGE_POINT("리뷰 이미지 작성 적립"),
    SIGNUP_POINT("회원 가입 적립"),
    USE_POINT("포인트 사용");
    private final String value;
}
