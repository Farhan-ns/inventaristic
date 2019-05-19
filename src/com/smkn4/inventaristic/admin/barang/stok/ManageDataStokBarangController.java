/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.barang.stok;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.smkn4.inventaristic.util.MySqlConnection;
import com.smkn4.inventaristic.util.barcode.BarcodeGen;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * FXML Controller class
 *
 * @author Farhanunnasih
 */
public class ManageDataStokBarangController implements Initializable {

    @FXML
    private TextArea tAreaDeskripsi;
    @FXML
    private DatePicker dpTglMasuk;
    @FXML
    private JFXRadioButton rbAset;
    @FXML
    private JFXRadioButton rbHabis;
    @FXML
    private JFXRadioButton rbBaru;
    @FXML
    private JFXRadioButton rbLama;
    @FXML
    private JFXRadioButton rbBekas;
    @FXML
    private TextField tFieldNama;
    @FXML
    private TextField tFieldLokasi;
    @FXML
    private TextField tFieldThnBarang;
    @FXML
    private TextField tFieldSumber;
    @FXML
    private TextField tFieldTglMasuk;
    @FXML
    private JFXCheckBox cbPinjaman;
    @FXML
    private JFXCheckBox cbGenerate;
    @FXML
    private JFXCheckBox cbToday;
    @FXML
    private JFXButton btnSimpan;
    @FXML
    private Label lblNotifSuccess;
    @FXML
    private Label lblNotifFail;
    @FXML
    private Label lblAction;
    
