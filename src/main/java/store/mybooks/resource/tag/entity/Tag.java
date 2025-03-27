package store.mybooks.resource.tag.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.utils.TimeUtils;

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
@AllArgsConstructor
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

    public Tag(String name) {
        this.name = name;
        this.createdDate = TimeUtils.nowDate();
    }

    public void setByTagModifyRequest(TagModifyRequest tagModifyRequest) {
        this.name = tagModifyRequest.getName();
    }
}
