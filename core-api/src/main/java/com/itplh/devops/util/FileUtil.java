package com.itplh.devops.util;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class FileUtil {

    public static InputStreamReader getInputStreamReader(String classpathFile) {
        try {
            return new InputStreamReader(new ClassPathResource(classpathFile).getInputStream());
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    public static InputStream getInputStream(File file) {
        try {
            return new BufferedInputStream(new FileInputStream(file), 8192);
        } catch (FileNotFoundException e) {
            // ignore
        }
        return null;
    }

}
