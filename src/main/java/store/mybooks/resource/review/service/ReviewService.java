package store.mybooks.resource.review.service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.exception.BookNotExistException;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.bookorder.entity.BookOrder;
import store.mybooks.resource.bookorder.exception.BookOrderNotExistException;
import store.mybooks.resource.bookorder.repository.BookOrderRepository;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.service.ImageService;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.image_status.exception.ImageStatusNotExistException;
import store.mybooks.resource.image_status.repository.ImageStatusRepository;
import store.mybooks.resource.orderdetail.entity.OrderDetail;
import store.mybooks.resource.orderdetail.enumulation.OrderDetailStatusName;
import store.mybooks.resource.orderdetail.exception.OrderDetailNotExistException;
import store.mybooks.resource.orderdetail.repository.OrderDetailRepository;
import store.mybooks.resource.orderdetailstatus.entity.OrderDetailStatus;
import store.mybooks.resource.orderdetailstatus.exception.OrderDetailStatusNotFoundException;
import store.mybooks.resource.orderdetailstatus.repository.OrderDetailStatusRepository;
import store.mybooks.resource.pointhistory.entity.PointHistory;
import store.mybooks.resource.pointhistory.repository.PointHistoryRepository;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.exception.PointRuleNotExistException;
import store.mybooks.resource.pointrule.repository.PointRuleRepository;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;
import store.mybooks.resource.pointrulename.exception.PointRuleNameNotExistException;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;
import store.mybooks.resource.review.dto.mapper.ReviewMapper;
import store.mybooks.resource.review.dto.reqeust.ReviewCreateRequest;
import store.mybooks.resource.review.dto.reqeust.ReviewModifyRequest;
import store.mybooks.resource.review.dto.response.ReviewCreateResponse;
import store.mybooks.resource.review.dto.response.ReviewDetailGetResponse;
import store.mybooks.resource.review.dto.response.ReviewGetResponse;
import store.mybooks.resource.review.dto.response.ReviewModifyResponse;
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
 * fileName       : ReviewService<br>
 * author         : masiljangajji<br>
 * date           : 3/17/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/17/24        masiljangajji       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final ImageStatusRepository imageStatusRepository;

    private final ImageService imageService;

    private final ReviewMapper reviewMapper;

    private final BookRepository bookRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final OrderDetailStatusRepository orderDetailStatusRepository;

    private final PointHistoryRepository pointHistoryRepository;

    private final PointRuleNameRepository pointRuleNameRepository;

    private final BookOrderRepository bookOrderRepository;

    private final PointRuleRepository pointRuleRepository;

    /**
     * methodName : createReview
     * author : masiljangajji
     * description : 리뷰를 생성함
     * 리뷰를 작성시 주문상세의 상태를 "구매확정" 으로 변경 후 포인트를 적립함
     *
     * @param createRequest 주문 , 주문디테일 아이디 , 제목 본문 별점
     * @param userId        id
     * @param image         이미지파일
     * @return review create response
     * @throws IOException                        the io exception
     * @throws OrderDetailNotExistException       주문상세가 존재하지 않는 경우
     * @throws ReviewAlreadyExistException        해당 주문상세에 대해서 리뷰가 존재하는 경우
     * @throws OrderDetailStatusNotFoundException 주문상세 상태가 존재하지 않는 경우
     * @throws BookOrderNotExistException         주문이 존재하지 않는 경우
     * @throws PointRuleNotExistException         리뷰 포인트 규정이 존재하지 않는 경우
     * @throws PointRuleNameNotExistException     리뷰 포인트 규정 이름이 존재하지 않는 경우
     * @throws ImageStatusNotExistException       리뷰 이미지 상태가 존재하지 않는 경우
     */
    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest createRequest, Long userId,
                                             MultipartFile image)
            throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));

        OrderDetail orderDetail = orderDetailRepository.findById(createRequest.getOrderDetailId())
                .orElseThrow(() -> new OrderDetailNotExistException(createRequest.getOrderDetailId()));

        // 리뷰 이미 존재하는지 확인
        if (reviewRepository.existsByOrderDetailId(orderDetail.getId())) {
            throw new ReviewAlreadyExistException(createRequest.getOrderDetailId());
        }

        Review review = new Review(user, orderDetail, createRequest.getRate(), createRequest.getTitle(),
                createRequest.getContent());

        Review resultReview = reviewRepository.save(review);


        OrderDetailStatus orderDetailStatus =
                orderDetailStatusRepository.findById(OrderDetailStatusName.PURCHASE_CONFIRMATION.getValue())
                        .orElseThrow(
                                OrderDetailStatusNotFoundException::new);

        orderDetail.setDetailStatus(orderDetailStatus);

        PointHistory pointHistory;
        PointRuleName pointRuleName;
        PointRule pointRule;

        BookOrder bookOrder = bookOrderRepository.findById(createRequest.getOrderId()).orElseThrow(
                BookOrderNotExistException::new);

        if (Objects.nonNull(image)) {

            pointRuleName = pointRuleNameRepository.findById(PointRuleNameEnum.REVIEW_IMAGE_POINT.getValue())
                    .orElseThrow(PointRuleNameNotExistException::new);

            pointRule = pointRuleRepository.findPointRuleByPointRuleName(pointRuleName.getId()).orElseThrow(
                    PointRuleNotExistException::new);

            ImageStatus imageStatus = imageStatusRepository.findById(ImageStatusEnum.REVIEW.getName()).orElseThrow(
                    () -> new ImageStatusNotExistException("리뷰 이미지 상태 없음."));

            pointHistory = new PointHistory(pointRule.getCost(), user, pointRule, bookOrder);
            imageService.saveImage(imageStatus, resultReview, null, image);
        } else {

            pointRuleName = pointRuleNameRepository.findById(PointRuleNameEnum.REVIEW_POINT.getValue())
                    .orElseThrow(PointRuleNameNotExistException::new);

            pointRule = pointRuleRepository.findPointRuleByPointRuleName(pointRuleName.getId()).orElseThrow(
                    PointRuleNotExistException::new);

            pointHistory = new PointHistory(pointRule.getCost(), user, pointRule, bookOrder);
        }

        pointHistoryRepository.save(pointHistory);
        return reviewMapper.toReviewCreateResponse(resultReview);
    }

    /**
     * methodName : modifyReview
     * author : masiljangajji
     * description : 리뷰 수정
     *
     * @param userId        id
     * @param reviewId      id
     * @param modifyRequest 제목 본문 별점
     * @param modifyImage   image
     * @return review modify response
     * @throws ImageStatusNotExistException 리뷰 이미지 상태가 존재하지 않는 경우
     * @throws IOException                  the io exception
     */
    @Transactional
    public ReviewModifyResponse modifyReview(Long userId, Long reviewId, ReviewModifyRequest modifyRequest,
                                             MultipartFile modifyImage)
            throws IOException {

        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotExistException(reviewId));
        review.modifyReview(modifyRequest.getRate(), modifyRequest.getTitle(), modifyRequest.getContent());

        if (Objects.nonNull(modifyImage)) {
            ImageStatus imageStatus = imageStatusRepository.findById(ImageStatusEnum.REVIEW.getName()).orElseThrow(
                    () -> new ImageStatusNotExistException("리뷰 이미지 상태 없음."));

            Optional<Image> image = imageService.getReviewImage(reviewId);


            image.ifPresent(imageService::deleteObject);

            imageService.saveImage(imageStatus, review, null, modifyImage);
        }

        return reviewMapper.toReviewModifyResponse(review);
    }

    /**
     * methodName : findReview
     * author : masiljangajji
     * description : 유저가 작성한 특정 리뷰를 반환
     *
     * @param userId   id
     * @param reviewId id
     * @return review get response
     * @throws UserNotExistException   유저가 존재하지 않음
     * @throws ReviewNotExistException 리뷰가 존재하지 않음
     */
    public ReviewGetResponse findReview(Long userId, Long reviewId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }

        Optional<ReviewGetResponse> response = reviewRepository.getReview(reviewId);

        if (response.isEmpty()) {
            throw new ReviewNotExistException(reviewId);
        }

        return response.get();
    }

    /**
     * methodName : findReviewByUserId
     * author : masiljangajji
     * description : 유저가 작성한 모든 리뷰를 반환
     *
     * @param userId   id
     * @param pageable pageable
     * @return page
     * @throws UserNotExistException 유저가 존재하지 않음
     */
    public Page<ReviewGetResponse> findReviewByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
        return reviewRepository.getReviewByUserId(userId, pageable);
    }

    /**
     * methodName : findReviewByBookId
     * author : masiljangajji
     * description : 책의 모든 리뷰를 반환
     *
     * @param bookId   id
     * @param pageable pageable
     * @return page
     * @throws BookNotExistException 책이 존재하지 않음
     */
    public Page<ReviewDetailGetResponse> findReviewByBookId(Long bookId, Pageable pageable) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }
        return reviewRepository.getReviewByBookId(bookId, pageable);
    }

    /**
     * methodName : findReviewRateByBookId
     * author : masiljangajji
     * description : 책의 리뷰 별점 및 총 리뷰 개수를 반환
     *
     * @param bookId id
     * @return review rate response
     * @throws BookNotExistException 책이 존재하지 않음
     */
    public ReviewRateResponse findReviewRateByBookId(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }

        return reviewRepository.getReviewRate(bookId);
    }


}
