/*
 * Inventaristic
 */
package com.smkn4.inventaristic.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Class Static yang digunakan untuk mendapatkan koneksi ke database di MySql host
 * @author Farhanunnasih
 * @version 1.0
 */
public class  MySqlConnection {
    
    /**
     * Mengambil koneksi dari database sesuai dengan konfigurasi file config
     * @since 1.0
     * @return Connection
     */
    public static Connection getConnection() {
        Map<String, String> dbConfig = null;
        try {
            dbConfig = readConfig();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Konfigurasi Database Tidak Ditemukan\nFile config akan dibuat...", "Error", 0);
            try {
                createConfig();
                JOptionPane.showMessageDialog(null, "File Konfigurasi Database telah dibuat, harap isi terlebih dahulu", "Peringatan", 2);
                System.exit(0);
            } catch (IOException ex1) {
                ex1.printStackTrace();
                JOptionPane.showMessageDialog(null, "File Konfigurasi Database Tidak Dapat Dibuat", "Error", 0);
            }
        }
        boolean emptyDbReference = false;
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String host = dbConfig.get("DatabaseHost");
        if (host == null || host.equals("")) {
            emptyDbReference = true;
        }
        String port = dbConfig.get("DatabasePort");
        if (port == null || port.equals("")) {
            port = "3306";
        }
        String dbName = dbConfig.get("DatabaseName");
        if (dbName == null || dbName.equals("")) {
            emptyDbReference = true;
        }
        if (emptyDbReference) {
            JOptionPane.showMessageDialog(null, "Database Host dan/atau Nama Database kosong, harap isi terlebih dahulu", "Config Kosong", 0);
            System.exit(0);
        }
        String user = dbConfig.get("DatabaseUser");
        String pass = dbConfig.get("DatabasePass");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        Connection connection= null;
	try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Tidak Dapat Menyambung Ke database, periksa file konfigurasi anda dan pastikan xampp aktif", "Error", 0);
            ex.printStackTrace();
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Driver untuk menyambung ke Host tidak ditemukan", "Error", 0);
            ex.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Mencoba untuk membuat file config jika tidak ada
     * @since 1.0
     * @throws IOException 
     */
    private static void createConfig() throws IOException {
        FileWriter configFile = new FileWriter(getUserDir()+"//config"+"//config.dbs");
        configFile.write("DatabaseHost=\n");
        configFile.write("DatabasePort=\n");
        configFile.write("DatabaseName=\n");
        configFile.write("DatabaseUser=\n");
        configFile.write("DatabasePass=\n");
        //success dialog
        configFile.close();
    }
    
    /**
     * Mencoba untuk membaca isi file config
     * @return Map
     * @throws FileNotFoundException 
     */
    private static Map<String, String> readConfig() throws FileNotFoundException {
        File file = new File(getUserDir()+"//config"+"//config.dbs");
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\Z");
        Map<String, String> dbConfig = new HashMap<>();
        while (scanner.hasNextLine()) {
            String[] str = scanner.nextLine().split("=");
            try {
                dbConfig.put(str[0], str[1]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                dbConfig.put(str[0], "");
            }
        }
        return dbConfig;
    }
    private static String getUserDir() {
        return System.getProperty("user.dir");
    }
}
