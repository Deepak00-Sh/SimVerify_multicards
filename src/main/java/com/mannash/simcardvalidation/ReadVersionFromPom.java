package com.mannash.simcardvalidation;

import java.io.FileReader;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ReadVersionFromPom {
    public static void main(String[] args) {
        try {
            JarFile jarFile = new JarFile("D:\\work\\SIMCardValidationProject\\SIMVerify\\lib\\simverify_multicards-1.0-SNAPSHOT.jar");
            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            String version = attributes.getValue("Manifest-Version");
            System.out.println("Version: " + version);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
