package store.mybooks.resource.orderdetail.repository;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import store.mybooks.resource.image.entity.QImage;
import store.mybooks.resource.image_status.entity.QImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.orderdetail.dto.response.OrderDetailInfoResponse;
import store.mybooks.resource.orderdetail.entity.OrderDetail;
import store.mybooks.resource.orderdetail.entity.QOrderDetail;
import store.mybooks.resource.orderdetailstatus.entity.QOrderDetailStatus;

/**
 * packageName    : store.mybooks.resource.order_detail.repository<br>
 * fileName       : OrderDetailRespositoryImpl<br>
 * author         : minsu11<br>
 * date           : 3/18/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/18/24        minsu11       최초 생성<br>
 */
public class OrderDetailRepositoryImpl extends QuerydslRepositorySupport implements OrderDetailRepositoryCustom {
    private QOrderDetail orderDetail = QOrderDetail.orderDetail;
    private QImage image = QImage.image;
    private QOrderDetailStatus orderDetailStatus = QOrderDetailStatus.orderDetailStatus;
    private QImageStatus imageStatus = QImageStatus.imageStatus;


    public OrderDetailRepositoryImpl() {
        super(OrderDetail.class);
    }

    @Override
    public List<OrderDetailInfoResponse> getOrderDetailList(Long bookOrderId) {
        return from(orderDetail)
                .select(Projections.constructor(
                        OrderDetailInfoResponse.class,
                        orderDetail.book.id,
                        orderDetail.book.name,
                        orderDetail.userCoupon.id,
                        orderDetail.bookCost,
                        orderDetail.amount,
                        orderDetail.isCouponUsed
                ))
                .where(orderDetail.id.eq(bookOrderId))
                .fetch();
    }

    @Override
    public List<OrderDetailInfoResponse> getOrderDetailListByOrderNumber(String orderNumber) {
        
        return from(orderDetail)
                .join(image).on(image.book.eq(orderDetail.book))
                .join(image.imageStatus, imageStatus)
                .join(orderDetail.detailStatus, orderDetailStatus)
                .where(imageStatus.id.eq(ImageStatusEnum.THUMBNAIL.getName()))
                .select(Projections.constructor(
                        OrderDetailInfoResponse.class,
                        orderDetail.book.id,
                        orderDetail.book.name,
                        orderDetail.userCoupon.id,
                        orderDetail.bookCost,
                        orderDetail.amount,
                        orderDetail.isCouponUsed,
                        image.path.concat(image.fileName).concat(image.extension),
                        orderDetail.detailStatus.id,
                        orderDetail.id
                ))
                .where(orderDetail.bookOrder.number.eq(orderNumber))
                .fetch();
    }
}
