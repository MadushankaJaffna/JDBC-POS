package sample.TableModel;

import javafx.fxml.Initializable;

public class summeryTM {
private String itemId;
private String desccription;
private Double unitPrice;
private Integer qty;
private Double total;

    public summeryTM() {
    }

    public summeryTM(String itemId, String desccription, Double unitPrice, Integer qty, Double total) {
        this.itemId = itemId;
        this.desccription = desccription;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDesccription() {
        return desccription;
    }

    public void setDesccription(String desccription) {
        this.desccription = desccription;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "summeryTM{" +
                "itemId='" + itemId + '\'' +
                ", desccription='" + desccription + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", total=" + total +
                '}';
    }
}
