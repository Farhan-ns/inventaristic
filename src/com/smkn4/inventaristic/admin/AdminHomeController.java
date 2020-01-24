/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin;

import com.jfoenix.controls.JFXButton;
import com.smkn4.inventaristic.pengajuan.DataPengajuan;
import com.smkn4.inventaristic.user.ScanUserController;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    private Label lblNama;
    @FXML
    private JFXButton btnSignOut;

    Map<String, String> admin = new HashMap<>();
    FXMLLoader loader;
    Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            boxBarang.getScene().getWindow().centerOnScreen();
        });
        setBoxAction();
    }

    private void setBoxAction() {
        btnSignOut.setOnAction((event) -> {
            this.admin.clear();
            try {
                stage = (Stage) boxBarang.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/LoginScreen.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxBarang.setOnMouseClicked((event) -> {
            try {
                stage = (Stage) boxBarang.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/StokBarang.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxSiswa.setOnMouseClicked((event) -> {
            try {
                stage = (Stage) boxBarang.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/siswa/DataKelas.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        bocPeminjaman.setOnMouseClicked((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader();

                stage = (Stage) bocPeminjaman.getScene().getWindow();
                loader.setLocation((getClass().getResource("/com/smkn4/inventaristic/user/ScanUser.fxml")));
                Parent root = loader.load();

                ScanUserController controller = loader.getController();
                controller.setAuth(true);

                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        boxLaporan.setOnMouseClicked((event) -> {
            try {
                stage = new Stage();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/laporan/Laporan.fxml")));
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

    public void setAdmin(Map<String, String> map) {
        admin = map;
        lblNama.setText(admin.get("nama"));
    }

    @FXML
    private void handleClicks(ActionEvent event) {
    }
}
