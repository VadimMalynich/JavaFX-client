package logic;

public enum UserRole {
    LOGIST("Логист"), COMPANY("Компания"), ADMIN("Администратор");
    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole getByCode(String role) {
        for (UserRole g : UserRole.values()) {
            if (g.value.equals(role)) {
                return g;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
