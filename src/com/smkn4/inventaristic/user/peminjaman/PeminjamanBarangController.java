/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user.peminjaman;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.smkn4.inventaristic.util.JenisBarangException;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class PeminjamanBarangController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnPengajuan;
    @FXML
    private JFXButton btnSignOut;
    @FXML
    private JFXTreeTableView<Barang> tabelBarangPinjam;
    @FXML
    private Label lblTotal;
    @FXML
    private JFXButton btnPinjam;
    @FXML
    private JFXTextField tFieldSearch;
    @FXML
    private TextField tFieldScanBarang;        
    @FXML
    private Label lblNotAset;
    
    ObservableList<Barang> barangs = FXCollections.observableArrayList();
    Set<String> dTracker = new HashSet<>();
    Map<String, String> map;
    Connection connection;
    int noUrut = 1;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXTreeTableColumn<Barang, String> colNo = new JFXTreeTableColumn<>("No");
        colNo.setPrefWidth(150);
        colNo.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().noUrut);
        
        JFXTreeTableColumn<Barang, String> colNama = new JFXTreeTableColumn<>("Nama");
        colNama.setPrefWidth(150);
        colNama.getStyleClass().add("tree-table-cell");
        colNama.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().namaBarang);
        
        JFXTreeTableColumn<Barang, String> colKode = new JFXTreeTableColumn<>("Kode Barang");
        colKode.setPrefWidth(150);
        colKode.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().kodeBarang);
        tabelBarangPinjam.getColumns().setAll(colNo, colNama, colKode);
        this.connection = MySqlConnection.getConnection();
//        this.noUrut = 1;
        
        setScanAction();
        setButtonAction();
    }
    
    private void setButtonAction() {
        btnPinjam.setOnAction((event) -> {
            getTanggalToday();
        });
    }
    
    private void setScanAction() {
        tFieldScanBarang.setOnAction((event) -> {
            String kodeBarang = tFieldScanBarang.getText();
            String[] str = kodeBarang.split("-");
            if (!str[0].equals("TI4")) {
                System.out.println(kodeBarang);
                System.out.println("bukan barang smkn 4");
            } else {
                if (isNotBarangDuplicate(str[1])) {
                    try {
                        showBarang(str[1], kodeBarang);
                    } catch (JenisBarangException e) {
                        lblNotAset.setVisible(true);
                    }
                }
            }
        });
    }
    
    private void showBarang(String idBarang, String  kodeBarang) throws JenisBarangException {
        //BUG CSS PROBLEM
        String query = "SELECT id_barang, nama_barang, jenis_barang FROM barang_masuk WHERE id_barang = " + idBarang;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            
            if (!rs.getString("jenis_barang").toLowerCase().equals("aset")) {
                throw new JenisBarangException("Bukan Barang jenis aset");
            }
            
            String namaBarang = rs.getString("nama_barang");
            String noUrut = String.valueOf(this.noUrut);
            this.barangs.add(new Barang(noUrut, idBarang, namaBarang, kodeBarang));
            this.noUrut++;
            lblNotAset.setVisible(false);
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        System.out.println(barangs.size());
        final TreeItem<Barang> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
        tabelBarangPinjam.setRoot(root);
        tabelBarangPinjam.setShowRoot(false);
    }
    
    private boolean isNotBarangDuplicate(String idBarang) {
        return dTracker.add(idBarang);
    }
    
    private String getTanggalToday() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    
    protected void setUserMap(Map map) {
        this.map = map;
        setUsername();
    }
    private void setUsername() {
        lblUsername.setText("Halo, "+ this.map.get("nama") + " !");
    }
    
    class Barang extends RecursiveTreeObject<Barang> {
        SimpleStringProperty noUrut;
        SimpleStringProperty idBarang;
        SimpleStringProperty namaBarang;
        SimpleStringProperty kodeBarang;

        public Barang(String noUrut, String idBarang, String namaBarang, String kodeBarang) {
            this.noUrut = new SimpleStringProperty(noUrut);
            this.idBarang = new SimpleStringProperty(idBarang);
            this.namaBarang = new SimpleStringProperty(namaBarang);
            this.kodeBarang = new SimpleStringProperty(kodeBarang);
        }

        public String getIdBarang() {
            return idBarang.get();
        }

        public String getNamaBarang() {
            return namaBarang.get();
        }

        public String getnoUrut() {
            return noUrut.get();
        }
        
        public String getKodeBarang() {
            return kodeBarang.get();
        }

        
    }
    
}
