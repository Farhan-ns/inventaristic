/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.siswa;

/**
 *
 * @author Tan
 * @author Rizki
 */
import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.smkn4.inventaristic.util.MySqlConnection;
;

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
    
    
    private void readData(){
        String[] kolomTabel = {"NIS", "Nama", "Kelas","Tanggal Lahir", "Jenis Kelamin"};
        defaultTableModel   = new DefaultTableModel(null, kolomTabel);
        try {
            connection      = MySqlConnection.getConnection();
            preStatement    = connection.prepareStatement("SELECT * FROM siswa,kelas");
            result          = preStatement.executeQuery();
            while(result.next()){
                String nis              = result.getString("nis");
                String nama             = result.getString("nama_siswa");
                String nama_kelas        =    result.getString("nama_kelas");
                String jurusan        =    result.getString("jurusan");
                String kelas_ke        =    result.getString("kelas_ke");
                String kelas        =    nama_kelas+" "+jurusan+" "+kelas_ke;;
                String tanggal_lahir      = result.getDate("tanggal_lahir").toString();
                String jenis_kelamin   = result.getString("jenis_kelamin");
                defaultTableModel.addRow(new String[]{nis,nama,kelas,tanggal_lahir,jenis_kelamin});
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
    
     private void filterData(){
         
        String kelass = null;
        
        if (fKelas.getSelectedItem() == "X"){
            kelass = "X";
        }else if (fKelas.getSelectedItem() == "XI"){
            kelass = "XI";
        }else if (fKelas.getSelectedItem() == "XII"){
            kelass = "XII";
        }else if (fKelas.getSelectedItem() == "Default"){
            kelass = "%%";
        }
        
        String[] kolomTabel = {"NIS", "Nama", "Kelas","Tanggal Lahir", "Jenis Kelamin"};
        defaultTableModel   = new DefaultTableModel(null, kolomTabel);
        try {
            connection      = MySqlConnection.getConnection();
            String jen1="";
            if(jenkel_cmb.getSelectedItem()==" "){
                jen1=" ";
            }else if(jenkel_cmb.getSelectedItem()=="Perempuan"){
                jen1=" && jenis_kelamin LIKE '%P%'";
            }else if(jenkel_cmb.getSelectedItem()=="Laki-Laki"){
                jen1=" && jenis_kelamin LIKE '%L%'";
            }else if(jenkel_cmb.getSelectedItem()=="Default"){
                jen1=" && jenis_kelamin LIKE '%%'";
            }
            String us1="";
            if(usia_cmb.getSelectedItem()=="Default"){
                 us1="";
            }else if(usia_cmb.getSelectedItem()=="Termuda"){
                 us1=" ORDER BY tanggal_lahir DESC ";   
            }else if(usia_cmb.getSelectedItem()=="Tertua"){
                 us1=" ORDER BY tanggal_lahir ASC ";   
            }
            preStatement    = connection.prepareStatement("SELECT * FROM siswa,kelas WHERE nama_kelas LIKE '"+kelass+"'" + jen1+us1);
            result          = preStatement.executeQuery();
            while(result.next()){
                String nis              = result.getString("nis");
               String nama             = result.getString("nama_siswa");
                String nama_kelas        =    result.getString("id_kelas");
                String jurusan        =    result.getString("jurusan");
                String kelas_ke        =    result.getString("kelas_ke");
                String kelas        =    nama_kelas+" "+jurusan+" "+kelas_ke;
                String tanggal_lahir      = result.getDate("tanggal_lahir").toString();
                String jenis_kelamin   = result.getString("jenis_kelamin");
                defaultTableModel.addRow(new String[]{nis,nama,kelas,tanggal_lahir,jenis_kelamin});
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_peminjam = new javax.swing.JTable();
        fKelas = new javax.swing.JComboBox<>();
        jenkel_cmb = new javax.swing.JComboBox<>();
        usia_cmb = new javax.swing.JComboBox<>();

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

        jenkel_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Laki-Laki", "Perempuan" }));
        jenkel_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jenkel_cmbActionPerformed(evt);
            }
        });

        usia_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Default", "Termuda", "Tertua" }));
        usia_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usia_cmbActionPerformed(evt);
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fKelas, 0, 81, Short.MAX_VALUE)
                    .addComponent(jenkel_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usia_cmb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(26, 26, 26)
                .addComponent(jenkel_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(usia_cmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jenkel_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jenkel_cmbActionPerformed
        // TODO add your handling code here:
        filterData();
    }//GEN-LAST:event_jenkel_cmbActionPerformed

    private void usia_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usia_cmbActionPerformed
    filterData();
// TODO add your handling code here:
    }//GEN-LAST:event_usia_cmbActionPerformed

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
    private javax.swing.JComboBox<String> fKelas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> jenkel_cmb;
    private javax.swing.JTable tabel_peminjam;
    private javax.swing.JComboBox<String> usia_cmb;
    // End of variables declaration//GEN-END:variables
}
