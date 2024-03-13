package store.mybooks.resource.user_grade.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.error.exception.ValidationFailException;
import store.mybooks.resource.user_grade.dto.request.UserGradeCreateRequest;
import store.mybooks.resource.user_grade.dto.response.UserGradeCreateResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeDeleteResponse;
import store.mybooks.resource.user_grade.dto.response.UserGradeGetResponse;
import store.mybooks.resource.user_grade.service.UserGradeService;

/**
 * packageName    : store.mybooks.resource.user_grade.controller<br>
 * fileName       : UserGradeRestController<br>
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
@RequestMapping("/api/users-grades")
public class UserGradeRestController {

    private final UserGradeService userGradeService;

    /**
     * methodName : createUserGrade
     * author : masiljangajji
     * description : 유저등급을 생성
     *
     * @param createRequest request
     * @return response entity
     */
    @PostMapping
    public ResponseEntity<UserGradeCreateResponse> createUserGrade(
            @Valid @RequestBody UserGradeCreateRequest createRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new ValidationFailException(bindingResult);
        }

        UserGradeCreateResponse createResponse = userGradeService.createUserGrade(createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    /**
     * methodName : deleteUserGradeById
     * author : masiljangajji
     * description :유저 등급을 삭제 (등급의 상태를 삭제로 만듬)
     *
     * @param id id
     * @return response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserGradeDeleteResponse> deleteUserGradeById(@PathVariable(name = "id") Integer id) {

        UserGradeDeleteResponse deleteResponse = userGradeService.deleteUserGrade(id);

        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    /**
     * methodName : findUserGradeById
     * author : masiljangajji
     * description : 유저등급을 찾음
     *
     * @param id id
     * @return response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGradeGetResponse> findUserGradeById(@PathVariable(name = "id") Integer id) {

        UserGradeGetResponse getResponse = userGradeService.findUserGradeById(id);

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    /**
     * methodName : findAllUserGrade
     * author : masiljangajji
     * description : 모든 유저등급을 list 형태로 으로 찾음
     *
     * @return response entity
     */
    @GetMapping
    public ResponseEntity<List<UserGradeGetResponse>> findAllUserGrade() {

        List<UserGradeGetResponse> paginationUserGrade = userGradeService.findAllUserGrade();
        return new ResponseEntity<>(paginationUserGrade, HttpStatus.OK);
    }


}
