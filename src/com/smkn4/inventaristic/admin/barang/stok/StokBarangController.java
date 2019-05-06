/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.barang.stok;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.smkn4.inventaristic.util.EnumParser;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class StokBarangController implements Initializable {

    @FXML
    private ImageView btnBack;

    @FXML
    private JFXButton btnStokBarang;

    @FXML
    private JFXButton btnBarangBermasalah;

    @FXML
    private Label lblTotalBarang;

    @FXML
    private Label lblBBermasalah;

    @FXML
    private JFXTreeTableView<Barang> tabelStokBarang;
    
    Connection connection;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXTreeTableColumn<Barang, String> colNama = new JFXTreeTableColumn<>("Nama");
        colNama.setPrefWidth(150);
        colNama.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().namaBarang);

        JFXTreeTableColumn<Barang, String> colJenis = new JFXTreeTableColumn<>("Jenis");
        colJenis.setPrefWidth(90);
        colJenis.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().jenisBarang);
        
        JFXTreeTableColumn<Barang, String> colTgl = new JFXTreeTableColumn<>("Tgl Masuk");
        colTgl.setPrefWidth(150);
        colTgl.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().tanggalMasuk);
        
        JFXTreeTableColumn<Barang, String> colKondisi = new JFXTreeTableColumn<>("Kondisi");
        colKondisi.setPrefWidth(150);
        colKondisi.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().kondisi);
        
        JFXTreeTableColumn<Barang, String> colLokasi = new JFXTreeTableColumn<>("Lokasi");
        colLokasi.setPrefWidth(150);
        colLokasi.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().lokasi);
        
        JFXTreeTableColumn<Barang, String> colUmur = new JFXTreeTableColumn<>("Umur");
        colUmur.setPrefWidth(150);
        colUmur.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().totalPenggunaan);
        
        JFXTreeTableColumn<Barang, String> colPinjaman = new JFXTreeTableColumn<>("Pinjaman");
        colPinjaman.setPrefWidth(150);
        colPinjaman.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().dapatDipinjam);
        
        ObservableList<Barang> barangs = FXCollections.observableArrayList();
//        barangs.add(new Barang("Proyektor", "Aset", "10-10-2019", "Baik", "A2", "10 jam", "Ya"));

        this.connection = MySqlConnection.getConnection();
        try {
            Statement stat = this.connection.createStatement();
            String query = "SELECT *, COUNT(nama_barang) FROM barang_masuk GROUP BY nama_barang";
            /*
            DEV NOTES
            Bagaimana Jika ada barang yang namanya sama namun isi di field lain berbeda?
            */
            ResultSet rs = stat.executeQuery(query);
            while(rs.next()) {
                String[] str = readData(rs);
                barangs.add(new Barang(str[0], str[1], str[2], str[3], str[4], str[5], str[6]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        lblTotalBarang.setText(String.valueOf(barangs.size()));
        

        final TreeItem<Barang> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
        tabelStokBarang.getColumns().setAll(colNama, colJenis, colTgl, colKondisi, colLokasi, colUmur, colPinjaman);
        tabelStokBarang.setRoot(root);
        tabelStokBarang.setShowRoot(false);

    }
  
    
    class Barang extends RecursiveTreeObject<Barang> {
        SimpleStringProperty namaBarang;
        SimpleStringProperty jenisBarang;
        SimpleStringProperty tanggalMasuk;
        SimpleStringProperty kondisi;
        SimpleStringProperty lokasi;
        SimpleStringProperty totalPenggunaan;
        SimpleStringProperty dapatDipinjam;
//        SimpleStringProperty deskripsi;
//        SimpleStringProperty petugas;

        public Barang(String namaBarang, String jenisBarang, String tanggalMasuk, String kondisi, String lokasi, String totalPenggunaan,String dapatDipinjam) {
            this.namaBarang = new SimpleStringProperty(namaBarang);
            this.jenisBarang = new SimpleStringProperty(jenisBarang);
            this.tanggalMasuk = new SimpleStringProperty(tanggalMasuk);
            this.dapatDipinjam = new SimpleStringProperty(dapatDipinjam);
            this.kondisi = new SimpleStringProperty(kondisi);
            this.lokasi = new SimpleStringProperty(lokasi);
            this.totalPenggunaan = new SimpleStringProperty(totalPenggunaan);
//            this.deskripsi = new SimpleStringProperty(deskripsi);
//            this.petugas = new SimpleStringProperty(petugas);
        }
    }
    
    public String[] readData(ResultSet rs) {
        try {
            String idBarang = rs.getString("id_barang");
            String namaBarang = rs.getString("nama_barang");
            String jenisBarang = rs.getString("jenis_barang");
            jenisBarang = EnumParser.format(jenisBarang);
            Date tgl = rs.getDate("tgl_masuk");
            String tanggalMasuk = DateFormatUtils.format(tgl, "dd-MM-yyy");
            String kondisi = rs.getString("kondisi");
            String lokasi = rs.getString("lokasi");
            String kuantitas = rs.getString("COUNT(nama_barang)");
            String umur = rs.getString("total_penggunaan");
            String dapatDipinjam = rs.getString("dapat_dipinjam");
            /*
            namaBarang;
            jenisBarang;
            tanggalMasuk;
            kondisi;
            lokasi;
            totalPenggunaan;
            dapatDipinjam;
            */
            return new String[]{namaBarang, jenisBarang, tanggalMasuk, kondisi, lokasi, umur, dapatDipinjam};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}