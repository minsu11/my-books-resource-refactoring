package store.mybooks.resource.tag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * fileName       : TagServiceTest
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    TagRepository tagRepository;

    @Mock
    TagMapper tagMapper;

    @InjectMocks
    TagService tagService;

    @Test
    @DisplayName("getTag 테스트")
    void givenTagId_whenGetTag_thenReturnTagGetResponse() {
        TagGetResponse expected = makeTagGetResponse(1, "tagName");
        when(tagRepository.existsById(1)).thenReturn(true);
        when(tagRepository.queryById(1)).thenReturn(expected);

        TagGetResponse actual = tagService.getTag(1);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    @DisplayName("getName 존재하지 않는 태그 아이디 테스트")
    void givenNotExistsTagId_whenGetTag_thenReturnTagGetResponse() {
        when(tagRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(TagNotExistsException.class, () -> tagService.getTag(1));
    }

    @Test
    @DisplayName("getTags() 테스트")
    void whenGetTags_thenReturnListOfTagGetResponse() {
        List<TagGetResponse> expectedList = new ArrayList<>();
        expectedList.add(makeTagGetResponse(1, "firstTagName"));
        expectedList.add(makeTagGetResponse(2, "secondTagName"));
        expectedList.add(makeTagGetResponse(3, "thirdTagName"));
        when(tagRepository.findAllBy()).thenReturn(expectedList);

        List<TagGetResponse> actualList = tagService.getTags();
        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getId()).isEqualTo(expectedList.get(0).getId());
        assertThat(actualList.get(0).getName()).isEqualTo(expectedList.get(0).getName());
        assertThat(actualList.get(1).getId()).isEqualTo(expectedList.get(1).getId());
        assertThat(actualList.get(1).getName()).isEqualTo(expectedList.get(1).getName());
        assertThat(actualList.get(2).getId()).isEqualTo(expectedList.get(2).getId());
        assertThat(actualList.get(2).getName()).isEqualTo(expectedList.get(2).getName());
    }

    @Test
    @DisplayName("getTags(Pageable) 리턴 값 테스트")
    void givenGetTags_whenNormalCase_thenReturnTagGetResponseList() {
        List<TagGetResponse> list = new ArrayList<>();
        list.add(makeTagGetResponse(1, "firstTagName"));
        list.add(makeTagGetResponse(2, "secondTagName"));
        list.add(makeTagGetResponse(3, "thirdTagName"));

        Pageable pageable = PageRequest.of(0, 10);

        when(tagRepository.findAllByOrderById(any())).thenReturn(new PageImpl<>(list, pageable, list.size()));

        List<TagGetResponse> actualList = tagService.getTags(pageable).getContent();

        assertThat(actualList).isNotNull().hasSize(3);
        assertThat(actualList.get(0).getName()).isEqualTo("firstTagName");
        assertThat(actualList.get(1).getName()).isEqualTo("secondTagName");
        assertThat(actualList.get(2).getName()).isEqualTo("thirdTagName");
    }

    @Test
    @DisplayName("createTag 메서드 정상적인 경우 테스트")
    void givenCreateTag_whenNormalCase_thenReturnTagCreateResponse() {
        String name = "IT";
        TagCreateRequest tagCreateRequest = new TagCreateRequest(name);
        TagCreateResponse tagCreateResponse = new TagCreateResponse();
        tagCreateResponse.setName(tagCreateRequest.getName());

        when(tagRepository.existsByName(anyString())).thenReturn(false);
        when(tagRepository.save(any())).thenReturn(new Tag(name));
        when(tagMapper.createResponse(any())).thenReturn(tagCreateResponse);

        TagCreateResponse actualResponse = tagService.createTag(tagCreateRequest);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getName()).isEqualTo(tagCreateRequest.getName());
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("createTag 메서드 중복되는 name 으로 생성하는 경우 테스트")
    void givenCreateTag_whenDuplicateTagName_thenThrowTagNameAlreadyExistsException() {
        TagCreateRequest tagCreateRequest = new TagCreateRequest("IT");

        when(tagRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(TagNameAlreadyExistsException.class, () -> tagService.createTag(tagCreateRequest));
    }

    @Test
    @DisplayName("modifyTag 메서드 정상적인 경우 테스트")
    void givenModifyTag_whenNormalCase_thenReturnTagModifyResponse() {
        Tag tag = new Tag("IT");

        TagModifyRequest tagModifyRequest = new TagModifyRequest("Education");
        TagModifyResponse tagModifyResponse = new TagModifyResponse();
        tagModifyResponse.setName(tagModifyRequest.getName());

        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));
        when(tagRepository.existsByName(anyString())).thenReturn(false);
        when(tagMapper.modifyResponse(any())).thenReturn(tagModifyResponse);

        TagModifyResponse actualResponse = tagService.modifyTag(1, tagModifyRequest);


        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getName()).isEqualTo(tagModifyRequest.getName());
    }

    @Test
    @DisplayName("modifyTag 메서드 태그 아이디가 존재하지 않는 경우 테스트")
    void givenModifyTag_whenNotExistsTagId_thenThrowTagNotExistsException() {
        TagModifyRequest tagModifyRequest = new TagModifyRequest("Education");

        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TagNotExistsException.class, () -> tagService.modifyTag(1, tagModifyRequest));
    }

    @Test
    @DisplayName("modifyTag 메서드 수정하려는 태그 이름이 이미 존재하는 경우 테스트")
    void givenModifyTag_whenDuplicateTagName_thenThrowTagNameAlreadyExistsException() {
        TagModifyRequest modifyRequest = new TagModifyRequest("Education");
        Tag tag = new Tag("IT");

        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));
        when(tagRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(TagNameAlreadyExistsException.class, () -> tagService.modifyTag(1, modifyRequest));
    }

    @Test
    @DisplayName("deleteTag 메서드 정상적인 경우 테스트")
    void givenDeleteTag_whenNormalCase_thenReturnTagDeleteResponse() {
        Tag tag = new Tag("IT");
        TagDeleteResponse tagDeleteResponse = new TagDeleteResponse();
        tagDeleteResponse.setName(tag.getName());

        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));
        when(tagMapper.deleteResponse(any())).thenReturn(tagDeleteResponse);

        TagDeleteResponse actualResponse = tagService.deleteTag(1);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getName()).isEqualTo(tag.getName());
        verify(tagRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteTag 메서드 존재하지 않는 태그 아이디로 삭제 요청하는 경우 테스트")
    void givenDeleteTag_NotExistsTagId_thenThrowTagNotExistsException() {
        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TagNotExistsException.class, () -> tagService.deleteTag(1));
        verify(tagRepository, times(0)).deleteById(1);
    }

    private TagGetResponse makeTagGetResponse(Integer id, String name) {
        return new TagGetResponse() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}