/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.util;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Farhanunnasih
 */
public class Barang extends RecursiveTreeObject<Barang> {
        SimpleStringProperty idBarang;
        public SimpleStringProperty namaBarang;
        public SimpleStringProperty jenisBarang;
        public SimpleStringProperty tanggalMasuk;
        public SimpleStringProperty kondisi;
        public SimpleStringProperty lokasi;
        public SimpleStringProperty totalPenggunaan;
        public SimpleStringProperty dapatDipinjam;
//        SimpleStringProperty deskripsi;
//        SimpleStringProperty petugas;

        public Barang(String idBarang, String namaBarang, String jenisBarang, String tanggalMasuk, String kondisi, String lokasi, String totalPenggunaan,String dapatDipinjam) {
            this.idBarang = new SimpleStringProperty(idBarang);
            this.namaBarang = new SimpleStringProperty(namaBarang);
            this.jenisBarang = new SimpleStringProperty(jenisBarang);
            this.tanggalMasuk = new SimpleStringProperty(tanggalMasuk);
            this.dapatDipinjam = new SimpleStringProperty(dapatDipinjam);
            this.kondisi = new SimpleStringProperty(kondisi);
            this.lokasi = new SimpleStringProperty(lokasi);
            this.totalPenggunaan = new SimpleStringProperty(totalPenggunaan);
//            this.deskripsi = new SimpleStringProperty(deskripsi);
//            this.petugas = new SimpleStringProperty(petugas);
        }
        public Barang(String idBarang, String namaBarang, String lokasi, String jenisMasalah, String tglMasalah, String totalPenggunaan) {
            this.idBarang = new SimpleStringProperty(idBarang);
            this.namaBarang = new SimpleStringProperty(namaBarang);
            this.kondisi = new SimpleStringProperty(jenisMasalah);
            this.tanggalMasuk = new SimpleStringProperty(tglMasalah);
            this.totalPenggunaan = new SimpleStringProperty(totalPenggunaan);
        }

        public String getIdBarang() {
            return idBarang.get();
        }

        public String getNamaBarang() {
            return namaBarang.get();
        }

        public String getJenisBarang() {
            return jenisBarang.get();
        }

        public String getTanggalMasuk() {
            return tanggalMasuk.get();
        }

        public String getKondisi() {
            return kondisi.get();
        }

        public String getLokasi() {
            return lokasi.get();
        }

        public String getTotalPenggunaan() {
            return totalPenggunaan.get();
        }

        public String getDapatDipinjam() {
            return dapatDipinjam.get();
        }
        
    }

