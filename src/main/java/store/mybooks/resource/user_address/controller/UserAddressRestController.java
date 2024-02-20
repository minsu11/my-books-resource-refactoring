package store.mybooks.resource.user_address.controller;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.Path;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
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
 * packageName    : store.mybooks.resource.user_address.controller
 * fileName       : UserAddressRestController
 * author         : masiljangajji
 * date           : 2/19/24
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

    @PostMapping("/{userId}")
    public ResponseEntity<UserAddressCreateResponse> createUserAddress(
            @PathVariable(name = "userId") String userId,
            @RequestBody UserAddressCreateRequest createRequest) {

        UserAddressCreateResponse createResponse =
                userAddressService.createUserAddress(Long.parseLong(userId), createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }


    @PutMapping("/{userId}/{addressId}")
    public ResponseEntity<UserAddressModifyResponse> modifyUserAddress(
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "addressId") String addressId,
            @RequestBody UserAddressModifyRequest modifyRequest) {

        UserAddressModifyResponse modifyResponse =
                userAddressService.modifyUserAddress(Long.parseLong(userId), Long.parseLong(addressId), modifyRequest);

        return new ResponseEntity<>(modifyResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{addressId}")
    public ResponseEntity<UserAddressDeleteResponse> deleteUserAddress(
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "addressId") String addressId) {

        UserAddressDeleteResponse deleteResponse =
                userAddressService.deleteUserAddress(Long.parseLong(userId), Long.parseLong(addressId));

        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }


    @GetMapping("/{userId}/{addressId}")
    public ResponseEntity<UserAddressGetResponse> findUserAddressByAddressId(
            @PathVariable(name = "userId") String userId,
            @PathVariable(name = "addressId") String addressId) {

        UserAddressGetResponse getResponse =
                userAddressService.findByAddressId(Long.parseLong(userId), Long.parseLong(addressId));
        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Page<UserAddressGetResponse>> findAllUserAddress(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<UserAddressGetResponse> paginationUserAddress = userAddressService.findByAllUserAddress(page, size);
        return new ResponseEntity<>(paginationUserAddress, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserAddressGetResponse>> findAllAddressByUserId(@PathVariable(name = "userId") String
                                                                                       userId) {

        List<UserAddressGetResponse> userGetResponseList =
                userAddressService.findAllAddressByUserId(Long.parseLong(userId));

        return new ResponseEntity<>(userGetResponseList, HttpStatus.OK);
    }

}

