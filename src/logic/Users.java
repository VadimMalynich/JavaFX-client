package logic;

import java.io.Serializable;

public class Users implements Serializable {
    private Integer idUser;
    private String login;
    private String password;
    private String role;

    public Users() {
    }

    public Users(String login) {
        this.login = login;
    }

    public Users(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = null;
    }

    public Users(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Users(Integer idUser, String login, String password, String role) {
        this.idUser = idUser;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "idUser=" + idUser +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
