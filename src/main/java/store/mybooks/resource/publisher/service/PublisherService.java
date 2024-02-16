package store.mybooks.resource.publisher.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherDeleteRequest;
import store.mybooks.resource.publisher.dto.request.PublisherGetRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.exception.PublisherAlreadyExistException;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.publisher.repository.PublisherRepository;

/**
 * packageName    : store.mybooks.resource.publisher.PublisherService
 * fileName       : PublisherService
 * author         : newjaehun
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        newjaehun       최초 생성
 */
@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public List<PublisherGetResponse> getAllPublisher() {
        List<Publisher> publisherList = publisherRepository.findAll();
        return publisherList.stream()
                .map(Publisher::convertToGetResponse)
                .collect(Collectors.toList());
    }

    public PublisherGetResponse getPublisher(PublisherGetRequest getRequest) {
        Publisher publisher =
                publisherRepository.findById(getRequest.getId()).orElseThrow(PublisherNotExistException::new);
        return publisher.convertToGetResponse();
    }

    public PublisherCreateResponse createPublisher(PublisherCreateRequest createRequest) {
        Publisher publisher = new Publisher();
        publisher.setByCreateRequest(createRequest);

        if (publisherRepository.findByName(createRequest.getName()).isPresent()) {
            throw new PublisherAlreadyExistException();
        }
        Publisher resultPublisher = publisherRepository.save(publisher);
        return resultPublisher.convertToCreateResponse();
    }

    public PublisherModifyResponse modifyPublisher(PublisherModifyRequest modifyRequest) {
        Publisher publisher =
                publisherRepository.findById(modifyRequest.getId()).orElseThrow(PublisherNotExistException::new);
        publisher.setByModifyRequest(modifyRequest);
        Publisher resultPublisher = publisherRepository.save(publisher);
        return resultPublisher.convertToModifyResponse();
    }


    public PublisherDeleteResponse deletePublisher(PublisherDeleteRequest deleteRequest) {
        Publisher publisher =
                publisherRepository.findById(deleteRequest.getId()).orElseThrow(PublisherNotExistException::new);

        publisherRepository.delete(publisher);
        return new PublisherDeleteResponse("출판사명: " + publisher.getName() + " 삭제 성공");
    }

}
