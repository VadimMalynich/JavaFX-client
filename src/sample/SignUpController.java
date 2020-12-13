package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.*;
import validators.CheckLogin;
import validators.CheckPassword;

public class SignUpController implements CheckLogin, CheckPassword, ChangeWindows, ShakeItem, ShowAlert {
    private ToggleGroup group = new ToggleGroup();
    private Users user;
    //    private static Pattern p1 = Pattern.compile(".{5,20}");
//    (?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}
    //private static Pattern p2 = Pattern.compile("(?=.*[0-9])(?=.*[a-z])[0-9a-z]{6,}");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private Button signUpButton;

    @FXML
    private RadioButton logistRadioButton;

    @FXML
    private RadioButton companyRadioButton;

    @FXML
    private Button signUpCancelButton;

    @FXML
    void initialize() {
        setGroup();
        signUpButton.setOnAction(actionEvent -> {
            try {
                String login = loginField.getText();
                if (isValidLogin(login)) {
                    String pass = passwordField.getText();
                    if (isValidPassword(pass)) {
                        if (logistRadioButton.isSelected() || companyRadioButton.isSelected()) {
                            Client.sendMessage("signUpUser");
                            String role = "";
                            if (logistRadioButton.isSelected()) {
                                role = UserRole.LOGIST.getValue();
                            } else {
                                role = UserRole.COMPANY.getValue();
                            }
                            user = new Users(login, pass, role);
                            Client.sendUser(user);
                            String message = Client.readMessage();
                            show("Регистрация пользователя", message, 100);
                            changeWindow("sample.fxml", signUpButton);
                        } else {
                            shakeItem(logistRadioButton);
                            shakeItem(companyRadioButton);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        signUpCancelButton.setOnAction(event -> {
            Client.sendMessage("cancel");
            changeWindow("sample.fxml", signUpCancelButton);
        });
    }

    private void setGroup() {
        logistRadioButton.setToggleGroup(group);
        companyRadioButton.setToggleGroup(group);
    }

//    public boolean isValidLogin(String string) {
//        Matcher m1 = p1.matcher(string);
//        if (m1.find() && string.length() <= 20) {
//            return true;
//        }
//        return false;
//    }

//    public static boolean isValidPassword(String string) {
//        Matcher m1 = p1.matcher(string);
//        if (m1.find() && string.length() <= 20) {
//            return true;
//        }
//        return false;
//    }
}
