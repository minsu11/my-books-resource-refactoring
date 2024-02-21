package store.mybooks.resource.publisher.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.entity.Publisher;

/**
 * packageName    : store.mybooks.resource.publisher.repository
 * fileName       : PublisherRepository
 * author         : newjaehun
 * date           : 2/14/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/14/24        newjaehun       최초 생성
 */
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Page<PublisherGetResponse> findAllBy(Pageable pageable);

    Boolean existsByName(String name);
}
