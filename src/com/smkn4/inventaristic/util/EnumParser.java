/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.util;

import com.smkn4.inventaristic.util.enums.Jurusan;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;


/**
 *
 * @author Farhanunnasih
 */
public class EnumParser {
    
    /**
     * Menyingkat sebuah String Jurusan
     * @param jurusan
     * @return Singkatan dari jurusan yang di masukkan dari parameter
     */
    public static String singkatJurusan(String jurusan){
        if(jurusan.equals("Multimedia")){
            return jurusan = "MM";
        }
        if (jurusan.contains("_")) {
            jurusan = jurusan.replace("_", " ");
        }
        jurusan = jurusan.replaceAll("\\s", ".") + ".";//spasi di replace titik, akhir jurusan di concate titik
        String[] akronim    = new String[4];
        int pos             = 0;
        while(!jurusan.equals("")){
            //setiap kata jadi terpisah oleh titik
            akronim[pos]    = StringUtils.substringBefore(jurusan, ".");//cut kata sebelum titik
            jurusan         = StringUtils.remove(jurusan, akronim[pos] + ".");//hapus kata yang telah di cut
            akronim[pos]    = String.valueOf(akronim[pos].charAt(0));//cut & simpan inisial dari kata barusan
            pos++;
        }
        //akronim dari kata tadi di concate ke satu string
        for(String acronym : akronim){
            if(acronym != null){
                jurusan = jurusan + acronym;
            }
        }
        return jurusan;
    }
    
    /**
     * Mem-Format suatu string dari enum menjadi lebih user-friendly
     * @param kalimat
     * @return enum yang telah di format menjadi lebih mudah dibaca
     */
    public static String format(String kalimat) {
        kalimat = kalimat.replace("_", " ");
        return WordUtils.capitalizeFully(kalimat);
    }
    
    /**
     * Mem-format sebuah enum untuk di simpan ke database
     * @param kalimat
     * @return 
     */
    public static String dbFormat(Enum str) {
        String kalimat = str.toString();
        kalimat = kalimat.replace(" ", "_");
        return kalimat.toLowerCase();
    }
}
