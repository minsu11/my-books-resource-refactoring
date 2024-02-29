package store.mybooks.resource.dooray.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.dooray.config.MessageBot;
import store.mybooks.resource.dooray.dto.response.DoorayAuthResponse;
import store.mybooks.resource.dooray.service.MessageSenderService;


/**
 * packageName    : store.mybooks.authorization.controller.dooray
 * fileName       : MessageRestController
 * author         : masiljangajji
 * date           : 2/26/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/26/24        masiljangajji       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dooray")
public class MessageRestController {

    private final MessageSenderService messageSenderService;

    private final MessageBot messageBot;

    /**
     * methodName : getPhoneNumberAuthMessage
     * author : masiljangajji
     * description : 유저 회원가입 및 전화번호 변경시 Dooray 로 인증번호를 만들어 보냄
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<DoorayAuthResponse> getPhoneNumberAuthMessage() {

        String randomNumber = messageSenderService.sendMessage(messageBot);
        DoorayAuthResponse authRequest = new DoorayAuthResponse(randomNumber);

        return new ResponseEntity<>(authRequest, HttpStatus.OK);
    }



}
