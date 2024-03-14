package store.mybooks.resource.image.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.image.dto.response.ImageGetResponse;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.review.entity.Review;

/**
 * packageName    : store.mybooks.resource.image.service <br/>
 * fileName       : ImageService<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/29/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/29/24        Fiat_lux       최초 생성<br/>
 */
public interface ImageService {
    ImageRegisterResponse saveImage(ImageStatus imageStatus, Review review, Book book, MultipartFile file) throws IOException;

    ImageGetResponse getObject(Long id);

    void deleteObject(Long id);
}
