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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.entity.Tag;
import store.mybooks.resource.tag.exception.TagNameAlreadyExistsException;
import store.mybooks.resource.tag.exception.TagNotExistsException;
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

    @InjectMocks
    TagService tagService;

    @Test
    void givenGetTags_whenNormalCase_thenReturnTagGetResponseList() {
        List<TagGetResponse> list = new ArrayList<>();
        list.add(() -> "firstTagName");
        list.add(() -> "secondTagName");
        list.add(() -> "thirdTagName");

        when(tagRepository.findAllBy()).thenReturn(list);

        List<TagGetResponse> actualList = tagService.getTags();

        assertThat(actualList).isNotNull();
        assertThat(actualList).hasSize(3);
        assertThat(actualList.get(0).getName()).isEqualTo("firstTagName");
        assertThat(actualList.get(1).getName()).isEqualTo("secondTagName");
        assertThat(actualList.get(2).getName()).isEqualTo("thirdTagName");
    }

    @Test
    void givenCreateTag_whenNormalCase_thenReturnTagCreateResponse() {
        TagCreateRequest tagCreateRequest = new TagCreateRequest("IT");

        when(tagRepository.existsByName(anyString())).thenReturn(false);
        when(tagRepository.save(any())).thenReturn(new Tag(tagCreateRequest));

        TagCreateResponse tagCreateResponse = tagService.createTag(tagCreateRequest);

        assertThat(tagCreateResponse).isNotNull();
        assertThat(tagCreateResponse.getName()).isEqualTo(tagCreateRequest.getName());
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    void givenCreateTag_whenDuplicateTagName_thenThrowTagNameAlreadyExistsException() {
        TagCreateRequest tagCreateRequest = new TagCreateRequest("IT");

        when(tagRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(TagNameAlreadyExistsException.class, () -> tagService.createTag(tagCreateRequest));
    }

    @Test
    void givenModifyTag_whenNormalCase_thenReturnTagModifyResponse() {
        TagCreateRequest tagCreateRequest = new TagCreateRequest("IT");
        Tag tag = new Tag(tagCreateRequest);

        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));
        when(tagRepository.existsByName(anyString())).thenReturn(false);

        TagModifyRequest tagModifyRequest = new TagModifyRequest("Education");
        TagModifyResponse tagModifyResponse = tagService.modifyTag(1, tagModifyRequest);

        assertThat(tagModifyResponse).isNotNull();
        assertThat(tagModifyResponse.getName()).isEqualTo(tagModifyRequest.getName());
    }

    @Test
    void givenModifyTag_whenNotExistsTagId_thenThrowTagNotExistsException() {
        TagModifyRequest tagModifyRequest = new TagModifyRequest("Education");

        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TagNotExistsException.class, () -> tagService.modifyTag(1, tagModifyRequest));
    }

    @Test
    void givenModifyTag_whenDuplicateTagName_thenThrowTagNameAlreadyExistsException() {
        TagModifyRequest modifyRequest = new TagModifyRequest("Education");
        TagCreateRequest tagCreateRequest = new TagCreateRequest("IT");
        Tag tag = new Tag(tagCreateRequest);

        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));
        when(tagRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(TagNameAlreadyExistsException.class, () -> tagService.modifyTag(1, modifyRequest));
    }

    @Test
    void givenDeleteTag_whenNormalCase_thenReturnTagDeleteResponse() {
        Tag tag = new Tag(new TagCreateRequest("IT"));
        when(tagRepository.findById(anyInt())).thenReturn(Optional.of(tag));

        TagDeleteResponse tagDeleteResponse = tagService.deleteTag(1);

        assertThat(tagDeleteResponse).isNotNull();
        assertThat(tagDeleteResponse.getName()).isEqualTo(tag.getName());
        verify(tagRepository, times(1)).deleteById(1);
    }

    @Test
    void givenDeleteTag_NotExistsTagId_thenThrowTagNotExistsException() {
        when(tagRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(TagNotExistsException.class, () -> tagService.deleteTag(1));
        verify(tagRepository, times(0)).deleteById(1);
    }
}