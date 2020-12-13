package sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.*;
import validators.*;

import java.io.IOException;

public class AdminMenuController implements ChangeWindows, ShowAlert, CheckPassword, CheckLogin, ShakeItem {
    ObservableList<Users> usersData = FXCollections.observableArrayList();
    private ObservableList<UserRole> userRoleList = FXCollections.observableArrayList(UserRole.values());
    private ObservableList<Companies> companiesData = FXCollections.observableArrayList();
    private ObservableList<Status> statusList = FXCollections.observableArrayList(Status.values());
    private ObservableList<Warehouses> warehousesData = FXCollections.observableArrayList();
    private ObservableList<ProductRequest> requestData = FXCollections.observableArrayList();
    private StringValidator stringValidator = new StringValidator();
    private CapacityValidator capacityValidator = new CapacityValidator();
    private NumberValidator numberValidator = new NumberValidator();
    private int countAdmins;
    private int countLogists;
    private int countCompanies;

    @FXML
    private TableView<Users> usersTable;

    @FXML
    private TableColumn<Users, Integer> idUserColumn;

    @FXML
    private TableColumn<Users, String> loginUserColumn;

    @FXML
    private TableColumn<Users, String> passwordUserColumn;

    @FXML
    private TableColumn<Users, UserRole> roleUserColumn;

    @FXML
    private Button addUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button saveUserChangesButton;

    @FXML
    private Label registeredUsersLabel;

    @FXML
    private TextField findUserTextField;

    @FXML
    private Button findUserButton;

    @FXML
    private Button cancelFindUserButton;

    //2

    @FXML
    private Button saveCompaniesTableEdit;

    @FXML
    private TableView<Companies> companiesTable;

    @FXML
    private TableColumn<Companies, String> companyLoginColumn;

    @FXML
    private TableColumn<Companies, String> companyNameColumn;

    @FXML
    private TableColumn<Companies, String> companyCategoryColumn;

    @FXML
    private TableColumn<Companies, Status> companyStatusColumn;

    @FXML
    private Text registeredCompaniesLabel;

    @FXML
    private Text NoRegisteredCompaniesLabel;

    //3

    @FXML
    private TextField warehouseCategoryTextField;

    @FXML
    private TextField warehouseCapacityTextField;

    @FXML
    private Button clearButton;

    @FXML
    private Button addWarehouseButton;

    //4

    @FXML
    private TableView<Warehouses> warehousesTable;

    @FXML
    private TableColumn<Warehouses, Integer> warehousesIDColumn;

    @FXML
    private TableColumn<Warehouses, String> warehouseCategoryColumn;

    @FXML
    private TableColumn<Warehouses, Integer> warehousesCurrentAmountColumn;

    @FXML
    private TableColumn<Warehouses, String> warehouseCapacityColumn;

    @FXML
    private Text listWarehousesLabel;

    @FXML
    private Button saveCompaniesTableEdit1;

    @FXML
    private Button editWarehousesButton;

    @FXML
    private Button deleteWarehousesButton;

    @FXML
    private Text NoRegisteredWarehousesLabel;

    @FXML
    private Button cancelEditWarehousesButton;

    //    5вкладка
    @FXML
    private TableView<ProductRequest> requestTable;

    @FXML
    private TableColumn<ProductRequest, String> requestLoginColumn;

    @FXML
    private TableColumn<ProductRequest, String> categoryColumn;

    @FXML
    private TableColumn<ProductRequest, String> productNameColumn;

    @FXML
    private TableColumn<ProductRequest, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductRequest, Double> defectColumn;

    @FXML
    private TableColumn<ProductRequest, Status> requestStatusColumn;

    @FXML
    private Label companiesRequestsInfo;

    @FXML
    private Button deleteRequestButton;

    @FXML
    private Label noRequestLabel;

    @FXML
    private Button saveCompaniesTableEdit2;

    @FXML
    private Button checkDefectButton;

    @FXML
    private Button filtrationQuantityButton;

