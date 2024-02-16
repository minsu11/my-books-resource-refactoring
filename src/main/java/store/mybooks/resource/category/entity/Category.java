package store.mybooks.resource.category.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.category.dto.request.CategoryCreateRequest;
import store.mybooks.resource.category.dto.response.CategoryCreateResponse;

/**
 * packageName    : store.mybooks.resource.category.entity
 * fileName       : Category
 * author         : damho
 * date           : 2/13/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/13/24        damho       최초 생성
 */
@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory")
    private List<Category> childCategoryList;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_created_date")
    private LocalDate createdDate;

    /**
     * Category 생성자.
     *
     * @param categoryCreateRequest 카테고리 생성 request
     */
    public Category(CategoryCreateRequest categoryCreateRequest) {
        this.parentCategory = categoryCreateRequest.getParentCategory();
        this.name = categoryCreateRequest.getName();
        this.createdDate = LocalDate.now();
        this.childCategoryList = new ArrayList<>();
    }

    /**
     * methodName : convertToCategoryCreateResponse
     * author : damho-lee
     * description : Category Entity 를 CategoryCreateResponse 로 변경하는 메서드.
     *
     * @return CategoryCreateResponse
     */
    public CategoryCreateResponse convertToCategoryCreateResponse() {
        return CategoryCreateResponse.builder()
                .name(this.name)
                .build();
    }
}
