package com.tech.cybercars.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    public static Bitmap CreateBitMapFromUri(Context context, Uri uri) throws FileNotFoundException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    public static File CreateFileFromUri(Context context, Uri uri) throws IOException {
        InputStream input_stream = context.getContentResolver().openInputStream(uri);
        File file = new File(uri.toString());
        file = CreateTemfile(context, file.getName());

        OutputStream output_stream = Files.newOutputStream(file.toPath());

        byte[] buffer = new byte[4 * 1024]; // 4KB buffer
        int bytesRead;

        while (true) {
            assert input_stream != null;
            if ((bytesRead = input_stream.read(buffer)) == -1) break;
            output_stream.write(buffer, 0, bytesRead);
        }

        output_stream.close();
        input_stream.close();

        return file;
    }

    public static File CreateTemfile(Context context, String filename) throws IOException {
        String suffix = "."+GetFileExtension(filename);
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(filename, suffix, storageDir);
    }

    public static String GetFileExtension(String filename){
        String[] parts = filename.split("\\.");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        } else {
            return "";
        }
    }
}
