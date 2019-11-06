package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import sample.DBConnection.DBConnection;
import sample.TableModel.CustomerTM;
import sample.TableModel.ItemTM;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class itemController implements Initializable {
    public AnchorPane root;
    public ImageView home;
    public JFXButton btnAdd;
    public JFXTextField itemCode;
    public JFXTextField itemDescription;
    public JFXTextField QtyOnHand;
    public JFXTextField unitPrice;
    public Button btnsave;
    public Button btnDelete;
    public TableView <ItemTM>tableItem;
    public TextField txt_search;


    private PreparedStatement addData;
    private PreparedStatement getData;
    private PreparedStatement deleteData;
    private PreparedStatement updatedata;
    private PreparedStatement autoIncrement;
    private PreparedStatement search;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
           Connection connection =DBConnection.getInstance().getConnection();
           addData = connection.prepareStatement("INSERT INTO item VALUES(?,?,?,?)");
            getData = connection.prepareStatement("SELECT * FROM item");
            deleteData= connection.prepareStatement("DELETE FROM item WHERE Code=?");
            updatedata = connection.prepareStatement("UPDATE item SET Description=?,QtyOnHand=?,UnitPrice=? WHERE Code=?");
            autoIncrement = connection.prepareStatement("SELECT Code FROM item ORDER BY Code DESC LIMIT 1");
            search=connection.prepareStatement("SELECT * FROM item WHERE Code LIKE ? OR Description LIKE ? OR QtyOnHand LIKE ? OR UnitPrice LIKE ? ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tableItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
        tableItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tableItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        
        itemCode.setDisable(true);
        itemDescription.setDisable(true);
        QtyOnHand.setDisable(true);
        unitPrice.setDisable(true);


        tableItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {
                ItemTM itemsselect = tableItem.getSelectionModel().getSelectedItem();

                if( itemsselect == null){
                    btnsave.setText("Save");
                    btnDelete.setDisable(true);


                }
                else{
                    btnsave.setText("Update");
                    btnsave.setDisable(false);
                    btnDelete.setDisable(false);
                    itemDescription.setDisable(false);
                    QtyOnHand.setDisable(false);
                    unitPrice.setDisable(false);
                    itemCode.setText(itemsselect.getItemCode());
                    itemDescription.setText(itemsselect.getItemDescription());
                    QtyOnHand.setText(itemsselect.getQtyOnHand());
                    unitPrice.setText(itemsselect.getUnitPrice());
                }
            }
        });

        txt_search.textProperty().addListener(observable -> {
            tableItem.getItems().clear();
            try {
                search.setString(1,"%"+txt_search.getText()+"%");
                search.setString(2,"%"+txt_search.getText()+"%");
                search.setString(3,"%"+txt_search.getText()+"%");
                search.setString(4,"%"+txt_search.getText()+"%");
                ResultSet resultSet = search.executeQuery();
                ObservableList<ItemTM> item = tableItem.getItems();
                while (resultSet.next()){
                    item.add(new ItemTM(resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)));

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            refreshmytable();
        } catch (SQLException e) {
        }
    }

    public void image_homeOnAction(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/sample/style/main.fxml"));
        Scene sence = new Scene(root);
        Stage primaryStage = (Stage)(this.root.getScene().getWindow());
        primaryStage.setScene(sence);
        primaryStage.centerOnScreen();
    }


    public void btn_addOnAction(ActionEvent actionEvent) throws SQLException {
        itemCode.clear();
        itemDescription.clear();
        QtyOnHand.clear();
        unitPrice.clear();
        itemDescription.setDisable(false);
        QtyOnHand.setDisable(false);
        unitPrice.setDisable(false);
        itemDescription.requestFocus();
        btnsave.setText("Save");

        ResultSet resultSet = autoIncrement.executeQuery();
        if(resultSet.next()){
            String string = resultSet.getString(1);
            String[] split = string.split(":");
            int count = Integer.parseInt(split[1]);
            count++;
            itemCode.setText("I:00"+count);
        }
        else{
            itemCode.setText("I:001");
        }
    }

    public void txtbox_itemCodeOnAction(ActionEvent actionEvent) { }

    public void txtbox_itemDescriptionOnAction(ActionEvent actionEvent) {
        QtyOnHand.requestFocus();
    }

    public void txtbox_qtyOnHandOnAction(ActionEvent actionEvent) {unitPrice.requestFocus();}

    public void txtbox_unitPriceOnAction(ActionEvent actionEvent) { }

    public void btn_saveOnAction(ActionEvent actionEvent) throws SQLException {
        String itemid = itemCode.getText();
        String description = itemDescription.getText();
        String quantity = QtyOnHand.getText();
        String unitprice1 = unitPrice.getText();

        if(!description.equals("")&&!quantity.equals("")&&!unitprice1.equals("")){
                if(!description.matches("^(.|\\s)*[a-zA-Z]+(.|\\s)*$")){
                    Alert newalaert = new Alert(Alert.AlertType.CONFIRMATION,"Please Fill valid Description with charactors",ButtonType.OK);
                    newalaert.showAndWait();
                    itemDescription.requestFocus();
                    return;
                }
                else if(!quantity.matches("[0-9]+")){
                    Alert newalert =new Alert(Alert.AlertType.CONFIRMATION,"Please use only numbers",ButtonType.OK);
                    newalert.showAndWait();
                    QtyOnHand.requestFocus();
                    return;
                }
                else if(!unitprice1.matches("([0-9]+.[0-9]{0,2})")){
                    Alert newalert = new Alert(Alert.AlertType.CONFIRMATION,"please use only numbers",ButtonType.OK);
                    newalert.showAndWait();
                    unitPrice.requestFocus();
                    return;
                }
                else {
                    if (btnsave.getText().equals("Save")) {

                       addData.setString(1,itemCode.getText());
                       addData.setString(2,description);
                       addData.setString(3,quantity);
                       addData.setString(4,unitprice1);

                        addData.executeUpdate();

                        refreshmytable();

                        itemDescription.clear();
                        QtyOnHand.clear();
                        unitPrice.clear();
                        itemDescription.setDisable(true);
                        QtyOnHand.setDisable(true);
                        unitPrice.setDisable(true);
                    }
                    else{
                        updatedata.setString(4,itemCode.getText());
                        updatedata.setString(1,description);
                        updatedata.setString(2,quantity);
                        updatedata.setString(3,unitprice1);

                        updatedata.executeUpdate();

                        refreshmytable();

                        itemCode.clear();
                        itemDescription.clear();
                        QtyOnHand.clear();
                        unitPrice.clear();
                        itemDescription.setDisable(true);
                        QtyOnHand.setDisable(true);
                        unitPrice.setDisable(true);
                        btnsave.setText("Save");
                    }
                }
        }
        else{
            if(description.equals("")){
                Alert newalart = new Alert(Alert.AlertType.WARNING, "Please Fill Item Description", ButtonType.OK);
                newalart.showAndWait();
                itemDescription.requestFocus();
                return;
            }
            else if(quantity.equals("")){
                Alert newalart = new Alert(Alert.AlertType.WARNING,"Plese Fill Quantity On Hand",ButtonType.OK);
                newalart.showAndWait();
                QtyOnHand.requestFocus();
                return;
            }
            else if(unitprice1.equals("")){
                Alert newalert = new Alert(Alert.AlertType.WARNING,"Please fill Unit Price",ButtonType.OK);
                newalert.showAndWait();
                unitPrice.requestFocus();
                return;
            }
        }

    }

    private void refreshmytable() throws SQLException {
        tableItem.getItems().clear();
        ObservableList<ItemTM> items = tableItem.getItems();
        ResultSet resultSet =getData.executeQuery();
        while(resultSet.next()){
            items.add(new ItemTM(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
    }


    public void btn_DeleteOnAction(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this item?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            ItemTM selectedItem = tableItem.getSelectionModel().getSelectedItem();
            deleteData.setString(1,selectedItem.getItemCode());
            deleteData.executeUpdate();
            refreshmytable();

            itemCode.clear();
            itemDescription.clear();
            QtyOnHand.clear();
            unitPrice.clear();
            itemDescription.setDisable(true);
            QtyOnHand.setDisable(true);
            unitPrice.setDisable(true);

        }
    }


    public void btn_reportOnAction(ActionEvent actionEvent) throws JRException {
        JasperDesign load = JRXmlLoader.load(itemController.class.getResourceAsStream("/sample/report/itemReport.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        Map<String,Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);


    }
}
