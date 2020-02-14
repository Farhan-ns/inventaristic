/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic;

import com.jfoenix.controls.JFXButton;
import com.smkn4.inventaristic.pengajuan.DataPengajuan;
import com.smkn4.inventaristic.user.ScanUserController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class PivotScreenController implements Initializable {

    @FXML
    private JFXButton btnLogin;
    @FXML
    private Pane bocPeminjaman;
    @FXML
    private Pane boxPengajuan;
    
    FXMLLoader loader;
    Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setBtnLogin();
        setBoxAction();
    }    
    
    public void setBoxAction() {
        bocPeminjaman.setOnMouseClicked((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                
                stage = (Stage) bocPeminjaman.getScene().getWindow();
                loader.setLocation((getClass().getResource("/com/smkn4/inventaristic/user/ScanUser.fxml")));
                Parent root = loader.load();
                
                ScanUserController controller = loader.getController();
                controller.setAuth(false);
                
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxPengajuan.setOnMouseClicked((event) -> {
            new DataPengajuan().setVisible(true);
        });
    }
    
    public void setBtnLogin() {
        btnLogin.setOnAction((event) -> {
            try {
                stage = (Stage) btnLogin.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/LoginScreen.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
    }
    
}
