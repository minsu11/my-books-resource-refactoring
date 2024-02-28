package store.mybooks.resource.user_address.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
import store.mybooks.resource.user_address.dto.request.UserAddressModifyRequest;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressDeleteResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.service.UserAddressService;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;

/**
 * packageName    : store.mybooks.resource.user_address.controller
 * fileName       : UserAddressRestControllerTest
 * author         : masiljangajji
 * date           : 2/21/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/21/24        masiljangajji       최초 생성
 */
@WebMvcTest(value = UserAddressRestController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class UserAddressRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserAddressService userAddressService;

    UserAddressGetResponse userAddressGetResponse1;
    UserAddressGetResponse userAddressGetResponse2;

    @Test
    @DisplayName("UserId , UserAddressCreateRequest 로 createUserAddress 실행시 UserAddressCreateResponse 반환")
    void givenUserIdAndUserAddressCreateRequest_whenCallCreateUserAddress_thenReturnUserAddressCreateResponse()
            throws Exception {

        UserAddressCreateRequest userAddressCreateRequest =
                new UserAddressCreateRequest("test", "test", "test", 1, "test");

        UserAddressCreateResponse userAddressCreateResponse =
                new UserAddressCreateResponse("test", "test", "test", 1, "test");

        when(userAddressService.createUserAddress(anyLong(), any(UserAddressCreateRequest.class))).thenReturn(
                userAddressCreateResponse);

        mockMvc.perform(post("/api/users/{userId}/addresses", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").exists())
                .andExpect(jsonPath("$.roadName").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.reference").exists());

    }

    @Test
    @DisplayName("UserId , AddressId , UserAddressModifyRequest 로 modifyUserAddress 실행시 UserAddressModifyResponse 반환")
    void givenUserIdAndAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenReturnUserAddressModifyResponse()
            throws Exception {

        UserAddressModifyRequest userAddressModifyRequest =
                new UserAddressModifyRequest("test", "test");
        UserAddressModifyResponse userAddressModifyResponse =
                new UserAddressModifyResponse("test", "test");

        when(userAddressService.modifyUserAddress(anyLong(), anyLong(),
                any(UserAddressModifyRequest.class))).thenReturn(userAddressModifyResponse);

        userAddressService.modifyUserAddress(1L, 1L, userAddressModifyRequest);

        mockMvc.perform(put("/api/users/{userId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressModifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").exists())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("UserId , AddressId 로 deleteUserAddress 실행시 UserAddressDeleteResponse 반환")
    void givenUserIdAndAddressId_whenCallDeleteUserAddress_thenReturnUserAddressDeleteResponse() throws Exception {

        UserAddressDeleteResponse userAddressDeleteResponse = new UserAddressDeleteResponse("test");
        when(userAddressService.deleteUserAddress(anyLong(), anyLong())).thenReturn(userAddressDeleteResponse);

        mockMvc.perform(delete("/api/users/{userId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("UserId , AddressId 로 findUserAddressByAddressId 실행시 UserAddressGetResponse 반환")
    void givenUserIdAndAddressId_whenCallFindUserAddressByAddressId_thenReturnUserAddressGetResponse()
            throws Exception {

        when(userAddressService.findByAddressId(anyLong(), anyLong())).thenReturn(userAddressGetResponse1);

        mockMvc.perform(get("/api/users/{userId}/addresses/{addressId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").exists())
                .andExpect(jsonPath("$.roadName").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.reference").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Pageable 로 findAllUserAddress 실행시 Page<UserAddressGetResponse> 반환")
    void givenPageable_whenCallFindAllUserAddress_thenReturnUserAddressGetResponsePage()
            throws Exception {

        List<UserAddressGetResponse> userAddressGetResponseList = new ArrayList<>();
        userAddressGetResponseList.add(userAddressGetResponse1);
        userAddressGetResponseList.add(userAddressGetResponse2);
        Page<UserAddressGetResponse> userAddressGetResponsePage = new PageImpl<>(userAddressGetResponseList);

        when(userAddressService.findByAllUserAddress(any(Pageable.class))).thenReturn(userAddressGetResponsePage);

        mockMvc.perform(get("/api/users/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }

    @Test
    @DisplayName("UserId 로 findAllAddressByUserId 실행시 List<UserAddressGetResponse> 반환")
    void givenUserId_whenCallFIndAllAddressByUserId_thenReturnUserAddressGetResponseList() throws Exception {

        List<UserAddressGetResponse> list = new ArrayList<>();
        list.add(userAddressGetResponse1);
        list.add(userAddressGetResponse2);

        when(userAddressService.findAllAddressByUserId(anyLong())).thenReturn(list);


        mockMvc.perform(get("/api/users/{userId}/addresses", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].reference").exists())
                .andExpect(jsonPath("$[*].number").exists())
                .andExpect(jsonPath("$[*].detail").exists())
                .andExpect(jsonPath("$[*].alias").exists())
                .andExpect(jsonPath("$[*].roadName").exists());

    }


    @BeforeEach
    void setUp() {

        userAddressGetResponse1 = new UserAddressGetResponse() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getAlias() {
                return "test";
            }

            @Override
            public String getRoadName() {
                return "test";
            }

            @Override
            public String getDetail() {
                return "test";
            }

            @Override
            public Integer getNumber() {
                return 1;
            }

            @Override
            public String getReference() {
                return "test";
            }
        };

        userAddressGetResponse2 = new UserAddressGetResponse() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getAlias() {
                return "test";
            }

            @Override
            public String getRoadName() {
                return "test";
            }

            @Override
            public String getDetail() {
                return "test";
            }

            @Override
            public Integer getNumber() {
                return 1;
            }

            @Override
            public String getReference() {
                return "test";
            }
        };

    }

}