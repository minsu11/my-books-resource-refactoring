package store.mybooks.resource.payment.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.bookorder.service.TotalOrderService;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.payment.dto.request.PayCreateRequest;
import store.mybooks.resource.payment.dto.response.PayCreateResponse;
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

//    @PostMapping
//    public ResponseEntity<PayCreateResponse> createPayment(@Valid @RequestBody PayCreateRequest request
//    ) {
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(paymentService.createPayment(request));
//
//    }

    @PostMapping
    public ResponseEntity<PayCreateResponse> pay(@Valid @RequestBody PayCreateRequest request,
                                                 @RequestHeader(name = HeaderProperties.USER_ID) Long userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(totalOrderService.payUser(request, userId));
    }


}
