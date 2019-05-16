/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user.peminjaman;

import com.jfoenix.controls.JFXButton;
import com.smkn4.inventaristic.util.JenisBarangException;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class PengembalianBarangController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnSignOut;
    @FXML
    private Pane pnlCustomer;
    @FXML
    private Pane pnlOrders;
    @FXML
    private Pane pnlMenus;
    @FXML
    private Pane pnlOverview;
    @FXML
    private Label lblTotal;
    @FXML
    private JFXButton btnSelesai;
    @FXML
    private JFXTextField tFieldSearch;
    @FXML
    private TextField tFieldScanBarang;
    @FXML
    private Label lblNotAset;
    @FXML
    private Label lblTotalKembali;
    @FXML
    private TableView<Barang> tabelPinjamBarang;
    @FXML
    private TableColumn<Barang, String> colNo;
    @FXML
    private TableColumn<Barang, String> colTanggal;
    @FXML
    private TableColumn<Barang, String> colKode;
    @FXML
    private TableColumn<Barang, String> colNama;
    
    ObservableList<Barang> barangs = FXCollections.observableArrayList();
    Set<String> dTracker = new HashSet<>();
    Map<String, String> map;
    Connection connection;
    FXMLLoader loader;
    int noUrut = 1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        colNo.setCellValueFactory(new PropertyValueFactory<>("noUrut"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
        colKode.setCellValueFactory(new PropertyValueFactory<>("idBarang"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
    }
    
    private void setScanAction() {
        tFieldScanBarang.setOnAction((event) -> {
//            String kodeBarang = tFieldScanBarang.getText();
//            String[] str = kodeBarang.split("-");
//            if (!str[0].equals("TI4")) {
//                System.out.println(kodeBarang);
//                System.out.println("bukan barang smkn 4");
//            } else {
//                if (isNotBarangDuplicate(str[1])) {
//                    try {
////                        showBarang(str[1]);
//                    } catch (JenisBarangException e) {
//                        lblNotAset.setVisible(true);
//                    }
//                }
//            }
        });
    }
    
    private void showBarang(String nis) {
        String query = "SELECT rincian.id_barang, barang_masuk.nama_barang, peminjaman.tgl_peminjaman " +
                "FROM barang_masuk, peminjaman, rincian, siswa " +
                "WHERE rincian.id_peminjaman = peminjaman.id_peminjaman  " +
                "AND rincian.id_barang = barang_masuk.id_barang " +
                "AND peminjaman.nis = siswa.nis " +
                "AND peminjaman.nis = ? " +
                "AND peminjaman.status_peminjaman = 'belum_kembali'";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, nis);
            ResultSet rs = ps.executeQuery();
            String tglPeminjaman;
            String idBarang;
            String namaBarang;
            int noUrut = 1;
            while (rs.next()) {
                tglPeminjaman = rs.getString("tgl_peminjaman");
                idBarang = rs.getString("id_barang");
                namaBarang = rs.getString("nama_barang");
                barangs.add(new Barang(String.valueOf(noUrut), tglPeminjaman, idBarang, namaBarang));
                noUrut++;
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        tabelPinjamBarang.setItems(barangs);
    }
    
//    private void scBarang(String idBarang) throws JenisBarangException {
//        String query = "SELECT tgl_peminjaman, id_barang, nama_barang, jenis_barang FROM barang_masuk WHERE id_barang = " + idBarang;
//        try {
//            Statement stmt = this.connection.createStatement();
//            ResultSet rs = stmt.executeQuery(query);
//            rs.first();
//            
//            if (!rs.getString("jenis_barang").toLowerCase().equals("aset")) {
//                throw new JenisBarangException("Bukan Barang jenis aset");
//            }
//            
//            String tglPeminjaman = rs.getString("tgl_peminjaman");
//            String namaBarang = rs.getString("nama_barang");
//            String noUrut = String.valueOf(this.noUrut);
//            barangs.add(new Barang(noUrut, tglPeminjaman, idBarang, namaBarang));
//            this.noUrut++;
//            lblNotAset.setVisible(false);
//        } catch (SQLException ex) {
//            ex.getCause();
//            ex.printStackTrace();
//        }
//        tabelPinjamBarang.setItems(barangs);
//    }
    
    private boolean isNotBarangDuplicate(String idBarang) {
        return dTracker.add(idBarang);
    }
    
    private String getTanggalToday() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    
    protected void setUserMap(Map map) {
        this.map = map;
        setUsername();
        Platform.runLater(() -> {
            cekSanksiSiswa();
            showBarang(this.map.get("nis"));
        });
    }
    private void setUsername() {
        lblUsername.setText("Halo, "+ this.map.get("nama") + " !");
    }
    
    private void cekSanksiSiswa() {
        int sanksi = Integer.parseInt(this.map.get("sanksi"));
        String namaMsg = "Nama Siswa : " + this.map.get("nama");
        String sanksiMsg = "Jumlah Sanksi : " + this.map.get("sanksi");
        if (sanksi >= 3) {
            String msg = "Anda tidak dapat meminjam di karenakan sanksi anda telah melebihi batas\n"
                    + "harap lunasi sanksi Anda terlebih dahulu untuk melanjutkan";
            JOptionPane.showMessageDialog(null, namaMsg + "\n" + sanksiMsg + "\n" + msg) ;
        } else if (sanksi > 0 && sanksi < 3) {
            String msg = "Anda memiliki sanksi, segera lunasi";
            JOptionPane.showMessageDialog(null, namaMsg + "\n" + sanksiMsg + "\n" + msg);
        }
    }
    
    public class Barang extends RecursiveTreeObject<Barang> {
        String noUrut;
        String tanggalPinjam;
        String idBarang;
        String namaBarang;
        
        public Barang(String noUrut, String tanggalPinjam, String idBarang, String namaBarang) {
            this.noUrut = noUrut;
            this.tanggalPinjam = tanggalPinjam;
            this.idBarang = idBarang;
            this.namaBarang = namaBarang;
        }

        public String getNoUrut() {
            return noUrut;
        }
        
        public String getTanggalPinjam() {
            return tanggalPinjam;
        }

        public String getIdBarang() {
            return idBarang;
        }

        public String getNamaBarang() {
            return namaBarang;
        }
    }
    
}
