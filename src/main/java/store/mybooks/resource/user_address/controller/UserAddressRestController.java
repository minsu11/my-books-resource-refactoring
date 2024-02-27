package store.mybooks.resource.user_address.controller;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.user.dto.request.UserCreateRequest;
import store.mybooks.resource.user.dto.response.UserDeleteResponse;
import store.mybooks.resource.user.dto.response.UserGetResponse;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
import store.mybooks.resource.user_address.dto.request.UserAddressModifyRequest;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressDeleteResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.entity.UserAddress;
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
@RequestMapping("/api/users")
public class UserAddressRestController {

    private final UserAddressService userAddressService;

    /**
     * methodName : createUserAddress
     * author : masiljangajji
     * description : 유저의 주소를 등록함
     *
     * @param userId   id
     * @param createRequest request
     * @return response entity
     */
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<UserAddressCreateResponse> createUserAddress(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserAddressCreateRequest createRequest) {

        UserAddressCreateResponse createResponse =
                userAddressService.createUserAddress(userId, createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }


    /**
     * methodName : modifyUserAddress
     * author : masiljangajji
     * description : 유저의 주소를 변경함 (별명,상세주소)
     *
     * @param userId    id
     * @param addressId id
     * @param modifyRequest  request
     * @return response entity
     */
    @PutMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<UserAddressModifyResponse> modifyUserAddress(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "addressId") Long addressId,
            @RequestBody UserAddressModifyRequest modifyRequest) {

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
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<UserAddressDeleteResponse> deleteUserAddress(
            @PathVariable(name = "userId") Long userId,
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
    @GetMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<UserAddressGetResponse> findUserAddressByAddressId(
            @PathVariable(name = "userId") Long userId,
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
    @GetMapping("/addresses")
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
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<UserAddressGetResponse>> findAllAddressByUserId(@PathVariable(name = "userId") Long
                                                                                       userId) {

        List<UserAddressGetResponse> userGetResponseList =
                userAddressService.findAllAddressByUserId(userId);

        return new ResponseEntity<>(userGetResponseList, HttpStatus.OK);
    }

}

