package logic;

import java.io.Serializable;

public class Warehouses implements Serializable {
    private int warehouseID;
    private String warehouseCategory;
    private int currentAmount;
    private String capacity;

    public Warehouses() {
    }

    public Warehouses(String warehouseCategory, String capacity) {
        this.warehouseCategory = warehouseCategory;
        this.currentAmount = 0;
        this.capacity = capacity;
    }

    public Warehouses(int warehouseID, String warehouseCategory, int currentAmount, String capacity) {
        this.warehouseID = warehouseID;
        this.warehouseCategory = warehouseCategory;
        this.currentAmount = currentAmount;
        this.capacity = capacity;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getWarehouseCategory() {
        return warehouseCategory;
    }

    public void setWarehouseCategory(String warehouseCategory) {
        this.warehouseCategory = warehouseCategory;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseCategory='" + warehouseCategory + '\'' +
                ", currentAmount=" + currentAmount +
                ", capacity=" + capacity +
                '}';
    }
}
