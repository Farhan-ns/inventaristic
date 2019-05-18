/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.smkn4.inventaristic.admin.AdminHomeController;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class LoginScreenController implements Initializable {

    @FXML
    private JFXTextField tFieldUsername;
    @FXML
    private JFXPasswordField pFieldPassword;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private Label lblNotif;

    Map<String, String> admin = new HashMap<>();
    Connection connection;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        setLoginButton();
    }
    
    private void setLoginButton() {
        btnLogin.setOnAction((event) -> {
            validasi();
        });
    }
    
    private void validasi() {
        String username = getUsername();
        String password = getPassword();
        System.out.println("called");
        String query = "SELECT no_induk, nama, password FROM petugas WHERE no_induk = " + username;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (password.equals(rs.getString("password"))) {
                    String noInduk = rs.getString("no_induk");
                    String nama = rs.getString("nama");
                    admin.put("noInduk", noInduk);
                    admin.put("nama", nama);
                    pass();
                    lblNotif.setVisible(false);
                } else {
                    lblNotif.setVisible(true);
                }
            }
            
        } catch (SQLException e) {
            e.getCause();
            e.printStackTrace();
        }
        
    }
    
    private void pass() {
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/AdminHome.fxml"));
            Parent root = loader.load();
            AdminHomeController controller = loader.getController();
            stage.setScene(new Scene(root));
            stage.show();
            controller.setAdmin(admin);
        } catch (IOException ex) {

        }
    }

    private String getUsername() {
        return tFieldUsername.getText();
    }
    
    private String getPassword() {
        return pFieldPassword.getText();
    }
}
