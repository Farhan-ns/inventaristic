/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.smkn4.inventaristic.util.MySqlConnection;
import com.smkn4.inventaristic.util.barcode.BarcodeGen;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Immanuel
 * co-
 * @author Farhanunnasih
 */
public class DataSiswaController implements Initializable {

    @FXML
    private Button btnMenu;
    @FXML
    private Button btnSignOut;
    @FXML
    private Pane pnlCustomer;
    @FXML
    private Pane pnlOrders;
    @FXML
    private Pane pnlMenus;
    @FXML
    private Pane pnlOverview;
    @FXML
    private TableView<Siswa> tabelSiswa;
    @FXML
    private TableColumn<Siswa, String> colNis;
    @FXML
    private TableColumn<Siswa, String> colNama;
    @FXML
    private TableColumn<Siswa, String> colLahir;
    @FXML
    private TableColumn<Siswa, String> colJenkel;
    @FXML
    private TableColumn<Siswa, String> colKelas;
    @FXML
    private TableColumn<Siswa, String> colJurusan;
    @FXML
    private TableColumn<Siswa, String> colAjaran;
    @FXML
    private TableColumn<Siswa, String> colSanksi;
    @FXML
    private JFXButton btnDetail;
    @FXML
    private JFXButton btnBeriSanksi;
    @FXML
    private JFXCheckBox cbPinjaman;


    ObservableList<Siswa> students = FXCollections.observableArrayList();
    Connection connection;
    @FXML
    private JFXButton btnRefresh;
    @FXML
    private JFXButton btnGene;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.connection = MySqlConnection.getConnection();
        colNis.setCellValueFactory(new PropertyValueFactory<>("nis"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colLahir.setCellValueFactory(new PropertyValueFactory<>("lahir"));
        colJenkel.setCellValueFactory(new PropertyValueFactory<>("jenkel"));
        colKelas.setCellValueFactory(new PropertyValueFactory<>("kelas"));
        colJurusan.setCellValueFactory(new PropertyValueFactory<>("jurusan"));
        colSanksi.setCellValueFactory(new PropertyValueFactory<>("sanksi"));
        colAjaran.setCellValueFactory(new PropertyValueFactory<>("ajaran"));
        readData(false);
        setButtonAction();
    }
    
    private void setButtonAction() {
        tabelSiswa.setOnMouseClicked((event) -> {
            btnDetail.setDisable(false);
            btnBeriSanksi.setDisable(false);
            btnGene.setDisable(false);
        });
        btnDetail.setOnAction((event) -> {
            String id = tabelSiswa.getSelectionModel().getSelectedItem().getNis();
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
        btnBeriSanksi.setOnAction((event) -> {
            String id = tabelSiswa.getSelectionModel().getSelectedItem().getNis();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smkn4/inventaristic/admin/siswa/ManageBeriSanksi.fxml"));
                Parent formDetail = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(formDetail));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
                ManageBeriSanksiController controller = loader.getController();
                controller.fookinRunit(id);
            } catch (IOException ex) {
                ex.getCause();
                ex.printStackTrace();
            }
        });
        btnGene.setOnAction((event) -> {
            String id = tabelSiswa.getSelectionModel().getSelectedItem().getNis();
            BarcodeGen.generate(id);
            JOptionPane.showMessageDialog(null, "Berhasil");
        });
        btnRefresh.setOnAction((event) -> {
            readData(false);
        });
        cbPinjaman.setOnAction((event) -> {
            if (cbPinjaman.isSelected()) {
                readData(true);
            } else {
                readData(false);
            }
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
    
    private void readData(boolean q2) {
        if (!students.isEmpty()) {
            students.clear();
        }
        String query = "SELECT * FROM siswa";
        String query2 = "SELECT * FROM siswa JOIN peminjaman ON peminjaman.nis = siswa.nis WHERE peminjaman.status_peminjaman = 'belum_kembali'";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet result;
            if (q2) {
                 result = stmt.executeQuery(query2);
            } else {
                 result = stmt.executeQuery(query);
            }
            while (result.next()) {
                String nis = result.getString("nis");
                String nama = result.getString("nama_siswa");
                String tanggal_lahir = result.getString("tgl_lahir");
                String jenis_kelamin = result.getString("jenkel");
                String kelas = result.getString("kelas");
                String jurusan = result.getString("jurusan");
                String tahun_ajaran = result.getString("thn_ajaran");                
                String sanksi = result.getString("sanksi");
                students.add(new Siswa(nis, nama, tanggal_lahir, jenis_kelamin, kelas, jurusan, sanksi, tahun_ajaran));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tabelSiswa.setItems(students);
    }

    public class Siswa {
        String nis;
        String nama;
        String lahir;
        String jenkel;
        String kelas;
        String jurusan;
        String sanksi;
        String ajaran;

        public Siswa(String nis, String nama, String lahir, String jenkel, String kelas, String jurusan, String sanksi, String ajaran) {
            this.nis = nis;
            this.nama = nama;
            this.lahir = lahir;
            this.jenkel = jenkel;
            this.kelas = kelas;
            this.jurusan = jurusan;
            this.sanksi = sanksi;
            this.ajaran = ajaran;
        }

        public String getSanksi() {
            return sanksi;
        }

        public String getNis() {
            return nis;
        }

        public String getNama() {
            return nama;
        }

        public String getLahir() {
            return lahir;
        }

        public String getJenkel() {
            return jenkel;
        }

        public String getKelas() {
            return kelas;
        }

        public String getJurusan() {
            return jurusan;
        }

        public String getAjaran() {
            return ajaran;
        }
        
        
    }
    
}
