package store.mybooks.resource.publisher.service;

import java.util.List;
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
import store.mybooks.resource.publisher.mapper.PublisherMapper;
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
    /**
     * methodName : getAllPublisher
     * author : newjaehun
     * description : 전체 출판사 리스트 반환
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<PublisherGetResponse> getAllPublisher() {
        return publisherRepository.findAllBy();
    }

    /**
     * methodName : createPublisher
     * author : newjaehun
     * description : 출판사 추가하는 메서드
     *
     * @param createRequest: 추가할 name, name이 이미 존재하는 경우 PublisherAlreadyExistException 발생
     * @return publisherCreateResponse: 추가된 name
     */
    @Transactional
    public PublisherCreateResponse createPublisher(PublisherCreateRequest createRequest) {
        Publisher publisher = new Publisher(createRequest.getName());

        if(Boolean.TRUE.equals(publisherRepository.existsByName(createRequest.getName()))){
            throw new PublisherAlreadyExistException();
        }

        Publisher resultPublisher = publisherRepository.save(publisher);
        return PublisherMapper.INSTANCE.createResponse(resultPublisher);
    }

    /**
     * methodName : modifyPublisher
     * author : newjaehun
     * description : 출판사 수정하는 메서드
     *
     * @param publisherId 수정하려는 publisher 의 id, 존재하지 않으면 PublisherNotExistException 발생
     * @param modifyRequest: 수정할 name 포함
     * @return publisherModifyResponse: 수정된 name 포함
     */
    @Transactional
     public PublisherModifyResponse modifyPublisher(Integer publisherId, PublisherModifyRequest modifyRequest) {
        Publisher publisher =
                publisherRepository.findById(publisherId).orElseThrow(PublisherNotExistException::new);

        if(Boolean.TRUE.equals(publisherRepository.existsByName(modifyRequest.getChangeName()))){
            throw new PublisherAlreadyExistException();
        }
        publisher.setByModifyRequest(modifyRequest);
        return PublisherMapper.INSTANCE.modifyResponse(publisher);
    }

    /**
     * methodName : deletePublisher
     * author : newjaehun
     * description : 출판사 삭제하는 메서드
     *
     * @param publisherId 삭제하려는 publisher 의 id, 존재하지 않으면 PublisherNotExistException 발생
     * @return publisherDeleteResponse: 삭제된 name 포함
     */
    @Transactional
    public PublisherDeleteResponse deletePublisher(Integer publisherId) {
        Publisher publisher =
                publisherRepository.findById(publisherId).orElseThrow(PublisherNotExistException::new);

        publisherRepository.deleteById(publisherId);
        return PublisherMapper.INSTANCE.deleteResponse(publisher);
    }

}
