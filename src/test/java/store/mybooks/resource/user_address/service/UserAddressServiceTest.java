package store.mybooks.resource.user_address.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.user.entity.User;
import store.mybooks.resource.user.exception.UserNotExistException;
import store.mybooks.resource.user.repository.UserRepository;
import store.mybooks.resource.user_address.dto.mapper.UserAddressMapper;
import store.mybooks.resource.user_address.dto.request.UserAddressCreateRequest;
import store.mybooks.resource.user_address.dto.request.UserAddressModifyRequest;
import store.mybooks.resource.user_address.dto.response.UserAddressCreateResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressGetResponse;
import store.mybooks.resource.user_address.dto.response.UserAddressModifyResponse;
import store.mybooks.resource.user_address.entity.UserAddress;
import store.mybooks.resource.user_address.exception.UserAddressFullException;
import store.mybooks.resource.user_address.exception.UserAddressNotExistException;
import store.mybooks.resource.user_address.repository.UserAddressRepository;

/**
 * packageName    : store.mybooks.resource.user_address.service
 * fileName       : UserAddressServiceTest
 * author         : masiljangajji
 * date           : 2/21/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/21/24        masiljangajji       최초 생성
 */

@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

    @InjectMocks
    UserAddressService userAddressService;

    @Mock
    UserAddressRepository userAddressRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserAddressMapper userAddressMapper;


    @Test
    @DisplayName("UserId , UserAddressCreateRequest 로 createUserAddress 실행시 동작 테스트")
    void givenUserIdAndUserAddressCreateRequest_whenCallCreateUserAddress_thenReturnUserAddressCreateResponse(
            @Mock User user,
            @Mock UserAddress userAddress,
            @Mock UserAddressCreateResponse userAddressCreateResponse,
            @Mock UserAddressCreateRequest userAddressCreateRequest
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userAddressRepository.save(any(UserAddress.class))).thenReturn(userAddress);
        when(userAddressRepository.countByUserId(anyLong())).thenReturn(1);
        when(userAddressMapper.toUserAddressCreateResponse(any(UserAddress.class))).thenReturn(
                userAddressCreateResponse);

        userAddressService.createUserAddress(1L, userAddressCreateRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
        verify(userAddressRepository, times(1)).countByUserId(anyLong());
        verify(userAddressMapper, times(1)).toUserAddressCreateResponse(any(UserAddress.class));
    }

    @Test
    @DisplayName("존재하지 않는 UserId , UserAddressCreateRequest 로 createUserAddress 실행시 UserNotExistException")
    void givenNotExistUserIdAndUserAddressCreateRequest_whenCallCreateUserAddress_thenThrowUserNotExistException(
            @Mock UserAddressCreateRequest userAddressCreateRequest
    ) {
        assertThrows(UserNotExistException.class,
                () -> userAddressService.createUserAddress(1L, userAddressCreateRequest));
    }

    @Test
    @DisplayName("11개 이상의 주소를 갖는 UserId , UserAddressCreateRequest 로 createUserAddress 실행시 UserAddressFullException")
    void givenUserIdAndUserAddressCreateRequest_whenCallCreateUserAddressAndMoreThenTenAddress_thenThrowUserAddressFullException(
            @Mock User user,
            @Mock UserAddressCreateRequest userAddressCreateRequest
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userAddressRepository.countByUserId(anyLong())).thenReturn(11);

        assertThrows(UserAddressFullException.class,
                () -> userAddressService.createUserAddress(anyLong(), userAddressCreateRequest));
    }

    @Test
    @DisplayName("UserId , AddressId , UserAddressModifyRequest 로 modifyUserAddress 실행시 동작 테스트")
    void givenUserIdAndAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenReturnUserAddressModifyResponse(
            @Mock User user,
            @Mock UserAddress userAddress,
            @Mock UserAddressModifyResponse userAddressModifyResponse,
            @Mock UserAddressModifyRequest userAddressModifyRequest
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userAddressRepository.findById(anyLong())).thenReturn(Optional.of(userAddress));
        when(userAddressMapper.toUserAddressModifyResponse(any(UserAddress.class))).thenReturn(
                userAddressModifyResponse);

        userAddressService.modifyUserAddress(1L, 1L, userAddressModifyRequest);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userAddressRepository, times(1)).findById(anyLong());
        verify(userAddressMapper, times(1)).toUserAddressModifyResponse(any(UserAddress.class));
    }

    @Test
    @DisplayName("존재하지않는 UserId , AddressId , UserAddressModifyRequest 로 modifyUserAddress 실행시 UserNotExistException")
    void givenNotExistUserIdAndAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenThrowUserNotExistException(
            @Mock UserAddressModifyRequest userAddressModifyRequest
    ) {
        assertThrows(UserNotExistException.class,
                () -> userAddressService.modifyUserAddress(1L, 1L, userAddressModifyRequest));
    }

    @Test
    @DisplayName("UserId , 존재하지 않는 AddressId , UserAddressModifyRequest 로 modifyUserAddress 실행시 UserAddressNotExistException")
    void givenUserIdAndNotExistAddressIdAndUserAddressModifyRequest_whenCallModifyUserAddress_thenThrowUserAddressNotExistException(
            @Mock User user,
            @Mock UserAddressModifyRequest userAddressModifyRequest
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(UserAddressNotExistException.class,
                () -> userAddressService.modifyUserAddress(1L, 1L, userAddressModifyRequest));
    }

    @Test
    @DisplayName("UserId , AddressId 로 deleteUserAddress 실행시 동작 테스트")
    void givenUserIdAndAddressId_whenCallDeleteUserAddress_thenReturnUserAddressDeleteResponse(
            @Mock User user,
            @Mock UserAddress userAddress
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userAddressRepository.findById(anyLong())).thenReturn(Optional.of(userAddress));
        doNothing().when(userAddressRepository).deleteById(anyLong());

        userAddressService.deleteUserAddress(1L, 1L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userAddressRepository, times(1)).findById(anyLong());
        verify(userAddressRepository, times(1)).deleteById(anyLong());

    }

    @Test
    @DisplayName("존재하지않는 UserId , AddressId 로 deleteUserAddress 실행시 UserNotExistException")
    void givenNotExistUserIdAndAddressId_whenCallDeleteUserAddress_thenThrowUserNotExistException(
    ) {
        assertThrows(UserNotExistException.class, () -> userAddressService.deleteUserAddress(1L, 1L));
    }

    @Test
    @DisplayName("UserId , 존재하지않는 AddressId 로 deleteUserAddress 실행시 UserAddressNotExistException")
    void givenUserIdAndNotExistAddressId_whenCallDeleteUserAddress_thenThrowUserAddressNotExistException(
            @Mock User user
    ) {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(UserAddressNotExistException.class, () -> userAddressService.deleteUserAddress(1L, 1L));
    }

   




}