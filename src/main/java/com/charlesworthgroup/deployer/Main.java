package com.charlesworthgroup.deployer;

import com.charlesworthgroup.deployer.domain.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = parseConfig();

        String cwd = configuration.getCurrentDirectory();
        String explodedFolder = cwd + "\\" + configuration.getExplodedName();
        String explodedWar = cwd + "\\" + configuration.getExplodedName() + ".war";

        try {
            File zipFile = ZipUtil.createZip(cwd + "\\", explodedFolder, explodedWar);

            Utils.deleteFile(new File(explodedWar));
            Utils.deleteFile(new File(explodedFolder));

            try {
                DownloadUtil.download(configuration.getWarUrl(), explodedWar);
            } catch (Exception e) {
                Log.error("Can not download file. Reason: [%s]", e.getMessage());
                Log.info("Unzipping old version");

                ZipUtil.unzipArchive(zipFile);
            }
        } catch (IOException e) {
            Log.error("Can not create zip file");
        }
    }

    private static Configuration parseConfig() {
        String currentDirectory = FileSystems.getDefault().getPath("").toFile().getAbsolutePath();

        Log.info("Current worked directory - [%s]", currentDirectory);

        try {
            Configuration configuration = new Configuration();

            configuration.setCurrentDirectory(currentDirectory);

            String configText = Utils.readFile(currentDirectory + "\\" + CONFIGURATION_FILE);
            String[] lines = configText.split("\n");

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("=");

                String paramName = parts[0].trim();
                String paramValue = parts[1].trim();

                if (paramName.equals("old-war-name")) {
                    configuration.setExplodedName(paramValue);
                } else if (paramName.equals("new-war-url")) {
                    configuration.setWarUrl(paramValue);
                }
            }

            return configuration;
        } catch (IOException e) {
            Log.error("Can not read configuration file. check is current directory contains file [%s]", CONFIGURATION_FILE);

            System.exit(1);

            return null;
        }
    }

    private static final String CONFIGURATION_FILE = "config.cfg";

}
