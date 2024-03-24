package store.mybooks.resource.user_address.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.config.HeaderProperties;
import store.mybooks.resource.error.Utils;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
import store.mybooks.resource.user_address.dto.request.UserAddressModifyRequest;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressDeleteResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.service.UserAddressService;

/**
 * packageName    : store.mybooks.resource.user_address.controller<br>
 * fileName       : UserAddressRestController<br>
 * author         : masiljangajji<br>
 * date           : 2/19/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/addresses")
public class UserAddressRestController {

    private final UserAddressService userAddressService;

    /**
     * methodName : createUserAddress
     * author : masiljangajji
     * description : 유저의 주소를 등록함
     *
     * @param userId        id
     * @param createRequest request
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<UserAddressCreateResponse> createUserAddress(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @Valid @RequestBody UserAddressCreateRequest createRequest, BindingResult bindingResult) {

        Utils.validateRequest(bindingResult);

        UserAddressCreateResponse createResponse =
                userAddressService.createUserAddress(userId, createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }


    /**
     * methodName : modifyUserAddress
     * author : masiljangajji
     * description : 유저의 주소를 변경함 (별명,상세주소)
     *
     * @param userId        id
     * @param addressId     id
     * @param modifyRequest request
     * @return response entity
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressModifyResponse> modifyUserAddress(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @PathVariable(name = "addressId") Long addressId,
            @Valid @RequestBody UserAddressModifyRequest modifyRequest,BindingResult bindingResult) {
        Utils.validateRequest(bindingResult);

        UserAddressModifyResponse modifyResponse =
                userAddressService.modifyUserAddress(userId, addressId, modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    /**
     * methodName : deleteUserAddress
     * author : masiljangajji
     * description : 유저의 주소를 삭제함
     *
     * @param userId    id
     * @param addressId id
     * @return response entity
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<UserAddressDeleteResponse> deleteUserAddress(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @PathVariable(name = "addressId") Long addressId) {

        UserAddressDeleteResponse deleteResponse =
                userAddressService.deleteUserAddress(userId, addressId);

        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }


    /**
     * methodName : findUserAddressByAddressId
     * author : masiljangajji
     * description : 유저의 특정 주소를 찾음
     *
     * @param userId    id
     * @param addressId id
     * @return response entity
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<UserAddressGetResponse> findUserAddressByAddressId(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId,
            @PathVariable(name = "addressId") Long addressId) {

        UserAddressGetResponse getResponse =
                userAddressService.findByAddressId(userId, addressId);
        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    /**
     * methodName : findAllUserAddress
     * author : masiljangajji
     * description : 모든 유저의 주소를 Pagination 처리
     *
     * @param pageable pageable
     * @return response entity
     */
    @GetMapping("/all")
    public ResponseEntity<Page<UserAddressGetResponse>> findAllUserAddress(Pageable pageable) {

        Page<UserAddressGetResponse> paginationUserAddress = userAddressService.findByAllUserAddress(pageable);
        return new ResponseEntity<>(paginationUserAddress, HttpStatus.OK);
    }

    /**
     * methodName : findAllAddressByUserId
     * author : masiljangajji
     * description : 유저의 모든 주소를 찾음
     *
     * @param userId id
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<UserAddressGetResponse>> findAllAddressByUserId(
            @RequestHeader(name = HeaderProperties.USER_ID) Long userId) {

        List<UserAddressGetResponse> userGetResponseList =
                userAddressService.findAllAddressByUserId(userId);

        return new ResponseEntity<>(userGetResponseList, HttpStatus.OK);
    }

}

