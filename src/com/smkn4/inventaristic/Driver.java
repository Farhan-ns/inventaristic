/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Farhanunnasih
 */
public class Driver extends Application{

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/StokBarang.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/com/smkn4/inventaristic/center-column.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.getCause();
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    } 
    
}
