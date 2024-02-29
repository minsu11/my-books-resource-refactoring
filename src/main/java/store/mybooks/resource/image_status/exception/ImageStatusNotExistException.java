package store.mybooks.resource.image_status.exception;

/**
 * packageName    : store.mybooks.resource.image_status.exception <br/>
 * fileName       : ImageStatusNotExistException<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/28/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/28/24        Fiat_lux       최초 생성<br/>
 */
public class ImageStatusNotExistException extends RuntimeException{
    public ImageStatusNotExistException(String message) {
        super(message);
    }
}
