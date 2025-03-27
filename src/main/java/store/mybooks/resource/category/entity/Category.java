package store.mybooks.resource.category.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.mybooks.resource.utils.TimeUtils;

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
        this.createdDate = TimeUtils.nowDate();
        this.childCategoryList = new ArrayList<>();
    }

    /**
     * methodName : modifyCategory
     * author : damho-lee
     * description : 카테고리 수정.
     *
     * @param name 카테고리 이름.
     * @return CategoryModifyResponse
     */
    public Category modifyCategory(String name) {
        this.name = name;

        return this;
    }
}
