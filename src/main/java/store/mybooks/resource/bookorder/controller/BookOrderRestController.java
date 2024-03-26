package store.mybooks.resource.bookorder.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.bookorder.dto.request.BookOrderAdminModifyRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderCreateRequest;
import store.mybooks.resource.bookorder.dto.request.BookOrderRegisterInvoiceRequest;
import store.mybooks.resource.bookorder.dto.response.BookOrderCreateResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderInfoPayResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderPaymentInfoRespones;
import store.mybooks.resource.bookorder.dto.response.BookOrderRegisterInvoiceResponse;
import store.mybooks.resource.bookorder.dto.response.BookOrderUserResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminModifyResponse;
import store.mybooks.resource.bookorder.dto.response.admin.BookOrderAdminResponse;
import store.mybooks.resource.bookorder.service.BookOrderService;
import store.mybooks.resource.bookorder.service.TotalOrderService;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.error.Utils;

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
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class BookOrderRestController {
    private final BookOrderService bookOrderService;
    private final TotalOrderService orderService;

    /**
     * methodName : getBookOrderPageById<br>
     * author : minsu11<br>
     * description : 회원 본인의 주문 내역.
     * <br> *
     *
     * @param id       조회할 아이디
     * @param pageable 페이징
     * @return response entity
     */

    @GetMapping("/users")
    public ResponseEntity<Page<BookOrderUserResponse>> getBookOrderPageById(
            Pageable pageable, @RequestHeader(name = HeaderProperties.USER_ID) Long id) {
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
     * @param pageable 페이징
     * @return response entity
     */
    @GetMapping("/admin")
    public ResponseEntity<Page<BookOrderAdminResponse>> getBookOrderPageByStatusId(@PageableDefault Pageable pageable) {
        Page<BookOrderAdminResponse> bookOrderAdminResponses = bookOrderService.getBookOrderAdminResponseList(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderAdminResponses);
    }

    /**
     * methodName : modifyOrderStatus<br>
     * author : minsu11<br>
     * description : 관리자가 회원의 주문 상태를 배송 중으로 변경하는 메서드.
     * <br> *
     *
     * @param request 변경할 DTO
     * @return response entity
     */
    @PutMapping("/statuses")
    public ResponseEntity<BookOrderAdminModifyResponse> modifyOrderStatus(
            @Valid @RequestBody BookOrderAdminModifyRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.modifyBookOrderAdminStatus(request));
    }

    /**
     * methodName : registerInvoiceNumber<br>
     * author : minsu11<br>
     * description : 관리자가 송장 번호를 입력.
     * <br> *
     *
     * @param request 송장 번호 DTO
     * @return response entity
     */
    @PutMapping("/invoiceNumbers")
    public ResponseEntity<BookOrderRegisterInvoiceResponse> registerInvoiceNumber(
            @Valid @RequestBody BookOrderRegisterInvoiceRequest request,
            BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.registerBookOrderInvoiceNumber(request));
    }

    /**
     * methodName : checkOrderUserAddress<br>
     * author : minsu11<br>
     * description : 주소가 있는지 확인.
     * <br> *
     *
     * @param id 주소 아이디
     * @return response entity
     */
    @GetMapping("/check/address/{id}")
    public ResponseEntity<Boolean> checkOrderUserAddress(@PathVariable(name = "id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.checkUserOrderAddress(id));

    }

    /**
     * methodName : checkBookOrderNumber<br>
     * author : minsu11<br>
     * description : 주문 번호가 있는지 확인.
     * <br> *
     *
     * @param orderNumber 주문 번호
     * @return response entity
     */
    @GetMapping("/orderNumber/{orderNumber}")
    public ResponseEntity<Boolean> checkBookOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.checkBookOrderNumberExists(orderNumber));
    }

    /**
     * methodName : createResponseResponseEntity<br>
     * author : minsu11<br>
     * description : 주문 요청이 들어오면 주문 생성.
     * <br>
     *
     * @param request the request
     * @param id      the id
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<BookOrderCreateResponse> createResponseResponseEntity(
            @Valid @RequestBody BookOrderCreateRequest request,
            BindingResult bindingResult,
            @RequestHeader(name = HeaderProperties.USER_ID) Long id) {
        Utils.validateRequest(bindingResult);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, id));
    }

    /**
     * methodName : createNonUserOrderResponseResponseEntity<br>
     * author : minsu11<br>
     * description : 비회원 주문 생성.
     * <br>
     *
     * @param request 생성할 주문
     * @return the response entity
     */
    @PostMapping("/non/user")
    public ResponseEntity<BookOrderCreateResponse> createNonUserOrderResponseResponseEntity(
            @RequestBody BookOrderCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, 0L));
    }

    /**
     * methodName : getBookOrderInfo<br>
     * author : minsu11<br>
     * description : 주문 창에서 결제 창으로 넘어갈 때 필요한 정보를 조회.
     * <br> *
     *
     * @param orderNumber 주문 번호
     * @return response entity
     */
    @GetMapping("/info/pay/{orderNumber}")
    public ResponseEntity<BookOrderInfoPayResponse> getBookOrderInfo(
            @PathVariable String orderNumber) {
        BookOrderInfoPayResponse response = bookOrderService.getBookInfo(orderNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * methodName : getBookOrderPageByStatusId<br>
     * author : minsu11<br>
     * description : 결제 창에서 js로 서버에 호출해서 결제 관련 정보를 받음.
     * <br> *
     *
     * @param orderNumber 주문 번호
     * @return response entity
     */
    @GetMapping("/info/{orderNumber}/pay")
    public ResponseEntity<BookOrderPaymentInfoRespones> getBookOrderPay(@PathVariable String orderNumber) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.getOrderInfoPayment(orderNumber));
    }


}
