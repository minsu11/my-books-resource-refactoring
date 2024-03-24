package store.mybooks.resource.pointhistory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.bookstatus.entity.BookStatus;
import store.mybooks.resource.delivery_rule.entity.DeliveryRule;
import store.mybooks.resource.delivery_rule_name.entity.DeliveryRuleName;
import store.mybooks.resource.orderdetailstatus.entity.OrderDetailStatus;
import store.mybooks.resource.ordersstatus.entity.OrdersStatus;
import store.mybooks.resource.pointhistory.dto.mapper.PointHistoryMapper;
import store.mybooks.resource.pointhistory.dto.request.PointHistoryCreateRequest;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryCreateResponse;
import store.mybooks.resource.pointhistory.dto.response.PointHistoryResponse;
import store.mybooks.resource.pointhistory.dto.response.PointResponse;
import store.mybooks.resource.pointhistory.entity.PointHistory;
import store.mybooks.resource.pointhistory.exception.AlreadyReceivedSignUpPoint;
import store.mybooks.resource.pointhistory.repository.PointHistoryRepository;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.exception.PointRuleNotExistException;
import store.mybooks.resource.pointrule.repository.PointRuleRepository;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_address.entity.UserAddress;
import store.mybooks.resource.user_grade.entity.UserGrade;
import store.mybooks.resource.user_grade_name.entity.UserGradeName;
import store.mybooks.resource.user_status.entity.UserStatus;
import store.mybooks.resource.utils.TimeUtils;

/**
 * packageName    : store.mybooks.resource.pointhistory.service
 * fileName       : PointHistoryServiceTest
 * author         : damho-lee
 * date           : 3/23/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/23/24          damho-lee          최초 생성
 */
