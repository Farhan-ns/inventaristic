/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user;

import com.jfoenix.controls.JFXButton;
import com.smkn4.inventaristic.pengajuan.Pengajuan_Barang;
import com.smkn4.inventaristic.user.peminjaman.MenuUserController;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class ScanUserController implements Initializable {

    @FXML
    private JFXButton btnMenu;
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
    private TextField tFieldUser;
    @FXML
    private Label lblFail;

    String barcodeCache = new String();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setFieldAction();
        Platform.runLater(() -> {
//            tFieldUser.requestFocus();
            setScanAction();
        });
    }
    
    private void setFieldAction() {
//        tFieldUser.setOnAction((event) -> {
//            scanUser();
//        });
        btnMenu.setOnAction((event) -> {
            try {
                Stage stage = (Stage) btnMenu.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/AdminHome.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
    }
    
    private void setScanAction() {
        Scene scene = btnMenu.getScene();
        if (scene == null) {
            System.out.println("scene is null");
        }
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                lblFail.setVisible(false);
                if (event.getCode() == KeyCode.ENTER) {
                    scanUser(barcodeCache);
                    barcodeCache = new String();
                } else {
                    barcodeCache += getInput(event);
                }
            }
        });
    }

    private String getInput(KeyEvent event) {
        if (event.getText().matches("[0-9]+")) {
            return event.getText();
        } else {
            return "";
        }
    }
    
    private void scanUser(String nis) {
        if (nis == null || nis.length() < 10) {
            System.out.println("GAGAL : " + nis);
            lblFail.setVisible(true);
            return;
        } else {
            System.out.println("NIS: " + nis);
            checkUser(nis);
        }
    }
    
    private void checkUser(String nis) {
        try {
            Connection connection = MySqlConnection.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT nis, nama_siswa, kelas, sanksi  FROM siswa WHERE nis = " + "'" + nis +"'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs != null && rs.next()) {
                Map map = new HashMap();
                map.put("nis", rs.getString("nis"));
                map.put("nama", rs.getString("nama_siswa"));
                map.put("kelas", rs.getString("kelas"));
                map.put("sanksi", rs.getString("sanksi"));
                grant(map);
                connection.close();
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    private void grant(Map map) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/MenuUser.fxml"));
            Parent viewBarang = loader.load();
            Stage stage = (Stage) tFieldUser.getScene().getWindow();
            stage.setScene(new Scene(viewBarang));
//            tFieldUser.getScene().getWindow().hide();
            stage.show();
            MenuUserController controller = loader.getController();
            controller.setUserMap(map);
        } catch (IOException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
}
