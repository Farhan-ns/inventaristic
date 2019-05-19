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
import java.util.logging.Logger;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Salman
 */
public class RekapPeminjaman extends javax.swing.JFrame {

    private final Connection koneksi;
    
    public RekapPeminjaman() {
        initComponents();
        koneksi = MySqlConnection.getConnection();
        showData();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
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
        data.put("-1",new Object[]{dtm.getColumnName(0),dtm.getColumnName(1),dtm.getColumnName(2),dtm.getColumnName(3),dtm.getColumnName(4),dtm.getColumnName(5),dtm.getColumnName(6)});
        
//    load data cell row
        for(int i = 0;i<dtm.getRowCount();i++){
            data.put(Integer.toString(i),new Object[]{getCellValue(i,0),getCellValue(i,1),getCellValue(i,2),getCellValue(i,3),getCellValue(i,4),getCellValue(i,5),getCellValue(i,6)});
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
            FileOutputStream fos = new FileOutputStream(new File("D:/Excel/RekapPeminjaman.xls"));
            wb.write(fos);
            fos.close();
        }catch(FileNotFoundException ex){
            Logger.getLogger(RekapPeminjaman.class.getName()).log(Level.SEVERE,null,ex);
        }catch(IOException ex){
            Logger.getLogger(RekapBarang.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void showData() {
        String[] kolom = {"No", "ID Peminjaman", "Tanggal Peminjaman", "NIS", "Jumlah dipinjam","Tanggal Kembali","Status" };

        dtm = new DefaultTableModel(null, kolom);
        JTableHeader header = tbl_peminjaman.getTableHeader();
        header.setFont(new java.awt.Font("Calibri", 1, 13));
        header.setForeground(new java.awt.Color(153, 0, 154));
        dtm.getDataVector().removeAllElements();
        try {
            Statement stmt = koneksi.createStatement();
            String query = "SELECT * FROM peminjaman\n";
//                    + "WHERE tanggal_peminjaman = '" ++"'";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("" +query);
            int no = 1;
            while (rs.next()) {
                String id_peminjaman = rs.getString("id_peminjaman");
                String tanggal_peminjaman = rs.getString("tgl_peminjaman");
                String nis = rs.getString("nis");
                String jumlah_dipinjam = rs.getString("jumlah_dipinjam");
                String tgl_kembali = rs.getString("tgl_kembali");
                String status_peminjaman = rs.getString("status_peminjaman");

                dtm.addRow(new String[]{no + "", id_peminjaman, tanggal_peminjaman, nis , jumlah_dipinjam, tgl_kembali,status_peminjaman});
                no++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tbl_peminjaman.setModel(dtm);    
        tbl_peminjaman.getColumnModel().getColumn(0).setPreferredWidth(2);
        tbl_peminjaman.getColumnModel().getColumn(1).setPreferredWidth(60);
        tbl_peminjaman.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbl_peminjaman.getColumnModel().getColumn(3).setPreferredWidth(50);
        tbl_peminjaman.getColumnModel().getColumn(4).setPreferredWidth(60);
        tbl_peminjaman.getColumnModel().getColumn(5).setPreferredWidth(100);
//        count.setText("" + tbl_rekap.getRowCount());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_peminjaman = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        jLabel1.setText("Laporan Data Peminjaman");

        tbl_peminjaman.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_peminjaman.getTableHeader().setResizingAllowed(false);
        tbl_peminjaman.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl_peminjaman);

        jButton1.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(205, 205, 205))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String Source = null;
        String Report = null;
        
        try {
            Source = System.getProperty("user.dir") + "/lib/rekap/RekapPinjam.jrxml";
            Report = System.getProperty("user.dir") + "/lib/rekap/RekapPinjam.jasper";
            
            JasperReport jasperReport = JasperCompileManager.compileReport(Source);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, koneksi);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, Report);
            JasperViewer.viewReport(jasperPrint, false);
        } catch(Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(RekapPeminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RekapPeminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RekapPeminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RekapPeminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RekapPeminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tbl_peminjaman;
    // End of variables declaration//GEN-END:variables
}
