package store.mybooks.resource.book_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.book_order.entity.BookOrder;

/**
 * packageName    : store.mybooks.resource.book_order.repository<br>
 * fileName       : BookOrderRepository<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
public interface BookOrderRepository extends JpaRepository<BookOrder, Long>, BookOrderRepositoryCustom {
}
