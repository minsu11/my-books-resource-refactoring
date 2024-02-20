package store.mybooks.resource.user_status.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.user_status.repository.UserStatusRepository;
import store.mybooks.resource.user_status.service.UserStatusService;

/**
 * packageName    : store.mybooks.resource.user_status.controller
 * fileName       : UserStatusRestControllerTest
 * author         : masiljangajji
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        masiljangajji       최초 생성
 */


@WebMvcTest(UserStatusRestController.class)
class UserStatusRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserStatusService userStatusService;

    @Test
    void givenUserStatusId_whenCallGetUserStatus_thenReturnUserStatusGetResponse() throws Exception {

        UserStatusGetResponse userStatusGetResponse = new UserStatusGetResponse() {
            @Override
            public String getId() {
                return "test";
            }
        };

        Mockito.when(userStatusService.findUserStatusById(anyString())).thenReturn(userStatusGetResponse);

        mockMvc.perform(get("/api/users-statuses/{userStatusId}", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(userStatusService, times(1)).findUserStatusById(anyString());
    }


    @Test
    void whenCallGetAllUserStatus_thenReturnUstStatusList() throws Exception {

        UserStatusGetResponse userStatusGetResponse1 = new UserStatusGetResponse() {
            @Override
            public String getId() {
                return "test1";
            }
        };

        UserStatusGetResponse userStatusGetResponse2 = new UserStatusGetResponse() {
            @Override
            public String getId() {
                return "test2";
            }
        };

        List<UserStatusGetResponse> list = List.of(userStatusGetResponse1, userStatusGetResponse2);

        Mockito.when(userStatusService.findAllUserStatus()).thenReturn(list);

        mockMvc.perform(get("/api/users-statuses"))
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.[*].id").exists());
    }




}