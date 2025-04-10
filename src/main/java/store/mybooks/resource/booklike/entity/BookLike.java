package store.mybooks.resource.booklike.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.book_like.entity
 * fileName       : BookLike
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
@Table(name = "book_like")
public class BookLike {
    @EmbeddedId
    private Pk pk;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * The type Pk.
     */
    @Embeddable
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Pk implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "book_id")
        private Long bookId;
    }
}
