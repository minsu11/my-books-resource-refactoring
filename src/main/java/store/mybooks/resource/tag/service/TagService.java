package store.mybooks.resource.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.entity.Tag;
import store.mybooks.resource.tag.exception.TagNameAlreadyExistsException;
import store.mybooks.resource.tag.exception.TagNotExistsException;
import store.mybooks.resource.tag.mapper.TagMapper;
import store.mybooks.resource.tag.repository.TagRepository;

/**
 * packageName    : store.mybooks.resource.tag.service
 * fileName       : TagService
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * methodName : getTag <br>
     * author : damho-lee <br>
     * description : id 로 TagGetResponse 리턴. id 가 존재하지 않으면 TagNotExistsException.<br>
     *
     * @param id Integer
     * @return TagGetResponse
     */
    @Transactional(readOnly = true)
    public TagGetResponse getTag(Integer id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotExistsException(id);
        }

        return tagRepository.queryById(id);
    }

    @Transactional(readOnly = true)
    public Page<TagGetResponse> getTags(Pageable pageable) {
        return tagRepository.findAllByOrderById(pageable);
    }

    /**
     * methodName : createTag
     * author : damho-lee
     * description : tag 생성하는 메서드.
     *
     * @param tagCreateRequest name 을 포함. TagName 이 이미 존재하는 경우 TagNameAlreadyExistsException.
     * @return TagCreateResponse
     */
    public TagCreateResponse createTag(TagCreateRequest tagCreateRequest) {
        if (tagRepository.existsByName(tagCreateRequest.getName())) {
            throw new TagNameAlreadyExistsException(tagCreateRequest.getName());
        }

        return tagMapper.createResponse(tagRepository.save(new Tag(tagCreateRequest.getName())));
    }

    /**
     * methodName : modifyTag
     * author : damho-lee
     * description : 태그 수정하는 메서드.
     *
     * @param id               수정하고자 하는 tag 의 Id.
     *                         존재하지 않는 Id 인 경우 TagNotExistsException.
     * @param tagModifyRequest name 을 포함.
     *                         이미 존재하는 name 인 경우 TagNameAlreadyExistsException.
     * @return TagModifyResponse
     */
    public TagModifyResponse modifyTag(int id, TagModifyRequest tagModifyRequest) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotExistsException(id));

        if (tagRepository.existsByName(tagModifyRequest.getName())) {
            throw new TagNameAlreadyExistsException(tagModifyRequest.getName());
        }

        tag.setByTagModifyRequest(tagModifyRequest);

        return tagMapper.modifyResponse(tag);
    }

    /**
     * methodName : deleteTag
     * author : damho-lee
     * description : 태그 삭제 메서드.
     *
     * @param id 삭제하려는 Tag 의 id.
     * @return TagDeleteResponse
     */
    public TagDeleteResponse deleteTag(int id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotExistsException(id));

        tagRepository.deleteById(id);

        return tagMapper.deleteResponse(tag);
    }
}
