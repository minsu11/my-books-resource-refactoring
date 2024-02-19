package store.mybooks.resource.tag.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.entity.Tag;

/**
 * packageName    : store.mybooks.resource.tag.repository
 * fileName       : TagRepository
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<TagGetResponse> findAllBy();

    boolean existsByName(String name);

}
