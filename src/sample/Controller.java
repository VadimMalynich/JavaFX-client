package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logic.*;
import validators.CheckLogin;
import validators.CheckPassword;

public class Controller implements CheckLogin, CheckPassword, ChangeWindows, ShakeItem, ShowAlert {
    private Client client;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    void initialize() {
        start();
        signInButton.setOnAction(actionEvent -> {
            String login = loginField.getText().trim();
            if (isValidLogin(login)) {
                String password = passwordField.getText().trim();
                if (isValidPassword(password)) {
                    Client.sendMessage("enter");
                    Users user = new Users(login, password);
                    Client.sendUser(user);
                    try {
                        String enter = Client.readMessage();
                        System.out.println(enter);
                        if ("Успешно".equals(enter)) {
                            String userRole = Client.readMessage();
                            System.out.println(userRole);
                            user.setRole(userRole);
                            if (UserRole.ADMIN.getValue().equals(user.getRole())) {
                                changeWindow("adminMenu.fxml", signInButton);
                            } else if (UserRole.COMPANY.getValue().equals(user.getRole())) {
                                changeWindow("companyMenu.fxml", signInButton);
                            } else {
                                changeWindow("logistMenu.fxml", signInButton);
                            }
                        } else {
                            shakeItem(passwordField);
                            shakeItem(loginField);
                            show("Авторизация пользователя", "Пользователь не зарегистрирован", 100);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    shakeItem(passwordField);
                    show("Неверный ввод пароля", "Пароль должен состоять из букв латинского " +
                            "алфавита и цифр.\nПароль должен содержать от 6 до 20 символов", 200);
                }
            } else {
                shakeItem(loginField);
                show("Неверный ввод логина", "Логин должен содержать от 5 до 20 символов", 100);
            }
            loginField.setText("");
            passwordField.setText("");
        });
        signUpButton.setOnAction(actionEvent -> {
            Client.sendMessage("signUp");
            changeWindow("signUp.fxml", signUpButton);
        });
    }

    private void start() {
        if (Client.getClientSocket() == null) {
            client = new Client("127.0.0.1", 2525);
        }
    }


//    private void changeWindow(String path, Node node) {
//        node.getScene().getWindow().hide();
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource(path));
//        try {
//            loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Parent parent = loader.getRoot();
//        Stage stage = new Stage();
//        stage.setScene(new Scene(parent));
//        stage.showAndWait();
//    }
}

