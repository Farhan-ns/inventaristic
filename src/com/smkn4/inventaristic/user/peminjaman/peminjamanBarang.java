/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.user.peminjaman;

import com.smkn4.inventaristic.util.MySqlConnection;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Aip Ariyadi
 */
public class peminjamanBarang extends javax.swing.JFrame {

    /**
     * Creates new form viewKartu
     */
    Connection koneksi;
    public int no = 1;
    public peminjamanBarang() {
        initComponents();
        createTable();
        koneksi = MySqlConnection.getConnection();
        txt_barang.setForeground(new Color(0,0,0,0));
        txt_barang.setBackground(new Color(0,0,0,0));
        //mengambil ukuran layar
        Dimension layar = Toolkit.getDefaultToolkit().getScreenSize();
        
        //membuat kordinat
        int x = layar.width / 2 - this.getSize().width / 2;
        int y = layar.height / 2 - this.getSize().height / 2;
        
        this.setLocation(x, y);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_nis = new javax.swing.JLabel();
        txt_nama = new javax.swing.JLabel();
        txt_kelas = new javax.swing.JLabel();
        txt_barang = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_pinjam = new javax.swing.JTable();
        btn_pinjam = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        lblNotif = new javax.swing.JLabel();
        judul = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txt_nis.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        txt_nis.setText("NIS");

        txt_nama.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        txt_nama.setText("Nama");

        txt_kelas.setFont(new java.awt.Font("Tekton Pro", 0, 18)); // NOI18N
        txt_kelas.setText("Kelas");

        txt_barang.setBorder(null);
        txt_barang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_barangKeyReleased(evt);
            }
        });

        tbl_pinjam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama Barang", "Kode Barang"
            }
        ));
        tbl_pinjam.setEnabled(false);
        jScrollPane1.setViewportView(tbl_pinjam);

        btn_pinjam.setText("Pinjam");
        btn_pinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pinjamActionPerformed(evt);
            }
        });

        btn_batal.setText("Batal");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        lblNotif.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblNotif.setText("Scan Barcode Barang");

        judul.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        judul.setText("Peminjaman Barang");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nama)
                            .addComponent(txt_nis)
                            .addComponent(txt_kelas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNotif)
                            .addComponent(btn_batal))
                        .addGap(18, 18, 18)
                        .addComponent(btn_pinjam)
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txt_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(judul)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_barang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(txt_nis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nama)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_kelas))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(judul)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                .addComponent(lblNotif)
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_pinjam)
                    .addComponent(btn_batal))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_barangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_barangKeyReleased
        scanBarang();
    }//GEN-LAST:event_txt_barangKeyReleased

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        pilihanMenu main = new pilihanMenu();
        main.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_batalActionPerformed

    private void btn_pinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pinjamActionPerformed
        createPeminjaman();
        addBarang();
        pilihanMenu main = new pilihanMenu();
        main.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_pinjamActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(peminjamanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(peminjamanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(peminjamanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(peminjamanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new peminjamanBarang().setVisible(true);
            }
        });
    }
    
   //start belum kepake
    String[] semuaBarang;
    public String[] cariBarangSama(String idBarang) {
        int max = tbl_pinjam.getRowCount();
        TableModel model = tbl_pinjam.getModel();
        String barangAda;
        for (int i = 0; i < max; i++){
            if(tbl_pinjam.getRowCount() > 0){
                barangAda = model.getValueAt(i,2).toString();
                semuaBarang[i] = barangAda;
            }
        }
        return semuaBarang;
    }
    
    public void cekBarang(String idBarang) {
        String[] Barang = cariBarangSama(idBarang);
        if(Barang == null) {
            showBarang(idBarang);
        }else {
            for(String i : Barang){
                if(idBarang != i) {
                    showBarang(idBarang);
                }
            }
        }
    }
    //end belum kepake
    
    public void scanBarang(){
       if(txt_barang.getText().length() == 12){
            String id_barang = txt_barang.getText();
            txt_barang.setText("");
            showBarang(id_barang);
//            cekBarang(id_barang);
            System.out.println(id_barang);
        }
    }
    
    public void showIdentitas(String id) {
        try {
            Statement stat = koneksi.createStatement();
            String query = "SELECT nis, nama_siswa, kelas  FROM siswa WHERE nis = '"+id+"'";
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                txt_nis.setText(rs.getString(1));
                txt_nama.setText(rs.getString(2));
                txt_kelas.setText(rs.getString(3));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    DefaultTableModel dtm;
    public void createTable(){
        String[] kolom = {"No", "Nama Barang", "Kode Barang"};
        dtm = new DefaultTableModel(null, kolom);
    }
    
    public void tambahBarang(String idBarang, String namaBarang) {
        dtm.addRow(new String[]{no + "", namaBarang, idBarang});
        no++;
    }
    
    public void showBarang(String id) {
        try {
            Statement stat = koneksi.createStatement();
            String query = "SELECT id_barang, nama_barang FROM barang_masuk WHERE id_barang = '"+id+"'";
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String idBarang = rs.getString(1);
                String namaBarang = rs.getString(2);
                tambahBarang(idBarang, namaBarang);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_pinjam.setModel(dtm);
    }
    
    public String getTanggal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void createPeminjaman() {
        String tanggalPinjam = getTanggal();
        String nis = txt_nis.getText();
        try {
            Statement stmt = koneksi.createStatement();
            String query = "INSERT INTO peminjaman(tgl_peminjaman, nis, status_peminjaman) " +
                "VALUES('"+tanggalPinjam+"', "+"'"+nis+"', 'Belum Kembali')";
            System.out.println(query);
            stmt.executeUpdate(query);
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan di Query");
        }
    }
    
    public String cariPeminjaman() {
        String nis = txt_nis.getText();
        String idPinjam = "";
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT id_peminjaman FROM peminjaman WHERE nis ='"+nis+"' AND tgl_kembali IS NULL";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            idPinjam = rs.getString("id_peminjaman");
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi Kesalahan di Query");
        }
        return idPinjam;
    }
    
    public void addBarang() {
        //ambil data dari tabel
        int max = tbl_pinjam.getRowCount();
        TableModel model = tbl_pinjam.getModel();
        String idBarang;
        String idPinjam = cariPeminjaman();
        for (int i = 0; i < max; i++){
            idBarang = model.getValueAt(i,2).toString();
            addRincian(idPinjam, idBarang);
        }
        jumlahPinjam(max+"", idPinjam);
    }
    
    public void addRincian(String idPinjam, String idBarang) {
        try {
            Statement stmt = koneksi.createStatement();
            String query = "INSERT INTO rincian(id_peminjaman, id_barang, status_barang) " +
                "VALUES('"+idPinjam+"', "+"'"+idBarang+"', 'Dipinjam')";
            System.out.println(query);
            stmt.executeUpdate(query);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void jumlahPinjam(String max, String idPinjam) {
        try {
            Statement stmt = koneksi.createStatement();
            String query = "UPDATE peminjaman SET jumlah_dipinjam = '"+ max +"' WHERE peminjaman.id_peminjaman = "+ idPinjam;
            System.out.println(query);
            stmt.executeUpdate(query);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_pinjam;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel judul;
    private javax.swing.JLabel lblNotif;
    private javax.swing.JTable tbl_pinjam;
    private javax.swing.JTextField txt_barang;
    private javax.swing.JLabel txt_kelas;
    private javax.swing.JLabel txt_nama;
    private javax.swing.JLabel txt_nis;
    // End of variables declaration//GEN-END:variables
}
