/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.barang.bermasalah;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.WordUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class BarangMasalahController implements Initializable {
    
    @FXML
    private Label lblBBermasalah;
    @FXML
    private JFXTreeTableView<Barang> tabelBarangBermasalah;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnHapus;
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private JFXButton btnSwitch;
    @FXML
    private JFXButton btnDataUser;
    @FXML
    private JFXButton btnPengajuan;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnSignOut;
    @FXML
    private TextField tFieldSearch;
    
    Connection connection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        
        JFXTreeTableColumn<Barang, String> colNama = new JFXTreeTableColumn<>("Nama");
        colNama.setPrefWidth(198);
        colNama.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().namaBarang); 
        
        JFXTreeTableColumn<Barang, String> colLokasi = new JFXTreeTableColumn<>("lokasi");
        colLokasi.setPrefWidth(90);
        colLokasi.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().lokasi); 
        
        JFXTreeTableColumn<Barang, String> colMasalah = new JFXTreeTableColumn<>("Masalah");
        colMasalah.setPrefWidth(90);
        colMasalah.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().jenisMasalah); 
        
        JFXTreeTableColumn<Barang, String> colTgl = new JFXTreeTableColumn<>("Tgl Masalah");
        colTgl.setPrefWidth(130);
        colTgl.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().tanggalMasalah); 
        
        JFXTreeTableColumn<Barang, String> colUmur = new JFXTreeTableColumn<>("Umur");
        colUmur.setPrefWidth(140);
        colUmur.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().totalPenggunaan); 
        
        JFXTreeTableColumn<Barang, String> colDesc = new JFXTreeTableColumn<>("Deskripsi");
        colDesc.setPrefWidth(150);
        colDesc.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().deskripsi); 
        
        readData();
        tabelBarangBermasalah.getColumns().setAll(colNama, colLokasi, colMasalah, colTgl, colUmur, colDesc);
        setbuttonAction();
    }    
    
    private void setbuttonAction() {
        btnEdit.setOnAction((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/barang/bermasalah/ManageDataBarangMasalah.fxml"));
                Parent formManage = loader.load();
                ManageDataBarangMasalahController controller = loader.getController();
                Stage stage = new Stage();
                stage.initOwner(btnEdit.getScene().getWindow());
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setScene(new Scene(formManage));
                stage.show();
                controller.action = "edit";
                String idBarangMasalah = "";
                if (tabelBarangBermasalah.getSelectionModel().getSelectedItem().getValue() != null) {
                    Barang barang = tabelBarangBermasalah.getSelectionModel().getSelectedItem().getValue();
                    idBarangMasalah = barang.getIdBarangMasalah();
                } else {
                    JOptionPane.showMessageDialog(null, "Pilih Row Terlebih dahulu");
                }
                //Bug note entah kenapa 2 value ini terbalik
                controller.idBarang = idBarangMasalah;
                controller.idBarangMasalah = getIdBarang();
                controller.showData(getIdBarang());
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnSwitch.setOnAction((event) -> {
            try {
                Stage stage = (Stage) btnSwitch.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/StokBarang.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                
            }
        });
        btnHapus.setOnAction ( (event) -> {
            deleteData();
        });
        btnRefresh.setOnAction((event) -> {
            readData();
        });
        btnMenu.setOnAction((event) -> {
            try {
                Stage stage = (Stage) btnMenu.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/AdminHome.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                
            }
        });
    }
    
    public void readData() {
        ObservableList<Barang> barangs = FXCollections.observableArrayList();
        try {
            Statement stat = this.connection.createStatement();
            String query = "SELECT"
                    + " barang_bermasalah.id_barang_masalah, barang_bermasalah.id_barang, barang_masuk.nama_barang, barang_masuk.lokasi, barang_bermasalah.jenis_masalah, "
                    + " barang_bermasalah.tgl_bermasalah, barang_masuk.total_penggunaan, barang_bermasalah.deskripsi "
                    + "FROM barang_bermasalah "
                    + " INNER JOIN barang_masuk on barang_masuk.id_barang = barang_bermasalah.id_barang";
            /*
            DEV NOTES
            Bagaimana Jika ada barang yang namanya sama namun isi di field lain berbeda?
            */
            ResultSet rs = stat.executeQuery(query);
            while(rs.next()) {
                String[] str = getDataBarang(rs);
                barangs.add(new Barang(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7]));
            }
            final TreeItem<Barang> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
            lblBBermasalah.setText(String.valueOf(barangs.size()));
            tabelBarangBermasalah.setRoot(root);
            tabelBarangBermasalah.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteData() {
        if (tabelBarangBermasalah.getSelectionModel().getSelectedItem() != null) {
            Barang barang = tabelBarangBermasalah.getSelectionModel().getSelectedItem().getValue();
            String idBarangBermasalah = barang.getIdBarang();
            try {
                String query = "DELETE FROM barang_bermasalah WHERE id_barang_masalah = '" + idBarangBermasalah + "'";
                Statement stmt = this.connection.createStatement();
                int success = stmt.executeUpdate(query);
                if (success == 1) {
                    this.readData();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih Row yang akan di hapus", "Error", 0);
        }
    }
    
    private String getIdBarang() {
        if (tabelBarangBermasalah.getSelectionModel().getSelectedItem() != null) {
            Barang barang = tabelBarangBermasalah.getSelectionModel().getSelectedItem().getValue();
            return barang.getIdBarang();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih Row yang akan di hapus", "Error", 0);
            return "";
        }
    }
    
    public String[] getDataBarang(ResultSet rs) {
        try {
            String idBarangMasalah = rs.getString("id_barang_masalah");
            String idBarang = rs.getString("id_barang");
            String namaBarang = rs.getString("nama_barang");
            Date tgl = rs.getDate("tgl_bermasalah");
            String tanggalMasalah = DateFormatUtils.format(tgl, "dd-MM-yyy");
            String jenisMasalah = WordUtils.capitalizeFully(rs.getString("jenis_masalah"));
            String lokasi = rs.getString("lokasi");
            String umur = rs.getString("total_penggunaan");
            String deskripsi = rs.getString("deskripsi");
            /*
            namaBarang;
            jenisBarang;
            tanggalMasuk;
            kondisi;
            lokasi;
            totalPenggunaan;
            dapatDipinjam;
            */
            return new String[]{idBarangMasalah, namaBarang, lokasi, jenisMasalah, tanggalMasalah, umur, deskripsi, idBarang};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    class Barang extends RecursiveTreeObject<Barang> {
        SimpleStringProperty idBarang;
        public SimpleStringProperty namaBarang;
        public SimpleStringProperty tanggalMasalah;
        public SimpleStringProperty jenisMasalah;
        public SimpleStringProperty lokasi;
        public SimpleStringProperty totalPenggunaan;
        public SimpleStringProperty deskripsi;
        public SimpleStringProperty idBarangMasalah;
//        SimpleStringProperty deskripsi;
//        SimpleStringProperty petugas;
        public Barang(String idBarang, String namaBarang, String lokasi, String jenisMasalah, String tglMasalah, String totalPenggunaan, String deskripsi, String idBarangMasalah) {
            this.idBarang = new SimpleStringProperty(idBarang);
            this.namaBarang = new SimpleStringProperty(namaBarang);
            this.lokasi = new SimpleStringProperty(lokasi);
            this.jenisMasalah = new SimpleStringProperty(jenisMasalah);
            this.tanggalMasalah = new SimpleStringProperty(tglMasalah);
            this.totalPenggunaan = new SimpleStringProperty(totalPenggunaan);
            this.deskripsi = new SimpleStringProperty(deskripsi);
            this.idBarangMasalah = new SimpleStringProperty(idBarangMasalah);
        }

        public String getIdBarang() {
            return idBarang.get();
        }

        public String getNamaBarang() {
            return namaBarang.get();
        }

        public String getTanggalMasalah() {
            return tanggalMasalah.get();
        }

        public String getJenisMasalah() {
            return jenisMasalah.get();
        }

        public String getLokasi() {
            return lokasi.get();
        }

        public String getTotalPenggunaan() {
            return totalPenggunaan.get();
        }

        public String getDeskripsi() {
            return deskripsi.get();
        }

        public String getIdBarangMasalah() {
            return idBarangMasalah.get();
        }
    }
    
}
