package store.mybooks.resource.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.author.dto.request
 * fileName       : AuthorCreateRequest
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    private String content;
}
