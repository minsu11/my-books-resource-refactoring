package store.mybooks.resource.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import store.mybooks.resource.elastic.entity.Elastic;

/**
 * packageName    : store.mybooks.resource.elastic.repository <br/>
 * fileName       : ElasticRepository<br/>
 * author         : newjaehun <br/>
 * date           : 3/19/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/19/24        newjaehun       최초 생성<br/>
 */
public interface ElasticRepository extends ElasticsearchRepository<Elastic, String>, ElasticRepositoryCustom {
}
