package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.*;
import validators.CapacityValidator;
import validators.NumberValidator;
import validators.StringValidator;

public class LogistMenuController implements ChangeWindows, ShowAlert, ShakeItem {
    private ObservableList<Companies> companiesData = FXCollections.observableArrayList();
    private ObservableList<Status> statusList = FXCollections.observableArrayList(Status.values());
    private ObservableList<Warehouses> warehousesData = FXCollections.observableArrayList();
    private ObservableList<ProductRequest> requestData = FXCollections.observableArrayList();
    private StringValidator stringValidator = new StringValidator();
    private CapacityValidator capacityValidator = new CapacityValidator();
    private NumberValidator numberValidator = new NumberValidator();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    //2

    @FXML
    private TextField warehouseCategoryTextField;

    @FXML
    private TextField warehouseCapacityTextField;

    @FXML
    private Button clearButton;

    @FXML
    private Button addWarehouseButton;

    //3

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

    //    4вкладка
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

    @FXML
    void initialize() {
        returnButton.setOnAction(actionEvent -> {
            Client.sendMessage("exitLogistAccount");
            changeWindow("sample.fxml", returnButton);
        });

        updateButton.setOnAction(event -> {
            setAttributes();
        });

//        1 вкладка

        setAttributes();

        companyStatusColumn.setOnEditCommit((TableColumn.CellEditEvent<Companies, Status> event) -> {
            TablePosition<Companies, Status> pos = event.getTablePosition();
            Status newStatus = event.getNewValue();
            int row = pos.getRow();
            Companies company = event.getTableView().getItems().get(row);
            company.setStatus(newStatus.getValue());
        });

        saveCompaniesTableEdit.setOnAction(event -> {
            editTables("updateCompanyTable", companiesData);
            setFirstAnchorPaneElements();
        });

//        2 вкладка

        clearButton.setOnAction(event -> {
            clear();
        });

        addWarehouseButton.setOnAction(event -> {
            addWarehouse();
            setThirdAnchorPaneElements();
            clear();
        });

//        3 вкладка

        deleteWarehousesButton.setOnAction(event -> {
            try {
                Warehouses selectWarehouse = warehousesTable.getSelectionModel().getSelectedItem();
                if (selectWarehouse != null) {
                    Client.sendMessage("deleteWarehouse");
                    Client.sendObject(selectWarehouse);
                    String message = Client.readMessage();
                    show("Удаление склада", message, 200);
                    setThirdAnchorPaneElements();
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
            setThirdAnchorPaneElements();
        });

        saveCompaniesTableEdit1.setOnAction(event -> {
            warehousesTable.setEditable(false);
            editTables("updateWarehousesTable", warehousesData);
            setThirdAnchorPaneElements();
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

//        4 вкладка

        requestStatusColumn.setOnEditCommit((TableColumn.CellEditEvent<ProductRequest, Status> event) -> {
            TablePosition<ProductRequest, Status> pos = event.getTablePosition();
            Status newStatus = event.getNewValue();
            int row = pos.getRow();
            ProductRequest productRequest = event.getTableView().getItems().get(row);
            productRequest.setStatus(newStatus.getValue());
        });

        saveCompaniesTableEdit2.setOnAction(event -> {
            editTables("updateProductRequestTable", requestData);
            setThirdAnchorPaneElements();
            setFourthAnchorPaneElements();
        });

        deleteRequestButton.setOnAction(event -> {
            try {
                ProductRequest selectProductRequest = requestTable.getSelectionModel().getSelectedItem();
                if (selectProductRequest != null) {
                    Client.sendMessage("deleteCompanyRequest");
                    Client.sendObject(selectProductRequest);
                    String message = Client.readMessage();
                    show("Удаление заявки", message, 100);
                    setThirdAnchorPaneElements();
                    setFourthAnchorPaneElements();
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
                setThirdAnchorPaneElements();
                setFourthAnchorPaneElements();
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
            setFourthAnchorPaneElements();
        });

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

    private void clear() {
        warehouseCategoryTextField.setText("");
        warehouseCapacityTextField.setText("");
    }

    private void setAttributes() {
        setFirstAnchorPaneElements();
        setThirdAnchorPaneElements();
        setFourthAnchorPaneElements();
    }

//    1 вкладка

    private void setFirstAnchorPaneElements() {
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

//    2 вкладка

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

    //    3 вкладка
    private void setThirdAnchorPaneElements() {
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

//    4 вкладка

    private void setFourthAnchorPaneElements() {
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
}




