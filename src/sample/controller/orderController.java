package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import sample.DBConnection.DBConnection;
import sample.TableModel.CustomerSubTM;
import sample.TableModel.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class orderController implements Initializable {
    public AnchorPane root;
    public ImageView home;
    public JFXButton newoder;
    public JFXTextField customerName;
    public JFXTextField itemDescription;
    public JFXTextField unitPrice;
    public JFXTextField qtyOnHand;
    public JFXTextField qty;
    public JFXButton adddata;
    public TableView<OrderTM> tableOrder;
    public JFXButton remove;
    public JFXComboBox customerId;
    public JFXComboBox ItemId;
    public JFXTextField orderId;
    public Label date;
    public TableView<CustomerSubTM> tableSubOrder;
    public TextField txt_searchBox;
    public Text total;
    public JFXButton deletebtn;


    private PreparedStatement getCustomerId;
    private PreparedStatement getItemCode;
    private PreparedStatement getCustomerName;
    private PreparedStatement getitemDetails;
    private PreparedStatement getOrderID;
    private PreparedStatement sendOrderDetails;
    private PreparedStatement sendOrder;
    private PreparedStatement tabledetails;
    private PreparedStatement deleteOrder;
    private PreparedStatement updateqty;
    private PreparedStatement descqty;
    private PreparedStatement search;


    LocalDate localDate = LocalDate.now();
    String inputuser;
    String inputitem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            getCustomerId = connection.prepareStatement("SELECT Id FROM customer");
            getItemCode = connection.prepareStatement("SELECT Code FROM item");
            getCustomerName = connection.prepareStatement("SELECT Name FROM customer WHERE Id=?");
            getitemDetails = connection.prepareStatement("SELECT Description,QtyOnHand,UnitPrice FROM item WHERE Code=?");
            getOrderID = connection.prepareStatement("SELECT Id FROM poswithsql.order ORDER BY Id DESC LIMIT 1");
            sendOrder = connection.prepareStatement("INSERT INTO poswithsql.order VALUES(?,?,?)");
            sendOrderDetails = connection.prepareStatement("INSERT INTO orderdetails VALUES(?,?,?,?)");
            tabledetails = connection.prepareStatement("SELECT o.Date,o.Id,o.CustomerId,c.Name,od.ItemId,i.Description,od.UnitPrice,od.Qty,i.QtyOnHand FROM poswithsql.order o,orderdetails od,item i,customer c WHERE o.Id=od.OrderId AND od.ItemId=i.Code AND o.CustomerId=c.Id ORDER BY o.Id");
            deleteOrder = connection.prepareStatement("DELETE FROM poswithsql.order WHERE Id=?");
            updateqty = connection.prepareStatement("UPDATE item SET QtyOnHand=(QtyOnHand-?) WHERE Code=?");
            descqty = connection.prepareStatement("UPDATE item SET QtyOnHand=(QtyOnHand+?) WHERE Code=?");
            search = connection.prepareStatement("SELECT o.Date,od.OrderId,c.Id,c.Name,it.Code,it.Description,it.UnitPrice,od.Qty,it.QtyOnHand FROM customer c JOIN poswithsql.order o ON c.Id=o.CustomerId JOIN orderdetails od ON o.Id=od.OrderId JOIN item it on od.ItemId = it.Code WHERE o.Date LIKE ? OR o.Id LIKE ? OR c.Id LIKE ? OR c.Name LIKE ? OR it.Code LIKE ? OR it.Description LIKE ? OR it.UnitPrice LIKE ? OR od.Qty LIKE ? OR it.QtyOnHand LIKE ?");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        tableOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tableOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("ItemId"));
        tableOrder.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        tableOrder.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableOrder.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tableOrder.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        tableSubOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableSubOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        tableSubOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("Qty"));
        tableSubOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("price"));
        tableSubOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("btn"));


        adddata.setDisable(true);
        orderId.setEditable(false);
        customerName.setEditable(false);
        itemDescription.setEditable(false);
        unitPrice.setEditable(false);
        qty.setEditable(false);
        qtyOnHand.setEditable(false);
        customerId.setDisable(true);
        ItemId.setDisable(true);
        newoder.requestFocus();


        try {
            ObservableList<String> customerlist = customerId.getItems();
            ResultSet resultSet = getCustomerId.executeQuery();
            while (resultSet.next()) {
                customerlist.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ObservableList<String> itemlist = ItemId.getItems();
            ResultSet resultSet = getItemCode.executeQuery();
            while (resultSet.next()) {
                itemlist.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        date.setText(String.valueOf(localDate));

        txt_searchBox.textProperty().addListener(observable -> {
            tableOrder.getItems().clear();
            if (!txt_searchBox.getText().equals("")) {
                try {
                    search.setObject(1, "%" + txt_searchBox.getText() + "%");
                    search.setObject(2, "%" + txt_searchBox.getText() + "%");
                    search.setObject(3, "%" + txt_searchBox.getText() + "%");
                    search.setObject(4, "%" + txt_searchBox.getText() + "%");
                    search.setObject(5, "%" + txt_searchBox.getText() + "%");
                    search.setObject(6, "%" + txt_searchBox.getText() + "%");
                    search.setObject(7, "%" + txt_searchBox.getText() + "%");
                    search.setObject(8, "%" + txt_searchBox.getText() + "%");
                    search.setObject(9, "%" + txt_searchBox.getText() + "%");

                    ObservableList<OrderTM> serchEnter = tableOrder.getItems();
                    ResultSet resultSet = search.executeQuery();
                    while (resultSet.next()) {
                        serchEnter.add(new OrderTM(
                                resultSet.getDate(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getDouble(7),
                                resultSet.getInt(8), resultSet.getInt(9)
                        ));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    refreshtable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            refreshtable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectedId(ActionEvent actionEvent) throws SQLException {
        inputuser = (String) customerId.getSelectionModel().getSelectedItem();
        getCustomerName.setString(1, inputuser);
        ResultSet resultSet = getCustomerName.executeQuery();
        if (resultSet.next()) {
            customerName.setText(resultSet.getString(1));
        }

    }

    public void selectedItem(ActionEvent actionEvent) throws SQLException {
        inputitem = (String) ItemId.getSelectionModel().getSelectedItem();
        getitemDetails.setString(1, inputitem);
        ResultSet resultSet = getitemDetails.executeQuery();
        while (resultSet.next()) {
            String descripton = resultSet.getString(1);
            String string = resultSet.getString(2);
            String string1 = resultSet.getString(3);

            itemDescription.setText(descripton);
            qtyOnHand.setText(string);
            unitPrice.setText(string1);
        }
    }

    public void image_homeOnAction(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/sample/style/main.fxml"));
        Scene scene = new Scene(root);
        Stage primarystage = (Stage) (this.root.getScene().getWindow());
        primarystage.setScene(scene);
        primarystage.centerOnScreen();
    }

    public void btn_neworderOnAction(MouseEvent mouseEvent) throws SQLException {
        mytotal = 0.00;
        qty.setEditable(true);
        tableSubOrder.getItems().clear();
        onCart.clear();
        total.setText("0.00");

        ResultSet resultSet = getOrderID.executeQuery();
        if (resultSet.next()) {
            String string = resultSet.getString(1);
            String[] split = string.split(":");
            int val = parseInt(split[1]);
            val++;
            orderId.setText("OD:00" + val);
        } else {
            orderId.setText("OD:001");
        }
        adddata.setDisable(false);
        customerId.setDisable(false);
        ItemId.setDisable(false);
        qty.setDisable(false);

        itemDescription.clear();
        customerName.clear();
        unitPrice.clear();
        qtyOnHand.clear();
        qty.clear();

        customerId.getSelectionModel().clearSelection();
        ItemId.getSelectionModel().clearSelection();

    }

    public void btn_adddataOnAction(MouseEvent mouseEvent) throws SQLException, JRException {

        if (parseInt(qty.getText()) <= parseInt(qtyOnHand.getText())) {

            sendOrder.setString(1, orderId.getText());
            sendOrder.setString(2, inputuser);
            sendOrder.setString(3, String.valueOf(localDate));
            sendOrder.executeUpdate();
            for (OrderTM buyItem : onCart) {
                sendOrderDetails.setString(1, String.valueOf(buyItem.getQty()));
                sendOrderDetails.setString(2, String.valueOf(buyItem.getUnitPrice()));
                sendOrderDetails.setString(3, buyItem.getOrderId());
                sendOrderDetails.setString(4, buyItem.getItemId());
                sendOrderDetails.executeUpdate();

                updateqty.setObject(1, buyItem.getQty());
                updateqty.setObject(2, buyItem.getItemId());
                updateqty.executeUpdate();
            }
            JasperReport load = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/sample/report/placeOrder.jasper"));
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();
            System.out.println(orderId.getText());
            objectObjectHashMap.put("orderId", orderId.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(load, objectObjectHashMap, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);

            onCart.clear();
            refreshtable();
            adddata.setDisable(true);
        } else {
            Alert newalert = new Alert(Alert.AlertType.CONFIRMATION, "Sorry We Have NOT This Much Of Item To Sell", ButtonType.OK);
            newalert.showAndWait();
            qty.requestFocus();
        }
    }

    public void refreshtable() throws SQLException {
        tableOrder.getItems().clear();
        ObservableList<OrderTM> myOrderTable = tableOrder.getItems();
        ResultSet resultSet = tabledetails.executeQuery();
        while (resultSet.next()) {
            myOrderTable.add(new OrderTM(resultSet.getDate(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getDouble(7),
                    resultSet.getInt(8),
                    resultSet.getInt(9)
            ));
        }
    }

    public void btn_removeOnAction(MouseEvent mouseEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Wish To Delete The Selected Order", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            OrderTM selectedItem = tableOrder.getSelectionModel().getSelectedItem();
            deleteOrder.setString(1, selectedItem.getOrderId());
            deleteOrder.executeUpdate();

            descqty.setObject(1, selectedItem.getQty());
            descqty.setObject(2, selectedItem.getItemId());
            descqty.executeUpdate();
            refreshtable();
        }
    }


    Double mytotal = 0.00;
    ObservableList<OrderTM> onCart = FXCollections.observableArrayList();


    public void addarraydata(){
        onCart.add(new OrderTM(Date.valueOf(date.getText()), orderId.getText(), inputuser, customerName.getText(), inputitem,
                itemDescription.getText(), Double.valueOf(unitPrice.getText()), Integer.valueOf(qty.getText()),parseInt(qtyOnHand.getText())));
    }
    public void btn_addToCartOnAction(ActionEvent actionEvent) {
        Boolean stats = false;
        Integer increment = -1;
        tableSubOrder.getItems().clear();
            if(!onCart.isEmpty()) {
                for (OrderTM fixerror:onCart) {
                    increment++;
                    if (fixerror.getItemId().equals(inputitem)) {
                        stats=true;
                       Integer qtyfix= fixerror.getQty()+ Integer.valueOf(qty.getText());

                        onCart.set(increment, new OrderTM(Date.valueOf(date.getText()), orderId.getText(), inputuser, customerName.getText(), inputitem,
                                itemDescription.getText(), Double.valueOf(unitPrice.getText()), qtyfix, parseInt(qtyOnHand.getText())));
                    }
                }
                if(stats.equals(false)){
                    addarraydata();
                }

            }
            else{
                addarraydata();
            }

        ObservableList<CustomerSubTM> oderCart = tableSubOrder.getItems();
        mytotal = mytotal + (Double.valueOf(qty.getText()) * Double.valueOf(unitPrice.getText()));

        for (OrderTM oncartItem : onCart) {
            Double count = Double.valueOf(oncartItem.getQty()) * Double.valueOf(oncartItem.getUnitPrice());
            total.setText(String.valueOf(mytotal));
            oderCart.add(new CustomerSubTM(oncartItem.getCustomerId(), oncartItem.getItemDescription(), oncartItem.getQty(), count,new JFXButton("DELETE")));

        }

    }
    public void btn_reportOnAction(ActionEvent actionEvent) throws JRException {


    }

}