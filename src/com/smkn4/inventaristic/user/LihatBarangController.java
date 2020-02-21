/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user;

import com.jfoenix.controls.JFXTextField;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class LihatBarangController implements Initializable {

    ObservableList<Barang> barangs = FXCollections.observableArrayList();
    Connection connection;
    
    @FXML
    private TableView<Barang> tabelBarang;
    @FXML
    private TableColumn<Barang, String> colNama;
    @FXML
    private TableColumn<Barang, String> colLokasi;
    @FXML
    private TableColumn<Barang, String> colJml;
    @FXML
    private TableColumn<Barang, String> colKode;
    @FXML
    private JFXTextField tFieldSearch;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colJml.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        colKode.setCellValueFactory(new PropertyValueFactory<>("kode"));
        Platform.runLater(() -> {
            readData("");
        });
        setSearchBar();
    }    
    
    private void setSearchBar() {
        tFieldSearch.setOnKeyReleased((event) -> {
            readData(tFieldSearch.getText());
        });
    }
    
    private void readData(String search) {
        barangs.clear();
        String query = "SELECT barang_masuk.nama_barang, barang_masuk.lokasi, COUNT(id_barang) AS jumlah, barang_masuk.id_barang "
                + " FROM barang_masuk ";
        if (!search.isEmpty()) {
            query += "WHERE nama_barang LIKE '%" + search + "%'";
        }
        query += " GROUP BY nama_barang, lokasi";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            String nama, lokasi, jumlah, kode;
            while (rs.next()) {
                nama = rs.getString("nama_barang");
                lokasi = rs.getString("lokasi");
                jumlah = rs.getString("jumlah");
                kode = "SMKN4BDG-" + rs.getString("id_barang");
                barangs.add(new Barang(nama, lokasi, jumlah, kode));
                no++;
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        tabelBarang.setItems(barangs);
    }
    
    public class Barang {
        String nama;
        String lokasi;
        String jumlah;
        String kode;

        public Barang(String nama, String lokasi, String jumlah, String kode) {
            this.nama = nama;
            this.lokasi = lokasi;
            this.jumlah = jumlah;
            this.kode = kode;
        }

        public String getNama() {
            return nama;
        }

        public String getLokasi() {
            return lokasi;
        }

        public String getJumlah() {
            return jumlah;
        }

        public String getKode() {
            return kode;
        }
    }
    
}
