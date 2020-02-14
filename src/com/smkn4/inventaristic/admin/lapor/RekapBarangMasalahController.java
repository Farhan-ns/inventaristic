/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.lapor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.smkn4.inventaristic.admin.barang.bermasalah.BarangMasalahController;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.WordUtils;

/**
 *
 * @author barne
 */
public class RekapBarangMasalahController implements Initializable{

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
    private JFXTreeTableView<BarangMasalah> tabelBarangBermasalah;

    Connection connection;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();

        JFXTreeTableColumn<RekapBarangMasalahController.BarangMasalah, String> colNama = new JFXTreeTableColumn<>("Nama");
        colNama.setPrefWidth(198);
        colNama.setCellValueFactory((TreeTableColumn.CellDataFeatures<RekapBarangMasalahController.BarangMasalah, String> param) -> param.getValue().getValue().namaBarang);

        JFXTreeTableColumn<RekapBarangMasalahController.BarangMasalah, String> colLokasi = new JFXTreeTableColumn<>("lokasi");
        colLokasi.setPrefWidth(90);
        colLokasi.setCellValueFactory((TreeTableColumn.CellDataFeatures<RekapBarangMasalahController.BarangMasalah, String> param) -> param.getValue().getValue().lokasi);

        JFXTreeTableColumn<RekapBarangMasalahController.BarangMasalah, String> colMasalah = new JFXTreeTableColumn<>("Masalah");
        colMasalah.setPrefWidth(90);
        colMasalah.setCellValueFactory((TreeTableColumn.CellDataFeatures<RekapBarangMasalahController.BarangMasalah, String> param) -> param.getValue().getValue().jenisMasalah);

        JFXTreeTableColumn<RekapBarangMasalahController.BarangMasalah, String> colTgl = new JFXTreeTableColumn<>("Tgl Masalah");
        colTgl.setPrefWidth(130);
        colTgl.setCellValueFactory((TreeTableColumn.CellDataFeatures<RekapBarangMasalahController.BarangMasalah, String> param) -> param.getValue().getValue().tanggalMasalah);

        JFXTreeTableColumn<RekapBarangMasalahController.BarangMasalah, String> colUmur = new JFXTreeTableColumn<>("Umur");
        colUmur.setPrefWidth(140);
        colUmur.setCellValueFactory((TreeTableColumn.CellDataFeatures<RekapBarangMasalahController.BarangMasalah, String> param) -> param.getValue().getValue().totalPenggunaan);

        JFXTreeTableColumn<RekapBarangMasalahController.BarangMasalah, String> colDesc = new JFXTreeTableColumn<>("Deskripsi");
        colDesc.setPrefWidth(150);
        colDesc.setCellValueFactory((TreeTableColumn.CellDataFeatures<RekapBarangMasalahController.BarangMasalah, String> param) -> param.getValue().getValue().deskripsi);

        readData();
        tabelBarangBermasalah.getColumns().setAll(colNama, colLokasi, colMasalah, colTgl, colUmur, colDesc);
        setbuttonAction();
        Platform.runLater(() -> {
            btnMenu.getScene().getWindow().centerOnScreen();
        });
    }

    

    public void readData() {
        ObservableList<RekapBarangMasalahController.BarangMasalah> barangs = FXCollections.observableArrayList();
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
            while (rs.next()) {
                String[] str = getDataBarang(rs);
                barangs.add(new RekapBarangMasalahController.BarangMasalah(str[0], str[1], str[2], str[3], str[4], str[5], str[6], str[7]));
            }
            final TreeItem<RekapBarangMasalahController.BarangMasalah> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
            lblBBermasalah.setText(String.valueOf(barangs.size()));
            tabelBarangBermasalah.setRoot(root);
            tabelBarangBermasalah.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData() {
        if (tabelBarangBermasalah.getSelectionModel().getSelectedItem() != null) {
            RekapBarangMasalahController.BarangMasalah barang = tabelBarangBermasalah.getSelectionModel().getSelectedItem().getValue();
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
            RekapBarangMasalahController.BarangMasalah barang = tabelBarangBermasalah.getSelectionModel().getSelectedItem().getValue();
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

    public void setbuttonAction() {
          btnRefresh.setOnAction((event) -> {
            readData();
          });
          
//          btnExport.setOnAction((event) ->{
//              
//          });
//          
//          btnPrint.setOnAction((event) ->{
//              
//          });
    }

    class BarangMasalah extends RecursiveTreeObject<BarangMasalah> {

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

        public BarangMasalah(String idBarang, String namaBarang, String lokasi, String jenisMasalah, String tglMasalah, String totalPenggunaan, String deskripsi, String idBarangMasalah) {
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
