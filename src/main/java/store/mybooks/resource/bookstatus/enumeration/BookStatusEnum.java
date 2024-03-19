package store.mybooks.resource.bookstatus.enumeration;

import lombok.Getter;
import lombok.Setter;
/**
 * packageName    : store.mybooks.resource.bookstatus.enumeration <br/>
 * fileName       : BookStatusEnum<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/17/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/17/24        Fiat_lux       최초 생성<br/>
 */
@Getter
public enum BookStatusEnum {
    DELETED_BOOK("삭제도서"),
    NO_STOCK("재고없음"),
    SELLING_END("판매종료"),
    SELLING_ING("판매중");

    private final String name;

    BookStatusEnum(String name) {
        this.name = name;
    }

}