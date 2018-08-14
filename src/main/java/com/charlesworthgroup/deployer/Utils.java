package com.charlesworthgroup.deployer;

import java.io.*;

public class Utils {

    public static String readFile(String path) throws IOException {
        FileReader fileReader = null;
        BufferedReader reader = null;

        StringBuilder result = new StringBuilder();

        try {
            fileReader = new FileReader(path);
            reader = new BufferedReader(fileReader);

            String line;

            while((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } finally {
            close(fileReader);
            close(reader);
        }

        return result.toString();
    }

    public static void deleteFile(File file) {
        Log.info("try to delete [%s]", file.getAbsolutePath());

        if (file.isDirectory()) {
            deleteFolder(file);
            return;
        }

        try {
            file.setWritable(true);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children)
                deleteFolder(new File(dir, child));
        }

        dir.delete();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) { }
        }
    }

}
