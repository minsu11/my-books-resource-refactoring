package store.mybooks.resource.image.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.review.entity.Review;

/**
 * packageName    : store.mybooks.resource.file.entity <br/>
 * fileName       : File<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/22/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/22/24        Fiat_lux       최초 생성<br/>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_path")
    private String path;

    @Column(name = "image_file_name")
    private String fileName;

    @Column(name = "image_extension")
    private String extension;

    @Column(name = "image_created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_status_id")
    private ImageStatus imageStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public Image(String path, String fileName, String extension, Book book, Review review, ImageStatus imageStatus) {
        this.path = path;
        this.fileName = fileName;
        this.extension = extension;
        this.book = book;
        this.review = review;
        this.createdDate = LocalDate.now();
        this.imageStatus = imageStatus;
    }
}
