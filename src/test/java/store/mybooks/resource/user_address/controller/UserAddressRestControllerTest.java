package store.mybooks.resource.user_address.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
import store.mybooks.resource.user_address.dto.request.UserAddressModifyRequest;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressDeleteResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.exception.UserAddressNotExistException;
import store.mybooks.resource.user_address.service.UserAddressService;

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
@WebMvcTest(value = UserAddressRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserAddressRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserAddressService userAddressService;

    UserAddressGetResponse userAddressGetResponse1;
    UserAddressGetResponse userAddressGetResponse2;


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
    @DisplayName("유저 UserAddressCreateRequest - Validation 실패")
    void givenUserAddressCreateRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserAddressCreateRequest request = new UserAddressCreateRequest("alias", null, "detail", 1, "");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post("/api/users/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header(HeaderProperties.USER_ID, 1L))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("유저 UserAddressModifyRequest - Validation 실패")
    void givenUserAddressModifyRequest_whenValidationFailure_thenReturnBadRequest() throws Exception {

        UserAddressModifyRequest request = new UserAddressModifyRequest("", "", "");

        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/addresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header(HeaderProperties.USER_ID, 1L))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOfAny(RequestValidationFailedException.class);
    }

    @Test
    @DisplayName("UserId , UserAddressCreateRequest 로 createUserAddress 실행시 UserAddressCreateResponse 반환")
    void givenUserIdAndUserAddressCreateRequest_whenCallCreateUserAddress_thenReturnUserAddressCreateResponse()
            throws Exception {

        UserAddressCreateRequest userAddressCreateRequest =
                new UserAddressCreateRequest("별명", "도로명주소", "상세주소", 1, "추가정보");

        UserAddressCreateResponse userAddressCreateResponse =
                new UserAddressCreateResponse("별명", "도로명주소", "상세주소", 1, "추가정보");

        when(userAddressService.createUserAddress(anyLong(), any(UserAddressCreateRequest.class))).thenReturn(
                userAddressCreateResponse);

        mockMvc.perform(post("/api/users/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressCreateRequest))
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").exists())
                .andExpect(jsonPath("$.roadName").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.reference").exists())
                .andDo(document("user_address-create",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        requestFields(
                                fieldWithPath("alias").description("별명"),
                                fieldWithPath("roadName").description("도로명주소"),
                                fieldWithPath("detail").description("상세주소"),
                                fieldWithPath("number").description("우편번호"),
                                fieldWithPath("reference").description("추가정보")
                        ),
                        responseFields(
                                fieldWithPath("alias").description("별명"),
                                fieldWithPath("roadName").description("도로명주소"),
                                fieldWithPath("detail").description("상세주소"),
                                fieldWithPath("number").description("우편번호"),
                                fieldWithPath("reference").description("추가정보")
                        )
                ));

    }

    @Test
    @DisplayName("UserId , AddressId , UserAddressModifyRequest 로 modifyUserAddress 실행시 UserAddressModifyResponse 반환")
    void givenUserIdAndAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenReturnUserAddressModifyResponse()
            throws Exception {

        UserAddressModifyRequest userAddressModifyRequest =
                new UserAddressModifyRequest("별명", "상세주소", "추가정보");
        UserAddressModifyResponse userAddressModifyResponse =
                new UserAddressModifyResponse("별명", "상세주소", "추가정보");

        when(userAddressService.modifyUserAddress(anyLong(), anyLong(),
                any(UserAddressModifyRequest.class))).thenReturn(userAddressModifyResponse);

        userAddressService.modifyUserAddress(1L, 1L, userAddressModifyRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/users/addresses/{addressId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressModifyRequest))
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.reference").exists())
                .andDo(document("user_address-modify",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        pathParameters(
                                parameterWithName("addressId").description("주소 아이디")
                        ),
                        requestFields(
                                fieldWithPath("alias").description("별명"),
                                fieldWithPath("detail").description("상세주소"),
                                fieldWithPath("reference").description("추가정보")
                        ),
                        responseFields(
                                fieldWithPath("alias").description("수정된 별명"),
                                fieldWithPath("detail").description("수정된 상세주소"),
                                fieldWithPath("reference").description("수정된 추가정보")
                        )
                ));
    }


    @Test
    @DisplayName("존재하지않는 UserId  , UserAddressModifyRequest 로 modifyUserAddress 실행시 UserNotExistException 반환")
    void givenNotExistUserIdAndAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenThrowUserNotExistException()
            throws Exception {

        UserAddressModifyRequest userAddressModifyRequest =
                new UserAddressModifyRequest("별명", "상세주소", "추가정보");

        when(userAddressService.modifyUserAddress(anyLong(), anyLong(),
                any(UserAddressModifyRequest.class))).thenThrow(UserNotExistException.class);

        mockMvc.perform(put("/api/users/addresses/{addressId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressModifyRequest))
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isNotFound())
                .andDo(document("user_address-modify-notExistUserId",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        )
                ));
    }


    @Test
    @DisplayName("UserId , 존재하지않는 AddressId , UserAddressModifyRequest 로 modifyUserAddress 실행시 UserAddressNotExistException ")
    void givenUserIdAndNotExistAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenThrowUserAddressNotExistException()
            throws Exception {

        UserAddressModifyRequest userAddressModifyRequest =
                new UserAddressModifyRequest("별명", "상세주소", "추가정보");

        when(userAddressService.modifyUserAddress(anyLong(), anyLong(),
                any(UserAddressModifyRequest.class))).thenThrow(UserAddressNotExistException.class);


        mockMvc.perform(put("/api/users/addresses/{addressId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddressModifyRequest))
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isNotFound())
                .andDo(document("user_address-modify-notExistAddressId",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        )
                ));
    }

    @Test
    @DisplayName("UserId , AddressId 로 deleteUserAddress 실행시 UserAddressDeleteResponse 반환")
    void givenUserIdAndAddressId_whenCallDeleteUserAddress_thenReturnUserAddressDeleteResponse() throws Exception {

        UserAddressDeleteResponse userAddressDeleteResponse = new UserAddressDeleteResponse("[우리집] 삭제 완료");
        when(userAddressService.deleteUserAddress(anyLong(), anyLong())).thenReturn(userAddressDeleteResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/users/addresses/{addressId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andDo(document("user_address-delete",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        pathParameters(
                                parameterWithName("addressId").description("주소 아이디")
                        ),
                        responseFields(
                                fieldWithPath("message").description("주소 별명 삭제 메시지")
                        )
                ));
    }


    @Test
    @DisplayName("존재하지 않는 UserId 로 deleteUserAddress 실행시 UserNotExistException 반환")
    void givenNotExistUserIdAndAddressId_whenCallDeleteUserAddress_thenThrowUserNotExistException() throws Exception {

        when(userAddressService.deleteUserAddress(anyLong(), anyLong())).thenThrow(UserNotExistException.class);

        mockMvc.perform(delete("/api/users/addresses/{addressId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isNotFound())
                .andDo(document("user_address-delete-notExistsUserId",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        )
                ));
    }


    @Test
    @DisplayName("존재하지 않는 addressId 로 deleteUserAddress 실행시 UserAddressNotExistException 반환")
    void givenUserIdAndNotExistAddressId_whenCallDeleteUserAddress_thenThrowUserAddressNotExistException()
            throws Exception {

        when(userAddressService.deleteUserAddress(anyLong(), anyLong())).thenThrow(UserAddressNotExistException.class);

        mockMvc.perform(delete("/api/users/addresses/{addressId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isNotFound())
                .andDo(document("user_address-delete-notExistsAddressId",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        )
                ));
    }


    @Test
    @DisplayName("UserId 로 findAllAddressByUserId 실행시 List<UserAddressGetResponse> 반환")
    void givenUserId_whenCallFIndAllAddressByUserId_thenReturnUserAddressGetResponseList() throws Exception {

        List<UserAddressGetResponse> list = new ArrayList<>();
        list.add(userAddressGetResponse1);
        list.add(userAddressGetResponse2);

        when(userAddressService.findAllAddressByUserId(anyLong())).thenReturn(list);


        mockMvc.perform(get("/api/users/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].reference").exists())
                .andExpect(jsonPath("$[*].number").exists())
                .andExpect(jsonPath("$[*].detail").exists())
                .andExpect(jsonPath("$[*].alias").exists())
                .andExpect(jsonPath("$[*].roadName").exists())
                .andDo(document("user_address-findAll",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("주소 아이디"),
                                fieldWithPath("[].alias").description("주소 별명"),
                                fieldWithPath("[].roadName").description("도로명 주소"),
                                fieldWithPath("[].detail").description("상세 주소"),
                                fieldWithPath("[].number").description("우편번호"),
                                fieldWithPath("[].reference").description("추가정보")
                        )
                ));
    }


    @Test
    @DisplayName("존재하지않는 UserId 로 findAllAddressByUserId 실행시 UserNotExistException ")
    void givenNotExistUserId_whenCallFIndAllAddressByUserId_thenThrowUserNotExistException() throws Exception {


        when(userAddressService.findAllAddressByUserId(anyLong())).thenThrow(UserNotExistException.class);


        mockMvc.perform(get("/api/users/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderProperties.USER_ID, "1"))
                .andExpect(status().isNotFound())
                .andDo(document("user_address-findAll-notExistUserId",
                        requestHeaders(
                                headerWithName("X-User-Id").description("회원 아이디")
                        )
                ));
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