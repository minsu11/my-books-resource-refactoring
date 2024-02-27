package store.mybooks.resource.book_category.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.category.entity.Category;

/**
 * packageName    : store.mybooks.resource.book_category.entity
 * fileName       : BookCategory
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "book_category")
@NoArgsConstructor
@AllArgsConstructor
public class BookCategory {
    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @MapsId(value = "categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @MapsId(value = "bookId")
    private Book book;

    /**
     * BookCategory 의 Pk.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "book_id")
        private Long bookId;

        @Column(name = "category_id")
        private Integer categoryId;
    }
}
