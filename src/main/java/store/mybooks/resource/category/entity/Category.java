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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
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
     * @param parentCategory the parent category
     * @param name           the name
     */
    public Category(Category parentCategory, String name) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.createdDate = LocalDate.now();
        this.childCategoryList = new ArrayList<>();
    }

    /**
     * methodName : modifyCategory
     * author : damho-lee
     * description : 카테고리 수정.
     *
     * @param parentCategory 부모 카테고리. null 인 경우 최상위 카테고리.
     * @param name           카테고리 이름.
     * @return CategoryModifyResponse
     */
    public Category modifyCategory(Category parentCategory, String name) {
        this.parentCategory = parentCategory;
        this.name = name;

        return this;
    }
}
