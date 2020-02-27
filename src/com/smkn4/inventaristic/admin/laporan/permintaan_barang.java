/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.admin.laporan;

import com.smkn4.inventaristic.admin.laporan.rekap.RekapBarang;
import com.smkn4.inventaristic.admin.laporan.rekap.RekapPeminjaman;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.smkn4.inventaristic.util.MySqlConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author andrew
 */
public class permintaan_barang extends javax.swing.JFrame {

    private final Connection koneksi;

    /**
     * Creates new form permintaan_barang
     */
    public permintaan_barang() {
        initComponents();
        koneksi = MySqlConnection.getConnection();
        showData(Filter(0));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    DefaultTableModel dtm;

    public void showData(String qryFilter) {
        String[] kolom = {"No", "Nama Barang", "Jenis Barang", "Tanggal Permintaan", "Jumlah Permintaan", "Deskripsi"};

        dtm = new DefaultTableModel(null, kolom);

        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT nama_barang, jenis_barang, tgl_permintaan, jumlah_permintaan, deskripsi "
                    + "FROM pengajuan_barang" + qryFilter;

            ResultSet rs = stmt.executeQuery(query);
            int no = 1;
            while (rs.next()) {
                String nama_barang = rs.getString("nama_barang");
                String jenis_barang = rs.getString("jenis_barang");
                String tgl_permintaan = rs.getString("tgl_permintaan");
                String jumlah_permintaan = rs.getString("jumlah_permintaan");
                String deskripsi = rs.getString("deskripsi");

                dtm.addRow(new String[]{no + "", nama_barang, jenis_barang, tgl_permintaan, jumlah_permintaan, deskripsi});
                no++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_permintaan.setModel(dtm);
        tbl_permintaan.getColumnModel().getColumn(0).setPreferredWidth(5);
        tbl_permintaan.getColumnModel().getColumn(1).setPreferredWidth(20);
        tbl_permintaan.getColumnModel().getColumn(2).setPreferredWidth(30);
        tbl_permintaan.getColumnModel().getColumn(3).setPreferredWidth(15);
        tbl_permintaan.getColumnModel().getColumn(4).setPreferredWidth(20);

        int j = tbl_permintaan.getRowCount();
        lbl_jumlah.setText("Jumlah Permintaan Barang : " + j);
    }

    public String Filter(int i) {
        String qryFilter = null;
        switch (i) {
            case 1:
                qryFilter = " WHERE pengajuan_barang.jenis_barang = '" + cb_jenis.getSelectedItem().toString().toLowerCase() + "'";
                break;
            default:
                qryFilter = " ORDER BY nama_barang ASC";
        }
        return qryFilter;
    }

    private String getCellValue(int x, int y) {
        return dtm.getValueAt(x, y).toString();
    }

    private void exportToExcel() {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();

//       header
        MessageFormat title = new MessageFormat("Laporan Data Barang Bermasalah");

//       load data
        TreeMap<String, Object[]> data = new TreeMap<>();
        data.put("-1", new Object[]{dtm.getColumnName(0), dtm.getColumnName(1), dtm.getColumnName(2), dtm.getColumnName(3), dtm.getColumnName(4), dtm.getColumnName(5), dtm.getColumnName(6)});

//    load data cell row
        for (int i = 0; i < dtm.getRowCount(); i++) {
            data.put(Integer.toString(i), new Object[]{getCellValue(i, 0), getCellValue(i, 1), getCellValue(i, 2), getCellValue(i, 3), getCellValue(i, 4), getCellValue(i, 5), getCellValue(i, 6)});
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

        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.LIME.getIndex());
        cs.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        Font f = wb.createFont();
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        f.setFontHeightInPoints((short) 12);
        cs.setFont(f);

        int idx = 0;
        Cell c = null;
        int idy = 0;
//        Save File
        try {
            FileOutputStream fos = new FileOutputStream(new File("rekap//rekap_permintaan_barang.xls"));
            wb.write(fos);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RekapPeminjaman.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RekapBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filterData() {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(dtm);
        tbl_permintaan.setRowSorter(tr);
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
        cb_jenis = new javax.swing.JComboBox<>();
        btn_batal = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_permintaan = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        lbl_jumlah = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 184, 148));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "F I L T E R", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Jenis Barang");

        cb_jenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Jenis Barang", "Habis Pakai", "Aset" }));
        cb_jenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_jenisActionPerformed(evt);
            }
        });

        btn_batal.setText("Reset");
        btn_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_batalActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("Export To Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
                        .addGap(36, 36, 36)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cb_jenis, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(btn_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_jenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(btn_batal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        tbl_permintaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama Barang", "Jenis Barang", "Tanggal Permintaan", "Jumlah Permintaan", "Deskripsi Barang"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_permintaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_permintaanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_permintaan);
        if (tbl_permintaan.getColumnModel().getColumnCount() > 0) {
            tbl_permintaan.getColumnModel().getColumn(0).setResizable(false);
            tbl_permintaan.getColumnModel().getColumn(1).setResizable(false);
            tbl_permintaan.getColumnModel().getColumn(2).setResizable(false);
            tbl_permintaan.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel5.setText("Rekap Permintaan Barang");

        lbl_jumlah.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        lbl_jumlah.setText("Jumlah Permintaan Barang  : ");

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lbl_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_refresh)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(340, 340, 340))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_refresh))
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cb_jenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_jenisActionPerformed
        // TODO add your handling code here:
        showData(Filter(1));
    }//GEN-LAST:event_cb_jenisActionPerformed

    private void btn_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_batalActionPerformed
        // TODO add your handling code here:
        btnGrup_waktu.clearSelection();
        cb_jenis.setSelectedIndex(0);
    }//GEN-LAST:event_btn_batalActionPerformed
    int baris;
    private void tbl_permintaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_permintaanMouseClicked
        // TODO add your handling code here:
        baris = tbl_permintaan.getSelectedRow();
    }//GEN-LAST:event_tbl_permintaanMouseClicked

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
        // TODO add your handling code here:
        showData(Filter(0));
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        exportToExcel();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(permintaan_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(permintaan_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(permintaan_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(permintaan_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new permintaan_barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGrup_waktu;
    private javax.swing.JButton btn_batal;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JComboBox<String> cb_jenis;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_jumlah;
    private javax.swing.JTable tbl_permintaan;
    // End of variables declaration//GEN-END:variables
}
