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
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.smkn4.inventaristic.admin.barang.bermasalah.ManageDataBarangMasalahController;
import com.smkn4.inventaristic.util.EnumParser;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.WordUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class StokBarangController implements Initializable {
    

    @FXML
    private JFXButton btnDataUser;

    @FXML
    private JFXButton btnPengajuan;

    @FXML
    private JFXButton btnMenu;

    @FXML
    private JFXButton btnSwitch;
    
    @FXML
    private JFXButton btnTambah;
    
    @FXML
    private JFXButton btnTBermasalah;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnHapus;

    @FXML
    private Label lblTotalBarang;

    @FXML
    private JFXTreeTableView<Barang> tabelStokBarang;
    
    @FXML
    private JFXButton btnRefresh;
    
    @FXML
    private JFXButton btnTambahBermasalah;
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

    Connection connection;

    /**
     * Inisialisi controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //MEMBUAT KOLOM TABEL DENGAN BINDING PROPERTY
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
        
        JFXTreeTableColumn<Barang, String> colJumlah = new JFXTreeTableColumn<>("Jumlah");
        colJumlah.setPrefWidth(100);
        colJumlah.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().lokasi);
        
        JFXTreeTableColumn<Barang, String> colUmur = new JFXTreeTableColumn<>("Umur");
        colUmur.setPrefWidth(150);
        colUmur.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().totalPenggunaan);
        
        JFXTreeTableColumn<Barang, String> colPinjaman = new JFXTreeTableColumn<>("Pinjaman");
        colPinjaman.setPrefWidth(150);
        colPinjaman.setCellValueFactory((TreeTableColumn.CellDataFeatures<Barang, String> param) -> param.getValue().getValue().dapatDipinjam);

        //READ DATA
        this.connection = MySqlConnection.getConnection();
        
        readData();
        //Menambah Objek Barang ke tabel
//        final TreeItem<Barang> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
        tabelStokBarang.getColumns().setAll(colNama, colJenis, colTgl, colKondisi, colLokasi, colUmur, colPinjaman);
//        tabelStokBarang.setRoot(root);
//        tabelStokBarang.setShowRoot(false);
        setbuttonAction();
        Platform.runLater(() -> {
            btnMenu.getScene().getWindow().centerOnScreen();
        });
    }
    
    public void deleteData() {
        if (tabelStokBarang.getSelectionModel().getSelectedItem() != null) {
            Barang barang = tabelStokBarang.getSelectionModel().getSelectedItem().getValue();
            String idBarang = barang.getIdBarang();
            try {
                String query = "DELETE FROM barang_masuk WHERE id_barang = '" + idBarang + "'";
                Statement stmt = connection.createStatement();
                int success = stmt.executeUpdate(query);
                if (success == 1) {
                    this.readData();
                }
            } catch (MySQLIntegrityConstraintViolationException integrity) {
                JOptionPane.showMessageDialog(null, "Barang ini gunakan di tabel lain\nhapus record barang ini di tabel lain terlebih dahulu");
                integrity.getCause();
                integrity.printStackTrace();
            } catch (SQLException sql) {
                sql.getCause();
                sql.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih Row yang akan di hapus", "Error", 0);
        }
    }
    
    public void readData() {
        ObservableList<Barang> barangs = FXCollections.observableArrayList();
        try {
            Statement stat = this.connection.createStatement();
            String query = "select * from barang_masuk WHERE NOT EXISTS (SELECT barang_bermasalah.id_barang FROM barang_bermasalah WHERE barang_masuk.id_barang = barang_bermasalah.id_barang) ORDER BY nama_barang";
            /*
            DEV NOTES
            Bagaimana Jika ada barang yang namanya sama namun isi di field lain berbeda?
            */
            ResultSet rs = stat.executeQuery(query);
            while(rs.next()) {
                String[] str = getDataBarang(rs);
                barangs.add(new Barang(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Menghitung jumlah total barang
        final TreeItem<Barang> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
        lblTotalBarang.setText(String.valueOf(barangs.size()));;
        tabelStokBarang.setRoot(root);
        tabelStokBarang.setShowRoot(false);
    }
    
    private void setbuttonAction() {
        btnTambah.setOnAction((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/ManageDataStokBarang.fxml"));
                Parent formManage = loader.load();
                Stage stage = new Stage();
                stage.initOwner(btnEdit.getScene().getWindow());
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(new Scene(formManage));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
            readData();
        });
        btnEdit.setOnAction((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/barang/stok/ManageDataStokBarang.fxml"));
                Parent formManage = loader.load();
                ManageDataStokBarangController controller = loader.getController();
                Stage stage = new Stage();
                stage.initOwner(btnEdit.getScene().getWindow());
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(new Scene(formManage));
                stage.show();
                controller.action = "edit";
                controller.setIdBarang(getIdBarang());
                controller.showData();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnSwitch.setOnAction((event) -> {
            try {
                Stage stage = (Stage) btnSwitch.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/barang/bermasalah/BarangMasalah.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                
            }
        });
        btnTBermasalah.setOnAction((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/barang/bermasalah/ManageDataBarangMasalah.fxml"));
                Parent formManage = loader.load();
                ManageDataBarangMasalahController controller = loader.getController();
                Stage stage = new Stage();
                stage.initOwner(btnTBermasalah.getScene().getWindow());
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(new Scene(formManage));
                stage.show();
                controller.readData(getIdBarang(), getNamaBarang());
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
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
    
    private String getIdBarang() {
        if (tabelStokBarang.getSelectionModel().getSelectedItem() != null) {
            Barang barang = tabelStokBarang.getSelectionModel().getSelectedItem().getValue();
            return barang.getIdBarang();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih Row terlebih dahulu", "Error", 0);
            return "";
        }
    }
    
    private String getNamaBarang() {
        if (tabelStokBarang.getSelectionModel().getSelectedItem() != null) {
            Barang barang = tabelStokBarang.getSelectionModel().getSelectedItem().getValue();
            return barang.getNamaBarang();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih Row terlebih dahulu", "Error", 0);
            return "";
        }
    }
    
    public String[] getDataBarang(ResultSet rs) {
        try {
            String idBarang = rs.getString("id_barang");
            String namaBarang = rs.getString("nama_barang");
            String jenisBarang = rs.getString("jenis_barang");
            jenisBarang =  WordUtils.capitalizeFully(EnumParser.format(jenisBarang));
            Date tgl = rs.getDate("tgl_masuk");
            String tanggalMasuk = DateFormatUtils.format(tgl, "dd-MM-yyy");
            String kondisi = WordUtils.capitalizeFully(rs.getString("kondisi"));
            String lokasi = rs.getString("lokasi");
//            String kuantitas = rs.getString("jumlah");
            String umur = rs.getString("total_penggunaan");
            String dapatDipinjam = WordUtils.capitalizeFully(rs.getString("dapat_dipinjam"));
            /*
            namaBarang;
            jenisBarang;
            tanggalMasuk;
            kondisi;
            lokasi;
            totalPenggunaan;
            dapatDipinjam;
            */
            return new String[]{idBarang, namaBarang, jenisBarang, tanggalMasuk, kondisi, lokasi, umur, dapatDipinjam};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

  
    
    class Barang extends RecursiveTreeObject<Barang> {
        SimpleStringProperty idBarang;
        SimpleStringProperty namaBarang;
        SimpleStringProperty jenisBarang;
        SimpleStringProperty tanggalMasuk;
        SimpleStringProperty kondisi;
        SimpleStringProperty lokasi;
        SimpleStringProperty totalPenggunaan;
        SimpleStringProperty dapatDipinjam;
//        SimpleStringProperty deskripsi;
//        SimpleStringProperty petugas;

        public Barang(String idBarang, String namaBarang, String jenisBarang, String tanggalMasuk, String kondisi, String lokasi, String totalPenggunaan,String dapatDipinjam) {
            this.idBarang = new SimpleStringProperty(idBarang);
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

        public String getIdBarang() {
            return idBarang.get();
        }

        public String getNamaBarang() {
            return namaBarang.get();
        }

        public String getJenisBarang() {
            return jenisBarang.get();
        }

        public String getTanggalMasuk() {
            return tanggalMasuk.get();
        }

        public String getKondisi() {
            return kondisi.get();
        }

        public String getLokasi() {
            return lokasi.get();
        }

        public String getTotalPenggunaan() {
            return totalPenggunaan.get();
        }

        public String getDapatDipinjam() {
            return dapatDipinjam.get();
        }
        
    }
    
}