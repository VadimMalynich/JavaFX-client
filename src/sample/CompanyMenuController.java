package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import logic.*;
import validators.EmailValidator;
import validators.NumberValidator;
import validators.PhoneValidator;

public class CompanyMenuController implements ShakeItem, ShowAlert, ChangeWindows {
    private Companies company;
    private EmailValidator emailValidator = new EmailValidator();
    private PhoneValidator phoneValidator = new PhoneValidator();
    private NumberValidator numberValidator = new NumberValidator();
    private ProductRequest productRequest;
    private ObservableList<ProductRequest> requestData = FXCollections.observableArrayList();
    private ObservableList<String> categoryList = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exitAccountButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField nameCompanyTextField;

    @FXML
    private TextField categoryCompanyTextField;

    @FXML
    private TextField emailCompanyTextField;

    @FXML
    private TextField phoneNumberCompanyTextField;

    @FXML
    private Button addCompanyInfoButton;

    @FXML
    private Button editCompanyInfoButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private Button editCompanyInfoButton1;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private Label productCategoryLabel;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productQuantityLabel;

    @FXML
    private Label informationRequestLabel;

    @FXML
    private VBox verticalProductBox;

    @FXML
    private TextField productCategoryTextField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField productQuantityTextField;

    @FXML
    private Button addRequestButton;

    @FXML
    private Button clearButton1;

    @FXML
    private Label informationRequestLabel1;

    @FXML
    private Label informationRequestLabel11;

    @FXML
    private TableView<ProductRequest> requestTable;

    @FXML
    private TableColumn<ProductRequest, String> categoryColumn;

    @FXML
    private TableColumn<ProductRequest, String> productNameColumn;

    @FXML
    private TableColumn<ProductRequest, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductRequest, String> requestStatusColumn;

    @FXML
    private Label noRequestLabel;

    @FXML
    private Label yourRequestsInfo;

    @FXML
    private Button deleteRequestButton;


