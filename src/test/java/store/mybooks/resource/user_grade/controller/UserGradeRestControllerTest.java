package store.mybooks.resource.user_grade.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
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
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserGradeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserGradeService userGradeService;

    UserGradeGetResponse userGradeGetResponse1;
    UserGradeGetResponse userGradeGetResponse2;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris(), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("유저 UserGradeCreateRequest - Validation 실패")
    void givenUserGradeCreateRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserGradeCreateRequest request = new UserGradeCreateRequest(100, "");

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

        UserGradeCreateRequest userGradeCreateRequest = new UserGradeCreateRequest(100, "test");
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
                .andExpect(jsonPath("$.createdDate").exists())
                .andDo(document("user_grade-create",
                        requestFields(
                                fieldWithPath("rate").description("포인트 적립률"),
                                fieldWithPath("userGradeNameId").description("유저등급 이름 아이디")
                        ),
                        responseFields(
                                fieldWithPath("name").description("별명"),
                                fieldWithPath("minCost").description("순수 최소금액"),
                                fieldWithPath("maxCost").description("순수 최대금액"),
                                fieldWithPath("rate").description("포인트 적립률"),
                                fieldWithPath("createdDate").description("생성일")
                        )
                ));
    }


    @Test
    @DisplayName("findAllUserGrade 실행시 모든 UserGrade 를 List 로 조회")
    void givenNothing_whenCallFindAllUserGrade_thenReturnUserGradeGetResponseList() throws Exception {

        List<UserGradeGetResponse> userGradeList = new ArrayList<>();
        userGradeList.add(userGradeGetResponse1);
        userGradeList.add(userGradeGetResponse2);

        when(userGradeService.findAllUserGrade()).thenReturn(userGradeList);

        mockMvc.perform(get("/api/users-grades/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[*].id").exists())
                .andExpectAll(jsonPath("$.[*].minCost").exists())
                .andExpectAll(jsonPath("$.[*].maxCost").exists())
                .andExpectAll(jsonPath("$.[*].rate").exists())
                .andExpectAll(jsonPath("$.[*].createdDate").exists())
                .andExpectAll(jsonPath("$.[*].userGradeNameId").exists())
                .andDo(document("user_grade-findAllList",
                        responseFields(
                                fieldWithPath("[].id").description("유저등급 아이디"),
                                fieldWithPath("[].userGradeNameId").description("유저등급 이름아이디"),
                                fieldWithPath("[].minCost").description("순수 최소금액"),
                                fieldWithPath("[].maxCost").description("순수 최대금액"),
                                fieldWithPath("[].rate").description("유저등급 비율"),
                                fieldWithPath("[].createdDate").description("생성일")
                        ))
                );
    }


    @Test
    @DisplayName("findAllAvailableUserGrade 실행시 모든 활성 UserGrade 를 List 로 조회")
    void givenNothing_whenCallFindAllAvailableUserGrade_thenReturnUserGradeGetResponseList() throws Exception {

        List<UserGradeGetResponse> userGradeList = new ArrayList<>();
        userGradeList.add(userGradeGetResponse1);
        userGradeList.add(userGradeGetResponse2);

        when(userGradeService.findAllAvailableUserGrade()).thenReturn(userGradeList);

        mockMvc.perform(get("/api/users-grades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[*].id").exists())
                .andExpectAll(jsonPath("$.[*].minCost").exists())
                .andExpectAll(jsonPath("$.[*].maxCost").exists())
                .andExpectAll(jsonPath("$.[*].rate").exists())
                .andExpectAll(jsonPath("$.[*].createdDate").exists())
                .andExpectAll(jsonPath("$.[*].userGradeNameId").exists())
                .andDo(document("user_grade-findAvailableList",
                        responseFields(
                                fieldWithPath("[].id").description("유저등급 아이디"),
                                fieldWithPath("[].userGradeNameId").description("유저등급 이름아이디"),
                                fieldWithPath("[].minCost").description("순수 최소금액"),
                                fieldWithPath("[].maxCost").description("순수 최대금액"),
                                fieldWithPath("[].rate").description("유저등급 비율"),
                                fieldWithPath("[].createdDate").description("생성일")
                        ))
                );
    }


    @BeforeEach
    void setUp() {

        userGradeGetResponse1 = new UserGradeGetResponse() {
            @Override
            public String getId() {
                return "1";
            }

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
            public String getId() {
                return "2";
            }

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