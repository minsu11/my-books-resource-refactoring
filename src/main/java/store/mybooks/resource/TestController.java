package store.mybooks.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : store.mybooks.resource
 * fileName       : TestController
 * author         : newjaehun
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        newjaehun       최초 생성
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping
    public String test() {
        return "test2222";
    }
}