    @FXML
    void initialize() {
        updateButton.setOnAction(event -> {
            setAttributes();
        });

        exitAccountButton.setOnAction(event -> {
            Client.sendMessage("exitLogistAccount");
            changeWindow("sample.fxml", exitAccountButton);
        });

//        1 вкладкая
        setAttributes();

        addCompanyInfoButton.setOnAction(event -> {
            workWithElements("setCompanyInfo");
        });

        clearButton.setOnAction(event -> {
            clearInfo();
        });

        editCompanyInfoButton.setOnAction(event -> {
            setCategoryChoiceBox();
            cancelEditButton.setVisible(true);
            setTextFieldsEditable(true);
            categoryCompanyTextField.setVisible(false);
            categoryChoiceBox.setVisible(true);
            clearInfo();
            editCompanyInfoButton1.setVisible(true);
            editCompanyInfoButton.setVisible(false);
        });

        cancelEditButton.setOnAction(event -> setFirstAnchorPaneElements());

        editCompanyInfoButton1.setOnAction(event1 -> {
            boolean flag = workWithElements("editCompanyInfo");
            if (flag) {
                try {
                    String message = Client.readMessage();
                    System.out.println(message);
                    show("Изменение данных", "Результат изменения данных: " + message, 100);
                    setFirstAnchorPaneElements();
                    setThirdAnchorPaneElements();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        2 вкладка

        clearButton1.setOnAction(event -> clearProductInfo());

        addRequestButton.setOnAction(event -> {
            workWithProductRequest("addProductRequest");
        });

//        3 вкладка
        deleteRequestButton.setOnAction(event -> {
            try {
                ProductRequest selectProductRequest = requestTable.getSelectionModel().getSelectedItem();
                if (selectProductRequest != null) {
                    Client.sendMessage("deleteCompanyRequest");
                    Client.sendObject(selectProductRequest);
                    String message = Client.readMessage();
                    show("Удаление заявки", message, 100);
                    setThirdAnchorPaneElements();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

//    1 вкладка

    private boolean workWithElements(String message) {
        try {
            String name = nameCompanyTextField.getText().trim();
            String category = categoryChoiceBox.getValue().trim();
            String email = emailCompanyTextField.getText().trim();
            String phone = phoneNumberCompanyTextField.getText().trim();
            if (!"".equals(name) && !"".equals(category) && !"".equals(email) && !"".equals(phone)) {
                if (emailValidator.validate(emailCompanyTextField.getText())
                        && phoneValidator.validate(phoneNumberCompanyTextField.getText())) {
                    Client.sendMessage(message);
                    Companies clone = company.copy();
                    clone.setName(name);
                    clone.setCategory(category);
                    clone.setEmail(email);
                    clone.setPhoneNumber(phone);
                    clone.setStatus(Status.IN_PROCESSING.getValue());
                    company.setName(name);
                    company.setCategory(category);
                    company.setEmail(email);
                    company.setPhoneNumber(phone);
                    company.setStatus(Status.IN_PROCESSING.getValue());
                    Client.sendObject(clone);
                    System.out.println(company.toString());
                    addCompanyInfoButton.setVisible(false);
                    clearButton.setVisible(false);
                    setTextFieldsEditable(false);
                    editCompanyInfoButton.setVisible(true);
                    cancelEditButton.setVisible(false);
                    editCompanyInfoButton1.setVisible(false);
                    categoryChoiceBox.setVisible(false);
                    categoryCompanyTextField.setText(category);
                    categoryCompanyTextField.setVisible(true);
                    setSecondAnchorPaneElements();
                    return true;
                } else {
                    shakeItem(emailCompanyTextField);
                    shakeItem(phoneNumberCompanyTextField);
                    show("Ошибка ввода", "Примеры правильного ввода почты:" +
                            "\nexample@gmail.com" +
                            "\nexample.example2@mail.ru.com" +
                            "\nНомер телефона должен начинаться с +37524/25/29/33/44,\n а сам номер должен состоять из любых 7 цифр", 200);
                }
            } else {
                shakeItem(nameCompanyTextField);
                shakeItem(categoryCompanyTextField);
                shakeItem(emailCompanyTextField);
                shakeItem(phoneNumberCompanyTextField);
            }
        } catch (Exception e) {
            show("Ошибка добавления данных", "Отсутсвуют склады для размещения товаров." +
                    "\nПодождите пока появятся склады!", 100);
        }
        return false;
    }

    private int setCategoryChoiceBox() {
        try {
            categoryList.clear();
            categoryCompanyTextField.setVisible(false);
            Client.sendMessage("getWarehousesCategory");
            int count = (int) Client.readObject();
            if (count > 0) {
                categoryList.clear();
                for (int i = 0; i < count; i++) {
                    String str = Client.readMessage();
                    categoryList.add(str);
                }
                categoryChoiceBox.getItems().clear();
                categoryChoiceBox.getItems().addAll(categoryList);
            }
            return count;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return 0;
        }
    }

    private void clearInfo() {
        nameCompanyTextField.setText("");
        categoryCompanyTextField.setText("");
        emailCompanyTextField.setText("");
        phoneNumberCompanyTextField.setText("");
    }

    private void setAttributes() {
        Client.sendMessage("checkCompanyInfo");
        company = (Companies) Client.readObject();
        System.out.println(company.toString());
        setFirstAnchorPaneElements();
        setSecondAnchorPaneElements();
        setThirdAnchorPaneElements();
    }

    private void setFirstAnchorPaneElements() {
        setCategoryChoiceBox();
        if (company.getName() != null) {
            categoryChoiceBox.setVisible(false);
            categoryCompanyTextField.setVisible(true);
            nameCompanyTextField.setText(company.getName());
            categoryCompanyTextField.setText(company.getCategory());
            emailCompanyTextField.setText(company.getEmail());
            phoneNumberCompanyTextField.setText(company.getPhoneNumber());
            setTextFieldsEditable(false);
            editCompanyInfoButton.setVisible(true);
            addCompanyInfoButton.setVisible(false);
            clearButton.setVisible(false);
            cancelEditButton.setVisible(false);
            editCompanyInfoButton1.setVisible(false);
        } else {
            categoryChoiceBox.setVisible(true);
            categoryCompanyTextField.setVisible(false);
            editCompanyInfoButton.setVisible(false);
            editCompanyInfoButton1.setVisible(false);
            cancelEditButton.setVisible(false);
        }
    }

    private void setTextFieldsEditable(boolean flag) {
        if (flag) {
            nameCompanyTextField.setEditable(true);
            categoryCompanyTextField.setEditable(true);
            emailCompanyTextField.setEditable(true);
            phoneNumberCompanyTextField.setEditable(true);
        } else {
            nameCompanyTextField.setEditable(false);
            categoryCompanyTextField.setEditable(false);
            emailCompanyTextField.setEditable(false);
            phoneNumberCompanyTextField.setEditable(false);
        }
    }

//    2 вкладка

    private void setSecondAnchorPaneElements() {
        if (Status.APPROVED.getValue().equals(company.getStatus())) {
            if (company.getCategory() != null) {
                verticalProductBox.setVisible(true);
                productCategoryTextField.setText(company.getCategory());
                addRequestButton.setVisible(true);
                clearButton1.setVisible(true);
                informationRequestLabel.setVisible(false);
            } else {
                verticalProductBox.setVisible(false);
                addRequestButton.setVisible(false);
                clearButton1.setVisible(false);
                informationRequestLabel.setVisible(true);
            }
            informationRequestLabel1.setVisible(false);
            informationRequestLabel11.setVisible(false);
        } else if (Status.DENIED.getValue().equals(company.getStatus())) {
            verticalProductBox.setVisible(false);
            clearButton1.setVisible(false);
            addRequestButton.setVisible(false);
            informationRequestLabel.setVisible(false);
            informationRequestLabel1.setVisible(false);
            informationRequestLabel11.setVisible(true);
        } else {
            verticalProductBox.setVisible(false);
            clearButton1.setVisible(false);
            addRequestButton.setVisible(false);
            informationRequestLabel.setVisible(false);
            informationRequestLabel1.setVisible(true);
            informationRequestLabel11.setVisible(false);
        }
    }

    private void clearProductInfo() {
        productNameTextField.setText("");
        productQuantityTextField.setText("");
    }

    private void workWithProductRequest(String message) {
        try {
            String name = productNameTextField.getText().trim();
            String count = productQuantityTextField.getText().trim();
            if (!"".equals(name) && !"".equals(count)) {
                boolean flag = numberValidator.validate(count);
                if (flag && !"0".equals(count)) {
                    Client.sendMessage(message);
                    int quantity = Integer.parseInt(count);
                    productRequest = new ProductRequest(company.getLogin(), company.getCategory(),
                            name, quantity, Status.IN_PROCESSING.getValue());
                    Client.sendObject(productRequest);
                    String alertMessage = Client.readMessage();
                    show("Добавление заявки", alertMessage, 100);
                    clearProductInfo();
                    setThirdAnchorPaneElements();
                } else {
                    shakeItem(productQuantityTextField);
                    show("Ошибка ввода", "Количество товаров должно быть от 1 до 500", 100);
                }
            } else {
                shakeItem(productNameTextField);
                shakeItem(productQuantityTextField);
                show("Ошибка ввода", "Количество товаров должно быть от 1 до 500", 100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    3 вкладка

    private void setThirdAnchorPaneElements() {
        Client.sendMessage("getRequestTableData");
        Client.sendObject(true);
        int count = (int) Client.readObject();
        if (count > 0) {
            requestData.clear();
            requestTable.setVisible(true);
            deleteRequestButton.setVisible(true);
            yourRequestsInfo.setVisible(true);
            for (int i = 0; i < count; i++) {
                ProductRequest prRq = (ProductRequest) Client.readObject();
                System.out.println(prRq.toString());
                requestData.add(new ProductRequest(prRq.getIdRequest(), prRq.getCategory(), prRq.getProductName(), prRq.getProductQuantity(), prRq.getStatus()));
            }
            categoryColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, String>("category"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, String>("productName"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, Integer>("productQuantity"));
            requestStatusColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, String>("status"));
            requestTable.setItems(requestData);
            noRequestLabel.setVisible(false);
        } else {
            requestTable.setVisible(false);
            yourRequestsInfo.setVisible(false);
            deleteRequestButton.setVisible(false);
            informationRequestLabel.setVisible(false);
            noRequestLabel.setVisible(true);
        }
    }
}