    @FXML
    private TextField minQuantityFiltrationField;

    @FXML
    private TextField maxQuantityFiltrationField;

    @FXML
    private TextField minDefectFiltrationField;

    @FXML
    private TextField maxDefectFiltrationField;

    @FXML
    private Button filtrationDefectButton;

    @FXML
    private Button cancelFiltrationButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button updateButton;

    //6

    @FXML
    private PieChart usersPieChart;

    @FXML
    private Button warehousesStackedBarChartButton;

    @FXML
    void initialize() {
        returnButton.setOnAction(event -> {
            Client.sendMessage("exitLogistAccount");
            changeWindow("sample.fxml", returnButton);
        });

        updateButton.setOnAction(event -> {
            setAttributes();
        });

        setAttributes();

//        1 вкладкая
        roleUserColumn.setOnEditCommit((TableColumn.CellEditEvent<Users, UserRole> event) -> {
            TablePosition<Users, UserRole> pos = event.getTablePosition();
            UserRole newRole = event.getNewValue();
            int row = pos.getRow();
            Users user = event.getTableView().getItems().get(row);
            user.setRole(newRole.getValue());
        });

        loginUserColumn.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();
            String newLogin = event.getNewValue();
            if (isValidLogin(newLogin)) {
                int row = pos.getRow();
                Users user = event.getTableView().getItems().get(row);
                user.setLogin(newLogin);
            } else {
                show("Неверный ввод логина", "Логин должен содержать от 5 до 20 символов" +
                        "\nПри сохранении изменения не вступят в силу!", 200);
            }
        });

        passwordUserColumn.setOnEditCommit((TableColumn.CellEditEvent<Users, String> event) -> {
            TablePosition<Users, String> pos = event.getTablePosition();
            String newPassword = event.getNewValue();
            if (isValidPassword(newPassword)) {
                int row = pos.getRow();
                Users user = event.getTableView().getItems().get(row);
                user.setPassword(newPassword);
            } else {
                show("Неверный ввод пароля", "Пароль должен состоять из букв латинского " +
                        "алфавита и цифр.\nПароль должен содержать от 6 до 20 символов" +
                        "\nПри сохранении изменения не вступят в силу!", 200);
            }
        });

        addUserButton.setOnAction(event -> {
            Client.sendMessage("signUp");
            changeWindow("signUp.fxml", addUserButton);
        });

        deleteUserButton.setOnAction(event -> {
            try {
                Users selectUser = usersTable.getSelectionModel().getSelectedItem();
                if (selectUser != null) {
                    Client.sendMessage("deleteUser");
                    Client.sendObject(selectUser);
                    String message = Client.readMessage();
                    show("Удаление пользователя", message, 100);
                    setFirstAnchorPaneElements();
                    setSixthAnchorPaneElements();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        saveUserChangesButton.setOnAction(event -> {
            editTables("updateUsers", usersData);
            setFirstAnchorPaneElements();
            setSixthAnchorPaneElements();
        });

        findUserButton.setOnAction(event -> {
            String text = findUserTextField.getText().trim();
            if (stringValidator.validate(text)) {
                ObservableList<Users> tempUsersData = FXCollections.observableArrayList();
                for (Users pr : usersData) {
                    if (pr.getLogin().equals(text)) {
                        tempUsersData.addAll(pr);
                        break;
                    }
                }
                usersTable.setItems(tempUsersData);
                cancelFindUserButton.setVisible(true);
            }
        });

        cancelFindUserButton.setOnAction(event -> {
            setFirstAnchorPaneElements();
        });

        //2

        companyStatusColumn.setOnEditCommit((TableColumn.CellEditEvent<Companies, Status> event) -> {
            TablePosition<Companies, Status> pos = event.getTablePosition();
            Status newStatus = event.getNewValue();
            int row = pos.getRow();
            Companies company = event.getTableView().getItems().get(row);
            company.setStatus(newStatus.getValue());
        });

        saveCompaniesTableEdit.setOnAction(event -> {
            editTables("updateCompanyTable", companiesData);
            setSecondAnchorPaneElements();
        });

//        3 вкладка

        clearButton.setOnAction(event -> {
            clear();
        });

        addWarehouseButton.setOnAction(event -> {
            addWarehouse();
            setFourthAnchorPaneElements();
            clear();
        });

//        4 вкладка

        deleteWarehousesButton.setOnAction(event -> {
            try {
                Warehouses selectWarehouse = warehousesTable.getSelectionModel().getSelectedItem();
                if (selectWarehouse != null) {
                    Client.sendMessage("deleteWarehouse");
                    Client.sendObject(selectWarehouse);
                    String message = Client.readMessage();
                    show("Удаление склада", message, 200);
                    setFourthAnchorPaneElements();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        editWarehousesButton.setOnAction(event -> {
            warehousesTable.setEditable(true);
            editWarehousesButton.setVisible(false);
            deleteWarehousesButton.setVisible(false);
            cancelEditWarehousesButton.setVisible(true);
            saveCompaniesTableEdit1.setVisible(true);
        });

        cancelEditWarehousesButton.setOnAction(event -> {
            warehousesTable.setEditable(false);
            setFourthAnchorPaneElements();
        });

        saveCompaniesTableEdit1.setOnAction(event -> {
            warehousesTable.setEditable(false);
            editTables("updateWarehousesTable", warehousesData);
            setFourthAnchorPaneElements();
        });

        warehouseCategoryColumn.setOnEditCommit((TableColumn.CellEditEvent<Warehouses, String> event) -> {
            TablePosition<Warehouses, String> pos = event.getTablePosition();
            String newCategory = event.getNewValue();
            if (stringValidator.validate(newCategory)) {
                int row = pos.getRow();
                Warehouses warehouse = event.getTableView().getItems().get(row);
                warehouse.setWarehouseCategory(newCategory);
            } else {
                show("Неправильный ввод", "Категория должна содержать буквы латиницы и/или кириллицы" +
                        "\nПри сохранении изменения не вступят в силу!", 100);
            }
        });

        warehouseCapacityColumn.setOnEditCommit((TableColumn.CellEditEvent<Warehouses, String> event) -> {
            TablePosition<Warehouses, String> pos = event.getTablePosition();
            String newCapacity = event.getNewValue();
            if (capacityValidator.validate(newCapacity)) {
                int row = pos.getRow();
                Warehouses warehouse = event.getTableView().getItems().get(row);
                warehouse.setCapacity(newCapacity);
            } else {
                show("Неправильный ввод", "Вместимость должна быть в пределах от 1000 до 10000." +
                        "\nПри сохранении изменения не вступят в силу!", 100);
            }
        });

//        5 вкладка

        requestStatusColumn.setOnEditCommit((TableColumn.CellEditEvent<ProductRequest, Status> event) -> {
            TablePosition<ProductRequest, Status> pos = event.getTablePosition();
            Status newStatus = event.getNewValue();
            int row = pos.getRow();
            ProductRequest productRequest = event.getTableView().getItems().get(row);
            productRequest.setStatus(newStatus.getValue());
        });

        saveCompaniesTableEdit2.setOnAction(event -> {
            editTables("updateProductRequestTable", requestData);
            setFourthAnchorPaneElements();
            setFifthAnchorPaneElements();
        });

        deleteRequestButton.setOnAction(event -> {
            try {
                ProductRequest selectProductRequest = requestTable.getSelectionModel().getSelectedItem();
                if (selectProductRequest != null) {
                    Client.sendMessage("deleteCompanyRequest");
                    Client.sendObject(selectProductRequest);
                    String message = Client.readMessage();
                    show("Удаление заявки", message, 100);
                    setFourthAnchorPaneElements();
                    setFifthAnchorPaneElements();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        checkDefectButton.setOnAction(event -> {
            int count = 0;
            for (ProductRequest pr : requestData) {
                if (0 == pr.getDefect()) {
                    count++;
                }
            }
            if (count != 0) {
                for (ProductRequest pr : requestData) {
                    if (0 == pr.getDefect()) {
                        double def = (double) (0 + Math.random() * 20);
                        def = Math.round(def * 100.0) / 100.0;
                        pr.setDefect(def);
                    }
                }
                editTables("updateProductRequestTable", requestData);
                setFourthAnchorPaneElements();
                setFifthAnchorPaneElements();
            } else {
                show("Проверка на брак", "Все товары проверены на брак!", 100);
            }
        });

        filtrationQuantityButton.setOnAction(event -> {
            String min = minQuantityFiltrationField.getText().trim();
            String max = maxQuantityFiltrationField.getText().trim();
            if (numberValidator.validate(min) && numberValidator.validate(max)) {
                ObservableList<ProductRequest> tempRequestData = FXCollections.observableArrayList();
                int minValue = Integer.parseInt(min);
                int maxValue = Integer.parseInt(max);
                if (minValue < maxValue && minValue != maxValue) {
                    for (ProductRequest pr : requestData) {
                        int quantity = pr.getProductQuantity();
                        if (quantity >= minValue && quantity <= maxValue) {
                            tempRequestData.addAll(pr);
                        }
                    }
                    requestTable.setItems(tempRequestData);
                    filtrationDefectButton.setVisible(false);
                    cancelFiltrationButton.setVisible(true);
                    minDefectFiltrationField.setVisible(false);
                    maxDefectFiltrationField.setVisible(false);
                }
            } else {
                show("Фильтрация", "Неверный ввод!", 100);
            }
        });

        filtrationDefectButton.setOnAction(event -> {
            String min = minDefectFiltrationField.getText().trim();
            String max = maxDefectFiltrationField.getText().trim();
            if (numberValidator.validate(min) && numberValidator.validate(max)) {
                ObservableList<ProductRequest> tempRequestData = FXCollections.observableArrayList();
                double minValue = Double.parseDouble(min);
                double maxValue = Double.parseDouble(max);
                if (minValue < maxValue && minValue != maxValue) {
                    for (ProductRequest pr : requestData) {
                        double defect = pr.getDefect();
                        if (defect >= minValue && defect <= maxValue) {
                            tempRequestData.addAll(pr);
                        }
                    }
                    requestTable.setItems(tempRequestData);
                    filtrationQuantityButton.setVisible(false);
                    cancelFiltrationButton.setVisible(true);
                    minQuantityFiltrationField.setVisible(false);
                    maxQuantityFiltrationField.setVisible(false);
                }
            } else {
                show("Фильтрация", "Неверный ввод!", 100);
            }
        });

        cancelFiltrationButton.setOnAction(event -> {
            setFifthAnchorPaneElements();
        });

//    6

        warehousesStackedBarChartButton.setOnAction(event -> {
            setSeventhAnchorPaneElements();
        });
    }

    private void setAttributes() {
        setFirstAnchorPaneElements();
        setSecondAnchorPaneElements();
        setFourthAnchorPaneElements();
        setFifthAnchorPaneElements();
        setSixthAnchorPaneElements();
    }

    private void editTables(String message, ObservableList list) {
        try {
            Client.sendMessage(message);
            Client.sendObject(list.size());
            System.out.println();
            for (Object w : list) {
                Client.sendObject(w);
                System.out.println(w.toString());
            }
            System.out.println();
            String alertMessage = Client.readMessage();
            show("Обновление таблицы с компаниями", alertMessage, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setFirstAnchorPaneElements() {
        Client.sendMessage("getUsers");
        int count = (int) Client.readObject();
        registeredUsersLabel.setText("Зарегистрированных пользователей: " + (count - 1));
        if (count > 0) {
            countAdmins = countLogists = countCompanies = 0;
            usersData.clear();
            usersTable.setVisible(true);
            usersTable.setEditable(true);
            addUserButton.setVisible(true);
            deleteUserButton.setVisible(true);
            saveUserChangesButton.setVisible(true);
            findUserTextField.setVisible(true);
            findUserTextField.setText("");
            cancelFindUserButton.setVisible(false);
            findUserButton.setVisible(true);
            for (int i = 0; i < count; i++) {
                Users user = (Users) Client.readObject();
                System.out.println(user.toString());
                if (UserRole.ADMIN.getValue().equals(user.getRole())) {
                    countAdmins++;
                } else if (UserRole.LOGIST.getValue().equals(user.getRole())) {
                    countLogists++;
                } else {
                    countCompanies++;
                }
                usersData.add(new Users(user.getIdUser(), user.getLogin(),
                        user.getPassword(), user.getRole()));
            }
            usersData.remove(0);
            idUserColumn.setCellValueFactory(new PropertyValueFactory<Users, Integer>("idUser"));
            loginUserColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("login"));
            loginUserColumn.setCellFactory(TextFieldTableCell.<Users>forTableColumn());
            passwordUserColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("password"));
            passwordUserColumn.setCellFactory(TextFieldTableCell.<Users>forTableColumn());
            roleUserColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Users, UserRole>,
                    ObservableValue<UserRole>>() {
                @Override
                public ObservableValue<UserRole> call(TableColumn.CellDataFeatures<Users, UserRole> param) {
                    Users user = param.getValue();
                    String userRole = user.getRole();
                    UserRole role = UserRole.getByCode(userRole);
                    return new SimpleObjectProperty<UserRole>(role);
                }
            });
            roleUserColumn.setCellFactory(ComboBoxTableCell.forTableColumn(userRoleList));
            usersTable.setItems(usersData);
        } else {
            usersTable.setVisible(false);
            usersTable.setEditable(false);
            addUserButton.setVisible(false);
            deleteUserButton.setVisible(false);
            saveUserChangesButton.setVisible(false);
            findUserTextField.setVisible(false);
            cancelFindUserButton.setVisible(false);
            findUserButton.setVisible(false);
        }
    }

    private void clear() {
        warehouseCategoryTextField.setText("");
        warehouseCapacityTextField.setText("");
    }

//    2 вкладка

    private void setSecondAnchorPaneElements() {
        Client.sendMessage("getCompaniesTableData");
        int count = (int) Client.readObject();
        if (count > 0) {
            companiesData.clear();
            companiesTable.setVisible(true);
            companiesTable.setEditable(true);
            registeredCompaniesLabel.setVisible(true);
            saveCompaniesTableEdit.setVisible(true);
            NoRegisteredCompaniesLabel.setVisible(false);
            for (int i = 0; i < count; i++) {
                Companies company = (Companies) Client.readObject();
                System.out.println(company.toString());
                companiesData.add(new Companies(company.getLogin(), company.getName(),
                        company.getCategory(), company.getStatus()));
            }
            companyLoginColumn.setCellValueFactory(new PropertyValueFactory<Companies, String>("login"));
            companyNameColumn.setCellValueFactory(new PropertyValueFactory<Companies, String>("name"));
            companyCategoryColumn.setCellValueFactory(new PropertyValueFactory<Companies, String>("category"));
            companyStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Companies, Status>,
                    ObservableValue<Status>>() {
                @Override
                public ObservableValue<Status> call(TableColumn.CellDataFeatures<Companies, Status> param) {
                    Companies company = param.getValue();
                    String companyStatus = company.getStatus();
                    Status status = Status.getByCode(companyStatus);
                    return new SimpleObjectProperty<Status>(status);
                }
            });

            companyStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusList));

            companiesTable.setItems(companiesData);
            companyStatusColumn.setSortType(TableColumn.SortType.ASCENDING);
        } else {
            companiesTable.setVisible(false);
            registeredCompaniesLabel.setVisible(false);
            saveCompaniesTableEdit.setVisible(false);
            NoRegisteredCompaniesLabel.setVisible(true);
        }
    }

//    3 вкладка

    public void addWarehouse() {
        try {
            String category = warehouseCategoryTextField.getText().trim();
            String capacity = warehouseCapacityTextField.getText().trim();
            if (!"".equals(category) && !"".equals(capacity)) {
                if (stringValidator.validate(category)) {
                    if (capacityValidator.validate(capacity)) {
                        Client.sendMessage("addWarehouse");
                        Warehouses warehouse = new Warehouses(category, capacity);
                        Client.sendObject(warehouse);
                        String message = Client.readMessage();
                        show("Добавление склада", message, 100);
                    } else {
                        show("Неправильный ввод", "Вместимость должна быть в пределах от 1000 до 10000", 100);
                    }
                } else {
                    show("Неправильный ввод", "Категория должна содержать буквы латиницы и/или кириллицы", 100);
                }
            } else {
                shakeItem(warehouseCategoryTextField);
                shakeItem(warehouseCapacityTextField);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    4 вкладка
    private void setFourthAnchorPaneElements() {
        Client.sendMessage("getWarehouses");
        int count = (int) Client.readObject();
        if (count > 0) {
            warehousesData.clear();
            warehousesTable.setVisible(true);
            warehousesTable.setEditable(false);
            listWarehousesLabel.setVisible(true);
            editWarehousesButton.setVisible(true);
            deleteWarehousesButton.setVisible(true);
            saveCompaniesTableEdit1.setVisible(false);
            cancelEditWarehousesButton.setVisible(false);
            NoRegisteredWarehousesLabel.setVisible(false);
            for (int i = 0; i < count; i++) {
                Warehouses warehouse = (Warehouses) Client.readObject();
                System.out.println(warehouse.toString());
                warehousesData.add(new Warehouses(warehouse.getWarehouseID(), warehouse.getWarehouseCategory(),
                        warehouse.getCurrentAmount(), warehouse.getCapacity()));
            }
            warehousesIDColumn.setCellValueFactory(new PropertyValueFactory<Warehouses, Integer>("warehouseID"));
            warehouseCategoryColumn.setCellValueFactory(new PropertyValueFactory<Warehouses, String>("warehouseCategory"));
            warehouseCategoryColumn.setCellFactory(TextFieldTableCell.<Warehouses>forTableColumn());
            warehousesCurrentAmountColumn.setCellValueFactory(new PropertyValueFactory<Warehouses, Integer>("currentAmount"));
            warehouseCapacityColumn.setCellValueFactory(new PropertyValueFactory<Warehouses, String>("capacity"));
            warehouseCapacityColumn.setCellFactory(TextFieldTableCell.<Warehouses>forTableColumn());
            warehousesTable.setItems(warehousesData);
        } else {
            warehousesTable.setVisible(false);
            listWarehousesLabel.setVisible(false);
            editWarehousesButton.setVisible(false);
            deleteWarehousesButton.setVisible(false);
            saveCompaniesTableEdit1.setVisible(false);
            cancelEditWarehousesButton.setVisible(false);
            NoRegisteredWarehousesLabel.setVisible(true);
        }
    }

//    5 вкладка

    private void setFifthAnchorPaneElements() {
        Client.sendMessage("getRequestTableData");
        Client.sendObject(false);
        int count = (int) Client.readObject();
        if (count > 0) {
            requestData.clear();
            requestTable.setVisible(true);
            requestTable.setEditable(true);
            minDefectFiltrationField.setVisible(true);
            minQuantityFiltrationField.setVisible(true);
            maxDefectFiltrationField.setVisible(true);
            maxQuantityFiltrationField.setVisible(true);
            minDefectFiltrationField.setText("");
            minQuantityFiltrationField.setText("");
            maxDefectFiltrationField.setText("");
            maxQuantityFiltrationField.setText("");
            filtrationDefectButton.setVisible(true);
            filtrationQuantityButton.setVisible(true);
            deleteRequestButton.setVisible(true);
            checkDefectButton.setVisible(true);
            cancelFiltrationButton.setVisible(false);
            saveCompaniesTableEdit2.setVisible(true);
            companiesRequestsInfo.setVisible(true);
            noRequestLabel.setVisible(false);
            for (int i = 0; i < count; i++) {
                ProductRequest prRq = (ProductRequest) Client.readObject();
                System.out.println(prRq.toString());
                requestData.add(new ProductRequest(prRq.getIdRequest(), prRq.getLogin(), prRq.getCategory(),
                        prRq.getProductName(), prRq.getProductQuantity(), prRq.getDefect(), prRq.getStatus()));
            }
            requestLoginColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, String>("login"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, String>("category"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, String>("productName"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, Integer>("productQuantity"));
            defectColumn.setCellValueFactory(new PropertyValueFactory<ProductRequest, Double>("defect"));
            requestStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ProductRequest, Status>,
                    ObservableValue<Status>>() {
                @Override
                public ObservableValue<Status> call(TableColumn.CellDataFeatures<ProductRequest, Status> param) {
                    ProductRequest productRequest = param.getValue();
                    String companyStatus = productRequest.getStatus();
                    Status status = Status.getByCode(companyStatus);
                    return new SimpleObjectProperty<Status>(status);
                }
            });

            requestStatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusList));
            requestTable.setItems(requestData);

        } else {
            requestTable.setVisible(false);
            companiesRequestsInfo.setVisible(false);
            deleteRequestButton.setVisible(false);
            checkDefectButton.setVisible(false);
            cancelFiltrationButton.setVisible(false);
            minDefectFiltrationField.setVisible(false);
            minQuantityFiltrationField.setVisible(false);
            maxDefectFiltrationField.setVisible(false);
            maxQuantityFiltrationField.setVisible(false);
            filtrationDefectButton.setVisible(false);
            filtrationQuantityButton.setVisible(false);
            noRequestLabel.setVisible(true);
            saveCompaniesTableEdit2.setVisible(false);
        }
    }

    //6

    private void setSixthAnchorPaneElements() {
        ObservableList<PieChart.Data> valueList = FXCollections.observableArrayList(
                new PieChart.Data("Администраторы", countAdmins),
                new PieChart.Data("Логисты", countLogists),
                new PieChart.Data("Компании", countCompanies));

        usersPieChart.setData(valueList);

        usersPieChart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%", (data.getPieValue() / 100));
            Tooltip toolTip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), toolTip);
        });
    }

    private void setSeventhAnchorPaneElements() {
        Client.sendMessage("getWarehouses");
        int count = (int) Client.readObject();
        if (count > 0) {
            ObservableList<String> categoryList = FXCollections.observableArrayList();
            ObservableList<Integer> quantityList = FXCollections.observableArrayList();
            ObservableList<Integer> capacityList = FXCollections.observableArrayList();
            for (int i = 0; i < count; i++) {
                Warehouses warehouse = (Warehouses) Client.readObject();
                categoryList.add(warehouse.getWarehouseCategory());
                quantityList.add(warehouse.getCurrentAmount());
                capacityList.add(Integer.parseInt(warehouse.getCapacity()));
            }
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setCategories(categoryList);
            xAxis.setLabel("Категории складов");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Количество товаров(шт)");
            BarChart<String, Number> warehouseCurrentAmountStackedBarChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName("Текущая загруженность склада");
            for (int i = 0; i < categoryList.size(); i++) {
                series1.getData().add(new XYChart.Data<>(categoryList.get(i), quantityList.get(i)));
            }
            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            series2.setName("Вместимость склада");
            for (int i = 0; i < categoryList.size(); i++) {
                series2.getData().add(new XYChart.Data<>(categoryList.get(i), capacityList.get(i)));
            }
            warehouseCurrentAmountStackedBarChart.getData().addAll(series1, series2);
            Group root = new Group(warehouseCurrentAmountStackedBarChart);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setTitle("Загруженность складов");
        }
    }
}
