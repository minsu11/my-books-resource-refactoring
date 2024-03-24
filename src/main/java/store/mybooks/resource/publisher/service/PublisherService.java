package store.mybooks.resource.publisher.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
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
    private final PublisherMapper publisherMapper;


    /**
     * methodName : getAllPublishers
     * author : newjaehun
     * description : 전체 출판사 리스트 반환.
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<PublisherGetResponse> getAllPublishers() {
        return publisherRepository.findAllBy();
    }


    /**
     * methodName : getAllPublisher
     * author : newjaehun
     * description : 페이징된 전체 출판사 리스트 반환.
     *
     * @param pageable pageable
     * @return Page
     */
    @Transactional(readOnly = true)
    public Page<PublisherGetResponse> getPagedPublisher(Pageable pageable) {
        return publisherRepository.getPagedBy(pageable);
    }

    /**
     * methodName : createPublisher
     * author : newjaehun
     * description : 출판사 추가하는 메서드.
     *
     * @param createRequest 추가할 name, name이 이미 존재하는 경우 PublisherAlreadyExistException 발생
     * @return publisherCreateResponse: 추가된 name
     */
    @Transactional
    public PublisherCreateResponse createPublisher(PublisherCreateRequest createRequest) {
        Publisher publisher = new Publisher(createRequest.getName());

        if (publisherRepository.existsByName(createRequest.getName())) {
            throw new PublisherAlreadyExistException(createRequest.getName());
        }
        return publisherMapper.createResponse(publisherRepository.save(publisher));
    }

    /**
     * methodName : modifyPublisher
     * author : newjaehun
     * description : 출판사 수정하는 메서드.
     *
     * @param publisherId   수정하려는 publisher 의 id, 존재하지 않으면 PublisherNotExistException 발생
     * @param modifyRequest 수정할 name 포함
     * @return publisherModifyResponse: 수정된 name 포함
     */
    @Transactional
    public PublisherModifyResponse modifyPublisher(Integer publisherId, PublisherModifyRequest modifyRequest) {
        Publisher publisher =
                publisherRepository.findById(publisherId)
                        .orElseThrow(() -> new PublisherNotExistException(publisherId));

        if (publisherRepository.existsByName(modifyRequest.getChangeName())) {
            throw new PublisherAlreadyExistException(publisher.getName());
        }
        publisher.setByModifyRequest(modifyRequest);
        return publisherMapper.modifyResponse(publisher);
    }

    /**
     * methodName : deletePublisher
     * author : newjaehun
     * description : 출판사 삭제하는 메서드.
     *
     * @param publisherId 삭제하려는 publisher 의 id, 존재하지 않으면 PublisherNotExistException 발생
     */
    @Transactional
    public void deletePublisher(Integer publisherId) {
        if (!publisherRepository.existsById(publisherId)) {
            throw new PublisherNotExistException(publisherId);
        }
        publisherRepository.deleteById(publisherId);
    }

}
