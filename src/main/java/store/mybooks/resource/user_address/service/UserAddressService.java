package store.mybooks.resource.user_address.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_address.dto.mapper.UserAddressMapper;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
import store.mybooks.resource.user_address.dto.request.UserAddressModifyRequest;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressDeleteResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.entity.UserAddress;
import store.mybooks.resource.user_address.exception.UserAddressFullException;
import store.mybooks.resource.user_address.exception.UserAddressNotExistException;
import store.mybooks.resource.user_address.repository.UserAddressRepository;

/**
 * packageName    : store.mybooks.resource.user_address.service
 * fileName       : UserAddressService
 * author         : masiljangajji
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        masiljangajji       최초 생성
 */

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    private final UserRepository userRepository;

    private final UserAddressMapper userAddressMapper;

    @Transactional
    public UserAddressCreateResponse createUserAddress(Long userId, UserAddressCreateRequest createRequest) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(userId));


        if (userAddressRepository.countByUserId(userId) >= 11) {
            throw new UserAddressFullException(userId);
        }

        UserAddress userAddress =
                new UserAddress(user, createRequest.getAlias(), createRequest.getRoadName(), createRequest.getDetail(),
                        createRequest.getNumber(), createRequest.getReference());

        userAddressRepository.save(userAddress);


        return userAddressMapper.toUserAddressCreateResponse(userAddress);
    }


    @Transactional
    public UserAddressModifyResponse modifyUserAddress(Long userId, Long addressId,
                                                       UserAddressModifyRequest modifyRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));

        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new UserAddressNotExistException(addressId));

        userAddress.modifyByUserAddressModifyRequest(modifyRequest.getAlias(), modifyRequest.getRoadName(),
                modifyRequest.getDetail(),
                modifyRequest.getNumber(), modifyRequest.getReference());

        return userAddressMapper.toUserAddressModifyResponse(userAddress);

    }

    @Transactional
    public UserAddressDeleteResponse deleteUserAddress(Long userId, Long addressId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));

        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new UserAddressNotExistException(addressId));

        userAddressRepository.deleteById(addressId);
        return new UserAddressDeleteResponse(String.format("[%s] 삭제 완료", userAddress.getAlias()));
    }

    public UserAddressGetResponse findByAddressId(Long userId, Long addressId) {

        return userAddressRepository.queryByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new UserAddressNotExistException(addressId));
    }

    public Page<UserAddressGetResponse> findByAllUserAddress(Pageable pageable) {

        return userAddressRepository.queryAllBy(pageable);
    }

    public List<UserAddressGetResponse> findAllAddressByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(userId));

        return userAddressRepository.queryAllByUserId(userId);

    }


}
