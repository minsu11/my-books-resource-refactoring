package store.mybooks.resource.image_status.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.image_status.entity <br/>
 * fileName       : ImageStatus<br/>
 * author         : Fiat_lux <br/>
 * date           : 2/27/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 2/27/24        Fiat_lux       최초 생성<br/>
 */

@Entity
@Table(name = "image_status")
@AllArgsConstructor
@NoArgsConstructor
public class ImageStatus {
    @Id
    @Column(name = "image_status_id")
    private String id;
}
