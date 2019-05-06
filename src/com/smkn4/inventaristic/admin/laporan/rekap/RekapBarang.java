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
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Salman
 */
public class RekapBarang extends javax.swing.JFrame {

    private final Connection koneksi;
    
    public RekapBarang() {
        initComponents();
        koneksi = MySqlConnection.getConnection();
        showData();
    }
    
    DefaultTableModel dtm;
    
      private String getCellValue(int x,int y){
        return dtm.getValueAt(x,y).toString();
    }
    
//    export data
    private void exportToExcel(){
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet ws = wb.createSheet();
        
//        header

        MessageFormat title = new MessageFormat("Laporan Data Barang Bermasalah");
        
//        load data
        TreeMap<String,Object[]> data = new TreeMap<>();
        data.put("-1",new Object[]{dtm.getColumnName(0),dtm.getColumnName(1),dtm.getColumnName(2),dtm.getColumnName(3),dtm.getColumnName(4),dtm.getColumnName(5),dtm.getColumnName(6),dtm.getColumnName(7),dtm.getColumnName(8)});
        
//    load data cell row
        for(int i = 0;i<dtm.getRowCount();i++){
            data.put(Integer.toString(i),new Object[]{getCellValue(i,0),getCellValue(i,1),getCellValue(i,2),getCellValue(i,3),getCellValue(i,4),getCellValue(i,5),getCellValue(i,6),getCellValue(i,7),getCellValue(i,8)});
        }
//     Write to excel
        Set<String> ids = data.keySet();
        XSSFRow row;
        int rowID = 0;
        
        for(String key : ids){
            row=ws.createRow(rowID++);
            
            Object[] values=data.get(key);
            int cellID = 0;
            for(Object o: values){
                Cell cell = row.createCell(cellID++);
                cell.setCellValue(o.toString());
            }
            
        }
        
//        Save File
        try{
            FileOutputStream fos = new FileOutputStream(new File("D:/Excel/RekapBarang.xls"));
            wb.write(fos);
            fos.close();
        }catch(FileNotFoundException ex){
            Logger.getLogger(RekapBarang.class.getName()).log(Level.SEVERE,null,ex);
        }catch(IOException ex){
            Logger.getLogger(RekapBarangBermasalah.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    
    
    
    public void showData() {
        String[] kolom = {"No", "ID Barang", "Nama Barang", "Jenis", "Tanggal Masuk", "Jumlah", "Status", "Lokasi", "Waktu Pakai"};

        dtm = new DefaultTableModel(null, kolom);
        JTableHeader header = tbl_rekap.getTableHeader();
        header.setFont(new java.awt.Font("Calibri", 1, 13));
        header.setForeground(new java.awt.Color(153, 0, 154));
        dtm.getDataVector().removeAllElements();
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT *, COUNT(nama_barang) FROM barang_masuk GROUP BY nama_barang";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("" +query);
            int no = 1;
            while (rs.next()) {
                String id_barang = rs.getString("id_barang");
                String nama_barang = rs.getString("nama_barang");
                String jenis_barang = rs.getString("jenis_barang");
                String tanggal_masuk = rs.getString("tgl_masuk");
                String jumlah= rs.getString("COUNT(nama_barang)");
                String status = rs.getString("kondisi");
                String lokasi = rs.getString("lokasi");
                String total_pakai = rs.getString("total_penggunaan");

                dtm.addRow(new String[]{no + "", id_barang, nama_barang, jenis_barang, tanggal_masuk, jumlah, status, lokasi, total_pakai});
                no++;
            }            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_rekap.setModel(dtm);    
        tbl_rekap.getColumnModel().getColumn(0).setPreferredWidth(15);
        tbl_rekap.getColumnModel().getColumn(1).setPreferredWidth(55);
        tbl_rekap.getColumnModel().getColumn(3).setPreferredWidth(40);
        tbl_rekap.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbl_rekap.getColumnModel().getColumn(5).setPreferredWidth(30);
        tbl_rekap.getColumnModel().getColumn(6).setPreferredWidth(35);
        tbl_rekap.getColumnModel().getColumn(7).setPreferredWidth(30);
//        tbl_rekap.getColumnModel().getColumn(8).setPreferredWidth(40);
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

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        jLabel1.setText("Laporan Data Barang");

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
        tbl_rekap.getTableHeader().setResizingAllowed(false);
        tbl_rekap.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl_rekap);

        Print.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        Print.setText("Print");
        Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
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
            .addGroup(layout.createSequentialGroup()
                .addGap(432, 432, 432)
                .addComponent(Print, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(jLabel1)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Print)
                    .addComponent(jButton2))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintActionPerformed
        
        MessageFormat title = new MessageFormat("Laporan Data Barang");
        MessageFormat footer = new MessageFormat("SMKN 4 Bandung - Inventaris App");
        
        try {
            tbl_rekap.print(JTable.PrintMode.NORMAL, title, footer);
        } catch(PrinterException ex) {
            System.err.print("Error Printer");
        }
        
    }//GEN-LAST:event_PrintActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        exportToExcel();
    }//GEN-LAST:event_jButton2ActionPerformed

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
