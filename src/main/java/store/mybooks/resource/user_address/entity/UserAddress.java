package store.mybooks.resource.user_address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
