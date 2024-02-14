package store.mybooks.resource.wrap.entity;

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
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.wrap.entity
 * fileName       : Wrap
 * author         : minsu11
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        minsu11       최초 생성
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wrap")
public class Wrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrap_id")
    private Integer id;

    @Column(name = "wrap_name", nullable = false)
    private String name;

    @Column(name = "wrap_cost", nullable = false)
    private Integer cost;

    @Column(name = "wrap_created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

}
