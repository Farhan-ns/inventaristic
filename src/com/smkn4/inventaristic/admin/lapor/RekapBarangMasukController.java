/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.lapor;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author barne
 */


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import java.io.IOException;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RekapBarangMasukController implements Initializable  {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnSwitch;

    @FXML
    private ComboBox<?> cbJenisBarang;

    @FXML
    private JFXButton btnMenu;

    @FXML
    private ComboBox<?> cbKondisiBarang;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnPrint;

    @FXML
    private JFXButton btnExport;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOverview;

    @FXML
    private TextField tFieldSearch;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private Label lblBBermasalah;

    @FXML
    private JFXTreeTableView<RekapBarangMasuk> tabelBarangBermasalah;
    
    Connection conn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setButtonEventAction(){
        btnReset.setOnAction((event) ->{
            try{
                
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        
        btnRefresh.setOnAction((event)->{
            readData();
        });
        
        btnMenu.setOnAction((event)->{
            try{
              Stage st = (Stage) btnMenu.getScene().getWindow();
              Parent root = FXMLLoader.load(getClass().getResource("/com/smkn4/inventaristic/admin/AdminHome.fxml")) ;
              st.setScene(new Scene(root));
              st.show();
            }catch(IOException e){
                e.printStackTrace();
            }
            
            
        });
        
        btnPrint.setOnAction((event)->{
            
        });
        
        btnExport.setOnAction((event)->{
        
        });
    }

     public void readData() {
        ObservableList<RekapBarangMasuk> barangs = FXCollections.observableArrayList();
        try {
            Statement stat = this.conn.createStatement();
            String query = "SELECT"
                    + " barang_bermasalah., barang_bermasalah.id_barang, barang_masuk.nama_barang, barang_masuk.lokasi, barang_bermasalah.jenis_masalah, "
                    + " barang_bermasalah.tgl_bermasalah, barang_masuk.total_penggunaan, barang_bermasalah.deskripsi "
                    + "FROM barang_bermasalah "
                    + " INNER JOIN barang_masuk on barang_masuk.id_barang = barang_bermasalah.id_barang";
            /*
            DEV NOTES
            Bagaimana Jika ada barang yang namanya sama namun isi di field lain berbeda?
            */
            ResultSet rs = stat.executeQuery(query);
            while(rs.next()) {
//                String[] str = get(rs);
//                barang.add(new RekapBarangMasuk(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7]));
            }
            final TreeItem<RekapBarangMasuk> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
            lblBBermasalah.setText(String.valueOf(barangs.size()));
            tabelBarangBermasalah.setRoot(root);
            tabelBarangBermasalah.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    class RekapBarangMasuk extends RecursiveTreeObject<RekapBarangMasuk>{
        public SimpleStringProperty idBarangMasuk;
        public SimpleStringProperty jenisBarangMasuk;
        public SimpleStringProperty tahunBarang;
        public SimpleStringProperty sumberPerolehan;
        public SimpleStringProperty kondisiBarang;
        public SimpleStringProperty lokasiPeny;
        public SimpleStringProperty Desk;

        public RekapBarangMasuk(SimpleStringProperty idBarangMasuk, SimpleStringProperty jenisBarangMasuk, SimpleStringProperty tahunBarang, SimpleStringProperty sumberPerolehan, SimpleStringProperty kondisiBarang, SimpleStringProperty lokasiPeny, SimpleStringProperty Desk) {
            this.idBarangMasuk = idBarangMasuk;
            this.jenisBarangMasuk = jenisBarangMasuk;
            this.tahunBarang = tahunBarang;
            this.sumberPerolehan = sumberPerolehan;
            this.kondisiBarang = kondisiBarang;
            this.lokasiPeny = lokasiPeny;
            this.Desk = Desk;
        }

        public SimpleStringProperty getIdBarangMasuk() {
            return idBarangMasuk;
        }

        public void setIdBarangMasuk(SimpleStringProperty idBarangMasuk) {
            this.idBarangMasuk = idBarangMasuk;
        }

        public SimpleStringProperty getJenisBarangMasuk() {
            return jenisBarangMasuk;
        }

        public void setJenisBarangMasuk(SimpleStringProperty jenisBarangMasuk) {
            this.jenisBarangMasuk = jenisBarangMasuk;
        }

        public SimpleStringProperty getTahunBarang() {
            return tahunBarang;
        }

        public void setTahunBarang(SimpleStringProperty tahunBarang) {
            this.tahunBarang = tahunBarang;
        }

        public SimpleStringProperty getSumberPerolehan() {
            return sumberPerolehan;
        }

        public void setSumberPerolehan(SimpleStringProperty sumberPerolehan) {
            this.sumberPerolehan = sumberPerolehan;
        }

        public SimpleStringProperty getKondisiBarang() {
            return kondisiBarang;
        }

        public void setKondisiBarang(SimpleStringProperty kondisiBarang) {
            this.kondisiBarang = kondisiBarang;
        }

        public SimpleStringProperty getLokasiPeny() {
            return lokasiPeny;
        }

        public void setLokasiPeny(SimpleStringProperty lokasiPeny) {
            this.lokasiPeny = lokasiPeny;
        }

        public SimpleStringProperty getDesk() {
            return Desk;
        }

        public void setDesk(SimpleStringProperty Desk) {
            this.Desk = Desk;
        }
        
        
        
     
    }

}

