package sample.TableModel;

public class ItemTM {
    private String itemCode;
    private String itemDescription;
    private String qtyOnHand;
    private String unitPrice;

    public ItemTM() {
    }

    public ItemTM(String itemCode, String itemDescription, String qtyOnHand, String unitPrice) {
        this.itemCode = itemCode;
        this.itemDescription = itemDescription;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(String qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "ItemTM{" +
                "itemCode='" + itemCode + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", QtyOnHand='" + qtyOnHand + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                '}';
    }


}
