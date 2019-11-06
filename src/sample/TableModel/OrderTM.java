package sample.TableModel;

import java.util.Date;

public class OrderTM {
    private Date date;
    private String orderId;
    private String customerId;
    private String customerName;
    private String itemId;
    private String itemDescription;
    private Double unitPrice;
    private int qty;
    private int qtyOnHand;

    public OrderTM() {
    }

    public OrderTM(Date date, String orderId, String customerId, String customerName, String itemId, String itemDescription, Double unitPrice, int qty, int qtyOnHand) {
        this.date = date;
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.itemId = itemId;
        this.itemDescription = itemDescription;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.qtyOnHand = qtyOnHand;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {this.itemId = itemId; }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "OrderTM{" +
                "date=" + date +
                ", orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", ItemId='" + itemId + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", qtyOnHand=" + qtyOnHand +
                '}';
    }
}
