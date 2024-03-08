package store.mybooks.resource.image_status.enumeration;

import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.image.enumeration <br/>
 * fileName       : ImageStatusEnum<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/2/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/2/24        Fiat_lux       최초 생성<br/>
 */
@Getter
public enum ImageStatusEnum {
    THUMBNAIL("썸네일"),
    CONTENT("본문"),
    REVIEW("리뷰");

    private final String name;

    ImageStatusEnum(String name) {
        this.name = name;
    }

}
