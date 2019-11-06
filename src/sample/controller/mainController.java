package sample.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    public AnchorPane root;
    @FXML
    private ImageView order;
    @FXML
    private ImageView customers;
    @FXML
    private ImageView searchOrder;
    @FXML
    private ImageView item;
    @FXML
    private Label lblmanu;
    @FXML
    private Label lbldescription;


    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(2.0);
        fadeIn.play();
    }


    @FXML
    void nagative (MouseEvent event) throws IOException {

        if(event.getSource() instanceof ImageView){
            Parent root = null;
        ImageView icon = (ImageView) event.getSource();

        switch(icon.getId()) {
            case "order":
                root = FXMLLoader.load(this.getClass().getResource("/sample/style/placeOrderForm.fxml"));
                break;

            case "customers":
                root = FXMLLoader.load(this.getClass().getResource("/sample/style/manageCustomerForm.fxml"));
                break;

            case "item":
                root = FXMLLoader.load(this.getClass().getResource("/sample/style/manageItemForm.fxml"));
                break;

            case "searchOrder":
                root = (AnchorPane) FXMLLoader.load(this.getClass().getResource("/sample/style/summery.fxml"));
                break;
        }
            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }


    }

    @FXML
    void playEntranceAnimation (MouseEvent event){
        ImageView icon = (ImageView)event.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D), icon);
        scaleT.setToX(1.2D);
        scaleT.setToY(1.2D);
        scaleT.play();
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setWidth(20.0D);
        glow.setHeight(20.0D);
        glow.setRadius(40.0D);
        icon.setEffect(glow);

        String runner = icon.getId();
        switch(runner){
            case "order" :
                this.lblmanu.setText("Place Orders");
                this.lbldescription.setText("Click here if you want to place a new order or Search Order");
                break;

            case "customes" :
                this.lblmanu.setText("Manage Customers");
                this.lbldescription.setText("Click to add, edit, delete, search or view customers");
                break;

            case "item":
                this.lblmanu.setText("Manage Items");
                this.lbldescription.setText("Click to add, edit, delete, search or view items");
                break;

            case "searchOrder":
                this.lblmanu.setText("Summery");
                this.lbldescription.setText("Click To get Day Summery");
                break;

        }
    }



    @FXML
    void playExitAnimation (MouseEvent event){
        ImageView icon = (ImageView) event.getSource();
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(200.0D),icon);
        scaleT.setToX(1D);
        scaleT.setToY(1D);
        scaleT.play();
        icon.setEffect(null);
        lblmanu.setText("Welcome");
        lbldescription.setText("Plese selecct one of aboue main operations to preceed");
    }
}
