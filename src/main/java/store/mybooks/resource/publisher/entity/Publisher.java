package store.mybooks.resource.publisher.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;

/**
 * packageName    : store.mybooks.resource.publisher.entity
 * fileName       : Publisher
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
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Integer id;

    @Column(name = "publisher_name")
    private String name;

    @Column(name = "publisher_created_date")
    private LocalDate createdDate;

    public Publisher(String name) {
        this.name = name;
        this.createdDate = LocalDate.now();
    }

    public void setByModifyRequest(PublisherModifyRequest modifyRequest) {
        this.name = modifyRequest.getChangeName();
    }
}
