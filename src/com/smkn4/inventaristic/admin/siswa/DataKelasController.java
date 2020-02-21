/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Farhanunnasih
 */
public class DataKelasController implements Initializable {

    @FXML
    private Button btnMenu;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOverview;

    @FXML
    private TableView<Kelas> tabelSiswa;

    @FXML
    private TableColumn<Kelas, String> colID;

    @FXML
    private TableColumn<Kelas, String> colNamaKelas;

    @FXML
    private TableColumn<Kelas, String> colTahunMasuk;

    @FXML
    private TableColumn<Kelas, String> colTingkat;

    @FXML
    private TableColumn<Kelas, String> colSanksi;

    @FXML
    private TableColumn<Kelas, String> colBarcode;

    @FXML
    private JFXButton btnDetail;

    @FXML
    private JFXButton btnBeriSanksi;

    @FXML
    private JFXCheckBox cbPinjaman;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXButton btnGene;

    @FXML
    private JFXButton btnShow;
    
    private Map<String, String> map;

    ObservableList<Kelas> classes = FXCollections.observableArrayList();
    Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        Platform.runLater(() -> {
            btnMenu.getScene().getWindow().centerOnScreen();
        });

        setColumns();
        readData(false);
        setButtonAction();
    }

    public void setButtonAction() {
        tabelSiswa.setOnMouseClicked((event) -> {
            btnDetail.setDisable(false);
            btnBeriSanksi.setDisable(true);
            btnGene.setDisable(false);
        });
        cbPinjaman.setOnAction((event) -> {
            if (cbPinjaman.isSelected()) {
                readData(true);
            } else {
                readData(false);
            }
        });
        btnDetail.setOnAction((event) -> {
            String id = tabelSiswa.getSelectionModel().getSelectedItem().getId();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/siswa/RincianPinjam.fxml"));
                Parent formDetail = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(formDetail));
                stage.show();
                RincianPinjamController controller = loader.getController();
                controller.showData(id);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnRefresh.setOnAction((event) -> {
            readData(false);
        });
        btnMenu.setOnAction((event) -> {
            try {
                Stage stage = (Stage) btnMenu.getScene().getWindow();
                Parent root = FXMLLoader.load((getClass().getResource("/com/smkn4/inventaristic/admin/AdminHome.fxml")));
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        btnShow.setOnAction((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/siswa/ManageTahunAjaran.fxml"));
                Parent formDetail = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(formDetail));
                //stage.initStyle(StageStyle.UTILITY);
                stage.show();
                ManageTahunAjaranController controller = loader.getController();
                controller.setMap(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void readData(boolean withPeminjaman) {
        if (!classes.isEmpty()) {
            classes.clear();
        }

        String query = "SELECT * FROM kelas";
        String query2 = "SELECT * FROM kelas JOIN peminjaman ON peminjaman.id_kelas = kelas.id_kelas WHERE peminjaman.status_peminjaman = 'belum_kembali'";

        try {
            Statement stmt = this.connection.createStatement();
            ResultSet result;
            if (withPeminjaman) {
                result = stmt.executeQuery(query2);
            } else {
                result = stmt.executeQuery(query);
            }
            while (result.next()) {
                String id = result.getString("id_kelas");
                String namaKelas = result.getString("nama_kelas");
                String tahunMasuk = result.getString("tahun_masuk");
                String tingkat = result.getString("tingkat");
                String sanksi = result.getString("sanksi");
                String barcode = result.getString("barcode");
                classes.add(new Kelas(id, namaKelas, tahunMasuk, tingkat, sanksi, barcode));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tabelSiswa.setItems(classes);
    }
    

    public void setColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNamaKelas.setCellValueFactory(new PropertyValueFactory<>("namaKelas"));
        colTahunMasuk.setCellValueFactory(new PropertyValueFactory<>("tahunMasuk"));
        colTingkat.setCellValueFactory(new PropertyValueFactory<>("tingkat"));
        colSanksi.setCellValueFactory(new PropertyValueFactory<>("sanksi"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
    }
    
    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public class Kelas {

        String id;
        String namaKelas;
        String tahunMasuk;
        String tingkat;
        String sanksi;
        String barcode;

        public Kelas(String id, String namaKelas, String tahunMasuk, String tingkat, String sanksi, String barcode) {
            this.id = id;
            this.namaKelas = namaKelas;
            this.tahunMasuk = tahunMasuk;
            this.tingkat = tingkat;
            this.sanksi = sanksi;
            this.barcode = barcode;
        }

        public String getId() {
            return id;
        }

        public String getNamaKelas() {
            return namaKelas;
        }

        public String getTahunMasuk() {
            return tahunMasuk;
        }

        public String getTingkat() {
            return tingkat;
        }

        public String getSanksi() {
            return sanksi;
        }

        public String getBarcode() {
            return barcode;
        }
    }

}
