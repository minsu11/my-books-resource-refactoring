package store.mybooks.resource.book.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import store.mybooks.resource.author.dto.response.AuthorGetResponse;
import store.mybooks.resource.category.dto.response.CategoryIdNameGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponseForBookDetail;

/**
 * packageName    : store.mybooks.resource.book.dto.response <br/>
 * fileName       : BookDetailResponse<br/>
 * author         : newjaehun <br/>
 * date           : 2/24/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/24/24        newjaehun       최초 생성<br/>
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailResponse {
    private Long id;

    private String thumbNailImage;

    private String name;

    private String bookStatus;

    private List<AuthorGetResponse> authorList;

    private PublisherGetResponse publisher;

    private LocalDate publishDate;

    private Integer saleCost;

    private Integer originalCost;

    private Integer disCountRate;

    private Double rate;

    private Long reviewCount;

    private Integer likeCount;

    private Boolean isPacking;

    private Integer page;

    private String isbn;

    private List<CategoryIdNameGetResponse> categoryList;

    private List<TagGetResponseForBookDetail> tagList;

    private Integer stock;

    private String index;

    private String explanation;

    private List<String> contentImageList;

}

