package store.mybooks.resource.review.controller;

import java.io.IOException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.Utils;
import store.mybooks.resource.review.dto.reqeust.ReviewCreateRequest;
import store.mybooks.resource.review.dto.reqeust.ReviewModifyRequest;
import store.mybooks.resource.review.dto.response.ReviewCreateResponse;
import store.mybooks.resource.review.dto.response.ReviewDetailGetResponse;
import store.mybooks.resource.review.dto.response.ReviewGetResponse;
import store.mybooks.resource.review.dto.response.ReviewModifyResponse;
import store.mybooks.resource.review.dto.response.ReviewRateResponse;
import store.mybooks.resource.review.service.ReviewService;

/**
 * packageName    : store.mybooks.resource.review.controller<br>
 * fileName       : ReviewController<br>
 * author         : masiljangajji<br>
 * date           : 3/17/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 3/17/24        masiljangajji       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    /**
     * methodName : modifyReview
     * author : masiljangajji
     * description : 리뷰를 수정 함
     *
     * @param reviewId      id
     * @param modifyRequest 타이틀 , 본문 , 별점
     * @param bindingResult result
     * @param userId        userId
     * @param image         이미지파일
     * @return response entity
     * @throws IOException the io exception
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewModifyResponse> modifyReview(@PathVariable(name = "reviewId") Long reviewId
            , @Valid @RequestPart("request") ReviewModifyRequest modifyRequest, BindingResult bindingResult
            , @RequestHeader(HeaderProperties.USER_ID) Long userId
            , @RequestPart(value = "contentImage", required = false) MultipartFile image) throws IOException {

        Utils.validateRequest(bindingResult);
        return new ResponseEntity<>(reviewService.modifyReview(userId, reviewId, modifyRequest, image), HttpStatus.OK);
    }

    /**
     * methodName : createReview
     * author : masiljangajji
     * description : 리뷰를 생성함
     *
     * @param userId        id
     * @param createRequest 타이틀 ,본문,별점
     * @param bindingResult result
     * @param image         이미지 파일
     * @return response entity
     * @throws IOException the io exception
     */
    @PostMapping
    public ResponseEntity<ReviewCreateResponse> createReview(@RequestHeader(HeaderProperties.USER_ID) Long userId,
                                                             @Valid @RequestPart("request")
                                                             ReviewCreateRequest createRequest,
                                                             BindingResult bindingResult,
                                                             @RequestPart(value = "contentImage", required = false)
                                                             MultipartFile image)
            throws IOException {


        Utils.validateRequest(bindingResult);
        return new ResponseEntity<>(reviewService.createReview(createRequest, userId, image), HttpStatus.CREATED);
    }


    /**
     * methodName : findReview
     * author : masiljangajji
     * description : 유저가 작성한 특정 리뷰를 찾음
     *
     * @param userId   userId
     * @param reviewId reviewId
     * @return response entity
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewGetResponse> findReview(@RequestHeader(HeaderProperties.USER_ID) Long userId,
                                                        @PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.findReview(userId, reviewId), HttpStatus.OK);
    }

    /**
     * methodName : findReviewByUserId
     * author : masiljangajji
     * description : 유저가 작성한 모든 리뷰를 찾음
     *
     * @param userId   id
     * @param pageable pageable
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<Page<ReviewGetResponse>> findReviewByUserId(
            @RequestHeader(HeaderProperties.USER_ID) Long userId,
            Pageable pageable) {

        return new ResponseEntity<>(reviewService.findReviewByUserId(userId, pageable), HttpStatus.OK);
    }

    /**
     * methodName : findReviewByBookId
     * author : masiljangajji
     * description : 책의 모든 리뷰를 찾음
     *
     * @param bookId   id
     * @param pageable pageable
     * @return response entity
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<ReviewDetailGetResponse>> findReviewByBookId(@PathVariable(name = "bookId") Long bookId,
                                                                            Pageable pageable) {
        return new ResponseEntity<>(reviewService.findReviewByBookId(bookId, pageable), HttpStatus.OK);
    }

    /**
     * methodName : findReviewRateByBookId
     * author : masiljangajji
     * description : 책에 달린 리뷰의 별점 평균 및 총 개수를 반환
     *
     * @param bookId id
     * @return response entity
     */
    @GetMapping("/book/{bookId}/rate")
    public ResponseEntity<ReviewRateResponse> findReviewRateByBookId(@PathVariable(name = "bookId") Long bookId) {
        return new ResponseEntity<>(reviewService.findReviewRateByBookId(bookId), HttpStatus.OK);
    }


}
