package store.mybooks.resource.bookorder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.bookorder.dto.request.BookOrderAdminModifyRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderRegisterInvoiceRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderRegisterInvoiceResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminModifyResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.service.BookOrderService;
import store.mybooks.resource.bookorder.service.OrderService;
import store.mybooks.resource.config.HeaderProperties;

/**
 * packageName    : store.mybooks.resource.book_order.controller<br>
 * fileName       : BookOrderRestController<br>
 * author         : minsu11<br>
 * date           : 3/2/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 3/2/24        minsu11       최초 생성<br>
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class BookOrderRestController {
    private final BookOrderService bookOrderService;
    private final OrderService orderService;

    /**
     * methodName : getBookOrderPageById<br>
     * author : minsu11<br>
     * description : 회원 본인의 주문 내역.
     * <br> *
     *
     * @param id
     * @param pageable
     * @return response entity
     */

    @GetMapping("/users/{id}")
    public ResponseEntity<Page<BookOrderUserResponse>> getBookOrderPageById(@PathVariable Long id, Pageable pageable
    ) {
        Page<BookOrderUserResponse> bookOrderResponses = bookOrderService.getBookOrderResponseList(id, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderResponses);
    }

    /**
     * methodName : getBookOrderPageByStatusId<br>
     * author : minsu11<br>
     * description : 관리자가 보는 주문 내역.
     * <br> *
     *
     * @param pageable
     * @return response entity
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<BookOrderAdminResponse>> getBookOrderPageByStatusId(Pageable pageable) {
        Page<BookOrderAdminResponse> bookOrderAdminResponses = bookOrderService.getBookOrderAdminResponseList(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderAdminResponses);
    }


    @PutMapping("/admin/statuses")
    public ResponseEntity<BookOrderAdminModifyResponse> modifyOrderStatus(@RequestBody BookOrderAdminModifyRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.modifyBookOrderAdminStatus(request));
    }

    @PutMapping("/admin/invoiceNumbers")
    public ResponseEntity<BookOrderRegisterInvoiceResponse> registerInvoiceNumber(@RequestBody BookOrderRegisterInvoiceRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.registerBookOrderInvoiceNumber(request));
    }

    @GetMapping("/check/address/{id}")
    public ResponseEntity<Object> checkOrderUserAddress(@PathVariable(name = "id") Long id) {
        bookOrderService.checkUserOrderAddress(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();

    }

    @GetMapping("/orderNumber/{orderNumber}")
    public ResponseEntity<Boolean> checkBookOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.checkBookOrderNumberExists(orderNumber));
    }

    /**
     * 주문 요청이 들어오면 주문 생성.
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<BookOrderCreateResponse> createResponseResponseEntity(
            @RequestBody BookOrderCreateRequest request,
            @RequestHeader(name = HeaderProperties.USER_ID) Long id) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, id));
    }

    @GetMapping("/info/pay/{orderNumber}")
    public ResponseEntity<BookOrderInfoPayResponse> getBookOrderInfo(
            @PathVariable String orderNumber) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.getBookInfo(orderNumber));
    }

}
