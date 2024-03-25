package store.mybooks.resource.user_status.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.user_status.dto.response.UserStatusGetResponse;
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


@WebMvcTest(value = UserStatusRestController.class)
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class UserStatusRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserStatusService userStatusService;

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
    @DisplayName("getAllUserStatus 실행시 모든 UserStatus 를 Pagination 해서 조회")
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
                .andExpectAll(jsonPath("$.[*].id").exists())
                .andDo(document("user_status-findAll",
                        responseFields(
                                fieldWithPath("[].id").description("유저상태 아이디")
                        )
                ));
    }


}