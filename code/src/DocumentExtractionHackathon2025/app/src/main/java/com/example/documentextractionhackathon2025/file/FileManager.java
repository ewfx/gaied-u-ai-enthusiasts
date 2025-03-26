package com.example.documentextractionhackathon2025.file;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class FileManager implements FileManagerInterface {

    private final int READ_BUFFER_SIZE = 1024 * 1;
    public static FileManager instance;
    private WeakReference<Context> context;


    public FileManager(Context context) {
        this.context = new WeakReference<>(context);
    }

    public static synchronized FileManager getInstance(Context context) {
        if (null == instance) {
            instance = new FileManager(context);
        }
        return instance;
    }

    @Override
    public byte[] read(String path) throws Exception {

        if (null == path || path.isEmpty()) {
            return null;
        }

        Context context = this.context.get();
        if (null == context) return null;

        File fileHandler = new File(path);
        InputStream inputStream = null;
        inputStream = context.getResources().getAssets().open(path);


        byte[] bytes = new byte[READ_BUFFER_SIZE];
        int bytesRead;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while ((bytesRead = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, bytesRead);
        }

        return outputStream.toByteArray();

    }
}
