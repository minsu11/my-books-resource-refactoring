package store.mybooks.resource.user_grade_name.enumeration;

/**
 * packageName    : store.mybooks.resource.user_grade.enumeration<br>
 * fileName       : UserGradeEnum<br>
 * author         : masiljangajji<br>
 * date           : 2/16/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        masiljangajji       최초 생성
 */
public enum UserGradeNameEnum {

    NORMAL("일반"),

    GOLD("골드"),
    PLATINUM("플레티넘"),

    ROYAL("로얄");


    private final String name;

    UserGradeNameEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
