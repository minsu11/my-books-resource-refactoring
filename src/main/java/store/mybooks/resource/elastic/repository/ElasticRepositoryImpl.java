//package store.mybooks.resource.elastic.repository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import store.mybooks.resource.book.dto.response.BookBriefResponse;
//import store.mybooks.resource.elastic.entity.Elastic;
//
///**
// * packageName    : store.mybooks.resource.elastic.repository <br/>
// * fileName       : ElasticRepositoryImpl<br/>
// * author         : newjaehun <br/>
// * date           : 3/19/24<br/>
// * description    :<br/>
// * ===========================================================<br/>
// * DATE              AUTHOR             NOTE<br/>
// * -----------------------------------------------------------<br/>
// * 3/19/24        newjaehun       최초 생성<br/>
// */
//@RequiredArgsConstructor
//public class ElasticRepositoryImpl implements ElasticRepositoryCustom {
//    private final ElasticsearchOperations elasticsearchOperations;
//
//    @Override
//    public Page<BookBriefResponse> search(String query, Pageable pageable) {
//        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query)
//                .field("book_name", 100)
//                .field("book_name.ngram", 85)
//                .field("book_name.nori", 85)
//                .field("book_explanation", 15)
//                .field("book_explanation.ngram", 10)
//                .field("book_explanation.nori", 10)
//                .field("tag_names", 70)
//                .field("publisher_name", 50)
//                .field("author_names", 50);
//
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .withPageable(pageable)
//                .withSort(pageable.getSort())
//                .build();
//
//        SearchHits<Elastic> searchHits = elasticsearchOperations.search(nativeSearchQuery, Elastic.class);
//        List<BookBriefResponse> lists = searchHits.getSearchHits().stream()
//                .map(SearchHit::getContent)
//                .map(elastic -> new BookBriefResponse(
//                        elastic.getBookId(),
//                        elastic.getImage(),
//                        elastic.getName(),
//                        elastic.getRate(),
//                        elastic.getReviewCount(),
//                        elastic.getCost(),
//                        elastic.getSaleCost()
//                ))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(lists, pageable, searchHits.getTotalHits());
//    }
//
//}
