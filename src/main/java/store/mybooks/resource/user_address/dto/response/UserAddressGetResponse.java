package store.mybooks.resource.user_address.dto.response;

/**
 * packageName    : store.mybooks.resource.user_address.dto.response<br>
 * fileName       : UserAddressGetResponse<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public interface UserAddressGetResponse {

    Long getId();
    String getAlias();

    String getRoadName();

    String getDetail();

    Integer getNumber();

    String getReference();

}
