package store.mybooks.resource.book_return.entity;

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
import lombok.Setter;
import store.mybooks.resource.return_rule.entity.ReturnRule;

/**
 * packageName    : store.mybooks.resource.book_return.entity
 * fileName       : BookReturn
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
@Table(name = "book_return")
@NoArgsConstructor
@AllArgsConstructor
public class BookReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_return_id")
    private Long id;

    @Column(name = "is_damage", nullable = false)
    private Boolean isDamage;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "return_rule_id")
    private ReturnRule returnRule;
}
