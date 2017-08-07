package com.pimo.thea.vaccinesschedulemvp.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by thea on 7/23/2017.
 */

public class FileHelper {
    public static void copyFile(String inputPath, String outputPath) {
        try {
            File inputFile = new File(inputPath);
            File outputFile = new File(outputPath);
            InputStream in = new FileInputStream(inputFile);
            OutputStream out = new FileOutputStream(outputFile);

            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("FileHelper", "Error File not found ... :(", e);
        } catch (IOException e) {
            Log.e("FileHelper", "Error IO File ... :(", e);
        }
    }
}
