package store.mybooks.resource.book_status.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book_status.entity
 * fileName       : BookStatus
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
@Table(name = "book_status")
public class BookStatus {

    @Id
    @Column(name = "book_status_id")
    private String id;
}
