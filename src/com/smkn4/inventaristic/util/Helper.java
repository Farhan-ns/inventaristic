/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smkn4.inventaristic.util;

import com.smkn4.inventaristic.Driver;
import java.io.File;
import java.net.URL;

/**
 *
 * @author acer
 */
public class Helper {
    public static String getDriverRunningDirectory() {
        URL jarLocationUrl = Driver.class.getProtectionDomain().getCodeSource().getLocation();
        String jarLocation = new File(jarLocationUrl.toString()).getParent();
        return jarLocation.substring(6).split("build")[0];//remove "the file:\" prefix
    }
}
