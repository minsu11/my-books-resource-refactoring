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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
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
    void getAllPublishers() throws Exception {
        Integer id1 = 1;
        Integer id2 =2;
        String name1 = "publisher1";
        String name2 = "publisher2";
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

        when(publisherService.getAllPublisher()).thenReturn(publisherList);
        mockMvc.perform(get(url)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].name").value(name2));
    }

    @Test
    void createPublisher() throws Exception{
        String name = "publisherName";
        PublisherCreateRequest request = new PublisherCreateRequest(name);
        PublisherCreateResponse response = PublisherCreateResponse.builder()
                .name(name)
                .build();
        when(publisherService.createPublisher(any(PublisherCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.getName()));

    }

    @Test
    void modifyPublisher() throws Exception {
        Integer publisherId = 1;
        String nameToChange = "nameToChange";
        PublisherModifyRequest request = new PublisherModifyRequest(nameToChange);
        PublisherModifyResponse response = PublisherModifyResponse.builder()
                .name(nameToChange)
                .build();

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
    void deletePublisher() throws Exception {
        Integer publisherId = 1;
        PublisherDeleteResponse response = new PublisherDeleteResponse("publisherName1");

        when(publisherService.deletePublisher(eq(publisherId))).thenReturn(response);

        mockMvc.perform(delete(url+"/{id}", publisherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("publisherName1"));
    }
}