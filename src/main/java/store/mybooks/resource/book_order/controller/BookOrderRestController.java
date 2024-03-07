package store.mybooks.resource.book_order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.book_order.dto.request.BookOrderAdminModifyRequest;
import store.mybooks.resource.book_order.dto.request.BookOrderRegisterInvoiceRequest;
import store.mybooks.resource.book_order.dto.response.BookOrderAdminModifyResponse;
import store.mybooks.resource.book_order.dto.response.BookOrderAdminResponse;
import store.mybooks.resource.book_order.dto.response.BookOrderRegisterInvoiceResponse;
import store.mybooks.resource.book_order.dto.response.BookOrderUserResponse;
import store.mybooks.resource.book_order.service.BookOrderService;

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

    @PutMapping("/admin/status")
    public ResponseEntity<BookOrderAdminModifyResponse> modifyOrderStatus(@RequestBody BookOrderAdminModifyRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.modifyBookOrderAdminStatus(request));
    }

    @PutMapping("/admin/invoiceNumber")
    public ResponseEntity<BookOrderRegisterInvoiceResponse> registerInvoiceNumber(@RequestBody BookOrderRegisterInvoiceRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookOrderService.registerBookOrderInvoiceNumber(request));
    }
}
