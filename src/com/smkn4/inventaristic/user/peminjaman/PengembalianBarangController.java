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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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
    private TextField tFieldScanBarang;
    @FXML
    private Label lblWarn;
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
    List<String> listIdBarang = new ArrayList<>();
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
        setScanAction();
        setButtonAction();
        setSideBarButtons();
        Platform.runLater(() -> {
            btnMenu.getScene().getWindow().centerOnScreen();
        });
    }
    
    private void setSideBarButtons() {
        btnMenu.setOnAction((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/MenuUser.fxml"));
                Parent viewMintaBarang = loader.load();
                Stage stage = (Stage) btnMenu.getScene().getWindow();
                stage.setScene(new Scene(viewMintaBarang));
                stage.show();
                MenuUserController controller = loader.getController();
                controller.setUserMap(this.map);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
    }
    
    private void setButtonAction() {
        btnSelesai.setOnAction((event) -> {
            updateRecordRincian(this.dTracker);
        });
    }
    
    private void setScanAction() {
        tFieldScanBarang.setOnAction((event) -> {
            String kodeBarang = tFieldScanBarang.getText();
            String[] str = kodeBarang.split("-");
            if (!str[0].equals("4BDG")) {
                System.out.println(kodeBarang);
                System.out.println("bukan barang smkn 4");
            } else {
                if (isNotBarangDuplicate(str[1])) {
                    for (String idBarang : this.listIdBarang) {
                        if (idBarang.equals(str[1])) {
                            dTracker.add(idBarang);
                            colorValidasiBarang(listIdBarang.indexOf(idBarang));
                        }
                    }
                }
            }
        });
    }
    
    private void updateRecordRincian(Set<String> set) {
        String query = "UPDATE rincian SET status_barang = 'dikembalikan' WHERE rincian.id_barang = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            for (String string : set) {
                ps.setString(1, string);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        Set<String> idsPeminjaman = getIdPeminjaman(this.map.get("nis"));
        cekStatusPeminjaman(idsPeminjaman);
    }
    
    private void cekStatusPeminjaman(Set<String> set) {
        for (String idPeminjaman : set) {
            if (isDikembalikanSemua(idPeminjaman)) {
                updatePengembalianLunas(idPeminjaman);
            }
        }
        JOptionPane.showMessageDialog(null, "Pengembalian Berhasil");
        backToMenu();
    }
    
    private void updatePengembalianLunas(String idPeminjaman) {
        String query = "UPDATE peminjaman SET status_peminjaman = 'sudah_kembali', tgl_kembali = ? WHERE id_peminjaman = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, getTanggalToday());
            ps.setString(2, idPeminjaman);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    private boolean isDikembalikanSemua(String idPeminjaman) {
        String query = "SELECT status_barang FROM rincian WHERE id_peminjaman = ? AND status_barang = ?";
        boolean isLunas = false;
         try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, idPeminjaman);
            ps.setString(2, "dipinjam");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                isLunas = true; 
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
         System.out.println("isLunas = " + isLunas);
         return isLunas;
    }
    
//    private void updateJumlahPeminjaman(String idPeminjaman, String jumlah) {
//        String query = "UPDATE peminjaman SET jumlah_dipinjam = ? WHERE id_peminjaman = ?";
//        try {
//            PreparedStatement ps = this.connection.prepareStatement(query);
//            ps.setString(1, jumlah);
//            ps.setString(2, idPeminjaman);
//            ps.executeUpdate();
//        } catch (SQLException ex) {
//            ex.getCause();
//            ex.printStackTrace();
//        }
//    }
    
    private Set<String> getIdPeminjaman(String nis) {
        String query = "SELECT id_peminjaman FROM peminjaman WHERE nis = ?";
        Set<String> idsPeminjaman = new HashSet<>();
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, nis);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_peminjaman");
                idsPeminjaman.add(id);
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        return idsPeminjaman;
    }
    
//    private void getIdPeminjamanOf(Set<String> set) {
//        String query = "SELECT id_peminjaman FROM rincian WHERE rincian.id_barang = ?";
//        Set<String> idPeminjaman
//        try {
//            PreparedStatement ps = this.connection.prepareStatement(query);
//            ResultSet rs = null;
//            for (String string : set) {
//                if (rs != null) {
//                    rs.beforeFirst();
//                }
//                ps.setString(1, string);
//                rs = ps.executeQuery();
//                rs.first();
//                
//            }
//        } catch (SQLException ex) {
//            ex.getCause();
//            ex.printStackTrace();
//        }
//    }
    
    private void showBarang(String nis) {
        String query = "SELECT rincian.id_barang, barang_masuk.nama_barang, peminjaman.tgl_peminjaman " +
                "FROM barang_masuk, peminjaman, rincian, siswa " +
                "WHERE rincian.id_peminjaman = peminjaman.id_peminjaman  " +
                "AND rincian.id_barang = barang_masuk.id_barang " +
                "AND peminjaman.nis = siswa.nis " +
                "AND peminjaman.nis = ? " +
                "AND peminjaman.status_peminjaman = 'belum_kembali'" +
                "AND rincian.status_barang = 'dipinjam'";
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
        lblTotal.setText(String.valueOf(barangs.size()));
        tabelPinjamBarang.setItems(barangs);
        for (Barang barang : tabelPinjamBarang.getItems()) {
            String idBarang = barang.getIdBarang();
            listIdBarang.add(idBarang);
        }
    }
    
    private void colorValidasiBarang(int index) {
        TableCell cell;
        for (int i = 0; i < 4; i++) {
            cell = (TableCell) tabelPinjamBarang.queryAccessibleAttribute(AccessibleAttribute.CELL_AT_ROW_COLUMN, index, i);
            cell.setStyle("-fx-background-color : #2beec3");
        }
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
        Platform.runLater(() -> {
            tFieldScanBarang.requestFocus();
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
    
    private void backToMenu() {
        try {
            this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/MenuUser.fxml"));
            Parent viewMintaBarang = loader.load();
            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(viewMintaBarang));
            stage.show();
            MenuUserController controller = loader.getController();
            controller.setUserMap(this.map);
        } catch (IOException ex) {
            ex.getCause();
            ex.printStackTrace();
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
