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
import store.mybooks.resource.user.entity.User;

/**
 * packageName    : store.mybooks.resource.user.entity
 * fileName       : UserAddress
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@Entity
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

    @Column(name = "address_full_name")
    private String fullName;


}
