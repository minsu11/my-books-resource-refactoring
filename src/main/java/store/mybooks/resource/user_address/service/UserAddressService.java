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
 * packageName    : store.mybooks.resource.user_address.service<br>
 * fileName       : UserAddressService<br>
 * author         : masiljangajji<br>
 * date           : 2/13/24<br>
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

    /**
     * methodName : createUserAddress
     * author : masiljangajji
     * description : 유저주소를 생성
     *
     * @param userId        id
     * @param createRequest request
     * @return user address create response
     */
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


    /**
     * methodName : modifyUserAddress
     * author : masiljangajji
     * description : 유저주소를 수정 (별명,상세주소)
     *
     * @param userId        id
     * @param addressId     id
     * @param modifyRequest request
     * @return user address modify response
     * @throws UserNotExistException        유저가 존재하지 않는경우
     * @throws UserAddressNotExistException 유저가주소가 존재하지 않는 경우
     */
    @Transactional
    public UserAddressModifyResponse modifyUserAddress(Long userId, Long addressId,
                                                       UserAddressModifyRequest modifyRequest) {
        IsExistUser(userId);

        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new UserAddressNotExistException(addressId));

        userAddress.modifyByUserAddressModifyRequest(modifyRequest.getAlias(),
                modifyRequest.getDetail());

        return userAddressMapper.toUserAddressModifyResponse(userAddress);

    }

    /**
     * methodName : deleteUserAddress
     * author : masiljangajji
     * description : 유저주소를 삭제
     *
     * @param userId    id
     * @param addressId id
     * @return user address delete response
     * @throws UserNotExistException        유저가 존재하지 않는 경우
     * @throws UserAddressNotExistException 유저가주소가 존재하지 않는 경우
     */
    @Transactional
    public UserAddressDeleteResponse deleteUserAddress(Long userId, Long addressId) {

        IsExistUser(userId);

        UserAddress userAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new UserAddressNotExistException(addressId));

        userAddressRepository.deleteById(addressId);
        return new UserAddressDeleteResponse(String.format("[%s] 삭제 완료", userAddress.getAlias()));
    }

    /**
     * methodName : findByAddressId
     * author : masiljangajji
     * description : 유저 주소를 찾음
     *
     * @param userId    id
     * @param addressId id
     * @return user address get response
     * @throws UserAddressNotExistException 유저주소가 존재하지 않는 경우
     */
    public UserAddressGetResponse findByAddressId(Long userId, Long addressId) {

        return userAddressRepository.queryByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new UserAddressNotExistException(addressId));
    }

    /**
     * methodName : findByAllUserAddress
     * author : masiljangajji
     * description : 모든 유저 주소를 Pagination 해서 찾음
     *
     * @param pageable pageable
     * @return page
     */
    public Page<UserAddressGetResponse> findByAllUserAddress(Pageable pageable) {

        return userAddressRepository.queryAllBy(pageable);
    }

    /**
     * methodName : findAllAddressByUserId
     * author : masiljangajji
     * description : 유저의 모든 주소를 List 로 반환
     *
     * @param userId id
     * @return list
     * @throws UserNotExistException 유저가 존재하지 않는 경우
     */
    public List<UserAddressGetResponse> findAllAddressByUserId(Long userId) {

        IsExistUser(userId);


        return userAddressRepository.queryAllByUserId(userId);

    }

    private void IsExistUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotExistException(userId);
        }
    }


}
