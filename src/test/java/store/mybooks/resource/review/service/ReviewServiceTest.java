package store.mybooks.resource.review.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.service.ImageService;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.exception.ImageStatusNotExistException;
import store.mybooks.resource.image_status.repository.ImageStatusRepository;
import store.mybooks.resource.orderdetail.entity.OrderDetail;
import store.mybooks.resource.orderdetail.exception.OrderDetailNotExistException;
import store.mybooks.resource.orderdetail.repository.OrderDetailRepository;
import store.mybooks.resource.orderdetailstatus.entity.OrderDetailStatus;
import store.mybooks.resource.orderdetailstatus.repository.OrderDetailStatusRepository;
import store.mybooks.resource.pointhistory.repository.PointHistoryRepository;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.repository.PointRuleRepository;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;
import store.mybooks.resource.review.dto.mapper.ReviewMapper;
import store.mybooks.resource.review.dto.reqeust.ReviewCreateRequest;
import store.mybooks.resource.review.dto.reqeust.ReviewModifyRequest;
import store.mybooks.resource.review.dto.response.ReviewDetailGetResponse;
import store.mybooks.resource.review.dto.response.ReviewGetResponse;
import store.mybooks.resource.review.dto.response.ReviewRateResponse;
import store.mybooks.resource.review.entity.Review;
import store.mybooks.resource.review.exception.ReviewAlreadyExistException;
import store.mybooks.resource.review.exception.ReviewNotExistException;
import store.mybooks.resource.review.repository.ReviewRepository;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;

