package store.mybooks.resource.author.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.author.dto.response
 * fileName       : AuthorGetResponse
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
@NoArgsConstructor
@AllArgsConstructor
public class AuthorGetResponse {
    private Integer id;

    private String name;

    private String content;
}
