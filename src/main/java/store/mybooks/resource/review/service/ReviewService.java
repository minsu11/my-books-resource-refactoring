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
import store.mybooks.resource.pointhistory.service.PointHistoryService;
import store.mybooks.resource.pointrule.entity.PointRule;
import store.mybooks.resource.pointrule.exception.PointRuleNotExistException;
import store.mybooks.resource.pointrule.repository.PointRuleRepository;
import store.mybooks.resource.pointrule.service.PointRuleService;
import store.mybooks.resource.pointrulename.entity.PointRuleName;
import store.mybooks.resource.pointrulename.enumulation.PointRuleNameEnum;
import store.mybooks.resource.pointrulename.exception.PointRuleNameNotExistException;
import store.mybooks.resource.pointrulename.repository.PointRuleNameRepository;
import store.mybooks.resource.pointrulename.service.PointRuleNameService;
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
    private final PointHistoryService pointHistoryService;

    private final PointRuleService pointRuleService;

    private final PointRuleNameService pointRuleNameService;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest createRequest, Long userId,
                                             MultipartFile image)
            throws IOException {

        System.out.println("오냐??");
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));

        OrderDetail orderDetail = orderDetailRepository.findById(createRequest.getOrderDetailId())
                .orElseThrow(() -> new OrderDetailNotExistException(createRequest.getOrderDetailId()));

        System.out.println("있냐??");
        // 리뷰 이미 존재하는지 확인
        if (reviewRepository.existsByOrderDetailId(orderDetail.getId())) {
            throw new ReviewAlreadyExistException(createRequest.getOrderDetailId());
        }

        System.out.println("44444444444");
        Review review = new Review(user, orderDetail, createRequest.getRate(), createRequest.getTitle(),
                createRequest.getContent());

        Review resultReview = reviewRepository.save(review);

        System.out.println("*******");

        System.out.println(OrderDetailStatusName.PURCHASE_CONFIRMATION);



        OrderDetailStatus orderDetailStatus =
                orderDetailStatusRepository.findById(OrderDetailStatusName.PURCHASE_CONFIRMATION.getValue()).orElseThrow(
                        OrderDetailStatusNotFoundException::new);

        System.out.println(orderDetailStatus.getId());

        System.out.println("55555555");
        System.out.println(orderDetail.getId());
        orderDetail.setDetailStatus(orderDetailStatus);
        System.out.println(orderDetail.getDetailStatus().getId());
        System.out.println("어째서??????");
        orderDetailRepository.save(orderDetail);


        PointHistory pointHistory;
        PointRuleName pointRuleName;
        PointRule pointRule;

        System.out.println("666666");

        BookOrder bookOrder = bookOrderRepository.findById(createRequest.getOrderId()).orElseThrow(
                BookOrderNotExistException::new);

        System.out.println("77777");
        if (Objects.nonNull(image)) {

            pointRuleName=pointRuleNameRepository.findById(PointRuleNameEnum.REVIEW_IMAGE_POINT.getValue())
                    .orElseThrow(PointRuleNameNotExistException::new);

            pointRule=pointRuleRepository.findPointRuleByPointRuleName(pointRuleName.getId()).orElseThrow(
                    PointRuleNotExistException::new);

            ImageStatus imageStatus = imageStatusRepository.findById(ImageStatusEnum.REVIEW.getName()).orElseThrow(
                    () -> new ImageStatusNotExistException("리뷰 이미지 상태 없음."));

            pointHistory = new PointHistory(pointRule.getCost(), user, pointRule, bookOrder);
            imageService.saveImage(imageStatus, resultReview, null, image);
        } else {

            pointRuleName=pointRuleNameRepository.findById(PointRuleNameEnum.REVIEW_POINT.getValue())
                    .orElseThrow(PointRuleNameNotExistException::new);

            pointRule = pointRuleRepository.findPointRuleByPointRuleName(pointRuleName.getId()).orElseThrow(
                    PointRuleNotExistException::new);

            pointHistory = new PointHistory(pointRule.getCost(), user, pointRule, bookOrder);
        }

        pointHistoryRepository.save(pointHistory);

        System.out.println("메무리~~");
        return reviewMapper.toReviewCreateResponse(resultReview);
    }

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

            Image image = imageService.getReviewImage(reviewId);

            imageService.deleteObject(image.getId());
            imageService.saveImage(imageStatus, review, null, modifyImage);
        }

        return reviewMapper.toReviewModifyResponse(review);
    }

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

    public Page<ReviewGetResponse> findReviewByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
        return reviewRepository.getReviewByUserId(userId, pageable);
    }

    public Page<ReviewDetailGetResponse> findReviewByBookId(Long bookId, Pageable pageable) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }
        return reviewRepository.getReviewByBookId(bookId, pageable);
    }

    public ReviewRateResponse findReviewRateByBookId(Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotExistException(bookId);
        }

        return reviewRepository.getReviewRate(bookId);
    }


}
