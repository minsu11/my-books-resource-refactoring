package store.mybooks.resource.author.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.author.dto.response
 * fileName       : AuthorModifyResponse
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@Getter
@Setter
public class AuthorModifyResponse {
    private String changedName;
    private String changedContent;
}
