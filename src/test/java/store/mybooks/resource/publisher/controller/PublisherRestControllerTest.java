package store.mybooks.resource.publisher.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
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
@WebMvcTest(PublisherRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class PublisherRestControllerTest {
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
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        publisher = new Publisher(1, name, LocalDate.of(2024, 1, 1));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("전체 출판사 조회(리스트)")
    void whenFindAllBy_thenReturnAllPublishersGetResponseList() throws Exception {
        Integer id2 = 2;
        String name2 = "publisher2";

        List<PublisherGetResponse> publisherList =
                Arrays.asList(new PublisherGetResponse(id, name), new PublisherGetResponse(id2, name2));


        when(publisherService.getAllPublishers()).thenReturn(publisherList);

        mockMvc.perform(get(url).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[1].id").value(id2))
                .andExpect(jsonPath("$.[1].name").value(name2))
                .andDo(document("publishers-list",
                        responseFields(
                                fieldWithPath("[].id").description("출판사 ID"),
                                fieldWithPath("[].name").description("출판사 이름")
                        )));

        verify(publisherService, times(1)).getAllPublishers();
    }


    @Test
    @DisplayName("전체 출판사 조회(페이징)")
    void givenPublisherList_whenGetPagedPublishers_thenReturnAllPagedPublishersGetResponseList() throws Exception {
        Integer id2 = 2;
        String name2 = "publisher2";
        Integer page = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(page, size);
        List<PublisherGetResponse> publisherList =
                Arrays.asList(new PublisherGetResponse(id, name), new PublisherGetResponse(id2, name2));

        Page<PublisherGetResponse> publisherGetResponsePage =
                new PageImpl<>(publisherList, pageable, publisherList.size());

        when(publisherService.getPagedPublisher(pageable)).thenReturn(publisherGetResponsePage);

        mockMvc.perform(get(url + "/page?page=" + page + "&size=" + size)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(id))
                .andExpect(jsonPath("$.content[0].name").value(name))
                .andExpect(jsonPath("$.content[1].id").value(id2))
                .andExpect(jsonPath("$.content[1].name").value(name2))
                .andDo(document("publishers-paged-list",
                        responseFields(
                                fieldWithPath("content[].id").description("출판사 ID"),
                                fieldWithPath("content[].name").description("출판사 이름"),
                                fieldWithPath("pageable").description("페이지정보"),
                                fieldWithPath("pageable.sort").description("페이지 정렬 정보"),
                                fieldWithPath("pageable.sort.sorted").description("페이지 정렬되었는지 여부(true: 정렬 됨)"),
                                fieldWithPath("pageable.sort.unsorted").description("페이지 정렬되지 않았는지 여부(true: 정렬 안 됨)"),
                                fieldWithPath("pageable.sort.empty").description("페이지 정렬 정보가 비어 있는지 여부(true: 비어있음)"),
                                fieldWithPath("pageable.pageSize").description("전체 페이지 수"),
                                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호(0부터 시작)"),
                                fieldWithPath("pageable.offset").description("현재 페이지의 시작 오프셋(0부터 시작)"),
                                fieldWithPath("pageable.paged").description("페이지네이션을 사용하는지 여부(true: 사용함)"),
                                fieldWithPath("pageable.unpaged").description("페이지네이션을 사용하는지 여부(true: 사용 안 함)"),
                                fieldWithPath("totalPages").description("전체 페이지 수"),
                                fieldWithPath("totalElements").description("전체 요소(항목) 수"),
                                fieldWithPath("last").description("마지막 페이지 여부(true: 마지막 페이지)"),
                                fieldWithPath("numberOfElements").description("혀재 페이지의 요소(항목) 수"),
                                fieldWithPath("size").description("페이지 당 요소(항목) 수"),
                                fieldWithPath("sort").description("결과 정렬 정보를 담은 객체"),
                                fieldWithPath("sort.sorted").description("결과가 정렬되었는지 여부(true: 정렬 됨)"),
                                fieldWithPath("sort.unsorted").description("결과가 정렬되지 않았는지 여부(true: 정렬 안 됨)"),
                                fieldWithPath("sort.empty").description("결과 정렬 정보가 비어 있는지 여부(true: 비어있음)"),
                                fieldWithPath("number").description("현재 페이지 번호(0부터 시작)"),
                                fieldWithPath("first").description("첫 페이지 여부(true: 첫 페이지)"),
                                fieldWithPath("empty").description("결과가 비어 있는지 여부(true: 비어있음)")
                        )));
        verify(publisherService, times(1)).getPagedPublisher(pageable);
    }

    @Test
    @DisplayName("출판사 등록(검증 성공)")
    void givenValidPublisherCreateRequest_whenCreatePublisher_thenSavePublisherAndReturnPublisherCreateResponse()
            throws Exception {
        PublisherCreateRequest request = new PublisherCreateRequest(name);
        PublisherCreateResponse response = new PublisherCreateResponse();
        response.setName(request.getName());

        when(publisherService.createPublisher(any(PublisherCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andDo(document("publisher-create",
                        requestFields(
                                fieldWithPath("name").description("출판사 이름")
                        ),
                        responseFields(
                                fieldWithPath("name").description("등록된 출판사 이름")
                        )));

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
                        .content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isBadRequest())
                .andDo(document("publisher-create-error"));

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

        mockMvc.perform(RestDocumentationRequestBuilders.put(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andDo(document("publisher-modify",
                        pathParameters(
                                parameterWithName("id").description("출판사 ID")
                        ),
                        requestFields(
                                fieldWithPath("changeName").description("변경할 출판사 이름")
                        ),
                        responseFields(
                                fieldWithPath("name").description("변경된 출판사 이름")
                        )));

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
                        .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest())
                .andDo(document("publisher-modify-error"));

        verify(publisherService, times(0)).modifyPublisher(eq(id), any(PublisherModifyRequest.class));
    }

    @Test
    @DisplayName("출판사 삭제")
    void givenPublisherId_whenDeletePublisher_thenDeletePublisherAndReturnPublisherDeleteResponse() throws Exception {
        doNothing().when(publisherService).deletePublisher(id);

        mockMvc.perform(RestDocumentationRequestBuilders.delete(url + "/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(document("publisher-delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 출판사 ID")
                        )));

        verify(publisherService, times(1)).deletePublisher(id);
    }
}