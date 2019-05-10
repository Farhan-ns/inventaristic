/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user.peminjaman;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class MenuUserController implements Initializable {

    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnPengajuan;
    @FXML
    private JFXButton btnSignOut;
    @FXML
    private Pane pnlCustomer;
    @FXML
    private Pane pnlOrders;
    @FXML
    private Pane pnlMenus;
    @FXML
    private Pane pnlOverview;
    @FXML
    private Pane boxPeminjaman;
    @FXML
    private Pane boxPermintaan;
    @FXML
    private Pane boxPengajuan;

    Map map;
    @FXML
    private Label lblNama;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }   
    
    private void setIdentitas() {
        lblNama.setText("Hallo," + map.get("nama").toString() + "!");
    }
    
    private void setButtonsAction() {
        boxPeminjaman.setOnMouseClicked((event) -> {
//            try {
//                Parent viewBarang = FXMLLoader.load(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/PeminjamanBarang.fxml"));
//                Stage stage = new Stage();
//                stage.initOwner(boxPeminjaman.getScene().getWindow());
//                stage.setScene(new Scene(viewBarang));
//                stage.show();
//                //pass the map
//            } catch (IOException ex) {
//            }
        });
    }
    
    public void setUserMap(Map map) {
        this.map = map;
        setIdentitas();
    }
    
}
