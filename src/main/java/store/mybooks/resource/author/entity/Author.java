package store.mybooks.resource.author.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.author.dto.request.AuthorModifyRequest;

/**
 * packageName    : store.mybooks.resource.author.entity
 * fileName       : Author
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
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Integer id;

    @Column(name = "author_name")
    private String name;

    @Column(name = "author_content")
    private String content;

    @Column(name = "author_created_date")
    private LocalDate createdDate;

    public Author(String name, String content) {
        this.name = name;
        this.content = content;
        this. createdDate = LocalDate.now();
    }

    public void setByModifyRequest(AuthorModifyRequest modifyRequest) {
        this.name = modifyRequest.getChangeName();
        this.content = modifyRequest.getChangeContent();
    }
}
