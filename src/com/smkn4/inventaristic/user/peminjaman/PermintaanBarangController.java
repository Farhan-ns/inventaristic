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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class PermintaanBarangController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnSwitch;
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
    private Label lblNotHP;
    @FXML
    private TableView<Barang> tabelPermintaanBarang;
    @FXML
    private TableColumn<Barang, String> colNo;
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
        setScanAction();
        setButtonAction();
        Platform.runLater(() -> {
            btnMenu.getScene().getWindow().centerOnScreen();
            stateCheck();
        });
    }
    
    private void setButtonAction() {
        btnSelesai.setOnAction((event) -> {
            createRecordPermintaan();
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
                    try {
                        showBarang(str[1]);
                        stateCheck();
                    } catch (JenisBarangException e) {
                        lblNotHP.setVisible(true);
                    } catch (Exception ex) {
                        lblNotHP.setVisible(true);
                    }
                }
            }
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
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnSwitch.setOnAction((event) -> {
            try {
                this.loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/user/peminjaman/PeminjamanBarang.fxml"));
                Parent viewPinjamBarang = loader.load();
                Stage stage = (Stage) btnSwitch.getScene().getWindow();
                stage.setScene(new Scene(viewPinjamBarang));
                stage.show();
                PeminjamanBarangController controller = loader.getController();
                controller.setUserMap(this.map);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
    }
    
    private void createRecordPermintaan() {
        List<String> list = new ArrayList<>();
        for (Barang barang : tabelPermintaanBarang.getItems()) {
            String idBarang = barang.getIdBarang();
            list.add(idBarang);
        }
        String tanggalPermintaan = getTanggalToday();
        String nis = this.map.get("nis");
        Statement stmt;
        try {
            stmt = connection.createStatement();
            for (String idBarang : list) {
                String query = "INSERT INTO permintaan(tgl_permintaan, nis, id_barang) " +
                        "VALUES('" + tanggalPermintaan + "', " + "'" + nis + "'," + "'" + idBarang + "')";
                stmt.executeUpdate(query);
            }
            JOptionPane.showMessageDialog(null, "Permintaan Berhasil", "Notifikasi", 1);
            backToMenu();
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    private void showBarang(String idBarang) throws JenisBarangException, Exception {
        //BUG CSS PROBLEM
        String query = "SELECT id_barang, nama_barang, jenis_barang FROM barang_masuk WHERE id_barang = " + idBarang;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                rs.close();
                lblNotHP.setText("Data barang tidak ditemukan/bukan habis pakai");
                throw new Exception("data barang tidak di temukan");
            }
            rs.beforeFirst();
            rs.first();
            
            if (!rs.getString("jenis_barang").toLowerCase().equals("habis_pakai")) {
                rs.close();
                lblNotHP.setText("Bukan Barang Habis Pakai");
                throw new JenisBarangException("Bukan Barang Habis Pakai");
            }
            
            String namaBarang = rs.getString("nama_barang");
            String noUrut = String.valueOf(this.noUrut);
            barangs.add(new Barang(noUrut, idBarang, namaBarang));
            this.noUrut++;
            lblNotHP.setVisible(false);
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
        lblTotal.setText(String.valueOf(barangs.size()));
        colNo.setCellValueFactory(new PropertyValueFactory<>("noUrut"));
        colKode.setCellValueFactory(new PropertyValueFactory<>("idBarang"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaBarang"));
        tabelPermintaanBarang.setItems(barangs);
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
        });
    }
    private void setUsername() {
        lblUsername.setText("Halo, "+ this.map.get("nama") + " !");
    }
    
    private void stateCheck() {
        if (barangs.isEmpty()) {
            btnSelesai.setDisable(true);
        } else {
            btnSelesai.setDisable(false);
        }
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
