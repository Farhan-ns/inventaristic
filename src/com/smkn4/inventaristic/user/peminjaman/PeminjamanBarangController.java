/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user.peminjaman;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.smkn4.inventaristic.util.JenisBarangException;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
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
    private JFXButton btnSwitch;
    @FXML
    private Label lblTotal;
    @FXML
    private JFXButton btnPinjam;
    @FXML
    private JFXButton btnCari;
    @FXML
    private TextField tFieldScanBarang;        
    @FXML
    private Label lblNotAset;
    @FXML
    private TableView<Barang> tabelPinjamBarang;

    @FXML
    private TableColumn<Barang, String> colNo;

    @FXML
    private TableColumn<Barang, String> colNama;

    @FXML
    private TableColumn<Barang, String> colKode;
    
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
        colKode.setCellValueFactory(new PropertyValueFactory<>("idBarang"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        setScanAction();
        setButtonAction();
        Platform.runLater(() -> {
            btnMenu.getScene().getWindow().centerOnScreen();
            stateCheck();
        });
    }
    
    //Event 
    private void setButtonAction() {
        btnPinjam.setOnAction((event) -> {
            createRecordPeminjaman();
        });
        btnMenu.setOnAction((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/MenuUser.fxml"));
                Parent viewMintaBarang = loader.load();
                Stage stage = (Stage) btnSwitch.getScene().getWindow();
                stage.setScene(new Scene(viewMintaBarang));
                stage.show();
                MenuUserController controller = loader.getController();
                controller.setUserMap(this.map);
                controller.setAuth(false);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnSwitch.setOnAction((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/PermintaanBarang.fxml"));
                Parent viewMintaBarang = loader.load();
                Stage stage = (Stage) btnSwitch.getScene().getWindow();
                stage.setScene(new Scene(viewMintaBarang));
                stage.show();
                PermintaanBarangController controller = loader.getController();
                controller.setUserMap(this.map);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnCari.setOnAction((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/LihatBarang.fxml"));
                Parent viewMintaBarang = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(viewMintaBarang));
                stage.show();
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
    }
    
    private void setScanAction() {
        tFieldScanBarang.setOnAction((event) -> {
            String kodeBarang = tFieldScanBarang.getText();
            String[] str = kodeBarang.split("-");
            if (!str[0].equals("SMKN4BDG")) {
                System.out.println(kodeBarang);
                System.out.println("bukan barang SMKN 4");
            } else {
                if (isNotBarangDuplicate(str[1])) {
                    try {
                        showBarang(str[1]);
                        stateCheck();
                    } catch (JenisBarangException e) {
                        lblNotAset.setVisible(true);
                    } catch (Exception e) {
                        lblNotAset.setVisible(true);
                    }
                }
            }
        });
    }
    
    private void createRecordPeminjaman() {
        String tanggalPinjam = getTanggalToday();
        String id = this.map.get("id");
        String query = "INSERT INTO peminjaman(tgl_peminjaman, id_kelas, status_peminjaman) " +
                "VALUES('"+tanggalPinjam+"', " + "'" + id + "', 'belum_kembali')";
        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
            createRecordRincian(getLatestRecordPeminjaman());
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    private void createRecordRincian(String idPinjam) {
        List<String> list = new ArrayList<>();
        for (Barang barang : tabelPinjamBarang.getItems()) {
            String idBarang = barang.getIdBarang();
            list.add(idBarang);
        }
        try {
            Statement stmt = connection.createStatement();
            for (String idBarang : list) {
                String query = "INSERT INTO rincian(id_peminjaman, id_barang, status_barang) " +
                        "VALUES('" + idPinjam + "', " + "'" + idBarang + "', 'dipinjam')";
                stmt.executeUpdate(query);
            }
            createJumlahDipinjam(idPinjam, String.valueOf(list.size()));
        } catch (SQLException e) {
            e.getCause();
        }
    }
    
    private void createJumlahDipinjam(String idPinjam, String jumlah) {
        String query = "UPDATE peminjaman SET jumlah_dipinjam = '"+ jumlah +"' WHERE peminjaman.id_peminjaman = "+ idPinjam;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Peminjaman Berhasil", "Notifikasi", 1);
            backToMenu();
        } catch (SQLException ex) {
            ex.getCause();
        }
    }
    
    private String getLatestRecordPeminjaman() {
//        String nis = this.map.get("nis");
        String query = "SELECT id_peminjaman FROM peminjaman ORDER BY id_peminjaman DESC LIMIT 1";
        Statement stmt;
        String idPeminjaman = "";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            idPeminjaman = rs.getString("id_peminjaman");
        } catch (SQLException ex) {
            ex.getCause();
        }
        return idPeminjaman;
    }
    
    private void showBarang(String idBarang) throws JenisBarangException, Exception {
        //BUG CSS PROBLEM
        String query = "SELECT id_barang, nama_barang, jenis_barang FROM barang_masuk WHERE id_barang = " + idBarang;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                rs.close();
                lblNotAset.setText("Data barang tidak ditemukan/bukan aset");
                throw new Exception("data barang tidak di temukan");
            }
            rs.beforeFirst();
            rs.first();
            
            if (!rs.getString("jenis_barang").toLowerCase().equals("aset")) {
                rs.close();
                lblNotAset.setText("Bukan Barang jenis aset");
                throw new JenisBarangException("Bukan Barang jenis aset");
            }
            
            String namaBarang = rs.getString("nama_barang");
            String noUrut = String.valueOf(this.noUrut);
            barangs.add(new Barang(noUrut, idBarang, namaBarang));
            this.noUrut++;
            lblNotAset.setVisible(false);
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        lblTotal.setText(String.valueOf(barangs.size()));
        tabelPinjamBarang.setItems(barangs);
    }
    
    private boolean isNotBarangDuplicate(String idBarang) {
        return dTracker.add(idBarang);
    }
    
    private String getTanggalToday() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    
    private void stateCheck() {
        if (barangs.isEmpty()) {
            btnPinjam.setDisable(true);
        } else {
            btnPinjam.setDisable(false);
        }
    }
    
    protected void setUserMap(Map map) {
        this.map = map;
        setUsername();
        Platform.runLater(() -> {
            tFieldScanBarang.requestFocus();
            cekSanksiSiswa();
        });
    }
    private void setUsername() {
        lblUsername.setText("Halo, "+ this.map.get("nama_kelas") + " !");
    }
    
    private void cekSanksiSiswa() {
        int sanksi = Integer.parseInt(this.map.get("sanksi"));
        String namaMsg = "Nama : " + this.map.get("nama_kelas");
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
            Stage stage = (Stage) btnSwitch.getScene().getWindow();
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
        String idBarang;
        String namaBarang;

        public Barang(String noUrut, String idBarang, String namaBarang) {
            this.noUrut = noUrut;
            this.idBarang = idBarang;
            this.namaBarang = namaBarang;
        }

        public String getNoUrut() {
            return noUrut;
        }

        public String getIdBarang() {
            return idBarang;
        }

        public String getNamaBarang() {
            return namaBarang;
        }
    }
    
}
