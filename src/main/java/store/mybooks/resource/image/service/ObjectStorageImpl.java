package store.mybooks.resource.image.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
import store.mybooks.resource.image.dto.request.ImageTokenRequest;
import store.mybooks.resource.image.dto.response.ImageGetResponse;
import store.mybooks.resource.image.dto.response.ImageRegisterResponse;
import store.mybooks.resource.image.dto.response.ImageTokenResponse;
import store.mybooks.resource.image.entity.Image;
import store.mybooks.resource.image.exception.ImageNotExistsException;
import store.mybooks.resource.image.repository.ImageRepository;
import store.mybooks.resource.image_status.entity.ImageStatus;
import store.mybooks.resource.image_status.enumeration.ImageStatusEnum;
import store.mybooks.resource.image_status.repository.ImageStatusRepository;
import store.mybooks.resource.review.entity.Review;


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
@RequiredArgsConstructor
@Transactional
public class ObjectStorageImpl implements ImageService {
    private final ObjectStorageProperties objectStorageProperties;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private String token;
    private LocalDateTime expireToken;
    private static final String X_AUTH_TOKEN = "X-Auth-Token";
    private final ImageStatusRepository imageStatusRepository;

    private String requestToken() {
        String identityUrl = objectStorageProperties.getIdentity() + "/tokens";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ImageTokenRequest imageTokenRequest =
                new ImageTokenRequest(objectStorageProperties.getTenantId(), objectStorageProperties.getUsername(),
                        objectStorageProperties.getPassword());

        HttpEntity<ImageTokenRequest> httpEntity = new HttpEntity<>(imageTokenRequest, httpHeaders);
        ResponseEntity<ImageTokenResponse> response
                = this.restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, ImageTokenResponse.class);
        token = Objects.requireNonNull(response.getBody()).getAccess().getToken().getId();
        expireToken = Objects.requireNonNull(response.getBody()).getAccess().getToken().getDateTime();

        return this.token;
    }

    private String getPath() {
        return objectStorageProperties.getUrl()
                + "/"
                + objectStorageProperties.getContainerName()
                + "/";
    }

    @Override
    public void updateImage(Book book, MultipartFile thumbNailFile, List<MultipartFile> content) throws
            IOException {
        if (Objects.isNull(thumbNailFile) && (Objects.isNull(content) || content.isEmpty())) {
            return;
        }

        if (Objects.nonNull(thumbNailFile)) {
            ImageStatus thumbNailImageStatus =
                    imageStatusRepository.findById(ImageStatusEnum.THUMBNAIL.getName()).orElseThrow();
            Image thumbNailImage =
                    imageRepository.findImageByBook_IdAndImageStatus_Id(book.getId(), thumbNailImageStatus.getId())
                            .orElseThrow();
            deleteObject(thumbNailImage);
            saveImage(thumbNailImageStatus, null, book, thumbNailFile);

        }
        if (Objects.isNull(content) || content.isEmpty()) {
            ImageStatus contentImageStatus =
                    imageStatusRepository.findById(ImageStatusEnum.CONTENT.getName()).orElseThrow();
            List<Image> contentImage =
                    imageRepository.findAllByBook_IdAndImageStatus_Id(book.getId(), contentImageStatus.getId());

            contentImage.forEach(this::deleteObject);
            for (MultipartFile file : content) {
                saveImage(contentImageStatus, null, book, file);
            }
        }
    }

    @Override
    public ImageRegisterResponse saveImage(ImageStatus imageStatus, Review review, Book book,
                                           MultipartFile file) throws IOException {
        String imageName = file.getOriginalFilename();
        int dot = Objects.requireNonNull(imageName).lastIndexOf(".");
        String extension = imageName.substring(dot);
        String nameSave = UUID.randomUUID().toString();

        String url = getPath() + nameSave + extension;

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

        restTemplates.execute(url, HttpMethod.PUT, requestCallback, responseExtractor);
        Image image =
                new Image(getPath(), nameSave, extension, book, review,
                        imageStatus);

        return imageMapper.mapToResponse(imageRepository.save(image));
    }

    @Override
    @Transactional(readOnly = true)
    public ImageGetResponse getObject(Long id) {
        Image image =
                imageRepository.findById(id).orElseThrow(() -> new ImageNotExistsException("해당하는 id의 이미지가 없습니다"));
        String url = image.getPath() + image.getFileName() + image.getExtension();

        return new ImageGetResponse(url);
    }

    @Override
    @Transactional(readOnly = true)
    public Image getThumbNailImage(Long id) {
        return imageRepository.findImageByBook_IdAndImageStatus_Id(id, ImageStatusEnum.THUMBNAIL.getName())
                .orElseThrow(() -> new ImageNotExistsException("해당하는 id의 이미지가 없습니다"));
    }

    @Override
    @Transactional(readOnly = true)
    public Image getReviewImage(Long id) {
        return imageRepository.findImageByReviewIdAndImageStatusId(id, ImageStatusEnum.REVIEW.getName())
                .orElseThrow(() -> new ImageNotExistsException("해당하는 id의 이미지가 없습니다"));
    }

    @Override
    public void deleteObject(Image image) {
        String url = image.getPath() + image.getFileName() + image.getExtension();
        System.out.println("hihihihi");
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.add(X_AUTH_TOKEN, requestToken());
        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        this.restTemplate.exchange(url, HttpMethod.DELETE, requestHttpEntity, String.class);

        imageRepository.deleteById(image.getId());
    }

}