package store.mybooks.resource.orderdetail.exception;

/**
 * packageName    : store.mybooks.resource.orderdetail.exception<br>
 * fileName       : OrderDetailNotExistException<br>
 * author         : masiljangajji<br>
 * date           : 3/23/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/23/24        masiljangajji       최초 생성
 */
public class OrderDetailNotExistException extends RuntimeException{
    public OrderDetailNotExistException(Long id) {
        super(String.format("[%d]은 존재하지않는 OrderDetailId 입니다",id));
    }
}
