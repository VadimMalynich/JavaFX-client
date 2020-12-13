package logic;

import java.io.Serializable;

public class ProductRequest implements Serializable {
    private Integer idRequest;
    private String login;
    private String category;
    private String productName;
    private Integer productQuantity;
    private Double defect;
    private String status;

    public ProductRequest() {
    }

    public ProductRequest(String productName, Integer productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    public ProductRequest(String category, String productName, Integer productQuantity, String status) {
        this.category = category;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.status = status;
    }

    public ProductRequest(Integer idRequest, String category, String productName, Integer productQuantity, String status) {
        this.idRequest = idRequest;
        this.category = category;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.status = status;
    }

    public ProductRequest(String login, String category, String productName, Integer productQuantity, String status) {
        this.login = login;
        this.category = category;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.status = status;
    }

    public ProductRequest(Integer idRequest, String login, String category, String productName, Integer productQuantity, String status) {
        this.idRequest = idRequest;
        this.login = login;
        this.category = category;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.status = status;
    }

    public ProductRequest(Integer idRequest, String login, String category, String productName, Integer productQuantity, Double defect, String status) {
        this.idRequest = idRequest;
        this.login = login;
        this.category = category;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.defect = defect;
        this.status = status;
    }

    public Integer getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Double getDefect() {
        return defect;
    }

    public void setDefect(Double defect) {
        this.defect = defect;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductRequest{" +
                "idRequest=" + idRequest +
                ", login='" + login + '\'' +
                ", category='" + category + '\'' +
                ", productName='" + productName + '\'' +
                ", productQuantity=" + productQuantity +
                ", defect='" + defect + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
