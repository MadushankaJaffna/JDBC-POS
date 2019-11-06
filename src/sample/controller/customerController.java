package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.data.DataSourceCollection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import sample.DBConnection.DBConnection;
import sample.TableModel.CustomerTM;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class customerController implements Initializable {
    public JFXTextField customerName;
    public JFXTextField customerId;
    public JFXTextField customerAddress;
    public ImageView home;
    public JFXButton newCustomer;
    public Button save;
    public Button delete;
    public TableView <CustomerTM>table;
    public AnchorPane root;

    public TextField txt_searchBox;
    private PreparedStatement setdata;
    private PreparedStatement getdata;
    private PreparedStatement deletedata;
    private PreparedStatement getlastId;
    private PreparedStatement search;


    int delet=0;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            setdata = connection.prepareStatement("INSERT INTO customer VALUES(?,?,?)");
            getdata = connection.prepareStatement("SELECT * FROM customer");
            deletedata = connection.prepareStatement("DELETE FROM customer WHERE Id=?");
            getlastId = connection.prepareStatement("SELECT Id FROM customer ORDER BY Id desc LIMIT 1");
            search = connection.prepareStatement("SELECT * FROM customer WHERE Id LIKE ? OR Name LIKE ? OR Address LIKE ?");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerID"));
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerAddress"));

        customerName.setDisable(true);
        customerAddress.setDisable(true);
        customerId.setDisable(true);
        save.setDisable(true);

    txt_searchBox.textProperty().addListener(observable ->  {
        table.getItems().clear();

        try {
            search.setString(1,"%"+txt_searchBox.getText()+"%");
            search.setString(2,"%"+txt_searchBox.getText()+"%");
            search.setString(3,"%"+txt_searchBox.getText()+"%");
            ResultSet  resultSet = search.executeQuery();
            ObservableList<CustomerTM> searchItem =  table.getItems();
            while(resultSet.next()) {
                searchItem.add(new CustomerTM(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    });
        try {
            refreshmytable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void image_homeClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/sample/style/main.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage)(this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

    }


    public void btn_newcustomerClick(ActionEvent actionEvent) throws SQLException {
        customerId.clear();
        customerAddress.clear();
        customerName.clear();
        customerName.setDisable(false);
        customerAddress.setDisable(false);
        save.setDisable(false);
        customerName.requestFocus();

        table.getSelectionModel().clearSelection();

        ResultSet resultSet = getlastId.executeQuery();
        if(resultSet.next()){
            String getid = resultSet.getString(1);
            String[] split = getid.split(":");
            int val = Integer.parseInt(split[1]);
            val++;
            customerId.setText("C:00"+val);
        }
        else{
            customerId.setText("C:001");
        }

    }



    public void btn_saveClick(ActionEvent actionEvent) throws SQLException {
        String id =customerId.getText();
        String name = customerName.getText();
        String address = customerAddress.getText();
        if( !name.equals("")&& !address.equals(""))
                if(!name.matches("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Plese enter valueble name with only use charactors",
                            ButtonType.OK);
                    alert.showAndWait();
                    customerName.requestFocus();
                    return;
                }
                else if(!address.matches("\\S+[a-zA-Z0-9/.,:\\sa-zA-Z0-9]+\\S+")){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Plese enter valueble address with only use charactors and numbers ",
                            ButtonType.OK);
                    alert.showAndWait();
                    customerAddress.requestFocus();
                    return;
                }
                else {
                   setdata.setString(1,id);
                   setdata.setString(2,name);
                   setdata.setString(3,address);

                   setdata.executeUpdate();

                   refreshmytable();
                }


        else{
            if(name.equals("")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please Enter Customer Name",
                        ButtonType.OK);
                alert.showAndWait();
                customerName.requestFocus();
                return;

            }
            if(address.equals("")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please Enter Customer address",
                        ButtonType.OK);
                alert.showAndWait();
                customerAddress.requestFocus();
                return;

            }
        }
        customerId.clear();
        customerAddress.clear();
        customerName.clear();
        customerName.setDisable(true);
        customerAddress.setDisable(true);

    }

    private void refreshmytable() throws SQLException {
        table.getItems().clear();
        ObservableList<CustomerTM> customer = table.getItems();
        ResultSet resultSet = getdata.executeQuery();
        while(resultSet.next()){
            customer.add(new CustomerTM(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)));
        }
    }


    public void btn_deleteClick(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether you want to delete this customer?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES){
            CustomerTM selected = table.getSelectionModel().getSelectedItem();
            deletedata.setString(1,selected.getCustomerID());
            deletedata.executeUpdate();
            refreshmytable();
        }

    }


    public void customerIdOnAction(ActionEvent actionEvent) {

    }

    public void customerNameOnAction(ActionEvent actionEvent) {
        customerAddress.requestFocus();
    }

    public void customerAddressOnAction(ActionEvent actionEvent) {

    }
    public void btn_reportOnAction(ActionEvent actionEvent) throws JRException {

        JasperDesign load = JRXmlLoader.load(customerController.class.getResourceAsStream("/sample/report/customer.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(load);

        Map<String,Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,new JRBeanCollectionDataSource(table.getItems()));
        JasperViewer.viewReport(jasperPrint,false);

    }
}
