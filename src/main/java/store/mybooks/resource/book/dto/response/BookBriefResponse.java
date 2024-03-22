package store.mybooks.resource.book.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String image;

    private String name;

    private Double rate;

    private Long reviewCount;

    private Integer cost;

    private Integer saleCost;

    public BookBriefResponse(Long id, String image, String name, Double rate, Long reviewCount, Integer cost,
                             Integer saleCost) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.rate = Math.round(rate * 10.0) / 10.0;
        this.reviewCount = reviewCount;
        this.cost = cost;
        this.saleCost = saleCost;
    }
}