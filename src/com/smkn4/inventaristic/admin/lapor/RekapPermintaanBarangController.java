/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.lapor;

/**
 *
 * @author barne
 */
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.jndi.toolkit.url.Uri;
import java.net.URL;
import java.sql.Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.WordUtils;

public class RekapPermintaanBarangController implements Initializable {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnSwitch;

//    @FXML
//    private ComboBox<?> cbJenisBarang;

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
    private JFXTreeTableView<PermintaanBarang> tabelPermintaanBarang;

    Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();

        JFXTreeTableColumn<PermintaanBarang, String> colId = new JFXTreeTableColumn<>("Id");
        colId.setPrefWidth(198);
        colId.setCellValueFactory((TreeTableColumn.CellDataFeatures<PermintaanBarang, String> param) -> param.getValue().getValue().idPermintaanBarang);

        JFXTreeTableColumn<PermintaanBarang, String> colNama = new JFXTreeTableColumn<>("Nama");
        colNama.setPrefWidth(198);
        colNama.setCellValueFactory((TreeTableColumn.CellDataFeatures<PermintaanBarang, String> param) -> param.getValue().getValue().namaBarang);

        JFXTreeTableColumn<PermintaanBarang, String> colBarang = new JFXTreeTableColumn<>("Total Barang");
        colBarang.setPrefWidth(90);
        colBarang.setCellValueFactory((TreeTableColumn.CellDataFeatures<PermintaanBarang, String> param) -> param.getValue().getValue().totalBarang);
        
        JFXTreeTableColumn<PermintaanBarang, String> colNis = new JFXTreeTableColumn<>("NIS");
        colNis.setPrefWidth(90);
        colNis.setCellValueFactory((TreeTableColumn.CellDataFeatures<PermintaanBarang, String> param) -> param.getValue().getValue().nis);

        JFXTreeTableColumn<PermintaanBarang, String> colTanggal = new JFXTreeTableColumn<>("Tanggal Permintaan");
        colTanggal.setPrefWidth(90);
        colTanggal.setCellValueFactory((TreeTableColumn.CellDataFeatures<PermintaanBarang, String> param) -> param.getValue().getValue().tglpermintaan);
        
      

        readData();
        tabelPermintaanBarang.getColumns().setAll(colId,colNama,colNis,colBarang,colTanggal);
        setEventButtonAction();
    }

    private void setEventButtonAction() {

    }

    public void readData() {
        ObservableList<RekapPermintaanBarangController.PermintaanBarang> barangs = FXCollections.observableArrayList();
        try {
            Statement stat = this.connection.createStatement();
            String query = "SELECT"
                    + " permintaan.id_permintaan, permintaan.nis, barang_masuk.nama_barang, barang_masuk.jenis_barang,permintaan.tgl_permintaan, "
                    + "barang_masuk.deskripsi "
                    + "FROM permintaan "
                    + " INNER JOIN barang_masuk on barang_masuk.id_barang = permintaan.id_barang";
            /*
            DEV NOTES
            Bagaimana Jika ada barang yang namanya sama namun isi di field lain berbeda?
             */
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String[] str = getDataBarang(rs);
                barangs.add(new RekapPermintaanBarangController.PermintaanBarang(str[0], str[1], str[2], str[3],str[4],str[5]));
            }
            final TreeItem<RekapPermintaanBarangController.PermintaanBarang> root = new RecursiveTreeItem<>(barangs, RecursiveTreeObject::getChildren);
            lblBBermasalah.setText(String.valueOf(barangs.size()));
            tabelPermintaanBarang.setRoot(root);
            tabelPermintaanBarang.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void deleteData() {
//        if (tabelPermintaanBarang.getSelectionModel().getSelectedItem() != null) {
//            RekapPermintaanBarangController.PermintaanBarang barang = tabelPermintaanBarang.getSelectionModel().getSelectedItem().getValue();
//            String idBarangBermasalah = barang.getIdPermintaanBarang();
//            try {
//                String query = "DELETE FROM barang_bermasalah WHERE id_barang_masalah = '" + idBarangBermasalah + "'";
//                Statement stmt = this.connection.createStatement();
//                int success = stmt.executeUpdate(query);
//                if (success == 1) {
//                    this.readData();
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Pilih Row yang akan di hapus", "Error", 0);
//        }
//    }

    private String getIdBarang() {
        if (tabelPermintaanBarang.getSelectionModel().getSelectedItem() != null) {
            RekapPermintaanBarangController.PermintaanBarang barang = tabelPermintaanBarang.getSelectionModel().getSelectedItem().getValue();
            return barang.getIdPermintaanBarang();
        } else {
            JOptionPane.showMessageDialog(null, "Pilih Row yang akan di hapus", "Error", 0);
            return "";
        }
    }

    public String[] getDataBarang(ResultSet rs) {
        try {
            String idPermintaanBarang = rs.getString("id_permintaan");
            Date tgl = rs.getDate("tgl_permintaan");
            String tanggalMasalah = DateFormatUtils.format(tgl, "dd-MM-yyy");
            String namaBarang = rs.getString("nama_barang");
            String nis = rs.getString("nis");
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
            return new String[]{idPermintaanBarang, namaBarang,nis,tanggalMasalah, deskripsi };
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class PermintaanBarang extends RecursiveTreeObject<PermintaanBarang> {

        public SimpleStringProperty idPermintaanBarang;
        public SimpleStringProperty namaBarang;
        public SimpleStringProperty totalBarang;
        public SimpleStringProperty deskripsi;
        public SimpleStringProperty nis;
        public SimpleStringProperty tglpermintaan;
        

        public PermintaanBarang(String idPermintaanBarang, String namaBarang, String totalBarang,String tglpermintaan,String nis,String deskripsi) {
            this.idPermintaanBarang = new SimpleStringProperty(idPermintaanBarang);
            this.namaBarang = new SimpleStringProperty(namaBarang);
            this.nis = new SimpleStringProperty(nis);
            this.tglpermintaan = new SimpleStringProperty(tglpermintaan);
            this.totalBarang = new SimpleStringProperty(totalBarang);
            this.deskripsi = new SimpleStringProperty(deskripsi);
        }

        public String getNis() {
            return nis.get();
        }

        public void setNis(SimpleStringProperty nis) {
            this.nis = nis;
        }

        public String getTglpermintaan() {
            return tglpermintaan.get();
        }

        public void setTglpermintaan(SimpleStringProperty tglpermintaan) {
            this.tglpermintaan = tglpermintaan;
        }

        public String getIdPermintaanBarang() {
            return idPermintaanBarang.get();
        }

        public void setIdPermintaanBarang(SimpleStringProperty idPermintaanBarang) {
            this.idPermintaanBarang = idPermintaanBarang;
        }

        public String getNamaBarang() {
            return namaBarang.get();
        }

        public void setNamaBarang(SimpleStringProperty namaBarang) {
            this.namaBarang = namaBarang;
        }

        public String getTotalBarang() {
            return totalBarang.get();
        }

        public void setTotalBarang(SimpleStringProperty totalBarang) {
            this.totalBarang = totalBarang;
        }

        public String getDeskripsi() {
            return deskripsi.get();
        }

        public void setDeskripsi(SimpleStringProperty deskripsi) {
            this.deskripsi = deskripsi;
        }

    }
}
