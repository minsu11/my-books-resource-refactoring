package store.mybooks.resource.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserEmailRequest;
import store.mybooks.resource.user.dto.request.UserGradeModifyRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.request.UserOauthCreateRequest;
import store.mybooks.resource.user.dto.request.UserOauthLoginRequest;
import store.mybooks.resource.user.dto.request.UserOauthRequest;
import store.mybooks.resource.user.dto.request.UserPasswordModifyRequest;
import store.mybooks.resource.user.dto.request.UserStatusModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserEmailCheckResponse;
import store.mybooks.resource.user.dto.response.UserEncryptedPasswordResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserInactiveVerificationResponse;
import store.mybooks.resource.user.dto.response.UserLoginResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
import store.mybooks.resource.user.dto.response.UserOauthCreateResponse;
import store.mybooks.resource.user.dto.response.UserPasswordModifyResponse;
import store.mybooks.resource.user.dto.response.UserStatusModifyResponse;
import store.mybooks.resource.user.service.UserService;

/**
 * packageName    : store.mybooks.resource.user.controller
 * fileName       : UserRestControllerTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */


@WebMvcTest(value = UserRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    PointHistoryService pointHistoryService;

    UserGetResponse userGetResponse1;
    UserGetResponse userGetResponse2;


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
    @DisplayName("유저 UserCreateRequest - Validation 실패")
    void givenUserCreateRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserCreateRequest request = new UserCreateRequest("test", null, "01000000", "asdas", null);

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 Oauth UserOauthCreateRequest - Validation 실패")
    void givenUserOauthLoginRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserOauthCreateRequest request = new UserOauthCreateRequest("test", "test", "dddd", null, "");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/users/oauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 Oauth UserOauthCreateRequest - Validation 실패")
    void givenUserOauthCreateRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {


        UserOauthCreateRequest request = new UserOauthCreateRequest("test", "test", "dddd@test.com", null, "oauth");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/users/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }


    @Test
    @DisplayName("유저 UserModifyRequest - Validation 실패")
    void givenUserModifyRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {


        UserModifyRequest request = new UserModifyRequest(null, "01012345678");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header(HeaderProperties.USER_ID, 1))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 UserGradeModifyRequest - Validation 실패")
    void givenUserGradeModifyRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {


        UserGradeModifyRequest request = new UserGradeModifyRequest("");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/1/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 UserStatusModifyRequest - Validation 실패")
    void givenUserStatusModifyRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {


        UserStatusModifyRequest request = new UserStatusModifyRequest("");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 UserPasswordModifyRequest - Validation 실패")
    void givenUserPasswordModifyRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserPasswordModifyRequest request = new UserPasswordModifyRequest("");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header(HeaderProperties.USER_ID, 1))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 UserEmailRequest  - Validation 실패")
    void givenUserEmailRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserEmailRequest request = new UserEmailRequest("test");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/users/verification/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("UserCreateRequest 로 createUser 실행시 UserCreateResponse 반환")
    void givenUserCreateRequest_whenCallCreateUser_thenReturnUserCreateResponse() throws Exception {

        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "test", "test", "test@naver.com", LocalDate.now());

        UserCreateResponse userCreateResponse = new UserCreateResponse("test", "test", 1000, "01-01", "test", "test");

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(userCreateResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.birthYear").exists())
                .andExpect(jsonPath("$.birthMonthDay").exists())
                .andExpect(jsonPath("$.userStatusName").exists())
                .andExpect(jsonPath("$.userGradeName").exists())
                .andDo(document("user-create",
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("phoneNumber").description("핸드폰"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("birth").description("생일")
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("birthYear").description("생년"),
                                fieldWithPath("birthMonthDay").description("월일"),
                                fieldWithPath("userStatusName").description("유저상태 이름"),
                                fieldWithPath("userGradeName").description("유저등급 이름")
                        )
                ));
    }

    @Test
    @DisplayName("UserModifyRequest 로 modifyUser 실행시 UserModifyResponse 반환")
    void givenUserModifyRequest_whenCallModifyUser_thenReturnUserModifyResponse() throws Exception {

        UserModifyRequest userModifyRequest =
                new UserModifyRequest("test", "01012345678");

        UserModifyResponse userModifyResponse =
                new UserModifyResponse("test", "01012345678");

        when(userService.modifyUser(anyLong(), any(UserModifyRequest.class))).thenReturn(userModifyResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest))
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.phoneNumber").exists())
                .andDo(document("user-modify",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("phoneNumber").description("핸드폰")
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("phoneNumber").description("핸드폰")
                        )
                ));
    }

    @Test
    @DisplayName("UserGradeModifyRequest 로 modifyUserGrade 실행시 UserGradeModifyResponse 반환")
    void givenUserGradeModifyRequest_whenCallModifyUserGrade_thenReturnUserModifyResponse() throws Exception {

        UserGradeModifyRequest userModifyRequest = new UserGradeModifyRequest("test");

        UserGradeModifyResponse userModifyResponse = new UserGradeModifyResponse("test");

        when(userService.modifyUserGrade(anyLong(), any(UserGradeModifyRequest.class))).thenReturn(userModifyResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/users/{id}/grade", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userGradeName").exists())
                .andDo(document("user_status-modify",
                        pathParameters(
                                parameterWithName("id").description("유저 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userGradeName").description("유저 등급이름")
                        ),
                        responseFields(
                                fieldWithPath("userGradeName").description("유저 등급이름")
                        )
                ));

    }

    @Test
    @DisplayName("UserStatusModifyRequest 로 modifyUserStatus 실행시 UserStatusModifyResponse 반환")
    void givenUserStatusModifyRequest_whenCallModifyUserStatus_thenReturnUserModifyResponse() throws Exception {

        UserStatusModifyRequest userModifyRequest = new UserStatusModifyRequest("test");
        UserStatusModifyResponse userModifyResponse = new UserStatusModifyResponse("test", LocalDate.now());

        when(userService.modifyUserStatus(anyLong(), any(UserStatusModifyRequest.class))).thenReturn(
                userModifyResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/users/{id}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatusName").exists())
                .andExpect(jsonPath("$.gradeChangedDate").exists())
                .andDo(document("user_status-modify",
                        pathParameters(
                                parameterWithName("id").description("유저 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userStatusName").description("유저 상태이름")
                        ),
                        responseFields(
                                fieldWithPath("userStatusName").description("유저 상태이름"),
                                fieldWithPath("gradeChangedDate").description("등급변경일")
                        )
                ));
    }


    @Test
    @DisplayName("UserId 로 deleteUser 실행시 UserDeleteResponse 반환")
    void givenUserId_whenCallDeleteUser_thenReturnUserDeleteResponse() throws Exception {

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse("[test@test.com] 유저 삭제완료");

        when(userService.deleteUser(anyLong())).thenReturn(userDeleteResponse);


        mockMvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1")) // 헤더 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(document("user-delete",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        responseFields(
                                fieldWithPath("message").description("유저삭제 메시지")
                        )
                ));

    }

    @Test
    @DisplayName("UserId 로 findUserById 실행시 UserGetResponse 반환")
    void givenUserId_whenCallFindUserById_thenReturnUserGetResponse() throws Exception {


        when(userService.findById(anyLong())).thenReturn(userGetResponse1);

        mockMvc.perform(get("/api/users")
                        .header(HeaderProperties.USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.userStatusId").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.phoneNumber").exists())
                .andExpect(jsonPath("$.latestLogin").exists())
                .andExpect(jsonPath("$.gradeChangedDate").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.birthYear").exists())
                .andExpect(jsonPath("$.birthMonthDay").exists())
                .andExpect(jsonPath("$.userGradeUserGradeNameId").exists())
                .andDo(document("user-find",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userGradeUserGradeNameId").description("유저등급 이름아이디"),
                                fieldWithPath("userStatusId").description("유저상태 아이디"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("phoneNumber").description("핸드폰"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("birthYear").description("생년"),
                                fieldWithPath("birthMonthDay").description("월일"),
                                fieldWithPath("createdAt").description("생성일"),
                                fieldWithPath("latestLogin").description("마지막 로그인"),
                                fieldWithPath("gradeChangedDate").description("최근 등급변경 일")
                        )
                ));

    }

    @Test
    @DisplayName("UserId 로 findAllUser 실행시 Page<UserGetResponse> 반환")
    void givenUserId_whenCallFindAllUser_thenReturnUserGetResponsePage() throws Exception {

        Pageable pageable = PageRequest.of(0, 2);
        List<UserGetResponse> userGetResponseList = List.of(userGetResponse1, userGetResponse2);


        Page<UserGetResponse> userGetResponsePage = new PageImpl<>(userGetResponseList, pageable, 120);

        when(userService.findAllUser(any(Pageable.class))).thenReturn(userGetResponsePage);

        mockMvc.perform(get("/api/users/page?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andDo(document("user-findAll",
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호(0부터 시작, default = 0)"),
                                parameterWithName("size").description("페이지 사이즈(default = 10)")
                        ),
                        responseFields(
                                fieldWithPath("content").description("리스트"),
                                fieldWithPath("content[].userGradeUserGradeNameId").description("유저등급 이름아이디"),
                                fieldWithPath("content[].userStatusId").description("유저상태 아이디"),
                                fieldWithPath("content[].name").description("이름"),
                                fieldWithPath("content[].phoneNumber").description("핸드폰"),
                                fieldWithPath("content[].email").description("이메일"),
                                fieldWithPath("content[].birthYear").description("생년"),
                                fieldWithPath("content[].birthMonthDay").description("월일"),
                                fieldWithPath("content[].createdAt").description("생성일"),
                                fieldWithPath("content[].latestLogin").description("마지막 로그인"),
                                fieldWithPath("content[].gradeChangedDate").description("최근 등급변경 일"),
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


    }

    @Test
    @DisplayName("UserEmailRequest 로 verifyUserStatus 실행시 UserEncryptedPasswordResponse 반환")
    void givenUserEmailRequest_whenCallVerifyUserStatus_thenReturnUserEncryptedPasswordResponse() throws Exception {

        UserEmailRequest request = new UserEmailRequest("test@test.com");
        UserEncryptedPasswordResponse response = new UserEncryptedPasswordResponse("password");

        when(userService.verifyUserStatusByEmail(any(UserEmailRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.encryptedPassword").exists())
                .andDo(document("user-verification",
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("encryptedPassword").description("암호화된 비밀번호")
                        )
                ));

    }

    @Test
    @DisplayName("UserEmailRequest 로 completeLoginProcess 실행시 UserLoginResponse 반환")
    void givenUserEmailRequest_whenCallCompleteLoginProcess_thenReturnUserLoginResponse() throws Exception {

        UserEmailRequest request = new UserEmailRequest("test@test.com");
        UserLoginResponse response = new UserLoginResponse(true, true, 1L, "test");
        when(userService.completeLoginProcess(any(UserEmailRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/verification/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidUser").exists())
                .andExpect(jsonPath("$.isAdmin").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.status").exists())
                .andDo(document("user-verificationComplete",
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("isValidUser").description("유효성 유무"),
                                fieldWithPath("isAdmin").description("관리자 유무"),
                                fieldWithPath("userId").description("유저 아이디"),
                                fieldWithPath("status").description("유저 상태")
                        )
                ));

    }

    @Test
    @DisplayName("UserId 로 dormancyUserVerification 실행시 UserInactiveVerificationResponse 반환")
    void givenUserId_whenCallDormancyUserVerification_thenReturnUserInactiveVerificationResponse() throws Exception {

        UserInactiveVerificationResponse response = new UserInactiveVerificationResponse("test");
        when(userService.verifyDormancyUser(anyLong())).thenReturn(response);

        mockMvc.perform(post("/api/users/verification/dormancy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").exists())
                .andDo(document("user_status-verifyDormancy",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userStatus").description("유저 상태")
                        )
                ));
    }

    @Test
    @DisplayName("UserId 로 lockUserVerification 실행시 UserInactiveVerificationResponse 반환")
    void givenUserId_whenCallLockUserVerification_thenReturnUserInactiveVerificationResponse() throws Exception {

        UserPasswordModifyRequest request = new UserPasswordModifyRequest("test");
        UserInactiveVerificationResponse response = new UserInactiveVerificationResponse("test");
        when(userService.verifyLockUser(anyLong(), any(UserPasswordModifyRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/verification/lock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HeaderProperties.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").exists())
                .andDo(document("user_status-verifyLock",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("userStatus").description("유저 상태")
                        )
                ));
    }


    @Test
    @DisplayName("회원가입 시 사용가능한 email 인지 검증")
    void givenUserEmail_whenCallVerifyUserEmail_thenReturnUserEmailCheckResponse() throws Exception {

        UserEmailCheckResponse userEmailCheckResponse = new UserEmailCheckResponse(true);
        when(userService.verifyUserEmail(any(UserEmailRequest.class))).thenReturn(userEmailCheckResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/users/email/verify/{email}", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAvailable").exists())
                .andDo(document("user-verifyEmail",
                        pathParameters(
                                parameterWithName("email").description("이메일")
                        ),
                        responseFields(
                                fieldWithPath("isAvailable").description("사용 가능 유무")
                        )
                ));
    }


    @Test
    @DisplayName("유저 비밀번호 변경 ")
    void givenUserPasswordModifyRequest_whenCallModifyUserPassword_thenReturnUserPasswordModifyResponse()
            throws Exception {

        UserPasswordModifyResponse userEmailCheckResponse = new UserPasswordModifyResponse(true);
        UserPasswordModifyRequest userPasswordModifyRequest = new UserPasswordModifyRequest("changedPassword");
        when(userService.modifyUserPassword(anyLong(), any(UserPasswordModifyRequest.class))).thenReturn(
                userEmailCheckResponse);

        mockMvc.perform(put("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPasswordModifyRequest))
                        .header(HeaderProperties.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isChangePassword").exists())
                .andDo(document("user-passwordModify",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("isChangePassword").description("변경 유무")
                        )
                ));
    }

    @Test
    @DisplayName("Oauth Login 로그인 실행")
    void givenUserOauthLoginRequest_whenCallLoginOauthUser_thenReturnUserLoginResponse()
            throws Exception {


        UserOauthLoginRequest userOauthLoginRequest = new UserOauthLoginRequest("oauthId");
        UserLoginResponse userLoginResponse = new UserLoginResponse(true, false, 1L, "활성");

        when(userService.loginOauthUser(any(UserOauthLoginRequest.class))).thenReturn(userLoginResponse);

        mockMvc.perform(post("/api/users/oauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOauthLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValidUser").exists())
                .andExpect(jsonPath("$.isAdmin").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.status").exists())
                .andDo(document("user_oauth-login",
                        requestFields(
                                fieldWithPath("oauthId").description("소셜 아이디")
                        ),
                        responseFields(
                                fieldWithPath("isValidUser").description("유효성 유무"),
                                fieldWithPath("isAdmin").description("관리자 유무"),
                                fieldWithPath("userId").description("유저 아이디"),
                                fieldWithPath("status").description("유저 상태")
                        )
                ));
    }

    @Test
    @DisplayName("Oauth 유저 최초 로그인시 회원가입 및 로그인 진행")
    void givenUserOauthCreateRequest_whenCallCreateOauthUser_thenReturnUserOauthCreateResponse()
            throws Exception {


        UserOauthCreateRequest userOauthCreateRequest =
                new UserOauthCreateRequest("name", "01012345678", "test@test.com", "12-17", "oauthId");
        UserOauthCreateResponse userOauthCreateResponse =
                new UserOauthCreateResponse("name", "email@email.com", 1L, 1999, "12-17", "statusName", "gradeName");

        when(userService.createOauthUser(any(UserOauthCreateRequest.class))).thenReturn(userOauthCreateResponse);

        doNothing().when(pointHistoryService).saveSignUpPoint(anyString());
        doNothing().when(pointHistoryService).saveOauthLoginPoint(anyLong());

        mockMvc.perform(post("/api/users/oauth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOauthCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.birthYear").exists())
                .andExpect(jsonPath("$.birthMonthDay").exists())
                .andExpect(jsonPath("$.userStatusName").exists())
                .andExpect(jsonPath("$.userGradeName").exists())
                .andDo(document("user_oauth-create-consent",
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("phoneNumber").description("핸드폰"),
                                fieldWithPath("birthMonthDay").description("월일"),
                                fieldWithPath("oauthId").description("소셜 아이디")
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("id").description("유저 아이디"),
                                fieldWithPath("birthYear").description("생년"),
                                fieldWithPath("birthMonthDay").description("월일"),
                                fieldWithPath("userStatusName").description("유저상태 이름"),
                                fieldWithPath("userGradeName").description("유저등급 이름")
                        )
                ));
    }

    @Test
    @DisplayName("정보제공 동의 안한 Oauth 유저 최초 로그인시 회원가입 및 로그인 진행")
    void givenUserOauthCreateRequestWithOutInfo_whenCallCreateOauthUser_thenReturnUserOauthCreateResponse()
            throws Exception {


        UserOauthRequest userOauthRequest =
                new UserOauthRequest("name", "test@test.com", "01012345678", LocalDate.now(), "oauthID");
        UserOauthCreateResponse userOauthCreateResponse =
                new UserOauthCreateResponse("name", "email@email.com", 1L, 1999, "12-17", "status", "grade");

        when(userService.createOauthUser(any(UserOauthRequest.class))).thenReturn(userOauthCreateResponse);

        doNothing().when(pointHistoryService).saveSignUpPoint(anyString());
        doNothing().when(pointHistoryService).saveOauthLoginPoint(anyLong());

        mockMvc.perform(post("/api/users/oauth/no-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOauthRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.birthYear").exists())
                .andExpect(jsonPath("$.birthMonthDay").exists())
                .andExpect(jsonPath("$.userStatusName").exists())
                .andExpect(jsonPath("$.userGradeName").exists())
                .andDo(document("user_oauth-create-nonConsent",
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("phoneNumber").description("핸드폰"),
                                fieldWithPath("birth").description("생년월일"),
                                fieldWithPath("oauthId").description("소셜 아이디")
                        ),
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("id").description("유저 아이디"),
                                fieldWithPath("birthYear").description("생년"),
                                fieldWithPath("birthMonthDay").description("월일"),
                                fieldWithPath("userStatusName").description("유저상태 이름"),
                                fieldWithPath("userGradeName").description("유저등급 이름")
                        )
                ));
    }


    @BeforeEach
    void setUp() {

        userGetResponse1 = new UserGetResponse() {
            @Override
            public String getUserGradeUserGradeNameId() {
                return "test1";
            }

            @Override
            public String getUserStatusId() {
                return "test1";
            }

            @Override
            public String getName() {
                return "test1";
            }

            @Override
            public String getPhoneNumber() {
                return "test1";
            }

            @Override
            public String getEmail() {
                return "test1@naver.com";
            }

            @Override
            public Integer getBirthYear() {
                return 1000;
            }

            @Override
            public String getBirthMonthDay() {
                return "01-01";
            }


            @Override
            public LocalDateTime getCreatedAt() {
                return LocalDateTime.now();
            }

            @Override
            public LocalDateTime getLatestLogin() {
                return LocalDateTime.now();
            }

            @Override
            public LocalDate getGradeChangedDate() {
                return LocalDate.now();
            }
        };


        userGetResponse2 = new UserGetResponse() {
            @Override
            public String getUserGradeUserGradeNameId() {
                return "test2";
            }

            @Override
            public String getUserStatusId() {
                return "test2";
            }

            @Override
            public String getName() {
                return "test2";
            }

            @Override
            public String getPhoneNumber() {
                return "test2";
            }

            @Override
            public String getEmail() {
                return "test2@naver.com";
            }

            @Override
            public Integer getBirthYear() {
                return 1000;
            }

            @Override
            public String getBirthMonthDay() {
                return "01-01";
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return LocalDateTime.now();
            }

            @Override
            public LocalDateTime getLatestLogin() {
                return LocalDateTime.now();
            }

            @Override
            public LocalDate getGradeChangedDate() {
                return LocalDate.now();
            }
        };
    }

}













