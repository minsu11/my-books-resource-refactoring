package store.mybooks.resource.tag.entity;

import java.time.LocalDate;
import javax.persistence.*;

/**
 * packageName    : store.mybooks.resource.domain.entity
 * fileName       : Tag
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer id;

    @Column(name = "tag_name")
    private String name;

    @Column(name = "tag_created_date")
    private LocalDate createdDate;
}
