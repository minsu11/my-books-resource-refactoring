package store.mybooks.resource.wrap.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.error.RequestValidationFailedException;
import store.mybooks.resource.wrap.dto.request.WrapCreateRequest;
import store.mybooks.resource.wrap.dto.request.WrapModifyRequest;
import store.mybooks.resource.wrap.dto.response.WrapCreateResponse;
import store.mybooks.resource.wrap.dto.response.WrapModifyResponse;
import store.mybooks.resource.wrap.dto.response.WrapPageResponse;
import store.mybooks.resource.wrap.dto.response.WrapResponse;
import store.mybooks.resource.wrap.service.WrapService;

/**
 * packageName    : store.mybooks.resource.wrap.controller<br>
 * fileName       : WrapRestController<br>
 * author         : minsu11<br>
 * date           : 2/27/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/27/24        minsu11       최초 생성<br>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wraps")
public class WrapRestController {
    private final WrapService wrapService;

    /**
     * methodName : getWrapResponse<br>
     * author : minsu11<br>
     * description : id로 포장지 조회
     * <br> *
     *
     * @param id 조회할 포장지의 {@code id}
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<WrapResponse> getWrapResponseById(@PathVariable Integer id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wrapService.getWrapById(id));
    }

    /**
     * methodName : getWrapResponsePage<br>
     * author : minsu11<br>
     * description : {@code pagination}된 포장지의 목록 조회
     * <br> *
     *
     * @param pageable 페이징 처리
     * @return response entity
     */
    @GetMapping("/page")
    public ResponseEntity<Page<WrapPageResponse>> getWrapResponsePage(Pageable pageable) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wrapService.getWrapPage(pageable));
    }

    /**
     * methodName : getWrapResponseList<br>
     * author : minsu11<br>
     * description : 전체 포장지의 목록 반환
     * <br> *
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<WrapResponse>> getWrapResponseList() {
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(wrapService.getWrapResponseList());
    }

    /**
     * methodName : createWrap<br>
     * author : minsu11<br>
     * description : {@code post} 요청 시 등록할 포장지를 등록.
     * 등록할 포장지가 유효성 검사에 실패 할 경우 {@code WrapValidationFailedNo}
     * <br> *
     *
     * @param wrapCreateRequest
     * @return response entity
     * @throws RequestValidationFailedException 등록할 포장지 유효성 실패 시
     */
    @PostMapping
    public ResponseEntity<WrapCreateResponse> createWrap(@Valid @RequestBody WrapCreateRequest wrapCreateRequest,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrapService.createWrap(wrapCreateRequest));
    }

    /**
     * methodName : modifyWrap<br>
     * author : minsu11<br>
     * description : {@code put} 요청 시 id의 포장지 정보를 수정 할 포장지 정보로 수정.
     * 수정 할 포장지 정보가 유효성 검사 실패 시 {@code WrapValidationFailedException}던짐
     * <br> *
     *
     * @param id
     * @param wrapModifyRequest
     * @param bindingResult
     * @return response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<WrapModifyResponse> modifyWrap(@PathVariable Integer id,
                                                         @Valid @RequestBody WrapModifyRequest wrapModifyRequest,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(wrapService.modifyWrap(wrapModifyRequest, id));
    }

    /**
     * methodName : deleteWrap<br>
     * author : minsu11<br>
     * description : 포장지 삭제
     * <br> *
     *
     * @param id 삭제할 포장지 아이디
     * @return response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWrap(@PathVariable Integer id) {
        wrapService.deleteWrap(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();


    }


}
