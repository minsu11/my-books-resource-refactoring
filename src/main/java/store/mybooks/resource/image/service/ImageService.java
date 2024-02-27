package store.mybooks.resource.image.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import store.mybooks.resource.book.entity.Book;
import store.mybooks.resource.book.repotisory.BookRepository;
import store.mybooks.resource.config.ObjectStorageProperties;
import store.mybooks.resource.image.dto.mapper.ImageMapper;
import store.mybooks.resource.image.dto.request.ImageRegisterRequest;
import store.mybooks.resource.image.dto.request.TokenRequest;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.dto.response.TokenResponse;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.repository.ImageRepository;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.repository.ImageStatusRepository;
import store.mybooks.resource.review.entity.Review;
import store.mybooks.resource.review.repository.ReviewRepository;


/**
 * packageName    : store.mybooks.resource.image.service <br/>
 * fileName       : ObjectStorage<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/23/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/23/24        Fiat_lux       최초 생성<br/>
 */
@Service
@Transactional(readOnly = true)
public class ImageService {

    private final ObjectStorageProperties objectStorageProperties;
    private RestTemplate restTemplate;
    private final ImageRepository imageRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ImageStatusRepository imageStatusRepository;
    private final ImageMapper imageMapper;
    public static final String X_AUTH_TOKEN = "X-Auth-Token";

    public ImageService(ObjectStorageProperties objectStorageProperties,
                        ImageRepository imageRepository, BookRepository bookRepository,
                        ReviewRepository reviewRepository,
                        ImageStatusRepository imageStatusRepository, ImageMapper imageMapper) {
        this.objectStorageProperties = objectStorageProperties;
        this.imageRepository = imageRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.imageStatusRepository = imageStatusRepository;
        restTemplate = new RestTemplate();
        this.imageMapper = imageMapper;
    }

    private String requestToken() {
        String identityUrl = objectStorageProperties.getIdentity() + "/tokens";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.restTemplate = new RestTemplate();
        TokenRequest tokenRequest =
                new TokenRequest(objectStorageProperties.getTenantId(), objectStorageProperties.getUsername(),
                        objectStorageProperties.getPassword());

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(tokenRequest, httpHeaders);
        ResponseEntity<TokenResponse> response
                = this.restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, TokenResponse.class);
        return Objects.requireNonNull(response.getBody()).getAccess().getToken().getId();
    }

    private String getPath() {
        return objectStorageProperties.getUrl()
                + "/"
                + objectStorageProperties.getContainerName()
                + "/";
    }

    private String getBookPath() {
        return objectStorageProperties.getUrl()
                + "/"
                + objectStorageProperties.getContainerName()
                + "/"
                + objectStorageProperties.getBook()
                + "/";
    }

    private String getReviewPath() {
        return objectStorageProperties.getUrl()
                + "/" + objectStorageProperties.getContainerName()
                + "/"
                + objectStorageProperties.getReview()
                + "/";
    }

    @Transactional
    public ImageRegisterResponse saveImage(ImageRegisterRequest imageRegisterRequest, MultipartFile file)
            throws IOException {
        String imageName = file.getOriginalFilename();
        int dot = Objects.requireNonNull(imageName).lastIndexOf(".");
        String extension = imageName.substring(dot);
        String nameSave = UUID.randomUUID().toString();
        String path = "";
        Optional<Book> optionalBook = bookRepository.findById(imageRegisterRequest.getBookId());
        Optional<Review> optionalReview = reviewRepository.findById(imageRegisterRequest.getReviewId());

        if (optionalBook.isPresent() && optionalReview.isEmpty()) {
            path = getBookPath();
        } else if (optionalReview.isPresent() && optionalBook.isEmpty()) {
            path = getReviewPath();
        } else {
            path = getPath();
        }

        String url = path + nameSave + extension;

        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        final RequestCallback requestCallback = new RequestCallback() {
            public void doWithRequest(final ClientHttpRequest request) throws IOException {
                request.getHeaders().add(X_AUTH_TOKEN, requestToken());
                IOUtils.copy(inputStream, request.getBody());
            }
        };

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate restTemplates = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor
                = new HttpMessageConverterExtractor<>(String.class, restTemplates.getMessageConverters());

        // API 호출
        restTemplates.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);

        ImageStatus imageStatus = imageStatusRepository.findById(imageRegisterRequest.getImageStatusId()).orElseThrow();

        Image image = new Image(path, nameSave, extension, optionalBook.orElse(null), optionalReview.orElse(null),
                imageStatus);

        return imageMapper.mapToResponse(imageRepository.save(image));
    }

    public ResponseEntity<byte[]> getObject(Long id) {
        Image image = imageRepository.findById(id).orElseThrow();
        String url = image.getPath() + image.getFileName() + image.getExtension();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        return this.restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, byte[].class);
    }

    @Transactional
    public void deleteObject(Long id) {
        Image image = imageRepository.findById(id).orElseThrow();
        String url = image.getPath() + image.getFileName() + image.getExtension();

        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());
        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        this.restTemplate.exchange(url, HttpMethod.DELETE, requestHttpEntity, String.class);

        imageRepository.deleteById(id);
    }

}