package store.mybooks.resource.user_grade.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.time.DurationMax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeDeleteResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.service.UserGradeService;

/**
 * packageName    : store.mybooks.resource.user_grade.controller
 * fileName       : UserGradeRestControllerTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */


@WebMvcTest(UserGradeRestController.class)
@ExtendWith(MockitoExtension.class)
class UserGradeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserGradeService userGradeService;

    UserGradeGetResponse userGradeGetResponse1;
    UserGradeGetResponse userGradeGetResponse2;


    @Test
    @DisplayName("UserGradeCreateRequest 로 createUserGrade 실행시 UserGradeCreateRequest 반환")
    void givenUserGradeCreateRequest_whenCallCCreateUserGrade_thenReturnUserGradeCreateRequest() throws Exception {

        UserGradeCreateRequest userGradeCreateRequest = new UserGradeCreateRequest("test", 1, 100, 3, LocalDate.now());
        UserGradeCreateResponse userGradeCreateResponse =
                new UserGradeCreateResponse("test", 1, 100, 3, LocalDate.now());

        when(userGradeService.createUserGrade(any(UserGradeCreateRequest.class))).thenReturn(userGradeCreateResponse);

        mockMvc.perform(post("/api/users-grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGradeCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.minCost").exists())
                .andExpect(jsonPath("$.maxCost").exists())
                .andExpect(jsonPath("$.rate").exists())
                .andExpect(jsonPath("$.createdDate").exists());
    }

    @Test
    @DisplayName("UserGradeId 로 deleteUserGradeById 실행시 UserGrade 삭제")
    void givenUserGradeId_whenCallDeleteUserGradeById_thenReturnUserGradeDeleteResponse() throws Exception {

        UserGradeDeleteResponse userGradeDeleteResponse = new UserGradeDeleteResponse("test");

        when(userGradeService.deleteUserGrade(anyInt())).thenReturn(userGradeDeleteResponse);

        mockMvc.perform(delete("/api/users-grades/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("UserGradeID 로 findUserGradeById 실행시 UserGrade 조회")
    void givenUserGradeId_whenCallFindUserGradeById_thenReturnUserGradeGetResponse() throws Exception {


        when(userGradeService.findUserGradeById(anyInt())).thenReturn(userGradeGetResponse1);

        mockMvc.perform(get("/api/users-grades/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.minCost").exists())
                .andExpect(jsonPath("$.maxCost").exists())
                .andExpect(jsonPath("$.rate").exists())
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.userGradeNameId").exists());
    }

    @Test
    @DisplayName("Pageable 로 findAllUserGrade 실행시 모든 UserGrade 를 Pagination 해서 조회")
    void givenPageable_whenCallFindAllUserGrade_thenReturnUserGradeGetResponsePage() throws Exception {

        List<UserGradeGetResponse> userGradeList = new ArrayList<>();
        userGradeList.add(userGradeGetResponse1);
        userGradeList.add(userGradeGetResponse2);
        Page<UserGradeGetResponse> userGradePage = new PageImpl<>(userGradeList);

        when(userGradeService.findAllUserGrade(any(Pageable.class))).thenReturn(userGradePage);

        mockMvc.perform(get("/api/users-grades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }


    @BeforeEach
    void setUp() {

        userGradeGetResponse1 = new UserGradeGetResponse() {
            @Override
            public String getUserGradeNameId() {
                return "test";
            }

            @Override
            public Integer getMinCost() {
                return 1;
            }

            @Override
            public Integer getMaxCost() {
                return 1;
            }

            @Override
            public Integer getRate() {
                return 1;
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }
        };

        userGradeGetResponse2 = new UserGradeGetResponse() {
            @Override
            public String getUserGradeNameId() {
                return "test";
            }

            @Override
            public Integer getMinCost() {
                return 1;
            }

            @Override
            public Integer getMaxCost() {
                return 1;
            }

            @Override
            public Integer getRate() {
                return 1;
            }

            @Override
            public LocalDate getCreatedDate() {
                return LocalDate.now();
            }
        };
    }

}