@ExtendWith({MockitoExtension.class})
class PointHistoryServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BookOrderRepository bookOrderRepository;

    @Mock
    PointRuleRepository pointRuleRepository;

    @Mock
    PointRuleNameRepository pointRuleNameRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    PointHistoryMapper pointHistoryMapper;

    @InjectMocks
    PointHistoryService pointHistoryService;

    PointHistoryCreateRequest pointHistoryCreateRequest;

    PointRuleName pointRuleName;

    PointRuleName loginPointRuleName;

    PointRuleName signupPointRuleName;

    PointRule pointRule;

    PointRule loginPoint;

    PointRule signupPoint;

    UserStatus userStatus;

    UserGradeName userGradeName;

    UserGrade userGrade;

    User user;

    UserAddress userAddress;

    BookStatus bookStatus;

    Publisher publisher;

    Book book;

    OrderDetailStatus orderDetailStatus;

    DeliveryRuleName deliveryRuleName;

    DeliveryRule deliveryRule;

    BookOrder bookOrder;

    OrdersStatus ordersStatus;

    PointHistory pointHistory;

    PointHistory loginPointHistory;

    PointHistory signupPointHistory;

    String pointSave = "포인트 적립";

    String loginSave = "로그인 적립";

    String signupSave = "회원가입 적립";

    String email = "test@naver.com";

    @BeforeEach
    void setup() {
        pointRuleName = new PointRuleName(pointSave);
        loginPointRuleName = new PointRuleName(loginSave);
        signupPointRuleName = new PointRuleName(signupSave);
        pointRule = new PointRule(pointRuleName, 1, null);
        ReflectionTestUtils.setField(pointRule, "id", 1);
        loginPoint = new PointRule(
                loginPointRuleName,
                null,
                500
        );
        ReflectionTestUtils.setField(loginPoint, "id", 2);
        signupPoint = new PointRule(
                signupPointRuleName,
                null,
                500
        );
        ReflectionTestUtils.setField(signupPoint, "id", 3);
        userStatus = new UserStatus("활동중");
        userGradeName = new UserGradeName("일반");
        userGrade = new UserGrade(0, 100000, 2, userGradeName);
        user = new User(
                "test@naver.com",
                LocalDate.of(1999, 1, 1),
                "dummy",
                "01012341234",
                false,
                "테스트유저",
                userStatus,
                userGrade,
                null
        );
        ReflectionTestUtils.setField(user, "latestLogin", TimeUtils.nowDateTime().minusDays(1L));
        ReflectionTestUtils.setField(user, "id", 1L);
        bookOrder = new BookOrder(
                null,
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 3),
                LocalDate.of(2024, 2, 28),
                "912871535",
                "김철수",
                null,
                "01012341234",
                null,
                6000,
                2000,
                0,
                false,
                "a98ewg9r7",
                null,
                user,
                ordersStatus,
                deliveryRule,
                null
        );
        userAddress = new UserAddress(user, "집", "광주광역시 동구 조선대6길 34 (서석동)", "726A호", 61452, null);
        bookStatus = new BookStatus("판매중");
        publisher = new Publisher("조선 출판사");
        book = Book.builder()
                .bookStatus(bookStatus)
                .publisher(publisher)
                .name("아프니까 청춘이다")
                .isbn("9788965700180")
                .publishDate(LocalDate.of(2010, 12, 24))
                .page(320)
                .index("index")
                .explanation("아프니까 청춘입니다.")
                .originalCost(10000)
                .discountRate(20)
                .saleCost(8000)
                .stock(10)
                .viewCount(0)
                .isPackaging(true)
                .createdDate(LocalDate.of(2010, 8, 1))
                .build();
        orderDetailStatus = new OrderDetailStatus("구매 확정");
        deliveryRuleName = new DeliveryRuleName("배송비");
        deliveryRule = new DeliveryRule(deliveryRuleName, "CJ대한통운", 3000, 10000);
        pointHistory = new PointHistory(300, user, pointRule, bookOrder);
        ReflectionTestUtils.setField(pointHistory, "id", 1L);
        loginPointHistory = new PointHistory(500, user, loginPoint, null);
        ReflectionTestUtils.setField(loginPointHistory, "id", 2L);
        signupPointHistory = new PointHistory(2000, user, signupPoint, null);
        ReflectionTestUtils.setField(loginPointHistory, "id", 3L);
    }

    @Test
    @DisplayName("getRemainingPoint 테스트")
    void givenUserId_whenGetRemainingPoint_thenReturnPointResponse() {
        PointResponse pointResponse = new PointResponse(1000);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(pointHistoryRepository.getRemainingPoint(anyLong())).thenReturn(pointResponse);

        PointResponse actual = pointHistoryService.getRemainingPoint(1L);
        assertThat(actual.getRemainingPoint()).isEqualTo(pointResponse.getRemainingPoint());
        verify(userRepository, times(1)).existsById(1L);
        verify(pointHistoryRepository, times(1)).getRemainingPoint(1L);
    }

    @Test
    @DisplayName("getRemainingPoint 테스트 - 없는 회원인 경우")
    void givenNotExistsUserId_whenGetRemainingPoint_thenThrowUserNotExistException() {
        PointResponse pointResponse = new PointResponse(1000);
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotExistException.class, () -> pointHistoryService.getRemainingPoint(1L));
        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("getPointHistory")
    void givenPageableAndUserId_whenGetPointHistory_thenReturnPointHistoryResponsePage() {
        long total = 100;
        Pageable pageable = PageRequest.of(0, 1);
        PointHistoryResponse response = new PointHistoryResponse(
                pointRuleName.getId(),
                pointHistory.getPointStatusCost(),
                TimeUtils.nowDate());
        Page<PointHistoryResponse> expect = new PageImpl<>(List.of(response), pageable, total);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(pointHistoryRepository.getPointHistoryByUserId(any(), anyLong())).thenReturn(expect);

        Page<PointHistoryResponse> actual = pointHistoryService.getPointHistory(pageable, 1L);
        List<PointHistoryResponse> actualList = actual.getContent();
        assertThat(actual).isNotNull().hasSize(1);
        assertThat(actualList).isNotNull().hasSize(1);
        assertThat(actualList.get(0).getPointRuleName()).isEqualTo(response.getPointRuleName());
        assertThat(actualList.get(0).getStatusCost()).isEqualTo(response.getStatusCost());
        assertThat(actualList.get(0).getCreatedDate()).isEqualTo(response.getCreatedDate());
        verify(userRepository, times(1)).existsById(1L);
        verify(pointHistoryRepository, times(1)).getPointHistoryByUserId(any(), anyLong());
    }

    @Test
    @DisplayName("getPointHistory - 없는 회원인 경우")
    void givenPageableAndNotExistsUserId_whenGetPointHistory_thenReturnPointHistoryResponsePage() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(UserNotExistException.class, () -> pointHistoryService.getRemainingPoint(1L));
        verify(userRepository, times(1)).existsById(1L);
        verify(pointHistoryRepository, times(0)).getPointHistoryByUserId(any(), anyLong());
    }

    @Test
    @DisplayName("getPointHistory 테스트")
    void givenPointHistoryCreateRequest_whenCreatePointHistory_thenReturnPointHistoryCreateResponse() {
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.of(pointRuleName));
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.of(pointRule));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookOrderRepository.findByNumber(anyString())).thenReturn(Optional.of(bookOrder));
        when(pointHistoryRepository.save(any())).thenReturn(pointHistory);
        PointHistoryCreateResponse expect = new PointHistoryCreateResponse(pointHistory.getId());
        when(pointHistoryMapper.mapToPointHistoryCreateResponse(any())).thenReturn(expect);

        PointHistoryCreateRequest request = pointHistoryCreateRequest = new PointHistoryCreateRequest(
                bookOrder.getNumber(),
                pointSave,
                2000
        );

        PointHistoryCreateResponse actual = pointHistoryService.createPointHistory(request, 1L);
        assertThat(actual).isNotNull();
        assertThat(actual.getPointId()).isEqualTo(expect.getPointId());
        verify(pointRuleNameRepository, times(1)).findById(pointRuleName.getId());
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(pointRuleName.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(bookOrderRepository, times(1)).findByNumber(bookOrder.getNumber());
        verify(pointHistoryRepository, times(1)).save(any());
        verify(pointHistoryMapper, times(1)).mapToPointHistoryCreateResponse(any());
    }

    @Test
    @DisplayName("getPointHistory 테스트 - 포인트 규정명이 존재하지 않는 경우")
    void givenNotExistsPointRuleNamePointHistoryCreateRequest_whenCreatePointHistory_thenReturnPointHistoryCreateResponse() {
        PointHistoryCreateRequest request = pointHistoryCreateRequest = new PointHistoryCreateRequest(
                bookOrder.getNumber(),
                "존재하지 않는 포인트 규정명",
                2000
        );
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(PointRuleNotExistException.class, () -> pointHistoryService.createPointHistory(request, 1L));
    }

    @Test
    @DisplayName("getPointHistory 테스트 - 포인트 규정명은 있으나 포인트 규정이 존재하지 않는 경우")
    void givenNotExistsPointRulePointHistoryCreateRequest_whenCreatePointHistory_thenReturnPointHistoryCreateResponse() {
        PointHistoryCreateRequest request = pointHistoryCreateRequest = new PointHistoryCreateRequest(
                bookOrder.getNumber(),
                "포인트규정명은 있으나 포인트 규정이 없는 규정명",
                2000
        );
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.of(pointRuleName));
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.empty());

        assertThrows(PointRuleNotExistException.class, () -> pointHistoryService.createPointHistory(request, 1L));
    }

    @Test
    @DisplayName("getPointHistory 테스트 - 유저가 존재하지 않는 경우")
    void givenNotExistsUserIdPointHistoryCreateRequest_whenCreatePointHistory_thenReturnPointHistoryCreateResponse() {
        PointHistoryCreateRequest request = pointHistoryCreateRequest = new PointHistoryCreateRequest(
                bookOrder.getNumber(),
                pointSave,
                2000
        );
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.of(pointRuleName));
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.of(pointRule));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, () -> pointHistoryService.createPointHistory(request, 1L));
    }

    @Test
    @DisplayName("getPointHistory 테스트 - 유저가 존재하지 않는 경우")
    void givenNotExistsBookOrderNumberPointHistoryCreateRequest_whenCreatePointHistory_thenReturnPointHistoryCreateResponse() {
        PointHistoryCreateRequest request = pointHistoryCreateRequest = new PointHistoryCreateRequest(
                "존재하지 않는 주문번호",
                pointSave,
                2000
        );
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.of(pointRuleName));
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.of(pointRule));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookOrderRepository.findByNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(BookOrderNotExistException.class, () -> pointHistoryService.createPointHistory(request, 1L));
    }

    @Test
    @DisplayName("saveLoginPoint 테스트")
    void givenUserId_whenSaveLoginPoint_thenReturnTrue() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(pointRuleRepository.findPointRuleByPointRuleName(loginSave)).thenReturn(Optional.of(loginPoint));
        when(pointHistoryRepository.save(any())).thenReturn(loginPointHistory);

        assertTrue(pointHistoryService.saveLoginPoint(user.getId()));
    }

    @Test
    @DisplayName("saveLoginPoint 테스트 - 유저가 존재하지 않는 경우")
    void givenNotExistsUserId_whenSaveLoginPoint_thenThrowUserNotExistException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        long id = user.getId();

        assertThrows(UserNotExistException.class, () -> pointHistoryService.saveLoginPoint(id));
    }

    @Test
    @DisplayName("saveLoginPoint 테스트 - 첫 로그인이 아닌 경우")
    void givenNotFirstLoginToday_whenSaveLoginPoint_thenReturnFalse() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ReflectionTestUtils.setField(user, "latestLogin", TimeUtils.nowDateTime());

        assertFalse(pointHistoryService.saveLoginPoint(user.getId()));
    }

    @Test
    @DisplayName("saveOauthLoginPoint 테스트")
    void givenUserId_whenSaveOauthLoginPoint_thenSavePointHistory() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.of(pointRule));
        when(pointHistoryRepository.save(any())).thenReturn(pointHistory);

        pointHistoryService.saveOauthLoginPoint(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(loginSave);
        verify(pointHistoryRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("saveOauthLoginPoint 테스트 - 존재하지 않는 회원")
    void givenNotExistsUserId_whenSaveOauthLoginPoint_thenThrowUserNotExistException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());


        assertThrows(UserNotExistException.class, () -> pointHistoryService.saveOauthLoginPoint(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("saveOauthLoginPoint 테스트 - 로그인 적립이름의 포인트 규정이 없는 경우")
    void givenNotExistsPointRuleName_whenSaveOauthLoginPoint_thenThrowPointRuleNotExistException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.empty());

        assertThrows(PointRuleNotExistException.class, () -> pointHistoryService.saveOauthLoginPoint(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(loginSave);
    }

    @Test
    @DisplayName("saveSignUpPoint 테스트")
    void givenEmail_whenSaveSignUpPoint_thenSaveSignupPoint() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(pointHistoryRepository.isAlreadyReceivedSignUpPoint(user.getEmail())).thenReturn(false);
        when(pointRuleRepository.findPointRuleByPointRuleName(signupSave)).thenReturn(Optional.of(signupPoint));

        pointHistoryService.saveSignUpPoint(user.getEmail());

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(pointHistoryRepository, times(1)).isAlreadyReceivedSignUpPoint(user.getEmail());
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(signupSave);
    }

    @Test
    @DisplayName("saveSignUpPoint 테스트")
    void givenNotExistsEmail_whenSaveSignUpPoint_thenThrowUserNotExistException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, () -> pointHistoryService.saveSignUpPoint(email));

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(pointHistoryRepository, times(0)).isAlreadyReceivedSignUpPoint(user.getEmail());
        verify(pointRuleRepository, times(0)).findPointRuleByPointRuleName(signupSave);
    }

    @Test
    @DisplayName("saveSignUpPoint 테스트")
    void givenAlreadyReceivedSignUpPointEmail_whenSaveSignUpPoint_thenSaveSignupPoint() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(pointHistoryRepository.isAlreadyReceivedSignUpPoint(user.getEmail())).thenReturn(true);

        assertThrows(AlreadyReceivedSignUpPoint.class, () -> pointHistoryService.saveSignUpPoint(email));

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(pointHistoryRepository, times(1)).isAlreadyReceivedSignUpPoint(user.getEmail());
        verify(pointRuleRepository, times(0)).findPointRuleByPointRuleName(signupSave);
    }

    @Test
    @DisplayName("saveSignUpPoint 테스트")
    void givenNotExistsPointRule_whenSaveSignUpPoint_thenSaveSignupPoint() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(pointHistoryRepository.isAlreadyReceivedSignUpPoint(user.getEmail())).thenReturn(false);
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.empty());

        assertThrows(PointRuleNotExistException.class, () -> pointHistoryService.saveSignUpPoint(email));

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(pointHistoryRepository, times(1)).isAlreadyReceivedSignUpPoint(user.getEmail());
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(signupSave);
    }
}