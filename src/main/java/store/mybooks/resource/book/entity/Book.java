package store.mybooks.resource.book.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.bookstatus.entity.BookStatus;
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
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_status_id")
    private BookStatus bookStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_isbn")
    private String isbn;

    @Column(name = "book_publish_date")
    private LocalDate publishDate;

    @Column(name = "book_page")
    private Integer page;

    @Column(name = "book_index")
    private String index;

    @Column(name = "book_explanation")
    private String explanation;

    @Column(name = "book_original_cost")
    private Integer originalCost;

    @Column(name = "book_sale_cost")
    private Integer saleCost;

    @Column(name = "book_discount_rate")
    private Integer discountRate;

    @Column(name = "book_stock")
    private Integer stock;

    @Column(name = "book_view_count")
    private Integer viewCount;

    @Column(name = "is_packaging")
    private Boolean isPackaging;

    @Column(name = "book_created_date")
    private LocalDate createdDate;


    /**
     * methodName : setModifyRequest
     * author : newjaehun
     * description : 도서 객체를 수정하는 메서드.
     *
     * @param bookStatus   bookstatus
     * @param saleCost     saleCost
     * @param discountRate discountRate
     * @param stock        stock
     * @param isPackaging  isPackaging
     */
    public void setModifyRequest(BookStatus bookStatus, Integer saleCost, Integer discountRate, Integer stock,
                                 Boolean isPackaging) {
        this.bookStatus = bookStatus;
        this.saleCost = saleCost;
        this.discountRate = discountRate;
        this.stock = stock;
        this.isPackaging = isPackaging;

    }

    public void modifyStock(Integer stock) {
        this.stock = stock;
    }

    public void soldOut(Integer stock, BookStatus bookStatus) {
        modifyStock(stock);
        this.bookStatus = bookStatus;
    }
}
