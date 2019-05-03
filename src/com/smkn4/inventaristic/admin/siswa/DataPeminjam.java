/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

/**
 *
 * @author Tan
 */
import java.sql.*;
//import java.text.SimpleDateFormat;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.smkn4.inventaristic.util.MySqlConnection;

public class DataPeminjam extends javax.swing.JFrame {

    /**
     * Creates new form DataPeminjam
     */
    int barisPilihan = 0;
    ResultSet result;
    Connection connection;
    PreparedStatement preStatement;
    DefaultTableModel defaultTableModel;
    public DataPeminjam() {
        initComponents();
        readData();
    }
    
    private void readData() {
        String[] kolomTabel = {"NIS", "Nama","Tanggal Lahir", "Jenis Kelamin", "Kelas", "Jurusan", "Tahun Ajaran"};
        defaultTableModel   = new DefaultTableModel(null, kolomTabel);
        try {
            connection      = MySqlConnection.getConnection();
            preStatement    = connection.prepareStatement("SELECT * FROM siswa");
            result          = preStatement.executeQuery();
            while(result.next()){
                String nis = result.getString("nis");
                String nama = result.getString("nama_siswa");
                String tanggal_lahir = result.getDate("tgl_lahir").toString();
                String jenis_kelamin = result.getString("jenkel");
                String kelas = result.getString("kelas");
                String jurusan = result.getString("jurusan");
                String tahun_ajaran = result.getString("thn_ajaran");

                defaultTableModel.addRow(new String[]{nis,nama,tanggal_lahir,jenis_kelamin,kelas,jurusan,tahun_ajaran});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ada Kesalahan Query");
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        tabel_peminjam.setModel(defaultTableModel);
        initTableColumn();
        
        ButtonGroup jenkel = new ButtonGroup();
        jenkel.add(fLaki);
        jenkel.add(fPerempuan);
        jenkel.add(fNone);
        
    }
    
     private void filterData(){
         
        String kelass = null;
        String jk = "";
        
        if (fKelas.getSelectedItem() == "X"){
            kelass = "%X-%";
        }else if (fKelas.getSelectedItem() == "XI"){
            kelass = "%XI-%";
        }else if (fKelas.getSelectedItem() == "XII"){
            kelass = "%XII-%";
        }else if (fKelas.getSelectedItem() == "Default"){
            kelass = "%%";
        }
        String jur = "";
        if (fJurusan.getSelectedItem() == "RPL"){
            jur = "%Rekayasa%";
        }else if (fJurusan.getSelectedItem() == "TKJ"){
            jur = "%Komputer%";
        } else if (fJurusan.getSelectedItem() == "MM"){
            jur = "%Multi%";
        } else if (fJurusan.getSelectedItem() == "TOI"){
            jur = "%Otomasi%";
        } else if (fJurusan.getSelectedItem() == "TITL"){
            jur = "%Listrik%";
        } else if (fJurusan.getSelectedItem() == "TEAV"){
            jur = "%Audio%";
        } else if (fJurusan.getSelectedItem() == "Default"){
            jur = "%%";
        }
        
        if(fLaki.isSelected()) {
            jk = "L";
        }else if (fPerempuan.isSelected()) {
            jk = "P";
        }else if (fNone.isSelected()){
            jk = "%%";
        }
        
        
        String[] kolomTabel = {"NIS", "Nama","Tanggal Lahir", "Jenis Kelamin", "Kelas", "Jurusan", "Tahun Ajaran"};
        defaultTableModel   = new DefaultTableModel(null, kolomTabel);
        try {
            connection      = MySqlConnection.getConnection();
            
            String us1="";
            if(usia_cmb.getSelectedItem()=="Default"){
                 us1="";
            }else if(usia_cmb.getSelectedItem()=="Termuda"){
                 us1=" ORDER BY tgl_lahir DESC ";   
            }else if(usia_cmb.getSelectedItem()=="Tertua"){
                 us1=" ORDER BY tgl_lahir ASC ";   
            }
            preStatement    = connection.prepareStatement("SELECT * FROM siswa WHERE kelas LIKE '"+kelass+"'AND jenkel LIKE" + "'%"+jk+"%' AND jurusan LIKE '"+jur+"'"+us1);
            result          = preStatement.executeQuery();
            while(result.next()){
                String nis              = result.getString("nis");
                String nama             = result.getString("nama_siswa");
                String tanggal_lahir      = result.getDate("tgl_lahir").toString();
                String jenis_kelamin   = result.getString("jenkel");
                String kelas       =    result.getString("kelas");
                String jurusan        =    result.getString("jurusan");
                String tahun_ajaran        =    result.getString("thn_ajaran");

                defaultTableModel.addRow(new String[]{nis,nama,tanggal_lahir,jenis_kelamin,kelas,jurusan,tahun_ajaran});
                System.out.println(kelas);
                System.out.println(kelass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ada Kesalahan Query");
        }finally{
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        tabel_peminjam.setModel(defaultTableModel);
        initTableColumn();
    }
    
     private void initTableColumn(){
        DefaultTableCellRenderer dtr = new DefaultTableCellRenderer(); 
        dtr.setHorizontalAlignment(JLabel.CENTER);
        tabel_peminjam.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        if (tabel_peminjam.getColumnModel().getColumnCount() > 0) {
        tabel_peminjam.getColumnModel().getColumn(0).setMaxWidth(70);
        tabel_peminjam.getColumnModel().getColumn(1).setMinWidth(100);
        tabel_peminjam.getColumnModel().getColumn(2).setMaxWidth(75);
        tabel_peminjam.getColumnModel().getColumn(3).setMinWidth(150);
      
        for(int i = 0; i < tabel_peminjam.getColumnCount(); i++){
            tabel_peminjam.getColumnModel().getColumn(i).setCellRenderer(dtr);
        }
        ((DefaultTableCellRenderer)tabel_peminjam.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER); 
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fJenKel = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_peminjam = new javax.swing.JTable();
        fKelas = new javax.swing.JComboBox<>();
        usia_cmb = new javax.swing.JComboBox<>();
        fJurusan = new javax.swing.JComboBox<>();
        fLaki = new javax.swing.JRadioButton();
        fPerempuan = new javax.swing.JRadioButton();
        fNone = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabel_peminjam.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabel_peminjam);

        fKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "X", "XI", "XII" }));
        fKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fKelasActionPerformed(evt);
            }
        });

