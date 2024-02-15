package store.mybooks.resource.book_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.book_order.entity.BookOrder;

/**
 * packageName    : store.mybooks.resource.book_order.repository
 * fileName       : BookOrderRepository
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
public interface BookOrderRepository extends JpaRepository<BookOrder, Integer> {
}
