package com.charlesworthgroup.deployer;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static File createZip(String wd, String... files) throws IOException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("__dd-MM-yyyy___HH-mm-ss__");

        File zip = new File("archive" + simpleDateFormat.format(date) + ".zip");
        ZipOutputStream zipOutputStream = null;

        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zip));

            List<ZipItem> zipItems = new ArrayList<>();
            for (String filePath : files) {
                File file = new File(filePath);

                if (file.isFile()) {
                    zipItems.add(new ZipItem(file.getAbsolutePath(), file.getAbsolutePath().replace(wd, "")));
                } else {
                    zipItems.addAll(findAllItems(file, wd));
                }
            }

            for (ZipItem item : zipItems) {
                ZipEntry entry = new ZipEntry(item.path);
                zipOutputStream.putNextEntry(entry);

                byte[] bytes = Files.readAllBytes(new File(item.realPath).toPath());
                zipOutputStream.write(bytes, 0, bytes.length);
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            Utils.deleteFile(zip);

            throw e;
        } finally {
            Utils.close(zipOutputStream);
        }

        return zip;
    }

    public static void unzipArchive(File zip) {
        ZipInputStream zipInputStream = null;

        try {
            zipInputStream = new ZipInputStream(new FileInputStream(zip));

            ZipEntry entry = zipInputStream.getNextEntry();

            while (entry != null) {
                File file = new File(entry.getName());

                try {
                    file.getParentFile().mkdirs();
                } catch (Exception e) { }

                FileOutputStream fileOutputStream = new FileOutputStream(file);

                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }

                Utils.close(fileOutputStream);

                entry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.close(zipInputStream);
            Utils.deleteFile(zip);
        }
    }

    private static List<ZipItem> findAllItems(File folder, String wd) {
        List<ZipItem> result = new ArrayList<>();

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    result.add(new ZipItem(file.getAbsolutePath(), file.getAbsolutePath().replace(wd, "")));
                } else if (file.isDirectory()) {
                    result.addAll(findAllItems(file, wd));
                }
            }
        }

        return result;
    }

    private static class ZipItem {
        public final String path;
        public final String realPath;

        public ZipItem(String realPath, String path) {
            this.path = path;
            this.realPath = realPath;
        }

        @Override
        public String toString() {
            return "ZipItem{" +
                    "path='" + path + '\'' +
                    ", realPath='" + realPath + '\'' +
                    '}';
        }
    }
}
