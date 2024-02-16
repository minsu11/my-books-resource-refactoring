package store.mybooks.resource.publisher.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
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

    @Transactional(readOnly = true)
    public List<PublisherGetResponse> getAllPublisher() {
        List<Publisher> publisherList = publisherRepository.findAll();
        return publisherList.stream()
                .map(Publisher::convertToGetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublisherGetResponse getPublisher(Integer publisherId) {
        Publisher publisher =
                publisherRepository.findById(publisherId).orElseThrow(PublisherNotExistException::new);
        return publisher.convertToGetResponse();
    }

    @Transactional
    public PublisherCreateResponse createPublisher(PublisherCreateRequest createRequest) {
        Publisher publisher = new Publisher(createRequest);

        if(Boolean.TRUE.equals(publisherRepository.existsByName(createRequest.getName()))){
            throw new PublisherAlreadyExistException();
        }

        Publisher resultPublisher = publisherRepository.save(publisher);
        return resultPublisher.convertToCreateResponse();
    }

    @Transactional
    public PublisherModifyResponse modifyPublisher(PublisherModifyRequest modifyRequest) {
        Publisher publisher =
                publisherRepository.findById(modifyRequest.getId()).orElseThrow(PublisherNotExistException::new);
        publisher.setByModifyRequest(modifyRequest);
        return publisher.convertToModifyResponse();
    }

    @Transactional
    public PublisherDeleteResponse deletePublisher(Integer publisherId) {
        Publisher publisher =
                publisherRepository.findById(publisherId).orElseThrow(PublisherNotExistException::new);
        publisherRepository.delete(publisher);
        return new PublisherDeleteResponse("출판사명: " + publisher.getName() + " 삭제 성공");
    }

}