    Connection connection;
    ToggleGroup groupJenis;
    ToggleGroup groupKondisi;
    private String idBarang;      
    public String action = "tambah";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
       this.connection = MySqlConnection.getConnection();
       tFieldTglMasuk.setText(DateFormatUtils.format(Calendar.getInstance().getTime(), "dd-MM-yyyy"));
       dpTglMasuk.setDisable(true);
       setToggleGroup();
       setComponentsAction();
       Platform.runLater(() -> {
            btnSimpan.getScene().getWindow().centerOnScreen();
        });
    }
    
    private void setComponentsAction() {
        tFieldNama.setOnMouseClicked((event) -> {
            lblNotifFail.setVisible(false);
            lblNotifSuccess.setVisible(false);
        });
        dpTglMasuk.setOnAction((event) -> {
            Date d = Date.from(dpTglMasuk.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            tFieldTglMasuk.setText(DateFormatUtils.format(d, "dd-MM-yyyy"));
        });
        cbToday.setOnAction((event) -> {
            if (cbToday.isSelected()) {
                tFieldTglMasuk.setText(DateFormatUtils.format(Calendar.getInstance().getTime(), "dd-MM-yyyy"));
                dpTglMasuk.setDisable(true);
            } else {
                tFieldTglMasuk.setText("");
                dpTglMasuk.setDisable(false);
            }
        });
        btnSimpan.setOnAction((event) -> {
            if (this.action.equals("edit")) {
                editData();
            } else {
                simpanData();
            }
        });
    }
    
    private void setToggleGroup() {
       this.groupJenis = new ToggleGroup();
       rbAset.setToggleGroup(groupJenis);
       rbHabis.setToggleGroup(groupJenis);
       this.groupKondisi = new ToggleGroup();
       rbBaru.setToggleGroup(groupKondisi);
       rbLama.setToggleGroup(groupKondisi);
       rbBekas.setToggleGroup(groupKondisi);
    }
    
    private void editData() {
//        int idPetugas = 8;
//        String[] str = getTanggalMasuk().split("-");
//        String tglMasuk = str[2] + "-" + str[1] + "-" + str[0];
        String tglMasuk = getTanggalMasuk();
        String namaBarang = tFieldNama.getText();
        String jenisBarang = getJenisBarang();
        String dapatDipinjam = (cbPinjaman.isSelected()) ? "ya" : "tidak";
        String thnBarang = tFieldThnBarang.getText();
        String sumberPerolehan = tFieldSumber.getText();
        String kondisi = getKondisi();
        String lokasi = tFieldLokasi.getText();
        String totalPenggunaan = "0";
        String deskripsi = tAreaDeskripsi.getText();
        
        String query = "UPDATE barang_masuk SET tgl_masuk = ?, "
                + "nama_barang = ?, jenis_barang= ?, dapat_dipinjam = ?, thn_barang = ?, sumber_perolehan = ?, "
                + "kondisi = ?, lokasi = ?, total_penggunaan = ?, deskripsi = ? "
                + " WHERE id_barang = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
//            ps.setString(1, idBarang);
//            ps.setInt(1, idPetugas);
            ps.setString(1, tglMasuk);
            ps.setString(2, namaBarang);
            ps.setString(3, jenisBarang);
            ps.setString(4, dapatDipinjam);
            ps.setString(5, thnBarang);
            ps.setString(6, sumberPerolehan);
            ps.setString(7, kondisi);
            ps.setString(8, lokasi);
            ps.setString(9, totalPenggunaan);
            ps.setString(10, deskripsi);
            ps.setString(11, this.idBarang);
            ps.execute();
            if (this.action.equalsIgnoreCase("Edit")) {
                lblNotifSuccess.setText("Barang Berhasil Di Edit");
            }
            lblNotifSuccess.setVisible(true);
        } catch (SQLException e) {
            if (this.action.equalsIgnoreCase("Edit")) {
                lblNotifFail.setText("Edit Barang gagal");
            }
            lblNotifFail.setVisible(true);
            e.getCause();
            e.printStackTrace();
            
        }
    }
    
    public void showData() {
        String query = "SELECT * FROM barang_masuk WHERE id_barang = " + this.idBarang;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            tFieldNama.setText(rs.getString("nama_barang"));
            tFieldLokasi.setText(rs.getString("lokasi"));
            tFieldThnBarang.setText(DateFormatUtils.format(rs.getDate("thn_barang"), "yyyy"));
            tFieldSumber.setText(rs.getString("sumber_perolehan"));
            String tglMasuk = DateFormatUtils.format(rs.getDate("tgl_masuk"), "dd-MM-YYY");
            tFieldTglMasuk.setText(tglMasuk);
            tAreaDeskripsi.setText(rs.getString("deskripsi"));
            cbPinjaman.setSelected((rs.getString("dapat_dipinjam")).equalsIgnoreCase("Ya") ? true : false);
            
            String jenis = rs.getString("jenis_barang");
            String kondisi = rs.getString("kondisi");
            switch (jenis.toLowerCase()) {
                case "aset":
                rbAset.setSelected(true);
                break;
                case "habis_pakai":
                rbHabis.setSelected(true);
                break;
            }
            switch (kondisi.toLowerCase()) {
                case "baru":
                rbBaru.setSelected(true);
                break;
                case "lama":
                rbLama.setSelected(true);
                break;
                case "bekas":
                rbBekas.setSelected(true);
                break;
            }
        } catch (SQLException ex) {
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    private void simpanData() {
        String idBarang = "";
        //TODO
        //ID Petugas ganti dengan yang login
        int idPetugas = 8;
        String tglMasuk = getTanggalMasuk();
        String namaBarang = tFieldNama.getText();
        String jenisBarang = getJenisBarang();
        String dapatDipinjam = (cbPinjaman.isSelected()) ? "ya" : "tidak";
        String thnBarang = tFieldThnBarang.getText();
        String sumberPerolehan = tFieldSumber.getText();
        String kondisi = getKondisi();
        String lokasi = tFieldLokasi.getText();
        String totalPenggunaan = "0";
        String deskripsi = tAreaDeskripsi.getText();
        String query = "INSERT INTO barang_masuk(id_petugas, tgl_masuk, "
                + "nama_barang, jenis_barang, dapat_dipinjam, thn_barang, sumber_perolehan, "
                + "kondisi, lokasi, total_penggunaan, deskripsi) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
//            ps.setString(1, idBarang);
            ps.setInt(1, idPetugas);
            ps.setString(2, tglMasuk);
            ps.setString(3, namaBarang);
            ps.setString(4, jenisBarang);
            ps.setString(5, dapatDipinjam);
            ps.setString(6, thnBarang);
            ps.setString(7, sumberPerolehan);
            ps.setString(8, kondisi);
            ps.setString(9, lokasi);
            ps.setString(10, totalPenggunaan);
            ps.setString(11, deskripsi);
            ps.execute();
            lblNotifSuccess.setVisible(true);
            query = "SELECT * FROM barang_masuk ORDER BY id_barang DESC LIMIT 1";
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            if (cbGenerate.isSelected()) {
                BarcodeGen.generate("4BDG-"+rs.getString("id_barang"));
            }
        } catch (SQLException ex) {
            lblNotifFail.setVisible(true);
            ex.getCause();
            ex.printStackTrace();
        }
    }
    
    private String getTanggalMasuk() {
        try {
            String str = tFieldTglMasuk.getText();
            String[] subDate = str.split("-");
            str = subDate[2] + "-" + subDate[1] + "-" + subDate[0];
            return str;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }
    
    private String getJenisBarang() {
        try {
            RadioButton rb;
            rb = (RadioButton) this.groupJenis.getSelectedToggle();
            return rb.getText().toLowerCase().replace(" ", "_");
        } catch (NullPointerException e) {
            return "";
        }
    }
    private String getKondisi() {
        try {
            RadioButton rb;
            rb = (RadioButton) this.groupKondisi.getSelectedToggle();
            return rb.getText().toLowerCase().replace(" ", "_");
        } catch (NullPointerException e) {
            return "";
        }
    }
    
    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
        if (this.action.toLowerCase().equals("edit")) {
            lblAction.setText("Edit Barang");
        }
    }
    
}
