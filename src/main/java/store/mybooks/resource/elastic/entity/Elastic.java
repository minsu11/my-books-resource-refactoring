package store.mybooks.resource.elastic.entity;

import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


/**
 * packageName    : store.mybooks.resource.elastic.entity <br/>
 * fileName       : Elastic<br/>
 * author         : newjaehun <br/>
 * date           : 3/19/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/19/24        newjaehun       최초 생성<br/>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "mybooks")
public class Elastic {
    @Id
    private String id;

    @Field(name = "book_id")
    private Long bookId;

    @Field(name = "image")
    private String image;

    @Field(name = "book_name")
    private String name;

    @Field(name = "avg_rate")
    private Float rate;

    @Field(name = "book_original_cost")
    private Integer cost;

    @Field(name = "book_sale_cost")
    private Integer saleCost;

    @Field(name = "book_view_count")
    private Integer viewCount;

    @Field(name = "book_publish_date")
    private LocalDateTime publishDate;

    @Field(name = "book_review_count")
    private Integer reviewCount;
}

