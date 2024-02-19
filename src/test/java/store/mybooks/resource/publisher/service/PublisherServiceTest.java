package store.mybooks.resource.publisher.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.mybooks.resource.publisher.dto.request.PublisherCreateRequest;
import store.mybooks.resource.publisher.dto.request.PublisherModifyRequest;
import store.mybooks.resource.publisher.dto.response.PublisherCreateResponse;
import store.mybooks.resource.publisher.dto.response.PublisherDeleteResponse;
import store.mybooks.resource.publisher.dto.response.PublisherGetResponse;
import store.mybooks.resource.publisher.dto.response.PublisherModifyResponse;
import store.mybooks.resource.publisher.entity.Publisher;
import store.mybooks.resource.publisher.exception.PublisherAlreadyExistException;
import store.mybooks.resource.publisher.exception.PublisherNotExistException;
import store.mybooks.resource.publisher.repository.PublisherRepository;

/**
 * packageName    : store.mybooks.resource.publisher.service
 * fileName       : PublisherServiceTest
 * author         : newjaehun
 * date           : 2/18/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/18/24        newjaehun       최초 생성
 */
@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {
    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void givenPublisherList_whenFindAllPublishers_thenReturnAllPublishersGetResponseList() {
        List<PublisherGetResponse> publisherGetResponseList = new ArrayList<>();
        publisherGetResponseList.add( new PublisherGetResponse() {
                    @Override
                    public Integer getId() {
                        return 1;
                    }

                    @Override
                    public String getName() {
                        return "publisher1";
                    }
                }
        );
        publisherGetResponseList.add( new PublisherGetResponse() {
                    @Override
                    public Integer getId() {
                        return 2;
                    }

                    @Override
                    public String getName() {
                        return "publisher2";
                    }
                }
        );
        when(publisherRepository.findAllBy()).thenReturn(publisherGetResponseList);
        assertThat(publisherService.getAllPublisher()).isEqualTo(publisherGetResponseList);
    }


    @Test
    void givenPublisherCreateRequest_whenCreatePublisher_thenSavePublisherAndReturnPublisherCreateResponse() {
        String name = "publisherName";
        PublisherCreateRequest request = new PublisherCreateRequest(name);
        when(publisherRepository.existsByName(request.getName())).thenReturn(false);

        Publisher resultPublisher = new Publisher(1, name, LocalDate.now());

        when(publisherRepository.save(any(Publisher.class))).thenReturn(resultPublisher);

        PublisherCreateResponse response = publisherService.createPublisher(request);

        assertThat(response.getName()).isEqualTo(name);

    }

    @Test
    void givenPublisherCreateRequest_whenAlreadyExistPublisherNameCreate_thenThrowPublisherAlreadyExistException() {
        PublisherCreateRequest request = new PublisherCreateRequest("publisherName");
        when(publisherRepository.existsByName(request.getName())).thenReturn(true);
        assertThrows(PublisherAlreadyExistException.class, () -> publisherService.createPublisher(request));
    }

    @Test
    void givenPublisherIdAndPublisherModifyRequest_whenModifyPublisher_thenModifyPublisherAndReturnPublisherModifyResponse() {
        Integer publisherId = 1;
        Publisher publisher = new Publisher(publisherId, "publisherName", LocalDate.now());

        PublisherModifyRequest modifyRequest= new PublisherModifyRequest("publisherNameChange");

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.of(publisher));

        PublisherModifyResponse response = publisherService.modifyPublisher(publisherId, modifyRequest);
        assertThat(response.getName()).isEqualTo(modifyRequest.getChangeName());
    }

    @Test
    void givenPublisherId_whenNotExistPublisherModify_thenThrowPublisherNotExistException() {
        Integer publisherId = 1;
        PublisherModifyRequest modifyRequest= new PublisherModifyRequest("publisherNameChange");

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.empty());
        assertThrows(PublisherNotExistException.class, () -> publisherService.modifyPublisher(publisherId, modifyRequest));
    }

    @Test
    void givenPublisherId_whenDeletePublisher_thenDeletePublisherAndReturnPublisherDeleteResponse() {
        Integer publisherId = 1;
        Publisher publisher = new Publisher(publisherId, "publisherName", LocalDate.now());

        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.of(publisher));
        doNothing().when(publisherRepository).deleteById(publisherId);

        PublisherDeleteResponse response = publisherService.deletePublisher(publisherId);
        assertThat(response.getMessage()).isEqualTo(publisher.getName());
        verify(publisherRepository, times(1)).deleteById(publisherId);
    }

    @Test
    void givenPublisherId_whenNotExistPublisherDelete_thenThrowPublisherNotExistException() {
        Integer publisherId = 1;
        when(publisherRepository.findById(eq(publisherId))).thenReturn(Optional.empty());
        assertThrows(PublisherNotExistException.class, () -> publisherService.deletePublisher(publisherId));
    }
}