/**
 * packageName    : store.mybooks.resource.review.service<br>
 * fileName       : ReviewServiceTest<br>
 * author         : masiljangajji<br>
 * date           : 3/24/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/24/24        masiljangajji       최초 생성
 */

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ImageStatusRepository imageStatusRepository;

    @Mock
    ImageService imageService;

    @Mock
    ReviewMapper reviewMapper;

    @Mock
    BookRepository bookRepository;

    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    OrderDetailStatusRepository orderDetailStatusRepository;
    @Mock
    PointHistoryRepository pointHistoryRepository;
    @Mock
    PointRuleNameRepository pointRuleNameRepository;
    @Mock
    BookOrderRepository bookOrderRepository;
    @Mock
    PointRuleRepository pointRuleRepository;

    @InjectMocks
    ReviewService reviewService;


    @Test
    @DisplayName("책의 리뷰 평점 및 개수를 구함")
    void givenBookId_whenCallFindReviewRateByBookId_thenReturnReviewRateResponse(@Mock ReviewRateResponse response) {

        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.getReviewRate(anyLong())).thenReturn(response);
        reviewService.findReviewRateByBookId(1L);
        verify(bookRepository, times(1)).existsById(anyLong());
        verify(reviewRepository, times(1)).getReviewRate(anyLong());

    }

    @Test
    @DisplayName("책의 리뷰 평점 및 개수를 구함 (책이 없어 에러발생)")
    void givenNotExistBookId_whenCallFindReviewRateByBookId_thenThrowBookNotExistException() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(BookNotExistException.class, () -> reviewService.findReviewRateByBookId(anyLong()));
    }

    @Test
    @DisplayName("책의 모든 리뷰를 구함")
    void givenBookIdAndPageable_whenCallFindReviewByBookId_thenReturnReviewDetailGetResponsePage(
            @Mock Pageable pageable, @Mock
    Page<ReviewDetailGetResponse> page) {

        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.getReviewByBookId(anyLong(), any(Pageable.class))).thenReturn(page);
        reviewService.findReviewByBookId(anyLong(), pageable);
        verify(bookRepository, times(1)).existsById(anyLong());
        verify(reviewRepository, times(1)).getReviewByBookId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("책의 모든 리뷰를 구함 (책이 없어 에러발생)")
    void givenNotExistBookIdAndPageable_whenCallFindReviewByBookId_thenThrowBookNotExistException() {

        when(bookRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(BookNotExistException.class, () -> reviewService.findReviewRateByBookId(anyLong()));
    }


    @Test
    @DisplayName("유저의 모든 리뷰를 구함")
    void givenUserIdAndPageable_whenCallFindReviewByBookId_thenReturnReviewGetResponse(@Mock Pageable pageable, @Mock
    Page<ReviewGetResponse> page) {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.getReviewByUserId(anyLong(), any(Pageable.class))).thenReturn(page);
        reviewService.findReviewByUserId(anyLong(), pageable);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(reviewRepository, times(1)).getReviewByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("유저의 모든 리뷰를 구함 (유저가 없어 에러발생)")
    void givenNotExistUserIdAndPageable_whenCallFindReviewByBookId_thenThrowUserNotExistException(
            @Mock Pageable pageable) {

        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(UserNotExistException.class, () -> reviewService.findReviewByUserId(anyLong(), pageable));
    }

    @Test
    @DisplayName("유저가 쓴 특정 리뷰를 구함")
    void givenUserIdAndReviewId_whenCallFindReview_thenReturnReviewGetResponse(@Mock ReviewGetResponse response) {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.getReview(anyLong())).thenReturn(Optional.ofNullable(response));
        reviewService.findReview(1L, 2L);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(reviewRepository, times(1)).getReview(anyLong());
    }


    @Test
    @DisplayName("유저가 쓴 특정 리뷰를 구함 (유저가 없어 에러발생)")
    void givenNotExistUserIdAndReviewId_whenCallFindReview_thenThrowUserNotExistException() {

        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(UserNotExistException.class, () -> reviewService.findReview(1L, 2L));
    }

    @Test
    @DisplayName("유저가 쓴 특정 리뷰를 구함 (리뷰가 없어 에러발생)")
    void givenUserIdAndNotExistReviewId_whenCallFindReview_thenThrowReviewNotExistException() {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.getReview(anyLong())).thenReturn(Optional.empty());
        assertThrows(ReviewNotExistException.class, () -> reviewService.findReview(1L, 2L));
    }

    @Test
    @DisplayName("유저가 쓴 리뷰를 수정함 (사진 수정은 하지않음)")
    void givenReviewModifyRequest_whenCallModifyReview_thenReturnReviewModifyResponse(@Mock Review review, @Mock
    ReviewModifyRequest modifyRequest) throws IOException {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        when(modifyRequest.getRate()).thenReturn(1);
        when(modifyRequest.getTitle()).thenReturn("title");
        when(modifyRequest.getContent()).thenReturn("content");
        reviewService.modifyReview(1L, 1L, modifyRequest, null);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(reviewRepository, times(1)).findById(anyLong());
        verify(modifyRequest, times(1)).getContent();
        verify(modifyRequest, times(1)).getRate();
        verify(modifyRequest, times(1)).getTitle();
    }

    @Test
    @DisplayName("유저가 쓴 리뷰를 수정함 (사진 수정도 함)")
    void givenReviewModifyRequestWithImage_whenCallModifyReview_thenReturnReviewModifyResponse(
            @Mock MultipartFile multipartFile
            , @Mock Review review, @Mock ReviewModifyRequest modifyRequest, @Mock ImageStatus imageStatus,
            @Mock Image image) throws IOException {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        when(modifyRequest.getRate()).thenReturn(1);
        when(modifyRequest.getTitle()).thenReturn("title");
        when(modifyRequest.getContent()).thenReturn("content");
        when(imageStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(imageStatus));
        when(imageService.getReviewImage(anyLong())).thenReturn(Optional.ofNullable(image));
        doNothing().when(imageService).deleteObject(any(Image.class));
        reviewService.modifyReview(1L, 1L, modifyRequest, multipartFile);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(reviewRepository, times(1)).findById(anyLong());
        verify(modifyRequest, times(1)).getContent();
        verify(modifyRequest, times(1)).getRate();
        verify(modifyRequest, times(1)).getTitle();
        verify(imageStatusRepository, times(1)).findById(anyString());
        verify(imageService, times(1)).getReviewImage(anyLong());
        verify(imageService, times(1)).deleteObject(any(Image.class));
    }

    @Test
    @DisplayName("유저가 쓴 리뷰를 수정함 (유저가 없어서 에러)")
    void givenReviewModifyRequestWithNotExistUser_whenCallModifyReview_thenThrowUserNotExistException(
            @Mock MultipartFile multipartFile, @Mock ReviewModifyRequest modifyRequest) throws IOException {

        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(UserNotExistException.class,
                () -> reviewService.modifyReview(1L, 2L, modifyRequest, multipartFile));
    }


    @Test
    @DisplayName("유저가 쓴 리뷰를 수정함 (이미지 상태가 없어 에러)")
    void givenReviewModifyRequestWithNotExistImageStatus_whenCallModifyReview_thenThrowImageStatusNotExistException(
            @Mock MultipartFile multipartFile
            , @Mock Review review, @Mock ReviewModifyRequest modifyRequest) {

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.ofNullable(review));
        when(modifyRequest.getRate()).thenReturn(1);
        when(modifyRequest.getTitle()).thenReturn("title");
        when(modifyRequest.getContent()).thenReturn("content");
        when(imageStatusRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(
                ImageStatusNotExistException.class,
                () -> reviewService.modifyReview(1L, 2L, modifyRequest, multipartFile));
    }


    @Test
    @DisplayName("유저가 쓴 리뷰를 등록 (이미지 없음)")
    void givenReviewCreateRequest_whenCallCreateReview_thenReturnReviewCreateResponse(
            @Mock MultipartFile multipartFile
            , @Mock Review review, @Mock ReviewCreateRequest createRequest, @Mock User user,
            @Mock OrderDetail orderDetail
            , @Mock OrderDetailStatus orderDetailStatus, @Mock BookOrder bookOrder, @Mock PointRuleName pointRuleName
            , @Mock PointRule pointRule) throws IOException {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.ofNullable(orderDetail));
        when(reviewRepository.existsByOrderDetailId(anyLong())).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(orderDetailStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(orderDetailStatus));
        when(bookOrderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookOrder));
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.ofNullable(pointRuleName));
        when(pointRuleName.getId()).thenReturn("pointRule");
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.ofNullable(pointRule));

        reviewService.createReview(createRequest, 1L, null);
        verify(userRepository, times(1)).findById(anyLong());
        verify(orderDetailRepository, times(1)).findById(anyLong());
        verify(reviewRepository, times(1)).existsByOrderDetailId(anyLong());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(orderDetailStatusRepository, times(1)).findById(anyString());
        verify(bookOrderRepository, times(1)).findById(anyLong());
        verify(pointRuleNameRepository, times(1)).findById(anyString());
        verify(pointRuleName, times(1)).getId();
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(anyString());
    }


    @Test
    @DisplayName("유저가 쓴 리뷰를 등록 (이미지 있음)")
    void givenReviewCreateRequestWithImage_whenCallCreateReview_thenReturnReviewCreateResponse(
            @Mock MultipartFile multipartFile
            , @Mock Review review, @Mock ReviewCreateRequest createRequest, @Mock User user,
            @Mock OrderDetail orderDetail
            , @Mock OrderDetailStatus orderDetailStatus, @Mock BookOrder bookOrder, @Mock PointRuleName pointRuleName
            , @Mock PointRule pointRule, @Mock ImageStatus imageStatus,
            @Mock ImageRegisterResponse imageRegisterResponse) throws IOException {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.ofNullable(orderDetail));
        when(reviewRepository.existsByOrderDetailId(anyLong())).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(orderDetailStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(orderDetailStatus));
        when(bookOrderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookOrder));
        when(pointRuleNameRepository.findById(anyString())).thenReturn(Optional.ofNullable(pointRuleName));
        when(pointRuleName.getId()).thenReturn("pointRule");
        when(pointRuleRepository.findPointRuleByPointRuleName(anyString())).thenReturn(Optional.ofNullable(pointRule));
        when(imageStatusRepository.findById(anyString())).thenReturn(Optional.ofNullable(imageStatus));

        reviewService.createReview(createRequest, 1L, multipartFile);
        verify(userRepository, times(1)).findById(anyLong());
        verify(orderDetailRepository, times(1)).findById(anyLong());
        verify(reviewRepository, times(1)).existsByOrderDetailId(anyLong());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(orderDetailStatusRepository, times(1)).findById(anyString());
        verify(bookOrderRepository, times(1)).findById(anyLong());
        verify(pointRuleNameRepository, times(1)).findById(anyString());
        verify(pointRuleName, times(1)).getId();
        verify(pointRuleRepository, times(1)).findPointRuleByPointRuleName(anyString());
        verify(imageStatusRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("유저가 쓴 리뷰를 등록 (주문 상세가 없어 에러)")
    void givenReviewCreateRequestWithOutOrderDetail_whenCallCreateReview_thenThrowOrderDetailNotExistException(
            @Mock MultipartFile multipartFile
            , @Mock Review review, @Mock ReviewCreateRequest createRequest, @Mock User user,
            @Mock OrderDetail orderDetail
            , @Mock OrderDetailStatus orderDetailStatus, @Mock BookOrder bookOrder, @Mock PointRuleName pointRuleName
            , @Mock PointRule pointRule, @Mock ImageStatus imageStatus,
            @Mock ImageRegisterResponse imageRegisterResponse) throws IOException {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                OrderDetailNotExistException.class,
                () -> reviewService.createReview(createRequest, 1L, multipartFile));
    }

    @Test
    @DisplayName("유저가 쓴 리뷰를 등록 (유저가 없어 에러)")
    void givenReviewCreateRequestWithOutUser_whenCallCreateReview_thenThrowUserNotExistException(
            @Mock MultipartFile multipartFile
            , @Mock Review review, @Mock ReviewCreateRequest createRequest, @Mock User user,
            @Mock OrderDetail orderDetail
            , @Mock OrderDetailStatus orderDetailStatus, @Mock BookOrder bookOrder, @Mock PointRuleName pointRuleName
            , @Mock PointRule pointRule, @Mock ImageStatus imageStatus,
            @Mock ImageRegisterResponse imageRegisterResponse) throws IOException {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                UserNotExistException.class,
                () -> reviewService.createReview(createRequest, 1L, multipartFile));
    }

    @Test
    @DisplayName("유저가 쓴 리뷰를 등록 (리뷰가 이미 존재해서 에러)")
    void givenReviewCreateRequestWithDuplicatedReview_whenCallCreateReview_thenThrowReviewAlreadyExistException(
            @Mock MultipartFile multipartFile, @Mock ReviewCreateRequest createRequest, @Mock User user,
            @Mock OrderDetail orderDetail) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(orderDetail));
        when(reviewRepository.existsByOrderDetailId(anyLong())).thenReturn(true);

        assertThrows(
                ReviewAlreadyExistException.class,
                () -> reviewService.createReview(createRequest, 1L, multipartFile));
    }


}