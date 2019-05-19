/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.laporan;

import com.jfoenix.controls.JFXButton;
import com.smkn4.inventaristic.admin.laporan.rekap.MenuRekap;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class LaporanController implements Initializable {

    @FXML
    private JFXButton btnBB;
    @FXML
    private JFXButton btnBM;
    @FXML
    private JFXButton btnP;
    @FXML
    private JFXButton btnPK;
    @FXML
    private JFXButton btnMP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setButtonAction();
        Platform.runLater(() -> {
            btnBB.getScene().getWindow().centerOnScreen();
        });
    }   
    
    private void setButtonAction() {
        btnBB.setOnAction((event) -> {
            new barang_bermasalah().setVisible(true);
        });
        btnBM.setOnAction((event) -> {
            new barang_masuk().setVisible(true);
        });
        btnMP.setOnAction((event) -> {
            new MenuRekap().setVisible(true);
        });
        btnP.setOnAction((event) -> {
            new permintaan_barang().setVisible(true);
        });
        btnPK.setOnAction((event) -> {
            new pinjam_kembali().setVisible(true);
        });
    }
    
}
