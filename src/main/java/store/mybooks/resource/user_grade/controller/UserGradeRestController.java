package store.mybooks.resource.user_grade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
     * Create user grade response entity.
     * <p>
     * userGrade를 생성하는 api
     *
     * @param createRequest the create request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<UserGradeCreateResponse> createUserGrade(
            @RequestBody UserGradeCreateRequest createRequest) {


        UserGradeCreateResponse createResponse = userGradeService.createUserGrade(createRequest);

        return new ResponseEntity<>(createResponse, HttpStatus.CREATED);
    }

    /**
     * Delete user grade by id response entity.
     * id로 찾은 UserGrade를 삭제하는 api
     * 강삭제가 아닌 약삭제로 isAvailable 를 false 로 변경한다
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserGradeDeleteResponse> deleteUserGradeById(@PathVariable(name = "id") Integer id) {

        UserGradeDeleteResponse deleteResponse = userGradeService.deleteUserGrade(id);

        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    /**
     * Find user grade by id response entity.
     * UserGrade를 id를 이용해 찾음
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserGradeGetResponse> findUserGradeById(@PathVariable(name = "id") Integer id) {

        UserGradeGetResponse getResponse = userGradeService.findUserGradeById(id);

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    /**
     * Find all user grade response entity.
     * 모든 UserGrade를 Pagination해서 보여줌
     * @param pageable the pageable
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<Page<UserGradeGetResponse>> findAllUserGrade(Pageable pageable) {

        Page<UserGradeGetResponse> paginationUserGrade = userGradeService.findAllUserGrade(pageable);
        return new ResponseEntity<>(paginationUserGrade, HttpStatus.OK);
    }


}
