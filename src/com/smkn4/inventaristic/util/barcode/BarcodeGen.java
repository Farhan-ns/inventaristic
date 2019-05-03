/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.util.barcode;

import com.smkn4.inventaristic.util.StrRandGenerator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;



/**
 *
 * @author Farhanunnasih
 */
public class BarcodeGen {
    private static final int dpi = 150;
    
    public static void generate() {
        Code128Bean bean = new Code128Bean();
        bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        bean.setBarHeight(15);
        bean.doQuietZone(true);
        bean.setVerticalQuietZone(0.15);
        bean.setQuietZone(1);
        
        File outputFile = new File(System.getProperty("user.dir")+"\\barcode"+"\\" + StrRandGenerator.generate() +".png");
        try {
            OutputStream out = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            bean.generateBarcode(canvas, StrRandGenerator.generate());
            canvas.finish();
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean generate(String text) {
        Code128Bean bean = new Code128Bean();
        bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        bean.setBarHeight(15);
        bean.doQuietZone(true);
        bean.setVerticalQuietZone(0.15);
        bean.setQuietZone(1);
        boolean success = false;
        
        File outputFile = new File(System.getProperty("user.dir")+"\\barcode"+"\\" + text +".png");
        try {
            OutputStream out = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            bean.generateBarcode(canvas, "SMKN4BDG-" + text);
            canvas.finish();
            out.close();
            success = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return success;
    }
    
    public static void generate(String filePath, String file) {
        Code128Bean bean = new Code128Bean();
        bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        bean.setBarHeight(15);
        bean.doQuietZone(true);
        bean.setVerticalQuietZone(0.15);
        bean.setQuietZone(1);
        
        File outputFile = new File(filePath + file);
        try {
            OutputStream out = new FileOutputStream(outputFile);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            bean.generateBarcode(canvas, "SMKN4BDG-123141");
            canvas.finish();
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
