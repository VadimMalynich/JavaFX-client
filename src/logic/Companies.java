package logic;

public class Companies extends Users implements Copyable {
    private String name;
    private String category;
    private String email;
    private String phoneNumber;
    private String status;

    public Companies() {
    }

    public Companies(String login, String password, String role) {
        super(login, password, role);
    }

    public Companies(String login, String name, String category, String status) {
        super(login);
        this.name = name;
        this.category = category;
        this.status = status;
    }

    public Companies(String login, String name, String category, String email, String phoneNumber, String status) {
        super(login);
        this.name = name;
        this.category = category;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public Companies(String login, String password, String role, String name, String category, String email, String phoneNumber) {
        super(login, password, role);
        this.name = name;
        this.category = category;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Companies{" +
                "login='" + getLogin() + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public Companies copy() {
        Companies company = new Companies();
        return company;
    }
}
