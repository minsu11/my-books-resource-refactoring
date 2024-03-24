package store.mybooks.resource.user_status.enumeration;

/**
 * packageName    : store.mybooks.resource.user_status.enumeration<br>
 * fileName       : UserStatusEnum<br>
 * author         : masiljangajji<br>
 * date           : 2/16/24<br>
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        masiljangajji       최초 생성
 */
public enum UserStatusEnum {


    ACTIVE("활성"),

    DORMANCY("휴면"),
    LOCK("잠금"),

    SOCIAL("소셜"),
    RESIGN("탈퇴");


    private final String name;

    UserStatusEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
