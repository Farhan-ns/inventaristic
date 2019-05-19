/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class ManageBeriSanksiController implements Initializable {

    @FXML
    private Label lblNama;
    @FXML
    private TextField tFIeldSanksi;
    @FXML
    private Button btnOk;

    String nis = "";
    Connection connection;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        setOkAction();
        Platform.runLater(() -> {
            btnOk.getScene().getWindow().centerOnScreen();
        });
    }    
    
    private void setOkAction() {
        btnOk.setOnAction((event) -> {
            simpanData();
        });
        tFIeldSanksi.setOnAction((event) -> {
            simpanData();
        });
    }
    
    private void showData(String nis) {
        String query = "SELECT siswa.nama_siswa, siswa.sanksi FROM siswa WHERE nis = " + nis;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            lblNama.setText(lblNama.getText() + " : " + rs.getString("nama_siswa"));
            tFIeldSanksi.setText(rs.getString("sanksi"));
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    protected void fookinRunit(String id) {
        Platform.runLater(() -> {
            showData(id);
            this.nis = id;
        });
    }
    
    private void simpanData() {
        String query = "UPDATE siswa SET sanksi = ? WHERE nis = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, tFIeldSanksi.getText());
            ps.setString(2, this.nis);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Berhasil");
            tFIeldSanksi.getScene().getWindow().hide();
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
}
