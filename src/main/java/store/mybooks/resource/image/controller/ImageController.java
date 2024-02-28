package store.mybooks.resource.image.controller;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.image.dto.request.ImageRegisterRequest;
import store.mybooks.resource.image.dto.response.ImageGetResponse;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.service.ImageService;

/**
 * packageName    : store.mybooks.resource.image.controller <br/>
 * fileName       : ImageController<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageRegisterResponse> saveImage(
            @RequestPart("dto") ImageRegisterRequest imageRegisterRequest,
            @RequestPart("files") MultipartFile multipartFile)
            throws IOException {
        ImageRegisterResponse imageRegisterResponse = imageService.saveImage(imageRegisterRequest, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(imageRegisterResponse);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageGetResponse> getImage(@PathVariable("imageId") Long id) {
        ImageGetResponse imageGetResponse = imageService.getObject(id);

        return ResponseEntity.ok().body(imageGetResponse);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("imageId") Long id) {
        imageService.deleteObject(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
