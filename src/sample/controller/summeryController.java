package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import sample.DBConnection.DBConnection;
import sample.TableModel.summeryTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class summeryController implements Initializable {
    public Text date;
    public Text summeryBalance;
    public Button back;
    public Button print;
    public TableView<summeryTM> summeryTable;
    public AnchorPane root;

    private PreparedStatement summerydata;

    LocalDate dateNow = LocalDate.now();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        summeryTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        summeryTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("desccription"));
        summeryTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        summeryTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        summeryTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            summerydata = connection.prepareStatement("SELECT o.ItemId,i.Description,o.UnitPrice,o.Qty,o.UnitPrice*o.Qty AS total FROM item i JOIN orderdetails o ON i.Code= o.ItemId JOIN poswithsql.order od ON od.Id=o.OrderId WHERE od.Date LIKE current_date");


            Double total=0.00;
            ObservableList<summeryTM> summery = summeryTable.getItems();
            ResultSet resultSet = summerydata.executeQuery();
            while(resultSet.next()){
                total = total + resultSet.getDouble(5);
                Integer increment = -1;
                Boolean stats = false;
                if(!summery.isEmpty()) {
                    for (summeryTM checking : summery) {
                        increment++;
                        if(checking.getItemId().equals(resultSet.getString(1))){
                            stats=true;
                            summery.set(increment, new summeryTM(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getDouble(3),
                                    resultSet.getInt(4)+checking.getQty(),
                                    resultSet.getDouble(5)+checking.getTotal()));
                        }

                    }
                    if(stats.equals(false)){
                        summery.add(new summeryTM(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getDouble(3),
                                resultSet.getInt(4),
                                resultSet.getDouble(5)));
                    }
                }
                else {
                    summery.add(new summeryTM(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getInt(4),
                            resultSet.getDouble(5)
                    ));
                }
            }
            summeryBalance.setText(String.valueOf(total));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        date.setText(String.valueOf(dateNow));

    }

    public void btn_BackOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/sample/style/main.fxml"));
        Scene scene = new Scene(root);
        Stage primarystage = (Stage) (this.root.getScene().getWindow());
        primarystage.setScene(scene);
        primarystage.centerOnScreen();
    }

    public void btn_PrintOnAction(ActionEvent actionEvent) throws JRException {
        JasperDesign load = JRXmlLoader.load(customerController.class.getResourceAsStream("/sample/report/summeryReport.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(load);

        Map<String,Object> params = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,new JRBeanCollectionDataSource(summeryTable.getItems()));
        JasperViewer.viewReport(jasperPrint,false);
    }
}
