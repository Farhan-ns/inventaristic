/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.barang.bermasalah;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.smkn4.inventaristic.util.MySqlConnection;
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
public class ManageDataBarangMasalahController implements Initializable {

    @FXML
    private TextField tFieldNama;
    @FXML
    private JFXRadioButton rbRusak;
    @FXML
    private JFXRadioButton rbHilang;
    @FXML
    private TextArea tAreaDeskripsi;
    @FXML
    private DatePicker dpTglMasuk;
    @FXML
    private JFXButton btnSimpan;
    @FXML
    private JFXCheckBox cbToday;
    @FXML
    private TextField tFieldTglMasalah;
    @FXML
    private Label lblNotifSuccess;
    @FXML
    private Label lblNotifFail;
    
    Connection connection;
    String idBarang;
    String idBarangMasalah;
    ToggleGroup groupJenis;
    String action = "tambah";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        tFieldTglMasalah.setText(DateFormatUtils.format(Calendar.getInstance().getTime(), "dd-MM-yyyy"));
        dpTglMasuk.setDisable(true);
        
        setComponentsAction();
        setToggleGroup();
        Platform.runLater(() -> {
            btnSimpan.getScene().getWindow().centerOnScreen();
        });
    }    
    
    private void setComponentsAction() {
        btnSimpan.setOnAction((event) -> {
            if (this.action.equals("edit")) {
                editData();
            } else {
                simpanData();
            }
        });
        dpTglMasuk.setOnAction((event) -> {
            Date d = Date.from(dpTglMasuk.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            tFieldTglMasalah.setText(DateFormatUtils.format(d, "dd-MM-yyyy"));
        });
        cbToday.setOnAction((event) -> {
            if (cbToday.isSelected()) {
                tFieldTglMasalah.setText(DateFormatUtils.format(Calendar.getInstance().getTime(), "dd-MM-yyyy"));
                dpTglMasuk.setDisable(true);
            } else {
                tFieldTglMasalah.setText("");
                dpTglMasuk.setDisable(false);
            }
        });
    }
    
    private void setToggleGroup() {
        this.groupJenis = new ToggleGroup();
        rbHilang.setToggleGroup(groupJenis);
        rbRusak.setToggleGroup(groupJenis);
    }
    
    private void simpanData() {
        String tglBermasalah = getTanggalMasuk();
        String idBarang = this.idBarang;
        System.out.println(idBarang);
        String deskripsi = tAreaDeskripsi.getText();
        String jenisMasalah = getMasalah();
        try {
            String query = "INSERT INTO barang_bermasalah(tgl_bermasalah, id_barang, deskripsi, jenis_masalah) "
                    + " VALUES(?, ?, ?, ?)";
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, tglBermasalah);
            ps.setString(2, idBarang);
            ps.setString(3, deskripsi);
            ps.setString(4, jenisMasalah);
            ps.executeUpdate();
//            String query = "INSERT INTO barang_bermasalah(tgl_bemasalah, id_barang, deskripsi, jenis_masalah) "
//                    + " VALUES( tgl_bermasalah = " + tglBermasalah + ", id_barang = " + idBarang 
//                    + ", deskripsi = " + deskripsi + ", jenis_masalah = " + jenisMasalah + ")";
//            Statement stmt = this.connection.createStatement();
//            System.out.println(query);
//            stmt.execute(query);
            lblNotifSuccess.setVisible(true);
        } catch (SQLException e) {
            lblNotifFail.setVisible(true);
            e.getCause();
            e.printStackTrace();
        }
    }
    private void editData() {
        String tglBermasalah = getTanggalMasuk();
        String idBarang = this.idBarang;
        String idBarangMasalah = this.idBarangMasalah;
        String deskripsi = tAreaDeskripsi.getText();
        String jenisMasalah = getMasalah();
        try {
            String query = "UPDATE barang_bermasalah SET tgl_bermasalah = ?, id_barang = ?, "
                    + " deskripsi = ?, jenis_masalah = ?"
                    + " WHERE id_barang_masalah = ?";
            System.out.println(idBarang);
            System.out.println(idBarangMasalah);
            PreparedStatement ps = this.connection.prepareStatement (query);
            ps.setString(1, tglBermasalah);
            ps.setString(2, idBarang);
            ps.setString(3, deskripsi);
            ps.setString(4, jenisMasalah);
            ps.setString(5, idBarangMasalah);
            ps.executeUpdate();
            lblNotifSuccess.setVisible(true);
        } catch (SQLException e) {
            lblNotifFail.setVisible(true);
            e.getCause();
            e.printStackTrace();
        }
    }
    
    public void showData(String idBarangMasalah) {
        try {
            String query = "SELECT * FROM barang_bermasalah "
                    + "INNER JOIN barang_masuk ON barang_masuk.id_Barang = barang_bermasalah.id_Barang "
                    + "WHERE id_barang_masalah = " + idBarangMasalah;
            Statement stmt =  this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.first();
            tFieldNama.setText(rs.getString("nama_barang"));
            tFieldTglMasalah.setText(DateFormatUtils.format(rs.getDate("tgl_bermasalah"), "dd-MM-yyyy"));
            String jenis = rs.getString("jenis_masalah").toLowerCase();
            if (jenis.equals("rusak")) {
                rbRusak.setSelected(true);
            } else {
                rbHilang.setSelected(true);
            }
            tAreaDeskripsi.setText(rs.getString("deskripsi"));
        } catch (SQLException e) {
            e.getCause();
            e.printStackTrace();
        }
    }
    
    public void readData(String idBarang, String namaBarang) {
        this.idBarang = idBarang;
        tFieldNama.setText(namaBarang);
    }
    
    private String getMasalah() {
        RadioButton rb = (RadioButton) this.groupJenis.getSelectedToggle();
        return rb.getText().toLowerCase();
    }
    
    private String getTanggalMasuk() {
        try {
            String str = tFieldTglMasalah.getText();
            String[] subDate = str.split("-");
            str = subDate[2] + "-" + subDate[1] + "-" + subDate[0];
            return str;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }
    
}
