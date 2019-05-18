/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user.peminjaman;

import com.jfoenix.controls.JFXButton;
import com.smkn4.inventaristic.pengajuan.Pengajuan_Barang;
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
    private JFXButton btnSignOut;
    @FXML
    private Pane boxPeminjaman;
    @FXML
    private Pane boxPermintaan;
    @FXML
    private Pane boxPengajuan;
    @FXML
    private Pane boxPengembalian;
    @FXML
    private Label lblNama;
    
    FXMLLoader loader;
    Map map;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setButtonsAction();
    }   
    
    private void setIdentitas() {
        lblNama.setText("Hallo, " + map.get("nama").toString() + "!");
    }
    
    private void setButtonsAction() {
        boxPengembalian.setOnMouseClicked((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/PengembalianBarang.fxml"));
                Parent viewPengembalian = loader.load();
                Stage stage = (Stage) boxPeminjaman.getScene().getWindow();
                stage.setScene(new Scene(viewPengembalian));
                stage.show();
                PengembalianBarangController controller = loader.getController();
                controller.setUserMap(this.map);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxPeminjaman.setOnMouseClicked((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/PeminjamanBarang.fxml"));
                Parent viewPinjamBarang = loader.load();
                Stage stage = (Stage) boxPeminjaman.getScene().getWindow();
                stage.setScene(new Scene(viewPinjamBarang));
                stage.show();
                PeminjamanBarangController controller = loader.getController();
                controller.setUserMap(this.map);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxPermintaan.setOnMouseClicked((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/PermintaanBarang.fxml"));
                Parent viewMintaBarang = loader.load();
                Stage stage = (Stage) boxPeminjaman.getScene().getWindow();
                stage.setScene(new Scene(viewMintaBarang));
                stage.show();
                PermintaanBarangController controller = loader.getController();
                controller.setUserMap(this.map);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxPengajuan.setOnMouseClicked((event) -> {
            new Pengajuan_Barang().setVisible(true);
        });
    }
    
    public void setUserMap(Map map) {
        this.map = map;
        setIdentitas();
    }
    
}
