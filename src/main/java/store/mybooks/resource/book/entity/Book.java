package store.mybooks.resource.book.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.book_status.entity.BookStatus;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.book.entity
 * fileName       : Book
 * author         : newjaehun
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        newjaehun       최초 생성
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_status_id")
    private BookStatus bookStatus;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_isbn")
    private String isbn;

    @Column(name = "book_publishdate")
    private LocalDate publishDate;

    @Column(name = "book_page")
    private Integer page;

    @Column(name = "book_index")
    private String index;

    @Column(name = "book_content")
    private String content;

    @Column(name = "book_original_cost")
    private Integer originalCost;

    @Column(name = "book_sale_cost")
    private Integer saleCost;


    @Column(name = "book_discount_rate")
    private int bookDiscountRate;

    @Column(name = "book_stock")
    private int bookStock;

    @Column(name = "book_viewcount")
    private int bookViewCount;


    @Column(name = "book_total_score")
    private int bookTotalScore;


    @Column(name = "book_review_count")
    private int bookReviewCount;

    @Column(name = "book_like_count")
    private int bookLikeCount;

    @Column(name = "is_packaging")
    private boolean isPackaging;

    @Column(name = "book_created_at")
    private LocalDate bookCreatedAt;
}
