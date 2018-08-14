package com.charlesworthgroup.deployer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {

    private static final int BUFFER_SIZE = 4096;

    public static void download(String fileURL, File file) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (contentLength <= 0 || contentType == null) {
                throw new FileNotFoundException("Passed URL doesn't stands for any file");
            }

            InputStream inputStream = httpConn.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }

            Utils.close(outputStream);
            Utils.close(inputStream);
        }

        httpConn.disconnect();
    }

}
