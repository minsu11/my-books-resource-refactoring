package store.mybooks.resource.tag.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * packageName    : store.mybooks.resource.tag.dto.request
 * fileName       : TagModifyRequest
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagModifyRequest {
    @Length(min = 2, max = 20)
    private String name;
}
