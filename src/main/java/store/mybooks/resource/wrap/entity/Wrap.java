package store.mybooks.resource.wrap.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.wrap.dto.request.WrapModifyRequest;

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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wrap")
public class Wrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrap_id")
    private Integer id;

    @Column(name = "wrap_name")
    private String name;

    @Column(name = "wrap_cost")
    private Integer cost;

    @Column(name = "wrap_created_date")
    private LocalDate createdDate;

    @Column(name = "is_available")
    private Boolean isAvailable;

    public Wrap(String name, Integer cost) {
        this.name = name;
        this.cost = cost;
        this.createdDate = LocalDate.now();
        this.isAvailable = true;
    }

    public void modifyWrap(WrapModifyRequest wrapModifyRequest) {
        this.name = wrapModifyRequest.getName();
        this.cost = wrapModifyRequest.getCost();
    }

    public void modifyIsAvailable(Boolean status) {
        isAvailable = status;
    }

}
