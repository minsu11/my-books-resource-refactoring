package store.mybooks.resource.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.author.dto.request
 * fileName       : AuthorModifyRequest
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
public class AuthorModifyRequest {
    @NotBlank
    @Size(min = 1, max = 50)
    private String changeName;

    private String changeContent;
}

