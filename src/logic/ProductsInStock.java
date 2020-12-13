package logic;

import java.io.Serializable;

public class ProductsInStock implements Serializable {
    private Integer idWarehouses;
    private Integer idProduct;
    private String productName;
    private Integer quantity;

    public ProductsInStock() {
    }

    public ProductsInStock(Integer idWarehouses, Integer idProduct, String productName, Integer quantity) {
        this.idWarehouses = idWarehouses;
        this.idProduct = idProduct;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Integer getIdWarehouses() {
        return idWarehouses;
    }

    public void setIdWarehouses(Integer idWarehouses) {
        this.idWarehouses = idWarehouses;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductsInStock{" +
                "idWarehouses=" + idWarehouses +
                ", idProduct=" + idProduct +
                ", productName=" + productName +
                ", quantity=" + quantity +
                '}';
    }
}
