package store.mybooks.resource.image.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.image.entity.Image;

/**
 * packageName    : store.mybooks.resource.image.repository <br/>
 * fileName       : ImageRepository<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/29/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/29/24        Fiat_lux       최초 생성<br/>
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findImageByBook_IdAndImageStatus_Id(Long bookId, String imageStatusId);

    Optional<Image> findImageByReviewIdAndImageStatusId(Long reviewId, String imageStatusId);

    List<Image> findAllByBook_IdAndImageStatus_Id(Long bookId, String imageStatusId);

}
