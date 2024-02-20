package store.mybooks.resource.orders_status.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusCreateRequest;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusRequest;

/**
 * packageName    : store.mybooks.resource.ordersstatus.entity
 * fileName       : OrdersStatus
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
@Table(name = "orders_status")
public class OrdersStatus {
    @Id
    @Column(name = "orders_status_id")
    private String id;

    public OrdersStatus(OrdersStatusRequest request) {
        this.id = request.getId();
    }

    public OrdersStatus(OrdersStatusCreateRequest request) {
        this.id = request.getId();
    }

    public OrdersStatus() {

    }


}
