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
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author andrew
 */
public class pinjam_kembali extends javax.swing.JFrame {

    private final Connection koneksi;
    /**
     * Creates new form pinjam_kembali
     */
    public pinjam_kembali() {
        initComponents();
        koneksi = MySqlConnection.getConnection();
        showData(Filter(0));
    }
    
    DefaultTableModel dtm;
    
    public void showData(String qryFilter) {
        String[] kolom = {"No", "Tanggal Peminjaman", "Nama Peminjaman", "Kelas", "Nama Barang", "Jenis Barang", "Kondisi Barang", "Lokasi Penyimpangan", "Jumlah Pinjam", "Tanggal Kembali", "Status Kembali"};
        
        dtm = new DefaultTableModel(null, kolom);
        
        try{
            Statement stmt = koneksi.createStatement();
            String query = "SELECT peminjaman.`tgl_peminjaman`, siswa.`nama_siswa`, siswa.`kelas`, barang_masuk.`nama_barang`, barang_masuk.`jenis_barang`,"
                    + "barang_masuk.`kondisi`, barang_masuk.`lokasi`, peminjaman.`jumlah_dipinjam`, peminjaman.`tgl_kembali`, peminjaman.`status_peminjaman`"
                    + "FROM peminjaman, siswa, barang_masuk, rincian"
                    + "WHERE peminjaman.`nis` = siswa.`nis`"
                    + "AND rincian.`id_peminjaman` = peminjaman.`id_peminjaman`"
                    + "AND rincian.`id_barang` = barang_masuk.`id_barang`" + qryFilter;
            
            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while(rs.next()) {
                String tgl_peminjaman     = rs.getString("tgl_peminjaman");
                String nama_siswa         = rs.getString("nama_siswa");
                String kelas              = rs.getString("kelas");
                String nama_barang        = rs.getString("nama_barang");
                String jenis_barang       = rs.getString("jenis_barang");
                String kondisi            = rs.getString("kondisi");
                String lokasi             = rs.getString("lokasi");
                String jumlah_dipinjam    = rs.getString("jumlah_dipinjam");
                String tgl_kembali        = rs.getString("tgl_kembali");
                String status_peminjaman  = rs.getString("status_peminjaman");
             
                dtm.addRow(new String[] {no + "", tgl_peminjaman, nama_siswa, kelas, nama_barang, jenis_barang, kondisi, lokasi, jumlah_dipinjam, tgl_kembali, status_peminjaman});
                no++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_peminjaman.setModel(dtm);
        tbl_peminjaman.getColumnModel().getColumn(0).setPreferredWidth(5);
        tbl_peminjaman.getColumnModel().getColumn(1).setPreferredWidth(20);
        tbl_peminjaman.getColumnModel().getColumn(2).setPreferredWidth(30);
        tbl_peminjaman.getColumnModel().getColumn(3).setPreferredWidth(15);
        tbl_peminjaman.getColumnModel().getColumn(4).setPreferredWidth(20);
        tbl_peminjaman.getColumnModel().getColumn(5).setPreferredWidth(30);
        tbl_peminjaman.getColumnModel().getColumn(6).setPreferredWidth(50);
        tbl_peminjaman.getColumnModel().getColumn(7).setPreferredWidth(50);
        tbl_peminjaman.getColumnModel().getColumn(8).setPreferredWidth(50);
        tbl_peminjaman.getColumnModel().getColumn(8).setPreferredWidth(50);
    
        int j = tbl_peminjaman.getRowCount();
        lbl_jumlah.setText("Jumlah Peminjaman : "+j);
    }
    
    public String Filter(int i) {
        String qryFilter = null;
        switch(i) {
            case 1:
                qryFilter = "AND barang_masuk.jenis_barang = '" + cb_jenis.getSelectedItem().toString() + "';";
                break;
            case 2:
                qryFilter = "AND peminjaman.status_peminjaman = '" + cb_status.getSelectedItem().toString() + "';";
                break;
            default:
                qryFilter = "ORDER BY tgl_pinjam ASC;";
        }
        return qryFilter;
    }
    
    public void filterData() {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(dtm);
        tbl_peminjaman.setRowSorter(tr);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        tr.setSortKeys(sortKeys);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cb_jenis = new javax.swing.JComboBox<>();
        cb_status = new javax.swing.JComboBox<>();
        btn_batal = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_peminjaman = new javax.swing.JTable();
        lbl_jumlah = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "F I L T E R", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Jenis Barang");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Status Peminjaman");

        cb_jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Jenis Barang", "Habis Pakai", "Asset" }));
        cb_jenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_jenisActionPerformed(evt);
            }
        });

        cb_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Status Peminjaman", "Dipinjam", "Kembali" }));
        cb_status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_statusActionPerformed(evt);
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(cb_jenis, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jButton1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 10, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_batal)
                .addGap(75, 75, 75)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel5.setText("Rekap Peminjaman");

        tbl_peminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal Pinjam", "Nama Peminjam", "Kelas", "Nama Barang", "Jenis Barang", "Kondisi Barang", "Lokasi Penyimpanan", "Jumlah Pinjam", "Tanggal Kembali", "Status Peminjaman"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_peminjaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_peminjamanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_peminjaman);
        if (tbl_peminjaman.getColumnModel().getColumnCount() > 0) {
            tbl_peminjaman.getColumnModel().getColumn(0).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(1).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(2).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(3).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(4).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(5).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(6).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(7).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(8).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(9).setResizable(false);
            tbl_peminjaman.getColumnModel().getColumn(10).setResizable(false);
        }

        lbl_jumlah.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        lbl_jumlah.setText("Jumlah Peminjaman   : ");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_refresh)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(530, 530, 530)
                        .addComponent(jLabel5)
                        .addGap(522, 522, 522))
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_refresh))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cb_jenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_jenisActionPerformed
        // TODO add your handling code here:
        showData(Filter(1));
    }//GEN-LAST:event_cb_jenisActionPerformed

    private void cb_statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_statusActionPerformed
        // TODO add your handling code here:
        showData(Filter(2));
    }//GEN-LAST:event_cb_statusActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
        btnGrup_waktu.clearSelection();
        cb_jenis.setSelectedIndex(0);
        cb_status.setSelectedIndex(0);
    }//GEN-LAST:event_btn_batalActionPerformed
int baris;
    private void tbl_peminjamanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_peminjamanMouseClicked
        // TODO add your handling code here:
        baris = tbl_peminjaman.getSelectedRow();
    }//GEN-LAST:event_tbl_peminjamanMouseClicked

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        // TODO add your handling code here:
        showData(Filter(0));
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
            java.util.logging.Logger.getLogger(pinjam_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pinjam_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pinjam_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pinjam_kembali.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pinjam_kembali().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrup_waktu;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JComboBox<String> cb_jenis;
    private javax.swing.JComboBox<String> cb_status;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_jumlah;
    private javax.swing.JTable tbl_peminjaman;
    // End of variables declaration//GEN-END:variables
}
