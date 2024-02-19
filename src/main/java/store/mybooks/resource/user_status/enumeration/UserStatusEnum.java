package store.mybooks.resource.user_status.enumeration;

/**
 * packageName    : store.mybooks.resource.user_status.enumeration
 * fileName       : UserStatusEnum
 * author         : masiljangajji
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        masiljangajji       최초 생성
 */
public enum UserStatusEnum {


    ACTIVE("활성"),

    INACTIVE("휴면"),
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
