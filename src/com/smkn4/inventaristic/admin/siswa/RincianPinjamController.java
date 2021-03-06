/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class RincianPinjamController implements Initializable {

    @FXML
    private Label lblNamaKelas;
    @FXML
    private Label lblTingkat;
    @FXML
    private Label lblSanksi;
    @FXML
    private TableView<Barang> tabelBarang;
    @FXML
    private TableColumn<Barang, String> colNama;
    @FXML
    private TableColumn<Barang, String> colTgl;
    @FXML
    private TableColumn<Barang, String> colStatus;
    @FXML
    private Label lblNoPinjam;

    ObservableList<Barang> barangs = FXCollections.observableArrayList();
    Connection connection;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colTgl.setCellValueFactory(new PropertyValueFactory<>("tgl"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        Platform.runLater(() -> {
            tabelBarang.getScene().getWindow().centerOnScreen();
        });
        lblNoPinjam.setVisible(false);
    }
    
    private void readData(String id) {
        if (!barangs.isEmpty()) {
            barangs.clear();
        }
        String query = "SELECT kelas.id_kelas, kelas.nama_kelas, kelas.tingkat, kelas.sanksi, peminjaman.tgl_peminjaman, "
                + " barang_masuk.nama_barang, rincian.status_barang "
                + "FROM kelas " 
                + "JOIN peminjaman ON peminjaman.`id_kelas` = kelas.`id_kelas` " 
                + "JOIN rincian ON rincian.`id_peminjaman` = peminjaman.`id_peminjaman` " 
                + "JOIN barang_masuk ON barang_masuk.`id_barang` = rincian.`id_barang` " 
                + "WHERE peminjaman.status_peminjaman = 'belum_kembali' " 
                + "HAVING kelas.id_kelas = " + id + "";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                lblNoPinjam.setVisible(true);
                lblTingkat.setVisible(false);
                lblNamaKelas.setVisible(false);
                lblSanksi.setVisible(false);
                return;
            }
            rs.first();
            lblNamaKelas.setText(lblNamaKelas.getText() + "\t: " + rs.getString("nama_kelas"));
            lblTingkat.setText(lblTingkat.getText() + "\t: " + rs.getString("tingkat"));
            lblSanksi.setText(lblSanksi.getText() + "\t: " + rs.getString("sanksi"));
            String nama, tgl, status;
            rs.beforeFirst();
            while (rs.next()) {
                nama = rs.getString("nama_barang");
                tgl = rs.getString("tgl_peminjaman");
                status = rs.getString("status_barang");
                barangs.add(new Barang(nama, tgl, status));
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        tabelBarang.setItems(barangs);
    }
    
    protected void showData(String id) {
        Platform.runLater(() -> {
            readData(id);
        });
    }
    
    public class Barang {
        String nama;
        String tgl;
        String status;

        public Barang(String nama, String tgl, String status) {
            this.nama = nama;
            this.tgl = tgl;
            this.status = status;
        }

        public String getNama() {
            return nama;
        }

        public String getTgl() {
            return tgl;
        }

        public String getStatus() {
            return status;
        }
        
        
    }
    
}
