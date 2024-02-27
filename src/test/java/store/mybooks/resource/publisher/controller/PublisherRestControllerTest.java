package store.mybooks.resource.publisher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
@WebMvcTest(value = PublisherRestController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PublisherRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublisherService publisherService;

    private final String url = "/api/publishers";

    private Publisher publisher;
    private static final Integer id = 1;
    private static final String name = "publisherName1";

    @BeforeEach
    void setUp() {
        publisher = new Publisher(1, name, LocalDate.now());
    }

    @Test
    @DisplayName("전체 출판사 조회")
    void givenPublisherList_whenFindAllPublishers_thenReturnAllPublishersGetResponseList() throws Exception {
        Integer id2 = 2;
        String name2 = "publisher2";
        Integer page = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(page, size);
        List<PublisherGetResponse> publisherList = Arrays.asList(new PublisherGetResponse() {
            @Override
            public Integer getId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }
        }, new PublisherGetResponse() {
            @Override
            public Integer getId() {
                return id2;
            }

            @Override
            public String getName() {
                return name2;
            }
        });

        Page<PublisherGetResponse> publisherGetResponsePage =
                new PageImpl<>(publisherList, pageable, publisherList.size());

        when(publisherService.getAllPublisher(pageable)).thenReturn(publisherGetResponsePage);

        mockMvc.perform(get(url + "?page=" + page + "&size=" + size).accept("application/json"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].name").value(name)).andExpect(jsonPath("$.content[1].id").value(id2))
                .andExpect(jsonPath("$.content[1].name").value(name2));
        verify(publisherService, times(1)).getAllPublisher(pageable);
    }

    @Test
    @DisplayName("출판사 등록(검증 성공)")
    void givenValidPublisherCreateRequest_whenCreatePublisher_thenSavePublisherAndReturnPublisherCreateResponse()
            throws Exception {
        PublisherCreateRequest request = new PublisherCreateRequest(name);
        PublisherCreateResponse response = new PublisherCreateResponse();
        response.setName(request.getName());

        when(publisherService.createPublisher(any(PublisherCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()));
        verify(publisherService, times(1)).createPublisher(any(PublisherCreateRequest.class));
    }

    @Test
    @DisplayName("출판사 등록(검증 실패)")
    void givenInvalidPublisherCreateRequest_whenCreatePublisher_thenThrowBindException() throws Exception {
        PublisherCreateRequest request = new PublisherCreateRequest("");
        PublisherCreateResponse response = new PublisherCreateResponse();
        response.setName(request.getName());

        when(publisherService.createPublisher(any(PublisherCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isBadRequest());


        verify(publisherService, times(0)).createPublisher(any(PublisherCreateRequest.class));
    }

    @Test
    @DisplayName("출판사 수정(검증 통과)")
    void givenPublisherIdAndValidPublisherModifyRequest_whenModifyPublisher_thenModifyPublisherAndReturnPublisherModifyResponse()
            throws Exception {
        String nameToChange = "nameToChange";
        PublisherModifyRequest request = new PublisherModifyRequest(nameToChange);
        PublisherModifyResponse response = new PublisherModifyResponse();
        response.setName(request.getChangeName());

        when(publisherService.modifyPublisher(eq(id), any(PublisherModifyRequest.class))).thenReturn(response);

        mockMvc.perform(put(url + "/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()));

        verify(publisherService, times(1)).modifyPublisher(eq(id), any(PublisherModifyRequest.class));
    }

    @Test
    @DisplayName("출판사 수정(검증 실패)")
    void givenPublisherIdAndInvalidPublisherModifyRequest_whenModifyPublisher_thenThrowBindException()
            throws Exception {
        String nameToChange = "";
        PublisherModifyRequest request = new PublisherModifyRequest(nameToChange);
        PublisherModifyResponse response = new PublisherModifyResponse();
        response.setName(request.getChangeName());

        when(publisherService.modifyPublisher(eq(id), any(PublisherModifyRequest.class))).thenReturn(response);

        mockMvc.perform(put(url + "/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());

        verify(publisherService, times(0)).modifyPublisher(eq(id), any(PublisherModifyRequest.class));
    }

    @Test
    @DisplayName("출판사 삭제")
    void givenPublisherId_whenDeletePublisher_thenDeletePublisherAndReturnPublisherDeleteResponse() throws Exception {
        PublisherDeleteResponse response = new PublisherDeleteResponse();
        response.setName(name);
        when(publisherService.deletePublisher(id)).thenReturn(response);

        mockMvc.perform(delete(url + "/{id}", id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()));

        verify(publisherService, times(1)).deletePublisher(id);

    }
}