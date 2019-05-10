/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin;

import com.smkn4.inventaristic.admin.siswa.DataPeminjam;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class AdminHomeController implements Initializable {

    @FXML
    private Button btnOverview;
    @FXML
    private Button btnMenus1;
    @FXML
    private Button btnOrders;
    @FXML
    private Button btnOrders1;
    @FXML
    private Button btnMenus;
    @FXML
    private Button btnMenus2;
    @FXML
    private Pane pnlCustomer;
    @FXML
    private Pane pnlOrders;
    @FXML
    private Pane pnlMenus;
    @FXML
    private Pane pnlOverview;
    @FXML
    private Pane boxPengajuan;
    @FXML
    private Pane boxSiswa;
    @FXML
    private Pane boxLaporan;
    @FXML
    private Pane boxBarang;
    @FXML
    private Pane bocPeminjaman;
    
    FXMLLoader loader;
    Stage stage = new Stage();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setBoxAction();
    }
    
    private void setBoxAction() {
        boxBarang.setOnMouseClicked((event) -> {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/StokBarang.fxml")));
                stage.initOwner(boxBarang.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                
            }
        });
        boxSiswa.setOnMouseClicked((event) -> {
            new DataPeminjam().setVisible(true);
        });
    }

    @FXML
    private void handleClicks(ActionEvent event) {
    }
    
}
