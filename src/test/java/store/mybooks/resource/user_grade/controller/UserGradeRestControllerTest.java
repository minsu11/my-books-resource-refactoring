package store.mybooks.resource.user_grade.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.error.exception.ValidationFailException;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
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


@WebMvcTest(value = UserGradeRestController.class)
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
    @DisplayName("유저 UserGradeCreateRequest - Validation 실패")
    void givenUserGradeCreateRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserGradeCreateRequest request = new UserGradeCreateRequest("alias",-1,100,100,null);

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/users-grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

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
    @DisplayName("findAllUserGrade 실행시 모든 UserGrade 를 List 로 조회")
    void givenNothing_whenCallFindAllUserGrade_thenReturnUserGradeGetResponseList() throws Exception {

        List<UserGradeGetResponse> userGradeList = new ArrayList<>();
        userGradeList.add(userGradeGetResponse1);
        userGradeList.add(userGradeGetResponse2);

        when(userGradeService.findAllUserGrade()).thenReturn(userGradeList);

        mockMvc.perform(get("/api/users-grades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[*].minCost").exists())
                .andExpectAll(jsonPath("$.[*].maxCost").exists())
                .andExpectAll(jsonPath("$.[*].rate").exists())
                .andExpectAll(jsonPath("$.[*].createdDate").exists())
                .andExpectAll(jsonPath("$.[*].userGradeNameId").exists());
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