package store.mybooks.resource.user_address.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user.entity<br>
 * fileName       : UserAddress<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Entity
@NoArgsConstructor
@Getter
@Table(name = "address")
public class UserAddress {


    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = " user_id")
    private User user;

    @Column(name = "address_alias")
    private String alias;

    @Column(name = "address_road_name")
    private String roadName;

    @Column(name = "address_detail")
    private String detail;

    @Column(name = "address_number")
    private Integer number;

    @Column(name = "address_reference")
    private String reference;


    public UserAddress(User user, String alias, String roadName, String detail, Integer number,
                       String reference) {
        this.user = user;
        this.alias = alias;
        this.roadName = roadName;
        this.detail = detail;
        this.number = number;
        this.reference = reference;
    }

    public void modifyByUserAddressModifyRequest(String alias, String detail, String reference) {

        this.alias = alias;
        this.detail = detail;
        this.reference = reference;
    }


}