        usia_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Termuda", "Tertua" }));
        usia_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usia_cmbActionPerformed(evt);
            }
        });

        fJurusan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "RPL", "TKJ", "MM", "TITL", "TOI", "TEAV" }));
        fJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fJurusanActionPerformed(evt);
            }
        });

        fLaki.setText("Laki-Laki");
        fLaki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fLakiActionPerformed(evt);
            }
        });

        fPerempuan.setText("Perempuan");
        fPerempuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fPerempuanActionPerformed(evt);
            }
        });

        fNone.setText("None");
        fNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fNoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(fKelas, 0, 81, Short.MAX_VALUE)
                        .addComponent(usia_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fJurusan, javax.swing.GroupLayout.Alignment.TRAILING, 0, 81, Short.MAX_VALUE))
                    .addComponent(fLaki)
                    .addComponent(fPerempuan)
                    .addComponent(fNone))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 25, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(fKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(usia_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fLaki)
                .addGap(18, 18, 18)
                .addComponent(fPerempuan)
                .addGap(18, 18, 18)
                .addComponent(fNone)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fKelasActionPerformed
        // TODO add your handling code here:
      filterData();
        
    }//GEN-LAST:event_fKelasActionPerformed

    private void usia_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usia_cmbActionPerformed
    filterData();
// TODO add your handling code here:
    }//GEN-LAST:event_usia_cmbActionPerformed

    private void fJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fJurusanActionPerformed
        filterData();
    }//GEN-LAST:event_fJurusanActionPerformed

    private void fLakiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fLakiActionPerformed
        filterData();
    }//GEN-LAST:event_fLakiActionPerformed

    private void fPerempuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fPerempuanActionPerformed
        filterData();
    }//GEN-LAST:event_fPerempuanActionPerformed

    private void fNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fNoneActionPerformed
        filterData();
    }//GEN-LAST:event_fNoneActionPerformed

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
            java.util.logging.Logger.getLogger(DataPeminjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataPeminjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataPeminjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataPeminjam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataPeminjam().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup fJenKel;
    private javax.swing.JComboBox<String> fJurusan;
    private javax.swing.JComboBox<String> fKelas;
    private javax.swing.JRadioButton fLaki;
    private javax.swing.JRadioButton fNone;
    private javax.swing.JRadioButton fPerempuan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabel_peminjam;
    private javax.swing.JComboBox<String> usia_cmb;
    // End of variables declaration//GEN-END:variables
}
