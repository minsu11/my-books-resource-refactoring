package store.mybooks.resource.image.exception;

/**
 * packageName    : store.mybooks.resource.image.exception <br/>
 * fileName       : ImageNotExistsException<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/29/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/29/24        Fiat_lux       최초 생성<br/>
 */
public class ImageNotExistsException extends RuntimeException {
    public ImageNotExistsException(String message) {
        super(message);
    }
}
