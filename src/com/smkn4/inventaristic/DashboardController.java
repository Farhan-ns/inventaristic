/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DashboardController implements Initializable {

    @FXML
    private Pane boxBarang;
    @FXML
    private Pane boxLaporan;
    @FXML
    private Pane boxSiswa;
    @FXML
    private Pane boxPengajuan;
    @FXML
    private Pane boxPeminjaman;
    
    FXMLLoader loader = new FXMLLoader();
    Stage stage = new Stage();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setBoxesAction();
    }   
    
    private void setBoxesAction() {
        boxBarang.setOnMouseClicked((event) -> {
            loader.setLocation(getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/StokBarang.fxml"));
            try {
                Parent viewBarang = loader.load();
                stage.initOwner(boxBarang.getScene().getWindow());
                stage.setScene(new Scene(viewBarang));
                stage.show();
            } catch (IOException ex) {
            }
        });
    }
    public void switchScene(Scene scene) {
        this.stage.setScene(scene);
        stage.show();
    }
    
}
