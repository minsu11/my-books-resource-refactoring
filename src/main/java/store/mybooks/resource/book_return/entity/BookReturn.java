package store.mybooks.resource.book_return.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.returnrule.entity.ReturnRule;

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
@Entity
@Table(name = "book_return")
@NoArgsConstructor
@AllArgsConstructor
public class BookReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_return_id")
    private Long id;

    @Column(name = "is_damage")
    private Boolean isDamage;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_rule_id")
    private ReturnRule returnRule;
}
