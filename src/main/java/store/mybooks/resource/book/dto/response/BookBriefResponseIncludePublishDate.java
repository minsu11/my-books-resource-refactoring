package store.mybooks.resource.book.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.book.dto.response
 * fileName       : BookBriefResponseIncludePublishDate
 * author         : damho-lee
 * date           : 3/14/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/14/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
public class BookBriefResponseIncludePublishDate {
    private Long id;

    private String image;

    private String name;

    private Double rate;

    private Long reviewCount;

    private Integer cost;

    private Integer saleCost;

    private LocalDate publishDate;
}
