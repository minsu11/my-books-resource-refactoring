package store.mybooks.resource.tag.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;

/**
 * packageName    : store.mybooks.resource.domain.entity
 * fileName       : Tag
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "tag")
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer id;

    @Column(name = "tag_name")
    private String name;

    @Column(name = "tag_created_date")
    private LocalDate createdDate;

    public Tag(TagCreateRequest tagCreateRequest) {
        this.name = tagCreateRequest.getName();
        this.createdDate = LocalDate.now();
    }

    /**
     * methodName : convertToCreateResponse
     * author : damho-lee
     * description : Tag 를 TagCreateResponse 로 변환시켜주는 메서드.
     *
     * @return TagCreateResponse
     */
    public TagCreateResponse convertToCreateResponse() {
        return TagCreateResponse.builder()
                .name(this.name)
                .build();
    }

    public void setByTagModifyRequest(TagModifyRequest tagModifyRequest) {
        this.name = tagModifyRequest.getName();
    }
}
