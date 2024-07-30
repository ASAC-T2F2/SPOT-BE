package T2F2.SPOT.domain.user;

public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_USER("일반사용자");

    private String label;

    private Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}