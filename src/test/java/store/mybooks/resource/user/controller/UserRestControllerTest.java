package store.mybooks.resource.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.With;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.request.UserGradeModifyRequest;
import store.mybooks.resource.user.dto.request.UserModifyRequest;
import store.mybooks.resource.user.dto.request.UserStatusModifyRequest;
import store.mybooks.resource.user.dto.response.UserCreateResponse;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user.dto.response.UserGradeModifyResponse;
import store.mybooks.resource.user.dto.response.UserModifyResponse;
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
@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    UserGetResponse userGetResponse1;
    UserGetResponse userGetResponse2;

    @Test
    @DisplayName("UserCreateRequest 로 createUser 실행시 UserCreateResponse 반환")
    void givenUserCreateRequest_whenCallCreateUser_thenReturnUserCreateResponse() throws Exception {

        UserCreateRequest userCreateRequest =
                new UserCreateRequest("test", "test", "test", "test@naver.com", LocalDate.now(), false);

        UserCreateResponse userCreateResponse = new UserCreateResponse("test", "test", LocalDate.now(), "test", "test");

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(userCreateResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.birth").exists())
                .andExpect(jsonPath("$.userStatusName").exists())
                .andExpect(jsonPath("$.userGradeName").exists());
    }

    @Test
    @DisplayName("UserModifyRequest 로 modifyUser 실행시 UserModifyResponse 반환")
    void givenUserModifyRequest_whenCallModifyUser_thenReturnUserModifyResponse() throws Exception {

        UserModifyRequest userModifyRequest =
                new UserModifyRequest("test", "01012345678");

        UserModifyResponse userModifyResponse =
                new UserModifyResponse("test", "01012345678");

        when(userService.modifyUser(anyLong(), any(UserModifyRequest.class))).thenReturn(userModifyResponse);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest))
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.phoneNumber").exists());
    }

    @Test
    @DisplayName("UserGradeModifyRequest 로 modifyUserGrade 실행시 UserGradeModifyResponse 반환")
    void givenUserGradeModifyRequest_whenCallModifyUserGrade_thenReturnUserModifyResponse() throws Exception {

        UserGradeModifyRequest userModifyRequest = new UserGradeModifyRequest("test");

        UserGradeModifyResponse userModifyResponse = new UserGradeModifyResponse("test");

        when(userService.modifyUserGrade(anyLong(), any(UserGradeModifyRequest.class))).thenReturn(userModifyResponse);

        mockMvc.perform(put("/api/users/{userId}/grade", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userGradeName").exists());
    }

    @Test
    @DisplayName("UserStatusModifyRequest 로 modifyUserStatus 실행시 UserStatusModifyResponse 반환")
    void givenUserStatusModifyRequest_whenCallModifyUserStatus_thenReturnUserModifyResponse() throws Exception {

        UserStatusModifyRequest userModifyRequest = new UserStatusModifyRequest("test");
        UserStatusModifyResponse userModifyResponse = new UserStatusModifyResponse("test", LocalDate.now());

        when(userService.modifyUserStatus(anyLong(), any(UserStatusModifyRequest.class))).thenReturn(
                userModifyResponse);

        mockMvc.perform(put("/api/users/{userId}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatusName").exists())
                .andExpect(jsonPath("$.gradeChangedDate").exists());
    }

    @Test
    @DisplayName("UserId 로 deleteUser 실행시 UserDeleteResponse 반환")
    void givenUserId_whenCallDeleteUser_thenReturnUserDeleteResponse() throws Exception {

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse("test");

        when(userService.deleteUser(anyLong())).thenReturn(userDeleteResponse);


        mockMvc.perform(delete("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1")) // 헤더 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

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
                .andExpect(jsonPath("$.birth").exists())
                .andExpect(jsonPath("$.userGradeUserGradeNameId").exists());

    }

    @Test
    @DisplayName("UserId 로 findAllUser 실행시 Page<UserGetResponse> 반환")
    void givenUserId_whenCallFindAllUser_thenReturnUserGetResponsePage() throws Exception {


        List<UserGetResponse> userGetResponseList = new ArrayList<>();
        userGetResponseList.add(userGetResponse1);
        userGetResponseList.add(userGetResponse2);
        Page<UserGetResponse> userGetResponsePage = new PageImpl<>(userGetResponseList);

        when(userService.findAllUser(any(Pageable.class))).thenReturn(userGetResponsePage);

        mockMvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.totalPages").exists());

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
            public LocalDate getBirth() {
                return LocalDate.now();
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
            public LocalDate getBirth() {
                return LocalDate.now();
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













