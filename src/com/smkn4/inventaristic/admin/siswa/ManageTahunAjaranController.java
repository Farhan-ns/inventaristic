/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class ManageTahunAjaranController implements Initializable {
    
    @FXML
    private PasswordField pass;

    @FXML
    private Button btnYa;

    @FXML
    private Button btnTIdak;
    
    private Map<String, String> map;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            //btnYa.getScene().getWindow().centerOnScreen();
        });
        setButton();
    }
    
    private void setButton(){
        btnYa.setOnAction((event) -> {
           String password = pass.getText();
           
           if(checkPassword(password)){
              updateTingkatKelas(); 
           } else {
               JOptionPane.showMessageDialog(null, "Password Salah", "Error", JOptionPane.ERROR_MESSAGE);
           }
           
        });
        btnTIdak.setOnAction((event) -> {
            btnTIdak.getScene().getWindow().hide();
        });
    }
    
    private boolean checkPassword(String password){
        String userPass = map.get("password");
        System.out.println(map.get("password"));
        System.out.println(password + " == " + userPass);
        if (password.equals(userPass)) {
            return true;
        } else {
            return false;
        }
        
    }

    private void updateTingkatKelas(){
        try {
            Connection con = MySqlConnection.getConnection();
            Statement stmt = con.createStatement();
            String query = "UPDATE kelas SET tingkat = tingkat + 1 WHERE tingkat < 3";
            int status = stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Berhasil Mengubah Tingkat Kelas", "Berhasil", 1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    
}
