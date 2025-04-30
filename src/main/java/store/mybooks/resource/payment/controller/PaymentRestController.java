package store.mybooks.resource.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.bookorder.service.TotalOrderService;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.Utils;
import store.mybooks.resource.payment.dto.request.PayCancelRequest;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
import store.mybooks.resource.payment.dto.response.PaymentResponse;
import store.mybooks.resource.payment.service.PaymentService;

/**
 * packageName    : store.mybooks.resource.payment.controller<br>
 * fileName       : PaymentRestController<br>
 * author         : minsu11<br>
 * date           : 3/19/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/19/24        minsu11       최초 생성<br>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pays")
public class PaymentRestController {
    private final PaymentService paymentService;
    private final TotalOrderService totalOrderService;

    /**
     * Create payment response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/non/user")
    public ResponseEntity<PayCreateResponse> createPayment(@Valid @RequestBody PayCreateRequest request,
                                                           BindingResult bindingResult
    ) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(totalOrderService.payUser(request, 0L));

    }

    /**
     * Pay response entity.
     *
     * @param request the request
     * @param userId  the user id
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<PayCreateResponse> pay(@Valid @RequestBody PayCreateRequest request,
                                                 @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
                                                 BindingResult bindingResult) {

        Utils.validateRequest(bindingResult);

        PayCreateResponse response = totalOrderService.payUser(request, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Gets payment key.
     *
     * @param orderNumber 주문 번호
     * @return the payment key
     */
    @GetMapping("/{orderNumber}")
    public ResponseEntity<PaymentResponse> getPaymentKey(@PathVariable String orderNumber) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(paymentService.getPaymentKey(orderNumber));

    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelProcessing(@RequestBody PayCancelRequest request,
                                                 @RequestHeader(name = HeaderProperties.USER_ID) Long userId) {

        totalOrderService.cancelOrderProcess(request, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
