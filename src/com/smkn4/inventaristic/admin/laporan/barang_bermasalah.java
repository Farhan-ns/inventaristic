/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.laporan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.util.ArrayList;
import com.smkn4.inventaristic.util.enums.JenisBarang;
import com.smkn4.inventaristic.util.enums.JenisMasalah;
/**
 *
 * @author andrew
 */
public class barang_bermasalah extends javax.swing.JFrame {

    private final Connection koneksi;
    
    /**
     * Creates new form barang_bermasalah
     */
    public barang_bermasalah() {
        initComponents();
        koneksi = MySqlConnection.getConnection();
        showData(false, null, null, "tgl_bermasalah", "ASC");

        
        btnGrup_waktu.add(rb_harian);
        btnGrup_waktu.add(rb_bulanan);
    }
    
    DefaultTableModel dtm;
    
    public void showData(Boolean search, ArrayList<String> filter, ArrayList<String> value, String order_by, String asc_desc) {
        String[] kolom = {"No", "Tanggal Bermasalah", "Nama Barang", "Tahun Barang", "Jenis Barang", "Sumber Perolehan Barang", "Jenis Masalah", "Deskripsi Masalah"};
        
        dtm = new DefaultTableModel(null, kolom);
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT barang_bermasalah.tgl_bermasalah, barang_masuk.nama_barang, barang_masuk.thn_barang, "
                         + "barang_masuk.jenis_barang, barang_masuk.sumber_perolehan, barang_bermasalah.jenis_masalah, barang_bermasalah.deskripsi "
                         + "FROM barang_bermasalah, barang_masuk "
                         + "WHERE barang_bermasalah.id_barang = barang_masuk.id_barang";
            
            if (search) {
                query += " WHERE ";
                    for (int i = 0; i < filter.size(); i++) {
                        if (i != 0) {
                            query += " AND ";
                        }
                        query += filter.get(i) + " LIKE '%" + value.get(i) + "%'";
                    }
            }
            
            query += " ORDER BY " + order_by + " " + asc_desc;
            System.out.println(query);
            
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String tgl_bermasalah       = rs.getString("tgl_bermasalah");
                String nama_barang          = rs.getString("nama_barang");
                String thn_barang           = rs.getString("thn_barang");
                String jenis_barang         = rs.getString("jenis_barang");
                String sumber_perolehan     = rs.getString("sumber_perolehan");
                String jenis_masalah        = rs.getString("jenis_masalah");
                String deskripsi            = rs.getString("deskripsi");
                
                dtm.addRow(new String[] {no + "", tgl_bermasalah, nama_barang, thn_barang, jenis_barang, sumber_perolehan, jenis_masalah, deskripsi});
                no++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_bermasalah.setModel(dtm);
        tbl_bermasalah.getColumnModel().getColumn(0).setPreferredWidth(5);
        tbl_bermasalah.getColumnModel().getColumn(1).setPreferredWidth(20);
        tbl_bermasalah.getColumnModel().getColumn(2).setPreferredWidth(30);
        tbl_bermasalah.getColumnModel().getColumn(3).setPreferredWidth(15);
        tbl_bermasalah.getColumnModel().getColumn(4).setPreferredWidth(20);
        tbl_bermasalah.getColumnModel().getColumn(5).setPreferredWidth(30);
        tbl_bermasalah.getColumnModel().getColumn(6).setPreferredWidth(50);
        tbl_bermasalah.getColumnModel().getColumn(7).setPreferredWidth(50);
        
        int j = tbl_bermasalah.getRowCount();
        lbl_jumlah.setText("Jumlah Barang Bermasalah : "+j);
    }
    

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrup_waktu = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rb_harian = new javax.swing.JRadioButton();
        rb_bulanan = new javax.swing.JRadioButton();
        cb_jenis = new javax.swing.JComboBox<>();
        cb_permasalahan = new javax.swing.JComboBox<>();
        btn_terapkan = new javax.swing.JButton();
        btn_batal = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_bermasalah = new javax.swing.JTable();
        lbl_jumlah = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "F I L T E R", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Periode Waktu");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Jenis Barang");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Jenis Permasalahan Barang");

        rb_harian.setBackground(new java.awt.Color(102, 102, 102));
        btnGrup_waktu.add(rb_harian);
        rb_harian.setForeground(new java.awt.Color(255, 255, 255));
        rb_harian.setText("Harian");

        rb_bulanan.setBackground(new java.awt.Color(102, 102, 102));
        btnGrup_waktu.add(rb_bulanan);
        rb_bulanan.setForeground(new java.awt.Color(255, 255, 255));
        rb_bulanan.setText("Bulanan");

        cb_jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Jenis Masalah", "Rusak", "Hilang" }));
        cb_jenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_jenisActionPerformed(evt);
            }
        });

        cb_permasalahan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Jenis Barang", "Asset", "Habis Pakai" }));
        cb_permasalahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_permasalahanActionPerformed(evt);
            }
        });

        btn_terapkan.setText("Terapkan");
        btn_terapkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_terapkanActionPerformed(evt);
            }
        });

        btn_batal.setText("Batal");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        jButton1.setText("PRINT LAPORAN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btn_terapkan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rb_harian)
                                .addGap(29, 29, 29)
                                .addComponent(rb_bulanan))
                            .addComponent(cb_jenis, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cb_permasalahan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jButton1)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_harian)
                    .addComponent(rb_bulanan))
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_permasalahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_terapkan)
                    .addComponent(btn_batal))
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Rekap Barang Bermasalah");

        tbl_bermasalah.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Tanggal Bermasalah", "Nama Barang", "Tahun Barang", "Jenis Barang", "Sumber Perolehan Barang", "Permasalahan Barang", "Deskripsi Permasalahan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_bermasalah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bermasalahMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_bermasalah);

        lbl_jumlah.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        lbl_jumlah.setText("Jumlah Barang Bermasalah : ");

        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_refresh)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(402, 402, 402)
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_jumlah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_refresh))
                .addGap(20, 20, 20))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cb_jenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_jenisActionPerformed
        // TODO add your handling code here:
        if (cb_jenis.getSelectedItem() != null) {
        }
    }//GEN-LAST:event_cb_jenisActionPerformed

    private void cb_permasalahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_permasalahanActionPerformed
        // TODO add your handling code here:
        if (cb_permasalahan.getSelectedItem() != null) {
        }
    }//GEN-LAST:event_cb_permasalahanActionPerformed

    private void btn_terapkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_terapkanActionPerformed
        // TODO add your handling code here:
        ArrayList<String> filter = new ArrayList();
        ArrayList<String> value = new ArrayList();
        if (!cb_permasalahan.getSelectedItem().toString().equals("Semua Jenis Masalah")) {
            filter.add("jenis_masalah ");
            value.add(cb_permasalahan.getSelectedItem().toString());
            showData(true,filter,value,"nama_barang","ASC");
        }

        if (!cb_jenis.getSelectedItem().toString().equals("Semua Jenis Barang")) {
            filter.add("jenis_barang ");
            value.add(cb_jenis.getSelectedItem().toString());
            showData(true,filter,value,"nama_barang","ASC");
        }

        if (rb_harian.isSelected()) {
            filter.add("tgl_bermasalah ");
            value.add("Harian");
            showData(true,filter,value,"tgl_bermasalah","ASC");
        } else if (rb_bulanan.isSelected()) {
            showData(true,filter,value,"tgl_bermasalah","DESC");
        }
    }//GEN-LAST:event_btn_terapkanActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
        btnGrup_waktu.clearSelection();
        cb_jenis.setSelectedIndex(0);
        cb_permasalahan.setSelectedIndex(0);
    }//GEN-LAST:event_btn_batalActionPerformed
int baris;
    private void tbl_bermasalahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bermasalahMouseClicked
        // TODO add your handling code here:
        baris = tbl_bermasalah.getSelectedRow();
    }//GEN-LAST:event_tbl_bermasalahMouseClicked

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        // TODO add your handling code here:
        showData(false, null, null, "tgl_bermasalah", "ASC");
    }//GEN-LAST:event_btn_refreshActionPerformed

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
            java.util.logging.Logger.getLogger(barang_bermasalah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(barang_bermasalah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(barang_bermasalah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(barang_bermasalah.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new barang_bermasalah().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrup_waktu;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_terapkan;
    private javax.swing.JComboBox<String> cb_jenis;
    private javax.swing.JComboBox<String> cb_permasalahan;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_jumlah;
    private javax.swing.JRadioButton rb_bulanan;
    private javax.swing.JRadioButton rb_harian;
    private javax.swing.JTable tbl_bermasalah;
    // End of variables declaration//GEN-END:variables
}
