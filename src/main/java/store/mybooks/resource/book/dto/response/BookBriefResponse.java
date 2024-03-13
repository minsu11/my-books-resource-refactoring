package store.mybooks.resource.book.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import store.mybooks.resource.image.dto.response.ImageResponse;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookBriefResponse<br/>
 * author         : newjaehun <br/>
 * date           : 2/25/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/25/24        newjaehun       최초 생성<br/>
 */
@Getter
@Setter
@NoArgsConstructor
public class BookBriefResponse {
    private Long id;

    private ImageResponse imageResponse;

    private String name;

//    private Double rate;

    private Integer saleCost;

    @QueryProjection
    public BookBriefResponse(Long id, ImageResponse imageResponse, String name, Integer saleCost) {
        this.id = id;
        this.imageResponse = imageResponse;
        this.name = name;
        this.saleCost = saleCost;
    }


}
