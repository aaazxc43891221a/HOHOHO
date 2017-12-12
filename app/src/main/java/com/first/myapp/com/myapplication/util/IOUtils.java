package com.first.myapp.com.myapplication.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by chauvard on 11/15/16.
 */

public class IOUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }
}
