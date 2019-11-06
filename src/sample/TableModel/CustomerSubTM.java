package sample.TableModel;

import com.jfoenix.controls.JFXButton;

public class CustomerSubTM {
    private String customerId;
    private String itemDescription;
    private int Qty;
    private Double price;
    private JFXButton btn;

    public CustomerSubTM() {
    }

    public CustomerSubTM(String customerId, String itemDescription, int qty, Double price, JFXButton btn) {


        btn.setStyle("-fx-background-color: red");

        this.customerId = customerId;
        this.itemDescription = itemDescription;
        Qty = qty;
        this.price = price;
        this.btn = btn;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public JFXButton getBtn() {
        return btn;
    }

    public void setBtn(JFXButton btn) {

        this.btn = btn;
    }

    @Override
    public String toString() {
        return "CustomerSubTM{" +
                "customerId='" + customerId + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", Qty=" + Qty +
                ", price=" + price +
                ", btn=" + btn +
                '}';
    }
}