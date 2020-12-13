package logic;

public enum Status {
    APPROVED("A", "Одобрен"), DENIED("O", "Отказано"), IN_PROCESSING("I", "В обработке");
    private String code;
    private String value;

    Status(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static Status getByCode(String statusCode) {
        for (Status g : Status.values()) {
            if (g.value.equals(statusCode)) {
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