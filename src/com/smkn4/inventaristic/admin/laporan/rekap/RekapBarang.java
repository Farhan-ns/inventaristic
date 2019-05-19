/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.laporan.rekap;

import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Salman
 */
public class RekapBarang extends javax.swing.JFrame {

    private final Connection koneksi;

    public RekapBarang() {
        initComponents();
        koneksi = MySqlConnection.getConnection();
        setLocationRelativeTo(null);
        showData();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    DefaultTableModel dtm;

    private String getCellValue(int x, int y) {
        return dtm.getValueAt(x, y).toString();
    }

//    export data
    private void exportToExcel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();

//        header
        MessageFormat title = new MessageFormat("Laporan Data Barang Bermasalah");

//        load data
        TreeMap<String, Object[]> data = new TreeMap<>();
        data.put("-1", new Object[]{dtm.getColumnName(0), dtm.getColumnName(1), dtm.getColumnName(2), dtm.getColumnName(3), dtm.getColumnName(4), dtm.getColumnName(5), dtm.getColumnName(6), dtm.getColumnName(7), dtm.getColumnName(8)});

//    load data cell row
        for (int i = 0; i < dtm.getRowCount(); i++) {
            data.put(Integer.toString(i), new Object[]{getCellValue(i, 0), getCellValue(i, 1), getCellValue(i, 2), getCellValue(i, 3), getCellValue(i, 4), getCellValue(i, 5), getCellValue(i, 6), getCellValue(i, 7), getCellValue(i, 8)});
        }
//     Write to excel
        Set<String> ids = data.keySet();
        XSSFRow row;
        int rowID = 0;

        for (String key : ids) {
            row = ws.createRow(rowID++);

            Object[] values = data.get(key);
            int cellID = 0;
            for (Object o : values) {
                Cell cell = row.createCell(cellID++);
                cell.setCellValue(o.toString());
            }

        }

//        Save File
        try {
            FileOutputStream fos = new FileOutputStream(new File("D:/Excel/RekapBarang.xls"));
            wb.write(fos);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RekapBarang.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RekapBarangBermasalah.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showData() {
        String[] kolom = {"No", "ID Barang", "Nama Barang", "Jenis", "Tanggal Masuk", "Jumlah", "Status", "Lokasi", "Waktu Pakai"};

        dtm = new DefaultTableModel(null, kolom);
        JTableHeader header = tbl_rekap.getTableHeader();
        header.setFont(new java.awt.Font("Calibri", 1, 13));
        header.setForeground(new java.awt.Color(0, 0, 0));
        dtm.getDataVector().removeAllElements();
        hitungUmur();
//        Map<String, String> penggunaan = new HashMap<String, String>();
//        String waktu[] = new String[5];
//        String totalWaktu = " ";
//        String concat = "";
//        String satuan[] = {" Tahun ", " Bulan ", " Hari ", " Jam ", " Menit "};

        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT *, COUNT(nama_barang) FROM barang_masuk GROUP BY nama_barang";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("" + query);
            int no = 1;
//            int w = 1;
            while (rs.next()) {
                String id_barang = rs.getString("id_barang");
                String nama_barang = rs.getString("nama_barang");
                String jenis_barang = rs.getString("jenis_barang");
                String tanggal_masuk = rs.getString("tgl_masuk");
                String jumlah = rs.getString("COUNT(nama_barang)");
                String status = rs.getString("kondisi");
                String lokasi = rs.getString("lokasi");
                String total_pakai = rs.getString("total_penggunaan");
                String hasilKonversi = hasilKonversi(total_pakai);
//                total_pakai = konversiWaktu("1051200").get("time_1");
//                total_pakai += konversiWaktu("1051200").get("time_2");
//                total_pakai += konversiWaktu("1051200").get("time_3");
//                total_pakai += konversiWaktu("1051200").get("time_4");
//                total_pakai += konversiWaktu("1051200").get("time_5");
//                for (String hasilKonversi1 : hasilKonversi) {
//                    System.out.println(hasilKonversi1);
//                }

//                total_pakai = hasilKonversi[0] + " Tahun " +
//                                hasilKonversi[1] + " Bulan " +
//                                   hasilKonversi[2] + " Hari " +
//                                    hasilKonversi[3] + " Jam " +
//                                      hasilKonversi[4] + " Menit ";
                System.out.println(total_pakai);
                //hasil 
                //00000
//                200323
//                00000

//                for (int i = 0; i < konversiWaktu(total_pakai).size(); i++) {
//                    waktu[i] = konversiWaktu(total_pakai).get("time_"+i+1);
////                    try {
//////                        concat = waktu[i].concat(satuan[i]);
////                        for (int j = 0; j < waktu.length; j++) {
////                          concat = waktu[i] + satuan[i];  
////                        }
////                    } catch (Exception e) {
////
////                    }
//                }
//                  /1 bulan 22hari (74880);
//                /*waktu[0] = konversiWaktu(total_pakai).get("time_1") +" Tahun ";
//                waktu[1] = konversiWaktu(total_pakai).get("time_2") +" Bulan ";
//                waktu[2] = konversiWaktu(total_pakai).get("time_3") +" Hari ";
//                waktu[3] = konversiWaktu(total_pakai).get("time_4") +" Jam ";
//                waktu[4] = konversiWaktu(total_pakai).get("time_5") +" Menit ";
//
//
//
//
//
//                for (int i = 0; i < waktu.length; i++) {
//                    totalWaktu += waktu[i];
//                } */               
                dtm.addRow(new String[]{no + "", id_barang, nama_barang, jenis_barang, tanggal_masuk, jumlah, status, lokasi, hasilKonversi});
                no++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_rekap.setModel(dtm);
        tbl_rekap.getColumnModel().getColumn(0).setPreferredWidth(5);
        tbl_rekap.getColumnModel().getColumn(1).setPreferredWidth(20);
        tbl_rekap.getColumnModel().getColumn(2).setPreferredWidth(30);
        tbl_rekap.getColumnModel().getColumn(3).setPreferredWidth(60);
        tbl_rekap.getColumnModel().getColumn(4).setPreferredWidth(80);
        tbl_rekap.getColumnModel().getColumn(5).setPreferredWidth(20);
        tbl_rekap.getColumnModel().getColumn(6).setPreferredWidth(20);
        tbl_rekap.getColumnModel().getColumn(7).setPreferredWidth(20);
        tbl_rekap.getColumnModel().getColumn(8).setPreferredWidth(100);
    }

    public String hasilKonversi(String s) {
        String[] lama = new String[5];
        String hasil = "";

        lama[0] = konversiWaktu(s).get("time_1");
        lama[1] = konversiWaktu(s).get("time_2");
        lama[2] = konversiWaktu(s).get("time_3");
        lama[3] = konversiWaktu(s).get("time_4");
        lama[4] = konversiWaktu(s).get("time_5");
        
        if (!(lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[1] + " Bulan "
                    + lama[2] + " Hari "
                    + lama[3] + " Jam "
                    + lama[4] + " Menit ";
        }
        if ((!lama[0].equals("0") && !lama[1].equals("0")
                && !lama[2].equals("0") && !lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[1] + " Bulan "
                    + lama[2] + " Hari "
                    + lama[3] + " Jam ";
        }
        if ((!lama[0].equals("0") && !lama[1].equals("0")
                && !lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[1] + " Bulan "
                    + lama[2] + " Hari ";
        }
        if ((!lama[0].equals("0") && !lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[1] + " Bulan ";
        }
        if ((!lama[0].equals("0") && lama[1].equals("0")
                && !lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[2] + " Hari ";
        }
        if ((!lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && !lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[3] + " Jam ";
        }
        if ((!lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun "
                    + lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && !lama[1].equals("0")
                && !lama[2].equals("0") && !lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[1] + " Bulan "
                    + lama[2] + " Hari "
                    + lama[3] + " Jam "
                    + lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && !lama[1].equals("0")
                && !lama[2].equals("0") && !lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[1] + " Bulan "
                    + lama[2] + " Hari "
                    + lama[3] + " Jam ";
        }
        if ((lama[0].equals("0") && !lama[1].equals("0")
                && !lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[1] + " Bulan "
                    + lama[2] + " Hari ";
        }
        if ((lama[0].equals("0") && !lama[1].equals("0")
                && lama[2].equals("0") && !lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[1] + " Bulan "
                    + lama[3] + " Jam ";
        }
        if ((lama[0].equals("0") && !lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[1] + " Bulan "
                    + lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && !lama[2].equals("0") && !lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[2] + " Hari "
                    + lama[3] + " Jam "
                    + lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && !lama[2].equals("0") && !lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[2] + " Hari "
                    + lama[3] + " Jam ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && !lama[2].equals("0") && lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[2] + " Hari "
                    + lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && !lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[3] + " Jam "
                    + lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && !lama[4].equals("0"))) {
            hasil = lama[4] + " Menit ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && !lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[3] + " Jam ";
        }
        if ((lama[0].equals("0") && lama[1].equals("0")
                && !lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[2] + " Hari ";
        }
        if ((lama[0].equals("0") && !lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[1] + " Bulan ";
        }
        if ((!lama[0].equals("0") && lama[1].equals("0")
                && lama[2].equals("0") && lama[3].equals("0")
                && lama[4].equals("0"))) {
            hasil = lama[0] + " Tahun ";
        }

        return hasil;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_rekap = new javax.swing.JTable();
        Print = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setType(java.awt.Window.Type.POPUP);

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel1.setText("Laporan Data Barang");

        tbl_rekap.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        tbl_rekap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_rekap.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl_rekap);

        Print.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        Print.setText("Print");
        Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jButton2.setText("Export Excel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Print, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jButton2)
                .addGap(67, 67, 67))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 854, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addComponent(jLabel1)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Print)
                    .addComponent(jButton2))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintActionPerformed
        String Source = null;
        String Report = null;

        try {
            Source = System.getProperty("user.dir") + "/lib/rekap/Rekap.jrxml";
            Report = System.getProperty("user.dir") + "/lib/rekap/Rekap.jasper";

            JasperReport jasperReport = JasperCompileManager.compileReport(Source);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, koneksi);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, Report);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_PrintActionPerformed

    public Map<String, String> showPeminjaman(String s) {
        Map<String, String> fdate = new HashMap<>();
        Map<String, String> edate = new HashMap<>();
        Map<String, String> date = new HashMap<>();
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM `db_inventaris`.`peminjaman`";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("" + query);
            int no = 1;
            while (rs.next()) {
                String tgl_peminjaman = rs.getString("tgl_peminjaman");
                String tgl_kembali = rs.getString("tgl_kembali");
                fdate.put("date_" + no, tgl_peminjaman);
                edate.put("date_" + no, tgl_kembali);
//                if (s == "kembali") {
//                    date.put(fdate.get("date_" + no), edate.get("date_" + no));
//                } else {
//                    date.put(edate.get("date_" + no), fdate.get("date_" + no));
//                }
                no++;
            }
            if ("pinjam".equals(s)) {
                date = fdate;
            } else {
                date = edate;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return date;
    }

    public void hitungUmur() {
        Map<String, String> dp = showPeminjaman("pinjam");
        Map<String, String> dk = showPeminjaman("kembali");

        Map<String, String> dateTime = new HashMap<String, String>();
        Map<String, String> lama = new HashMap<String, String>();

//        java.time.format.DateTimeParseException: could not be parsed at index 20
//      ss must be 00
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");

        int o = 1;
        int nk = dk.size();
        int np = dp.size();
        LocalDateTime dateTime1 = null;
        LocalDateTime dateTime2 = null;
        long diffInMinutes;

        while (o <= nk && o <= np) {
            dateTime1 = LocalDateTime.parse(dp.get("date_" + o), formatter);
            dateTime2 = LocalDateTime.parse(dk.get("date_" + o), formatter);
            lama.put("lama_" + o, java.time.Duration.between(dateTime1, dateTime2).toMinutes() + "");
            dateTime.put("date_" + o, lama.get("lama_" + o));
            o++;
        }

//        long diffInMilli = java.time.Duration.between(dateTime1, dateTime2).toMillis();
//        long diffInSeconds = java.time.Duration.between(dateTime1, dateTime2).getSeconds();
////        long diffInMinutes = java.time.Duration.between(dateTime1, dateTime2).toMinutes();
//        long diffInHours = java.time.Duration.between(dateTime1, dateTime2).toHours();
//        long diffInDays = java.time.Duration.between(dateTime1, dateTime2).toDays();
//        long lama = diffInMinutes;
//        if (diffInMinutes < 60) {
//            lama = diffInMinutes;
//            satuan = " Menit";
//        }
//        if (diffInMinutes >= 60) {
//            lama = diffInHours;
//            satuan = " Jam";
//        }
//        if (diffInHours >= 24) {
//            lama = diffInDays;
//            satuan = " Hari";
//        }
//        if (diffInDays >= 30) {
//            lama = diffInDays / 30;
//            satuan = " Bulan";
////                    diffInDays / 30;
//        }if (diffInDays >= 365){
//            lama = diffInDays / 365;
//            satuan = " Tahun";
//        }
//        isi[0] = lama / 525600;
//        isi[1] = (lama % 525600) / 43200;
//        isi[2] = ((lama % 525600) % 43200) / 1440;
//        isi[3] = (((lama % 525600) % 43200) % 1440) / 60;
//        isi[4] = (((lama % 525600) % 43200) % 1440) % 60;
//        for (int i = 0; i < satuan.length; i++) {
//            periode.put(satuan[i], isi[i] + "");
//        }
        int u = 1;
        try {
            Statement stmt = koneksi.createStatement();
            while (u <= dateTime.size()) {
                String query = "UPDATE `barang_masuk` SET `total_penggunaan` = '" + dateTime.get("date_" + u) + "" + "' "
                        + "WHERE id_barang = '" + u + "" + "'";
                System.out.println(query);
                stmt.executeUpdate(query);
                u++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan di Query");
        }

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        exportToExcel();
    }//GEN-LAST:event_jButton2ActionPerformed
    public Map<String, String> konversiWaktu(String lama) {
        Map<String, String> periode = new HashMap<String, String>();
        int isi[];
        isi = new int[5];
        int lama_ = Integer.parseInt(lama);
        // mulai dari Tahun ke Menit
        String satuan[] = {"time_1", "time_2", "time_3", "time_4", "time_5"};

        isi[0] = lama_ / 525600;
        isi[1] = (lama_ % 525600) / 43200;
        isi[2] = ((lama_ % 525600) % 43200) / 1440;
        isi[3] = (((lama_ % 525600) % 43200) % 1440) / 60;
        isi[4] = (((lama_ % 525600) % 43200) % 1440) % 60;

        for (int i = 0; i < satuan.length; i++) {
            periode.put(satuan[i], isi[i] + "");
        }
        return periode;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RekapBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RekapBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RekapBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RekapBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RekapBarang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Print;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_rekap;
    // End of variables declaration//GEN-END:variables
}
