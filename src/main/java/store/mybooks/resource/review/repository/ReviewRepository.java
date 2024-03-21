package store.mybooks.resource.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.mybooks.resource.review.dto.response.ReviewRateResponse;
import store.mybooks.resource.review.entity.Review;

/**
 * packageName    : store.mybooks.resource.review.repository <br/>
 * fileName       : ReviewRepository<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */
public interface ReviewRepository extends JpaRepository<Review, Long> ,ReviewRepositoryCustom{


    Boolean existsByOrderDetailId(Long orderDetailId);

    @Query("SELECT new store.mybooks.resource.review.dto.response.ReviewRateResponse(COUNT(r), ROUND(AVG(r.rate), 1)) FROM Review r WHERE r.orderDetail.book.id = :bookId")
    ReviewRateResponse getReviewRate(@Param("bookId") Long bookId);





}
