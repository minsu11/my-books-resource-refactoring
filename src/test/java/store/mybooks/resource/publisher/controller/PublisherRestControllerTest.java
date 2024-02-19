package store.mybooks.resource.publisher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.mapper.PublisherMapper;
import store.mybooks.resource.publisher.service.PublisherService;

/**
 * packageName    : store.mybooks.resource.publisher.controller
 * fileName       : PublisherRestControllerTest
 * author         : newjaehun
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        newjaehun       최초 생성
 */
@WebMvcTest(PublisherRestController.class)
@ExtendWith(MockitoExtension.class)
class PublisherRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublisherService publisherService;

    private final String url = "/api/publishers";

    @Test
    @DisplayName("전체 출판사 조회")
    void givenPublisherList_whenFindAllPublishers_thenReturnAllPublishersGetResponseList() throws Exception {
        Integer id1 = 1;
        Integer id2 =2;
        String name1 = "publisher1";
        String name2 = "publisher2";
        Integer page = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(page, size);
        List<PublisherGetResponse> publisherList = Arrays.asList(
                new PublisherGetResponse() {
                    @Override
                    public Integer getId() {
                        return id1;
                    }

                    @Override
                    public String getName() {
                        return name1;
                    }
                },
                new PublisherGetResponse() {
                    @Override
                    public Integer getId() {
                        return id2;
                    }

                    @Override
                    public String getName() {
                        return name2;
                    }
                });
        Page<PublisherGetResponse> publisherGetResponsePage = new PageImpl<>(publisherList, pageable,publisherList.size());
        when(publisherService.getAllPublisher(pageable)).thenReturn(publisherGetResponsePage);
        mockMvc.perform(get(url+"?page="+page+"&size="+ size)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(id1))
                .andExpect(jsonPath("$.content[0].name").value(name1))
                .andExpect(jsonPath("$.content[1].id").value(id2))
                .andExpect(jsonPath("$.content[1].name").value(name2));
    }

    @Test
    @DisplayName("출판사 등록")
    void givenPublisherCreateRequest_whenCreatePublisher_thenSavePublisherAndReturnPublisherCreateResponse() throws Exception{
        String name = "publisherName";
        PublisherCreateRequest request = new PublisherCreateRequest(name);
        Publisher publisher = new Publisher(name);
        PublisherCreateResponse response=new PublisherCreateResponse() {
            @Override
            public String getName() {
                return publisher.getName();
            }
        };
        when(publisherService.createPublisher(any(PublisherCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()));

    }

    @Test
    @DisplayName("출판사 수정")
    void givenPublisherIdAndPublisherModifyRequest_whenModifyPublisher_thenModifyPublisherAndReturnPublisherModifyResponse() throws Exception {
        Integer publisherId = 1;
        String nameToChange = "nameToChange";
        Publisher publisher = new Publisher(nameToChange);
        PublisherModifyRequest request = new PublisherModifyRequest(nameToChange);
        PublisherModifyResponse response = PublisherMapper.INSTANCE.modifyResponse(publisher);

        when(publisherService.modifyPublisher(eq(publisherId), any(PublisherModifyRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put(url + "/{id}", publisherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value(response.getName()))
                .andDo(print())

        ;
    }

    @Test
    @DisplayName("출판사 삭제")
    void givenPublisherId_whenDeletePublisher_thenDeletePublisherAndReturnPublisherDeleteResponse() throws Exception {
        Integer publisherId = 1;
        Publisher publisher = new Publisher("publisherName1");
        PublisherDeleteResponse response= PublisherMapper.INSTANCE.deleteResponse(publisher);

        when(publisherService.deletePublisher(eq(publisherId))).thenReturn(response);

        mockMvc.perform(delete(url+"/{id}", publisherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("publisherName1"));
    }
}